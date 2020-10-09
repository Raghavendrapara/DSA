/*input
10
1 3 5 6 7 8 9 0 12 13
3 3
1 2
1 10
5 6
9 10
*/
import java.util.*;
class SqrtDecomp{

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		int size=sc.nextInt();
		int arr[] = new int[size];
        
        for (int i=0;i<size ;i++) {
        	
        	arr[i]=sc.nextInt();
        
        }

        int len=(int)Math.sqrt(size);
        int b[]=new int[len+1];
        for(int i=0;i<size;i++){

        	b[i/len]+=arr[i];
        
        }
        //Queries
        for(int i=1;i<=5;i++)
        {

        	int l=sc.nextInt();
        	int r=sc.nextInt();
        	int sum=0;
        	for(int j=l-1;j<r;j++){

        		if(j%len==0 && j+len<r){

        			sum+=b[j/len];
        			if(j+len<r)
        				j+=len;        		
        		}
        		sum+=arr[j];
            }
        	
        	System.out.println(sum);
        }

    }
}



