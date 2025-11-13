import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;
public class Moving {
public static void main(String args[]) throws IOException
{
	Scanner sc=new Scanner(new BufferedReader(new InputStreamReader(System.in)));
	int x=sc.nextInt();
	int y=sc.nextInt();
	int n=sc.nextInt();
	int temp=(x>y)?x:y;
	x=x+y-temp;
	y=temp;
	BufferedReader br = new BufferedReader( 
            new InputStreamReader(System.in)); 

StringTokenizer st = new StringTokenizer(br.readLine()); 	
	float arr[]=new float[n];
	int l=0;
	while(st.hasMoreTokens()) {
		arr[l++]=Float.parseFloat(st.nextToken());
	}
	float sumx=0.0f;
	float sumy=0.0f;
	for(int i=y-x;i<y;i++)
	sumx+=arr[i];
	for(int i=0;i<y;i++)
		sumy+=arr[i];
	int ans=0;
	int up=0;
	int dn=0;
	if(sumx/x > sumy/y)
	{
	up=1;	
	}
	else if(sumx/x < sumy/y)
	{
		dn=1;
	}
	for(int k=y;k<n;k++)
	{
		sumx=sumx-arr[k-x]+arr[k];
		sumy=sumy-arr[k-y]+arr[k];
		if(sumx/x > sumy/y)
		{
			if(dn==1)
			{
				dn=0;
				ans++;
				up=1;
			}
		}
		else if(sumx/x < sumy/y)
		{
			if(up==1)
			{
				dn=1;
				ans++;
				up=0;
			}
		}
		
	}
	
	System.out.println(ans);
}

}
