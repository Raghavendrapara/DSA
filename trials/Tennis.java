import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.*;
public class Tennis {
public static void main(String args[])
{
	Scanner sc=new Scanner(new BufferedReader(new InputStreamReader(System.in)));
    String s=sc.nextLine();
    StringTokenizer st=new StringTokenizer(s);
    ArrayList<String> arr=new ArrayList<>();
    while(st.hasMoreTokens())
    {
    	arr.add(st.nextToken());
    }
int q1,q2,q3,q4,h1,h2,o1,o2;
int start1,start2=0;
int state1=1;
int state2=0;
start1=1;
String set1="";
String set2="";
String score="";
int score2=0;
int score1=0;
int startfault=0;
for(String a:arr)
{	
	if(start1==1 && state1==1)//player low starts
	{
		
		if(a.equals("Q1") || a.equals("Q2") || a.equals("O1"))
			{
			startfault++;
			if(startfault==2)
				{startfault=0;start2=1;state2=1;start1=0;state1=0;score2=score2+15;}
			else continue;
			
			}
		
			
	}
	else if(start2==1 && state2==1)
	{
		if(a.equals("Q3") || a.equals("Q4") || a.equals("O2"))
		{
		startfault++;
		if(startfault==2)
			{start2=0;state2=0;start1=1;state1=1;score1=score1+15;}
		else
		continue;
		}

	}
	
	if(score2>score1 && score2==30 && state2==1)
		score="Advantage Server";
	if(score2>score1 && score1==30 && state2==0)
		score="Advantage Receiver";
	if(score1==score2 && score1==40)
		score="Deuce";

}
if(state1==1) {
System.out.println(set1);
System.out.println(set2);
}
if(state2==1) {
System.out.println(set2);
System.out.println(set1);
}
System.out.println(score);
}
}