package algo;

/*
 * Code by Shay Mozes (2007)
 * Feel free to download and change for scholarly and academic use.
 * If used in any publication, Please reference
 * Yury Lifshits , Shay Mozes , Oren Weimann  and Michal Ziv-Ukelson
 * Speeding Up HMM Decoding and Training by Exploiting Sequence Repetitions
 * In algorithmica (2007) DOI 10.1007/s00453-007-9128-0
 *
 */

import java.util.concurrent.*;
import java.util.*;
//import java.lang.Math.*;

public class Viterbi {
	
	static public int p_v=1; //number of threads per matrix-vector multiplocation
	static public int p_k=1; //number of threads per matrix-matrix multiplocation
	static public int p_n=1; //number of sequences of matrix product computed concurrently (using p_n*p_k threads)
	static public int p_p=1;
	static final int NPROCS = 64;
	static int[] pows;
	
	static ExecutorService exec = Executors.newCachedThreadPool();
//	static ExecutorService exec = Executors.newFixedThreadPool(4);
	
	/**
	 * almost regular implementation of Viterbi's algorithm
	 * The only difference is that instead of transition and 
	 * emission probabilities we use precomputed matrices
	 * @param mats - an array of k by k matrices
	 * @param inds - k vector of indices into mats describing the observed sequence
	 * @param v - k vector with initial distribution
	 * @return n+1 vector of most probable sequence of states
	 * @author Shay Mozes (2007)
	 */
	static int[] viterbi(double[][][]mats, int[] inds, double[] v) {
		
		int i,j,l;
		int n = inds.length;
		int k = v.length;		
		int[] path = new int[n+1];
		int[][] tb = new int[n][k];
		
		double[] curr = new double[k];
		double[] prev= new double[k];
		double[] tmp;
		System.arraycopy(v, 0, prev, 0, k);
		double dtemp,dmax;
		int itemp;
		
		//viterbi
		for (i=0;i<n;i++){
			for (j=0;j<k;j++){
				dmax = mats[inds[i]][j][0] + prev[0];
				itemp=0;
				for (l=1;l<k;l++) {
					dtemp = mats[inds[i]][j][l] + prev[l];
					if (dmax < dtemp) {
						dmax = dtemp;
						itemp=l;
					}
				}	
				tb[i][j]=itemp;
				curr[j] = dmax;
			}
			tmp = curr;
			curr = prev;
			prev = tmp;
		}
		
		//traceback
		path[n]=0;
		for(i=1;i<k;i++) {
			if (prev[path[n]] < prev[i]){
				path[n] = i;
			}
		}		
//		System.out.println("max: " + prev[path[n]]);
		
		for (i=n-1;i>=0;i--){
			path[i] = tb[i][path[i+1]];
		}
		
		return path;

	}

	/**
	 * Viterbi's algorithm with pre-calculation of blockSize matrices
	 * (Four Russians)
	 * @param mats - an array of k by k matrices
	 * @param inds - k vector of indices into mats describing the observed sequence
	 * @param v - k vector with initial distribution
	 * @param blockSize - size of pre-calculated matrices
	 * @return n+1 vector of most probable sequence of states
	 */
	static int[] viterbiRep(double[][][]orig_mats, int[] inds, double[] v, int blockSize, char method) {
		
		int i,j,l;
		int n = inds.length;
		int k = v.length;		
		int N = orig_mats.length;
		double dmax, dtemp;
		int itemp;
		int[] path = null;

//		long start = System.nanoTime();
		
		//precalculate matrices
		pows = new int[blockSize+1];
		pows[0] = 1;
		for (i=1;i<=blockSize;i++) {
			pows[i] = N*pows[i-1];
		}
		
		try{
		
		double[][][][] tmpmats = new double[blockSize+1][][][];
		tmpmats[1] = orig_mats;		
		int[][][][] tb = new int[blockSize+1][][][]; //traceback info

		for (int len=2; len<=blockSize; len++) {  //current length

			int prevnum = tmpmats[len-1].length;
			if (len==blockSize) {
				tmpmats[len] = new double[N*prevnum+N][k][k]; // the +N is for later use
			} else {
				tmpmats[len] = new double[N*prevnum][k][k];
			}
			tb[len] = new int[N*prevnum][k][k];

			for(int c=0;c<N;c++) {  //current letter
				for (int prev=0; prev<prevnum;prev++) { //prev matrix
					int tempind = c*prevnum+prev;

					//matrix multiplication
					for (i=0;i<k;i++) {
						for (j=0;j<k;j++) {
							dmax = tmpmats[1][c][i][0] + tmpmats[len-1][prev][0][j];
							itemp=0;
							for (l=1;l<k;l++) {
								dtemp = tmpmats[1][c][i][l] + tmpmats[len-1][prev][l][j];
								if (dmax < dtemp) {
									dmax = dtemp;
									itemp=l;
								}
							}	
							tb[len][tempind][i][j]=itemp;
							tmpmats[len][tempind][i][j] = dmax;						
						}
					}
				}
			}
		}
		
//		long estimated = System.nanoTime() - start;
//		System.out.println("precalc time: " + estimated);
		//only keep the matrices of size blockSize
		double[][][] repmats = tmpmats[blockSize];
		//keep the original matrices to deal with slacks
		for (i=0;i<N;i++) {
			repmats[repmats.length-N+i] = orig_mats[i];
		}
		tmpmats = null; //recycle 

		//translate indices
		int nblocks = n/blockSize;
		int[] repinds = new int[nblocks+n%blockSize];

		int newind;
		for (i=0;i<nblocks*blockSize;i+=blockSize){
			newind = 0;
			for (j=0;j<blockSize;j++){
				newind += pows[j]*inds[i+j];
			}
			repinds[i/blockSize] = newind; 
		}
		
		//fill in slack indices
		for (i=0;i<n%blockSize;i++){
			repinds[nblocks+i] = pows[blockSize] + inds[nblocks*blockSize+i];
		}

		//run Viterbi according to the specified method
		int[] tmppath = null;
		try {
			switch (method){
			case 'o': 	tmppath = viterbi(repmats, repinds, v); break;
			case 'v':	tmppath = vMatVecProd(repmats, repinds, v); break;
			case 'm':	tmppath = vMatMatProd(repmats, repinds, v); break;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}


		//expand traceback
		path = new int[n+1];		
		for (i=0;i<=nblocks;i++) {
			path[i*blockSize] = tmppath[i];
		}
		
		//slack indices
		for (i=0;i<n%blockSize;i++){
			path[1+nblocks*blockSize+i] = tmppath[1+nblocks+i];
		}


		int tmpind;
		for (i=0; i<nblocks; i++){
			tmpind = repinds[i];
			for (j=blockSize-1;j>0;j--){
				path[i*blockSize+j] = tb[j+1][tmpind][path[i*blockSize+j+1]][path[i*blockSize]];
				tmpind %= pows[j];
			}
		}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	
		return path;
	}
	

	/**
	 * Computes Viterbi from right to left. That is, by a 
	 * sequence of matrix-vector multiplications. 
	 * Each matrix-vector multiplication is carried out in 
	 * parallel using p_k threads.
	 * @param mats - an array of k by k matrices
	 * @param inds - k vector of indices into mats describing the observed sequence
	 * @param v - k vector with initial distribution
	 * @return n+1 vector of most probable sequence of states
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	static int[] vMatVecProd(double[][][] mats, int[] inds, double[] v) throws InterruptedException, ExecutionException {
		
		int n = inds.length;
		int k = v.length;  
		int block = k/p_v; //System.out.println("block = "+block);
		int i,j;
		int[][] tb = new int[n][k];
		
		
		//viterbi
		for (i=0; i<n; i++) {

			double[] res = new double[k];
			Future<?>[] futures = new Future<?>[p_v];

			//submit tasks for all blocks
			for (j=0; j<p_v-1; j++) {
				futures[j] = exec.submit(new MatVecProdTask(mats[inds[i]],v,res,j*block,(j+1)*block,tb[i]));
			}
			futures[j] = exec.submit(new MatVecProdTask(mats[inds[i]],v,res,j*block,k,tb[i]));

			//wait for completion
			for (j=0; j<p_v;j++){
				futures[j].get();
			}
			
			v = res;
		}
		
		//traceback
		int[] path = new int[n+1];

		path[n] = 0;
		double max = v[0];

		for (j=1;j<k;j++){
			if (v[j]>max){
				max = v[j];
				path[n]=j;
			}
		}
//		System.out.println("max prob:" + max);
		
		for (i=n-1;i>=0;i--){
			path[i] = tb[i][path[i+1]]; 
		}
			
		return path;
	}
	
	/**
	 * Computes Viterbi by Dividing the input sequence into pn blocks. 
	 * First computes the products of matrices within each block.
	 * The calculation for each block is done by separate pk threads, 
	 * so a total of pn*pk threads are running concurrently.
	 * This results in a reduced problem with only p_n matrices.
	 * We then use xMatVecProd to compute viterbi on the reduced problem.
	 * @param mats - an array of k by k matrices
	 * @param inds - k vector of indices into mats describing the observed sequence
	 * @param v - k vector with initial distribution
	 * @return n+1 vector of most probable sequence of states
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	static int[] vMatMatProd(double[][][] mats, int[] inds, double[] v) throws InterruptedException, ExecutionException {
		
		int n = inds.length;
		int k = v.length;		
//		long startTime,estimatedTime=0;
//		startTime = System.nanoTime();
		
		//calculate p_n block matrices
		double[][][] res = new double[p_n][k][k];
		int[][][][] tb = new int[p_n][][][];
		Future<?>[] futures = new Future<?>[p_n];
		
		int block = n/p_n;
		
//		int last=-1;
		
		for (int i=0;i<p_n-1;i++) {
			tb[i] = new int[block-1][k][k];
			futures[i] = exec.submit(new xMatMatProdTask(mats, inds, i*block, (i+1)*block, res[i], tb[i]));
			
//			if(i%40==0) {
//				for (int j=last+1; j<i; j++)
//					futures[j].get();
//				last = i;
//			}
//			futures[i].get();
		}
		tb[p_n-1] = new int[n-(p_n-1)*block-1][k][k];
		futures[p_n-1] = exec.submit(new xMatMatProdTask(mats, inds, (p_n-1)*block, n, res[p_n-1], tb[p_n-1]));
//		futures[p_n-1].get();
		 
//		for (int j=last+1; j<p_n; j++)
//			futures[j].get();

		for (int i=0;i<p_n;i++) {
			futures[i].get();
		}
		
//		estimatedTime = System.nanoTime() - startTime;
//		System.out.print(estimatedTime/1e3 + " ");
//		startTime = System.nanoTime();

		//viterbi on reduced problem
		int[] tmpind = new int[p_n];
		for (int i=0;i<p_n;i++){
			tmpind[i] = i;
		}
		//int[] tmppath = vMatVecProd(res, tmpind, v);
		int[] tmppath = viterbi(res, tmpind, v);

//		estimatedTime = System.nanoTime() - startTime;
//		System.out.print(estimatedTime/1e3 + " ");
//		startTime = System.nanoTime();
		
		
		//expand traceback within each matrix concurrently
		int[] path = new int[n+1];
		path[0] = tmppath[0];
		for (int i=0;i<p_n;i++){
			path[i*block+tb[i].length+1] = tmppath[i+1];
			futures[i] = exec.submit(new xMatMatTraceTask(tb[i],i*block+tb[i].length+1,path));
		}
		for (int i=0;i<p_n;i++) {
			futures[i].get();
		}
//		estimatedTime = System.nanoTime() - startTime;
//		System.out.print(estimatedTime/1e3 + " ");
//		startTime = System.nanoTime();
		
		return path;
	}
	
	/**
	 * First computes (concurrently) matrices for all possible sequences up to length blockSize.
	 * Then transform (concurrently) the input sequence (length n) into a sequence of length n/blockSize
	 * and compute viterbi on that sequence using the desired method.
	 * Finally expand (concurrently) the sequence of states from the previous stage into one that
	 * corresponds to the original problem.   
	 * @param mats - an array of k by k matrices
	 * @param inds - k vector of indices into mats describing the observed sequence
	 * @param v - k vector with initial distribution
	 * @param blockSize - size of blocks to precompute (the number of matrices that will be
	 * computed is exponential in this parameter. The base of the exponent is the length of
	 * the mats array (i.e., the size of the alphabet)
	 * @param method - should be either 'v' for vMatVecProd or 'm' for vMatMatProd
	 * @return n+1 vector of most probable sequence of states
	 */
	static int[] vReps(double[][][] mats, int[] inds, double[] v, int blockSize, char method) {

		int N = mats.length;
		int k = mats[0].length;
		int n = inds.length;
		int[] path=null;

		pows = new int[blockSize+1];
		pows[0] = 1;
		for (int i=1;i<=blockSize;i++) {
			pows[i] = N*pows[i-1];
		}

//		long start, estimated;
//		start = System.nanoTime();
		
		try {

			//precalculate all matrices of given blockSize
			//int p_p = NPROCS/N/p_k;
			

			//tmpmats will hold all matrices for each size up to blocksize
			double[][][][] tmpmats = new double[blockSize+1][][][];
			int[][][][] tb = new int[blockSize+1][][][]; //traceback info

			tmpmats[1] = mats;
			for (int i=2;i<=blockSize;i++) {
				int lastnum = tmpmats[i-1].length;
				if (i==blockSize) {
					tmpmats[i] = new double[N*lastnum+N][k][k]; // the +N is for later use
				} else {
					tmpmats[i] = new double[N*lastnum][k][k];
				}
				tb[i] = new int[N*lastnum][k][k];

				//concurrent chunks
				int chunk = lastnum/p_p;
				Future<?>[][] futures = new Future<?>[N][p_p];
				for(int c=0;c<N;c++) {
					for (int j=0;j<p_p-1;j++) {
						futures[c][j] = exec.submit(new RepMatTask(tmpmats,i,tb[i],c,j*chunk,(j+1)*chunk));
					}
					futures[c][p_p-1] = exec.submit(new RepMatTask(tmpmats,i,tb[i],c,(p_p-1)*chunk,lastnum));
				}

				for(int c=0;c<N;c++) {
					for (int j=0;j<p_p;j++) {
						futures[c][j].get();
					}
				}
			}

//			estimated = System.nanoTime() - start;
//			System.out.println("precalc time: " + estimated);
			
			//only keep the matrices of size blockSize
			double[][][] repmats = tmpmats[blockSize];
			//keep the original matrices to deal with slacks
			for (int i=0;i<N;i++) {
				repmats[repmats.length-N+i] = mats[i];
			}
			tmpmats = null; //recycle 

			//translate indices
			int nblocks = n/blockSize;
			int[] repinds = new int[nblocks+n%blockSize];

			//p_p = Math.min(NPROCS, nblocks);
			Future<?>[] futures = new Future<?>[p_p];

			int chunk = blockSize*(nblocks/p_p);
			for (int i=0;i<p_p-1;i++){
				futures[i] = exec.submit(new RepIndTask(inds, repinds, blockSize, i*chunk, (i+1)*chunk));
			}
			futures[p_p-1] = exec.submit(new RepIndTask(inds, repinds, blockSize, (p_p-1)*chunk, nblocks*blockSize));

			for (int i=0;i<p_p;i++) {
				futures[i].get();
			}

			//fill in slack indices
			for (int i=0;i<n%blockSize;i++){
				repinds[nblocks+i] = pows[blockSize] + inds[nblocks*blockSize+i];
			}

			//run Viterbi according to the specified method
			int[] tmppath = null;
			switch (method) {
			case 'v': tmppath = vMatVecProd(repmats, repinds, v); break;
			case 'm': tmppath = vMatMatProd(repmats, repinds, v); break;
			case 'o': tmppath = viterbi(repmats, repinds, v); break;
			}

			//expand traceback
			path = new int[inds.length+1];		
			for (int i=0;i<=nblocks;i++) {
				path[i*blockSize] = tmppath[i];
			}
			//slack indices
			for (int i=0;i<n%blockSize;i++){
				path[1+nblocks*blockSize+i] = tmppath[1+nblocks+i];
			}

			chunk = nblocks/p_p;
			for (int i=0;i<p_p-1;i++){
				futures[i] = exec.submit(new RepTraceTask(path, repinds, tb, i*chunk, (i+1)*chunk));
			}
			futures[p_p-1] = exec.submit(new RepTraceTask(path, repinds, tb, (p_p-1)*chunk, nblocks));

			for (int i=0;i<p_p;i++) {
				futures[i].get();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	
		return path;
		
	}
	
	/**
	 * Runnable implementation that
	 * calculates elements [from,to) in the product of mat and vec.
	 * Also computes the corresponding traceback information (i.e. stores
	 * the maximizing arguments)
	 * @author Shay
	 *
	 */
	static class MatVecProdTask implements Runnable {
		
		double[][] m;
		double[] v;
		double[] res;
		int[] tb;
		
		int from, to;
		
		public MatVecProdTask(double[][] mat, double[] vec, double[] result, int f, int t, int[] trb) {
			m = mat;
			v = vec;
			res = result;
			from = f;
			to = t;
			tb = trb;
		}
		
		public void run() {

			double dtemp,dmax;
			int itemp;

			for (int i=from; i<to; i++) {
				dmax = m[i][0] + v[0];
				itemp = 0;
				for (int j=1; j<m.length; j++) {
					dtemp = m[i][j] + v[j];
					if (dmax < dtemp) {
						dmax = dtemp;
						itemp = j;
					}
				}
				tb[i] = itemp;
				res[i] = dmax;
			}
		}
	}
	
	/**
	 * Runnable implementation that calculates (sequenctially) the product of the matrices
	 * referred to by locations [from,to) in inds. Each matrix multiplication
	 * is performed using p_k threads. Also stores the corresponding traceback information. 
	 * 
	 * @author Shay
	 *
	 */
	static class xMatMatProdTask implements Runnable {

		double[][][] m;
		double[][] res;
		int[] inds;
		int from, to;
		int[][][] tb;

		public xMatMatProdTask(double[][][] mats, int[] indx, int f, int t, double[][] result, int[][][] trb) {
			m = mats;
			inds = indx;
			res = result;
			from = f;
			to = t;
			tb = trb;
		}

		public void run() {

			int k = m[0].length;

			switch (p_k) {

			//if only one thread, do job yourself
			case 1: {

				int i,j;
				double dmax, dtemp;
				int itemp;
				
				double[][][] tmpres = new double[2][k][k];

				//copy first matrix to res
				for (i=0;i<k;i++) {
					for (j=0;j<k;j++) {
						tmpres[(from+1)%2][i][j] = m[inds[from]][i][j];
					}
				}

				for (int l=from+1;l<to;l++) {
					
					for (i=0;i<k;i++) {
						for (j=0;j<k;j++) {					
							dmax = m[inds[l]][i][0] + tmpres[l%2][0][j];
							itemp = 0;
							for (int r=1; r<k; r++) {
								dtemp = m[inds[l]][i][r] + tmpres[l%2][r][j];
								if (dmax < dtemp) {
									dmax = dtemp;
									itemp = r;
								}
							}
							tb[l-from-1][i][j] = itemp;
							tmpres[(l+1)%2][i][j] = dmax;
						}
					}
				}

				for (i=0;i<k;i++) {
					for (j=0;j<k;j++) {
						res[i][j] = tmpres[to%2][i][j];
					}
				}

				break;
			}
			default: { 

				int i,j;
				Future<?>[] futures = new Future<?>[p_k];
				int block = (k*k)/p_k;

				//use 2 matrices for interchanging steps
				double[][][] tmpres = new double[2][k][k];

				for (i=0;i<k;i++) {
					for (j=0;j<k;j++) {
						tmpres[(from+1)%2][i][j] = m[inds[from]][i][j];
					}
				}

				try {

					//loop: tmpres = m[j]*tmpres concurrently
					for (j=from+1;j<to;j++) {

						for (i=0; i<p_k-1;i++) {
							futures[i] = exec.submit(new MatMatProdTask(m[inds[j]],tmpres[j%2],tmpres[(j+1)%2],i*block,(i+1)*block,tb[j-from-1]));
						}					
						futures[i] = exec.submit(new MatMatProdTask(m[inds[j]],tmpres[j%2],tmpres[(j+1)%2],i*block,k*k,tb[j-from-1]));

						for (i=0; i<p_k;i++){
							futures[i].get();
						}
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}

				//move result to res
				for (i=0;i<k;i++) {
					for (j=0;j<k;j++) {
						res[i][j] = tmpres[to%2][i][j];
					}
				}
			}
			}
		}
	}

	/**
	 * Runnable implementation for computing certain consecutive elements in the product
	 * of mat1 and mat2 along with the corresponding traceback information.
	 * @author Shay
	 *
	 */
	static class MatMatProdTask implements Runnable {
		
		double[][] m1;
		double[][] m2;
		double[][] res;
		int from, to;
		int[][] tb;
		
		public MatMatProdTask(double[][] mat1, double[][] mat2, double[][] result, int f, int t, int[][] trb) {
			m1 = mat1;
			m2 = mat2;
			res = result;
			from = f;
			to = t;
			tb = trb;
			
		}
		
		public void run() {
			int n = m1.length;
			int i,j,k;
			double dtemp,dmax;
			int itemp;
			
			for (int cnt=from; cnt<to; cnt++) {
				//convert one dimensional range to 2-D matrix index				
				i = cnt%n;
				j = cnt/n;


				dmax = m1[i][0] + m2[0][j];
				itemp = 0;
				for (k=1; k<m1.length; k++) {
					dtemp = m1[i][k] + m2[k][j];
					if (dmax < dtemp) {
						dmax = dtemp;
						itemp = k;
					}
				}
				tb[i][j] = itemp;
				res[i][j] = dmax;

			}
		}
	}

	/**
	 * Runnable implementation for expanding the traceback of a sequence of matrices 
	 * computed by xMatMatProdTask
	 * @author Shay
	 *
	 */
	static class xMatMatTraceTask implements Runnable {

		int[][][] tb;
		int[] path;
		int ind;
		
		/**
		 * 
		 * @param trb - traceback information for this block (filled by xMatMatProdTask)
		 * @param offset - the index in p corresponding to the last element in tb (this element should already be filled in p)
		 * @param p - state path to be updated (partially filled by the values from the reduced problem)
		 */
		public xMatMatTraceTask(int[][][] trb, int offset, int[] p) {
			tb = trb;
			path = p;
			ind = offset;  			
		}
		
		public void run(){
			int prev_ind = ind - tb.length - 1;
			for (int i=tb.length; i>0; i--){
				path[prev_ind+i] = tb[i-1][path[prev_ind+i+1]][path[prev_ind]];
			}
		}
		
	}
	
	
	/**
	 * Runnable implementation to compute part of the matrices of length len
	 * given the matrices of length len-1
	 * @author Shay
	 *
	 */
	static class RepMatTask implements Runnable {
		
		double[][][][] mats;
		int[][][] tb;
		int len; //current length
		int c; //symbol to append
		int fr;
		int to;
		
		public RepMatTask(double[][][][] tmpmats, int i, int[][][] trb, int ch, int f, int t) {
			mats = tmpmats;
			tb = trb;
			len = i;
			c = ch;
			fr = f;
			to = t;			
		}
		
		public void run() {
			
			int i,j;
			int prevnum = mats[len-1].length;
			
			int k = mats[1][0][0].length;
			int block = (k*k)/p_k;
			Future<?>[] futures = new Future<?>[p_k];

			try {
				for (j=fr;j<to;j++) {

					for (i=0; i<p_k-1;i++) {
						futures[i] = exec.submit(new MatMatProdTask(mats[1][c],mats[len-1][j],mats[len][c*prevnum+j],i*block,(i+1)*block,tb[c*prevnum+j]));
					}					
					futures[i] = exec.submit(new MatMatProdTask(mats[1][c],mats[len-1][j],mats[len][c*prevnum+j],i*block,k*k,tb[c*prevnum+j]));

					for (i=0; i<p_k;i++){
						futures[i].get();
					}
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
	}

	/**
	 * Runnable implementation to compute part ([fr,to)) of the translation 
	 * from the original sequence of indices into a sequence of
	 * indices into the matrices computed by RepMatTask.
	 * That is, replacing each blockSize indices by one large index 
	 * @author Shay
	 *
	 */
	static class RepIndTask implements Runnable {
		 int[] inds;
		 int[] repinds;
		 int blockSize;
		 int fr;
		 int to;
		  		
		 public RepIndTask(int[] ind, int[] repind, int bs, int f, int t) {
			 inds = ind;
			 repinds = repind;
			 blockSize = bs;
			 fr = f;
			 to = t;
		 }
		 
		 public void run() {
			 
			 int i,j;
			 
			 int newind;
			 for (i=fr;i<to;i+=blockSize){
				 newind = 0;
				 for (j=0;j<blockSize;j++){
					 newind += pows[j]*inds[i+j];
				 }
				 repinds[i/blockSize] = newind; 
			 }
		 }
	}
	
	/**
	 * Runnable implementation for computing part ([fr,to)) of the 
	 * expansion of the sequence of states for the reduced problem
	 * (output of RepMatTask and RepIndTask) into a sequence of
	 * states for the original problem 
	 * @author Shay
	 *
	 */
	static class RepTraceTask implements Runnable {
		
		int[] path;
		int[] repinds;
		int[][][][] tb;
		int fr,to;
		
		public RepTraceTask( int[] pa, int[] repind, int[][][][] trb, int f, int t) {
			path = pa;
			repinds = repind;
			tb = trb;
			fr = f;
			to = t;
		}
		
		public void run() {
			
			int blocksize = tb.length-1;
			int i,j;
			int tmpind;
			for (i=fr;i<to;i++){
				tmpind = repinds[i];
				for (j=blocksize-1;j>0;j--){
					path[i*blocksize+j] = tb[j+1][tmpind][path[i*blocksize+j+1]][path[i*blocksize]];
					tmpind %= pows[j];
				}
			}			
		}
	}
	
	
	/**
	 * test main for the Viterbi class
	 * 
	 * @param args  a string of the form:<br>
	 * n k variant {param val}
	 * <br> NOTE this is a test main. No fancy verification of the input string is made
	 * <br>n - length of sequence
	 * <br>k - number of states (dimension of matrices)
	 * <br>variant - any of: o,p,P,v,m,x,X,y,Y (see writeup for details)
	 * <br>param - any of: pv, pk, pn, pp, bs (default value for all is 1)
	 */
	public static void main(String[] args) {

		Random rand = new Random(1001);

		int N = 4;
		int n= Integer.parseInt(args[0]);
		int k= Integer.parseInt(args[1]);
		int nreps=15;
		int i,j,l;

		int bs=1;
		for (i=3;i<args.length;i+=2) {
			if (args[i].equalsIgnoreCase("pv")) p_v = Integer.parseInt(args[i+1]);
			else if (args[i].equalsIgnoreCase("pn")) p_n = Integer.parseInt(args[i+1]);
			else if (args[i].equalsIgnoreCase("pp")) p_p = Integer.parseInt(args[i+1]);
			else if (args[i].equalsIgnoreCase("bs")) bs = Integer.parseInt(args[i+1]);
			else if (args[i].equalsIgnoreCase("pk")) p_k = Integer.parseInt(args[i+1]);
		}

		double[][][] mats = new double[N][k][k];
		double[] vec = new double[k];
		int[] inds = new int[n];
		int[] res = new int[n];

//		System.out.println("N = " + N + ", n = " + n + ", k = " + k);
//		System.out.println("nreps = " + nreps);

		for (i=0;i<N;i++) {
			for (j=0;j<k;j++) {
				for (l=0;l<k;l++) {
//					mats[i].set(rand.nextInt(5),j,l);
					mats[i][j][l] = rand.nextDouble();					
//					mats[i].set((i+0.1)*(0.5+j)/(k-l+0.2),j,l);
				}
			}
		}

		for(j=0;j<k;j++) {
			vec[j] = j;
		}

		for(j=0;j<n;j++) {
			inds[j] = rand.nextInt(N);
//			inds[j] = j%N;
		}


		long startTime,estimatedTime=0;
		for (int rep=0;rep<2;rep++) {
			nreps = rep*7 + 3;
			try {
				startTime = System.nanoTime();
				for (i=0;i<nreps;i++) {
					switch (args[2].charAt(0)) {
					case 'o': res = Viterbi.viterbi(mats, inds, vec);break;
					case 'P': res = Viterbi.vReps(mats, inds, vec, bs, 'o');break;
					case 'p': res = Viterbi.viterbiRep(mats, inds, vec, bs, 'o');break;
					case 'X': res = Viterbi.vReps(mats, inds, vec, bs, 'v'); break;
					case 'x': res = Viterbi.viterbiRep(mats, inds, vec, bs, 'v'); break;
					case 'Y': res = Viterbi.vReps(mats, inds, vec, bs, 'm'); break;
					case 'y': res = Viterbi.viterbiRep(mats, inds, vec, bs, 'm'); break;
					case 'v': res = Viterbi.vMatVecProd(mats, inds, vec);break;
					case 'm': res = Viterbi.vMatMatProd(mats, inds, vec);break;
					}
				}
				estimatedTime = System.nanoTime() - startTime;
				if (rep>0)
					System.out.println(args[2] + " N= " + N + " n= " + n + " k= " + k + " bs= " + bs + " p_k= " + p_k + " p_n= " + p_n +" p_v= "+ p_v + " p_p= " + p_p + " time: " + estimatedTime/1e3/nreps);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		Viterbi.exec.shutdown();

	}
}