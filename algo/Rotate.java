package algo;


import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
public class Rotate {
public static void main(String args[])
{
	Scanner sc=new Scanner(new BufferedReader(new InputStreamReader(System.in)));
	int m=sc.nextInt();
	int n=sc.nextInt();
	int temp=(m>n)?m:n;
int arr[][]=new int[m][n];
        for(int i=0;i<m;i++)
        {
        for(int j=0;j<n;j++)
        arr[i][j]=sc.nextInt();
        }
        
	 n=m+n-temp;
	 m=temp;
	 int layer=sc.nextInt();
	int rot[]=new int[layer];
	for(int i=0;i<layer;i++)
		rot[i]=sc.nextInt();



}
int[][] rotate1(int mat[][]){




}
int[][] rotate2(int mat[][]){

}
int[][] rotate3(int mat[][]){

}
int[][] rotate1(int mat[][]){

}
}