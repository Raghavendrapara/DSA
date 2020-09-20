import java.util.*;
import java.io.*;
public class Sieve
{
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		int tests=sc.nextInt();
		int a=tests;
		int k=1;
		while(tests-->0)
		{
			int n=sc.nextInt();
            int arr[]=new int[n];
            Arrays.fill(arr,0);
            for(int i=2;i*i<=n;i++)
            {
               
              for(int j=2*i;j<n;j=j+i)
             {  if(arr[j]!=1)
               	arr[j]=1;
               k++;}
            }
            
            for(int i=1;i<=100;i++)
            	if(arr[i]==0)
            	System.out.println(i);
System.out.println(k);





		}

 }
}