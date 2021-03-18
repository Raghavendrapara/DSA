/*input
10
8
1 2
3 4
0 1
0 4
5 6
7 8
8 9
9 7
*/
import java.util.*;
import java.io.*;
import static java.lang.System.out;
class ConnectedComp{

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int V=sc.nextInt();
		ArrayList<Integer> adj[]=new ArrayList[V];
		int E=sc.nextInt();
		for(int i=0;i<V;i++)
			adj[i]=new ArrayList<Integer>();
		while(E-->0){
			int a=sc.nextInt();
			int b=sc.nextInt();
				adj[a].add(b);
				adj[b].add(a);
			
		}

		HashSet<HashSet<Integer>> arr=new HashSet<>();

		boolean vis[]=new boolean[V];
		for(int i=0;i<V;i++){
			if(!vis[i]){
				HashSet<Integer> temp=new HashSet<>();
				dfs(adj,i,vis,temp);
				arr.add(temp);
			}
		}
			System.out.println(arr);
		

	}
	static void dfs(ArrayList<Integer> adj[],int curr,boolean vis[],HashSet<Integer> set){

		vis[curr]=true;
		set.add(curr);
		for(int j:adj[curr]){
			if(!vis[j])
				dfs(adj,j,vis,set);
		}
	}
	
	static int gcd(int x,int y){
	return 1;
	}
	
	//static int[] sieve(int n){
	
	
	//}	
		
	}	
		
		
		
		