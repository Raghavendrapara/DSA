/*input
"hello"
"hell"
"lull"
"name"
"fame"
"game"
"fast"
"faster"
"fastest"
"fasting"
*/
import java.util.*;
class TrieImp{

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		
		Trie root=new Trie();
		TrieImp obj=new TrieImp();
		//if(!found(word))
		for(int i=1;i<=10;i++){
		
			String word=sc.next();	
		
			obj.insert(root,word);
		}
		//else
			if(obj.search(root,"hell")){

			System.out.println("Present");
		    
		    }
		    else{ 
		    
		    System.out.println("Not Present");
		    
		    }
			/*
			if(obj.search(root,"fasti"))
			System.out.println("Present");
			else System.out.println("Not Present");
		    if(obj.search(root,"fasting"))
			System.out.println("Present");
  			else System.out.println("Not Present");
		   */
		
		
	}

	void insert(Trie root, String word){

		Trie temp=root;
		for(int index=0;index<word.length();index++){
     
          char ch=word.charAt(index);
          temp.character.put(ch,new Trie());
          if(index==word.length()-1){

         	temp.character.get(ch).isEndOfWord=true;
		   }
		  
		  temp=temp.character.get(ch);
		
		}
       
	}

	boolean search(Trie root, String word){

        Trie temp=root;
		for(int in=0;in<word.length();in++){
			
			char ch=word.charAt(in);
			
			if(in==word.length()-1){
		
				if(temp.character.get(ch).isEndOfWord==true){
		
					return true;
		
				}
			}

			if(temp.character.get(ch)!=null){
		
				temp=temp.character.get(ch);
		
			}

		}
    return false;
	}
}

class Trie{
	
	HashMap<Character,Trie> character;
	boolean isEndOfWord;

	Trie()
	{

		character=new HashMap<>();
		isEndOfWord=false;

	}
}