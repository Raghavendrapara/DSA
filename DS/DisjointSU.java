/*input
10
*/
import java.util.*;
import java.io.*;
import static java.lang.System.out;
class DisjointSU{

	static int size[];
	static int p[];
	static int n;
	static void initialize(){
		for(int i=0;i<n;i++){
			size[i]=1;
			p[i]=i;
		}
	}

	static int root (int i)
   {
     
     while(p[ i ] != i)
     {
       p[ i ] = p[ p[ i ] ] ; 
       i = p[ i ]; 
     }
     
     return i;
   }

	static boolean check(int e1,int e2){
		if(root(e1)==root(e2))
			return true;
		return false;
	}

	static void union(int e1,int e2){

		int p1=root(e1);
		int p2=root(e2);
		if(size[p1]>size[p2])
			{
				p[p1]=p2;
				size[p2]+=size[p1];
			}
		else
			{
				p[p2]=p1;
				size[p1]+=size[p2];
 	}
	}

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		n=sc.nextInt();
		p=new int[n];
		size=new int[n];
		initialize();
		union(2,1);
		union(3,2);
		System.out.println(root(3));
		union(1,4);
		union(5,4);

		System.out.println(root(1));
		for(int i=0;i<n;i++)
			System.out.print(p[i]+" ");
	}
	

	static int gcd(int x,int y){
	return 1;
	}
	
	//static int[] sieve(int n){
	
	
	//}	
		
	}	
		
		
		
		