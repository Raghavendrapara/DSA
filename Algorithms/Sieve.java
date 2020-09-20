package Algorithms;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Sieve {

	public static void main(String args[])
	{
		 Scanner sc=new Scanner(System.in);
		    int n=sc.nextInt();
		    int k=sc.nextInt();
		    int m=sc.nextInt();
		    int arr[]=new int[n];
		    
		    for(int i=0;i<n;i++)
		    arr[i]=sc.nextInt();

		    int sum=arr[m];

		     for(int j=0;j<=k;j++)
		    sum=sum^arr[j+m];

		    System.out.println(sum);
}
}
class AB
{
	int a,b;
	AB(int x,int y)
	{
		a=x;
		b=y;
	}
}