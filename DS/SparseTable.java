/*input
15
1 -8 -9 -23 45 -9 2 3 4 5 6 7 -8 9 10
*/
import java.util.*;
class SparseTable{

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int maxn=1000000;
		int k=(int)(Math.log(maxn)) + 1;
		int sparse_table[][] = new int[maxn][k+1];
        int log[]=new int[maxn+1];
        log[1]=0;
        for(int i=2;i<=maxn;i++)
        	log[i]=log[i/2]+1;


        int size=sc.nextInt();
        int inp_arr[]=new int[size];

        for(int ind=0;ind<size;ind++){

        	inp_arr[ind]=sc.nextInt();
        
        }
        SparseTable ob=new SparseTable();

        ob.build(inp_arr,sparse_table);
        System.out.println(ob.minQuery(sparse_table, 14, 14,log));



		}

		void build(int arr[],int sparse_table[][]){

			for(int i=0;i<arr.length;i++){

				sparse_table[i][0]=arr[i];

			}

			for(int j=1;j<sparse_table[0].length;j++){

				for(int i=0;i+(1<<j)<=arr.length;i++){

					sparse_table[i][j] = Math.min(sparse_table[i][j-1],sparse_table[i + (1 << (j-1))][j-1]);
				}

			}

		}

		int minQuery(int [][]sparse_table, int left, int right, int log[]){

			int j=log[right-left+1];

			return Math.min(sparse_table[left][j],sparse_table[right- (1<<j) +1][j]);


}
