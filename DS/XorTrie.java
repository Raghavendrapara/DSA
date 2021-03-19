import java.util.*;
import java.io.*;
import static java.lang.System.out;
class XorTrie{

	static class TrieNode{
		
		TrieNode nodes[]=new TrieNode[2];
	
	}
	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		TrieNode root=new TrieNode();
		int arr[]={1,4,5,7};

		for(int i:arr)
		insert(root,i);
		int max=0;
		for(int i:arr)
			max=Math.max(max,maxPairXor(i,root));
		out.println(max);

	}

	static void insert(TrieNode root,int val){

		TrieNode curr=root;

		for(int i=30;i>=0;i--){
			int j=(1<<i)&val;
			if(j==0)
			{	if(curr.nodes[0]==null)
				curr.nodes[0]=new TrieNode();
				curr=curr.nodes[0];
			}
			else
			{	if(curr.nodes[1]==null)
				curr.nodes[1]=new TrieNode();
				curr=curr.nodes[1];
			}		
		}
	}
	

	static int maxPairXor(int val, TrieNode root){

		TrieNode curr=root;
		int sum=0;
		for(int i=30;i>=0;i--){
			int j=(1<<i)&val;
			if(j==0)
			{
				if(curr.nodes[1]==null)
				curr=curr.nodes[0];
				else{
				curr=curr.nodes[1];
				sum+=1<<i;
				}
			}
			else{
				if(curr.nodes[0]==null)
				curr=curr.nodes[1];
				else{
				curr=curr.nodes[0];
				sum+=1<<i;
				}	
			}
		}
		return sum;
	}
}