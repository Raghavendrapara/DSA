import java.util.*;
class KMP{
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
          
         String str=sc.nextLine();
         String ss=sc.nextLine();
         int presuf[]=new int[str.length()];
         Arrays.fill(presuf,0);
         for(int i=1;i<str.length();i++)
         {
           
           int j=presuf[i-1];
           while(str.charAt(i)!=str.charAt(j) && j>0)
           {
           	j=presuf[j-1];
           } 
           
           
            if(str.charAt(i)==str.charAt(j))
            j++;
            presuf[i]=j;
         }
         System.out.println(str);
          for(int i:presuf)
          	System.out.print(i+" ");

         int k=0;
         for(int i=0;i<ss.length();i++)
         {
             if(ss.charAt(i)==str.charAt(k)){
             	k++;
             if(k==str.length())  
             	System.out.printf("Found at %d",i);

             }

             else k=presuf[k];

         }





      



         }

	}
