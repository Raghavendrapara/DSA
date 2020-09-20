import java.util.*;
import java.io.*;
public class Bubble
{
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		int tests=sc.nextInt();
		int a=tests;
		int arr[]={1,2,3,4,5,3,5,-1,8,2,-1};
		while(tests-- > 0)
		{
          
          sort(arr,0,0,arr.length);
		}
    for(int i:arr)
    	System.out.println(i);
	System.out.println("Case #"+a+" : ");
  }
  static void sort(int arr[],int i,int k,int j)
  {
  	if(i==j-1)
  		return;
  	if(k==j)
  		return;
  	if(arr[i]<arr[i+1])
  		swap(arr,i);
  		sort(arr,i+1,i+1,j);
        sort(arr,i,k+1,j);

  }
  static void swap(int arr[],int i){

    int temp=arr[i];
  	arr[i]=arr[i+1];
  	arr[i+1]=temp;
  }
}
