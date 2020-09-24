import java.util.*;
class BinarySearchTree{
     
     BinarySearchTree node;
     BinarySearchTree left;
     BinarySearchTree right;
     int value;
     BinarySearchTree(int value)
     {
     	this.value=value;
        left=right=null;
     }


}
class Runn{

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
      BinarySearchTree root=new BinarySearchTree(sc.nextInt());
    
     do{
     	int a=sc.nextInt();
        if(a==-1)
        	break;
        insert(root,a);
     
	}while(true);

levelOrder(root,1);

if(search(root,40,1))
System.out.println("true");
else
System.out.println("No");

}
static void insert(BinarySearchTree root,int key){

if(root.left==null && root.value>key)
	root.left=new BinarySearchTree(key);
else if(root.right==null && root.value<=key)
	root.right=new BinarySearchTree(key);
else{ 
	if(root.value>key)
    insert(root.left,key);
    else
    insert(root.right,key);
   }

}

static void levelOrder(BinarySearchTree root,int level)
{
	if(root==null)
		return;
	levelOrder(root.left,level+1);

	System.out.println(root.value+"   "+level);
	
	levelOrder(root.right,level+1);
}
  
static boolean search(BinarySearchTree root,int val,int k){

   k++;
   System.out.println(k);
   if(root==null)
   	return false;
   if(root.value==val)
   	return true;
   else if(root.value<=val)
   	return search(root.right,val,k);
   
   	return search(root.left,val,k);
  
   	
   }
  //TO-DO Dlete Operation



}