/*input

*/
import java.util.*;
import java.io.*;
import static java.lang.System.out;
class TrieNode{

	TrieNode[] nodes=new TrieNode[26];
	boolean end=false;
}
class Trie{
	public static void main(String[] args) {
	
		TrieNode root=new TrieNode();
		Trie obj=new Trie();
		obj.insert(root,"abc");
		obj.insert(root,"abd");
		obj.insert(root,"abe");
		obj.insert(root,"a");
		out.println(obj.search(root,"ab"));
	}
	void insert(TrieNode root,String word){

		TrieNode curr=root;
		int n=word.length();
		for(int i=0;i<n;i++){
			char ch=word.charAt(i);
			if(curr.nodes[ch-97]==null)
			curr.nodes[ch-97]=new TrieNode();
			curr=curr.nodes[ch-97];
			if(i==n-1)
				curr.end=true;
		}
	}
	boolean search(TrieNode root,String word){
		TrieNode curr=root;
		int n=word.length();
		for(int i=0;i<n;i++){
			char ch=word.charAt(i);
			if(curr.nodes[ch-97]==null)
				return false;
			curr=curr.nodes[ch-97];
			if(i==n-1){
				if(curr.end==true)
					return true;
				else
					return false;
			}
		}
		return false;

	}
}

		
		
		