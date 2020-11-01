/*input
5
5
5 2
1 3
2 4
3 4
1 5
*/

/*
Topological Sorting Using kahn's Algorithm
Prints the Lexicographically smallest or largest order
*/
import java.util.*;
class KahnTSort{

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		ArrayList<ArrayList<Integer>> adjList = new ArrayList<>();
		System.out.println("Enter Number Of Vertices");
		int ver = sc.nextInt();

		for(int ind=0;ind<=ver;ind++){

			adjList.add(new ArrayList<Integer>());
		
		}

		System.out.println("Enter Number of Edges");
		int edges=sc.nextInt();
		for(int index=1;index<=edges;index++){

			int first = sc.nextInt();
			int second = sc.nextInt();
			adjList.get(first).add(second);
		
		}

        //For Lexicographic Smallest TSort.For Lexicographic Largest reverse the PQueue order
		PriorityQueue<Integer> pqueue = new PriorityQueue<>((n1,n2)->(n1-n2));

		int indegree[] = new int[ver+1];
		for(ArrayList<Integer> arr:adjList)
		{
			for(int val:arr)
				indegree[val]++;
		}
        
        for(int val=1;val<=ver;val++)
        	if(indegree[val]==0)
        		pqueue.add(val);


        String sorder="";
        while(pqueue.size()>0){

        	int curr=pqueue.poll();
        	sorder = sorder + curr;
        	for(int i:adjList.get(curr))
        		{
        			indegree[i]--;
        			if(indegree[i]==0){
        				pqueue.offer(i);
        			}
        		}
        }
    System.out.println(sorder);
}

}
