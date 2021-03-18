/*input
9
11
0 1
1 2
2 3
3 0
3 4
4 5
5 4
7 5
6 7
7 8
8 6
*/
import java.util.*;
import java.io.*;
import static java.lang.System.out;
class KosarajuSCC{

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int V=sc.nextInt();
		ArrayList<Integer> adj[]=new ArrayList[V];
		ArrayList<Integer> jda[]=new ArrayList[V];
		int E=sc.nextInt();
		for(int i=0;i<V;i++){
			adj[i]=new ArrayList<>();
			jda[i]=new ArrayList<>();
		}
		while(E-->0){
			int a=sc.nextInt();
			int b=sc.nextInt();
				adj[a].add(b);
				jda[b].add(a);
			
		}
		Stack<Integer> temp=new Stack<>();		
		boolean vis[]=new boolean[V];
		for(int i=0;i<V;i++){
			if(!vis[i]){
				dfs(adj,i,vis,temp);
			}
		}	
			HashSet<HashSet<Integer>> ans=new HashSet<>();
		Arrays.fill(vis,false);
		while(temp.size()!=0){
			int elem = temp.pop();
			HashSet<Integer> temp1=new HashSet<>();
			if(!vis[elem]){
				dfsT(jda,elem,vis,temp1);
			}
			if(temp1.size()>0)
				ans.add(temp1);
			
		}
		out.println(ans);

	}
	//for dfs on transpose
	static void dfsT(ArrayList<Integer> jda[],int curr,boolean vis[],HashSet<Integer> set){
		vis[curr]=true;
		for(int j:jda[curr]){
			if(!vis[j])
				dfsT(jda,j,vis,set);
		}
		set.add(curr);
	}
	//stack of elements ordered by exit time
	static void dfs(ArrayList<Integer> adj[],int curr,boolean vis[],Stack<Integer> set){

		vis[curr]=true;
		for(int j:adj[curr]){
			if(!vis[j])
				dfs(adj,j,vis,set);
		}
		set.push(curr);
	}
	
	}	
		
		
