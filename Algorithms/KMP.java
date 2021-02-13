/*input
abcfabcf
bcabcfabcfabcfgdfgfrsscchabcfabcfjdcdjgcdhcg
*/
import java.util.*;
/*
Knuth Morris Pratt Algorithm
For String Matching in O(n)
*/
class KMP{
	public static void main(String[] args) {
		
    Scanner sc = new Scanner(System.in);
          
         String str = sc.nextLine();
         String ss  = sc.nextLine();
         int presuf[] = new int[str.length()];
         Arrays.fill(presuf,0);
         
          for(int i=1;i<str.length();i++){
           
           int j=presuf[i-1];
           
           while(str.charAt(i)!=str.charAt(j) && j>0){
           	
            j=presuf[j-1];
           
           }
           
          if(str.charAt(i)==str.charAt(j)){

            j++;

          }

            presuf[i] = j;
         
         }
         
         System.out.println(str);
          for(int i:presuf){
          	System.out.print(i+" ");
          }

         int k=0;
         int cnt=0;
         //outer:
         for(int i=0;i<ss.length();i++)
         {
              if(str.charAt(k)==ss.charAt(i)){
                cnt++;
              int j=i;
              while(j<ss.length() && ss.charAt(j)==str.charAt(k) ){
              cnt++;
              k++;
              j++;
              
              if(k==str.length())  
              {
                System.out.printf("Found at %d",i);
                //break outer;
                k=0;
              }

             }
            }

             else{cnt++; k=presuf[k];}

         }
         System.out.println("\n"+cnt);
         }

	}
