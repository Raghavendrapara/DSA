import java.util.*;
class AVlTreeMap{
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
      System.out.println("Enter the numbers");
      int n=sc.nextInt();
      TreeMap<Integer,Integer> tree=new TreeMap<>();
      for(int i=0;i<n;i++)
      	tree.put(sc.nextInt(),0);
      
      System.out.println(tree.higherKey(4));
     System.out.println(tree.higherKey(5));
     System.out.println(tree.lowerKey(6));

	}
}