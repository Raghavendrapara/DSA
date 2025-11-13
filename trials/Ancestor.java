
import java.util.*;
import java.io.*;
public class Ancestor
{
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		int tests=sc.nextInt();
		int a=tests;
		while(tests-->0)
		{
           
           int arr[]={0,1,2,3,4,5,0,7};
           int tar=sc.nextInt();
           solve(arr,tar,1);
        
		}
    System.out.println(Integer.MAX_VALUE+"\n"+Long.MIN_VALUE+"\n"+Double.MAX_VALUE);
	System.out.println("Case #"+a+" : ");
  }


    static boolean solve(int arr[],int tar,int index){
     if(index>7)
     	return false;
     if(arr[index]==tar)
     	return true;
     
     if( solve(arr,tar,2*index) || solve(arr,tar,2*index+1))
     {
    	 System.out.println(arr[index]);
         return true;
     }

return false;
    }

}