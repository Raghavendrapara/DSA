import java.util.*;
import java.io.*;
/*
A priority queue based implementation 
Min-Heap is implemented using Lambda function instead of Comparator
*/
class Dijsktra
{
	public static void main(String[] args) {

		Scanner sc=new Scanner(System.in);
		int tests=sc.nextInt();
		int a=tests;
    ArrayList<ArrayList<Edges>> adjList=new ArrayList<>();


		while(tests-->0){

            System.out.println("Enter the number Of Nodes");
            int n=sc.nextInt();

            for(int i=0;i<n;i++){
            	adjList.add(new ArrayList<Edges>());
            }

            System.out.println("Enter the edges");
            
            int num=sc.nextInt();
           
            for(int i=1;i<=num;i++){

            	int aa    = sc.nextInt();
            	int b    = sc.nextInt();
            	double c = sc.nextDouble();
            	adjList.get(aa).add(new Edges(b,c));
            
            }
          
          long aa=System.nanoTime();
          System.out.println(aa);
          double dis[]=new double[n];
          Arrays.fill(dis,1000000.0);
          dis[0]=0;
          
          PriorityQueue<Edges> pQueue=new PriorityQueue<>((n1,n2)->(n1.b>n2.b)?1:-1);
          
          pQueue.add(new Edges(0,0.0));
          
          int x=1;//Source

          HashSet<Edges> hash=new HashSet<>();
          while(pQueue.size()>0){
           
           Edges curr=pQueue.poll();
           for(Edges edge:adjList.get(curr.a))
           {
                 if(edge.b+dis[curr.a]<dis[edge.a])
                 {
                 	dis[edge.a]=edge.b+dis[curr.a];
                    pQueue.add(new Edges(edge.a,dis[edge.a]));

                 }      
 
                 x++;
          }
               x++;
          }

        System.out.println(System.nanoTime()-aa);
        System.out.println(x);
		
        for(int i=0;i<n;i++){
	     
        System.out.println("Case #"+i+":  "+dis[i]+" ");
      
        }
  }
}
}
//Edges Contain destination vertex and cost of traversing that edge
class Edges{
	int a;
	double b;
	Edges(int a,double b){
		
    this.a=a;
		this.b=b;
	
  }
}
