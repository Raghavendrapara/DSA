import java.util.*;
class Fenwick
{

   void update(int arr[],int index,int value){
     
     int indTemp=index;
     while(indTemp<11)
     {
     	arr[indTemp]+=value;
     	indTemp+=(indTemp&(-indTemp));
     }   


   }

   int query(int arr[],int pos){

     int sum=0;
     while(pos > 0){
     	sum+=arr[pos];
     	pos-=pos&(-pos);
     }


      return sum;
   }


	/*public static void main(String[] args) {
	
	         Scanner sc=new Scanner(System.in);
            int n=11;
            int arr[]=new int[n];
            int arr1[]=new int[n];
             for(int i=1;i<=10;i++){
            	arr[i]=sc.nextInt();
            	update(arr1,i,arr[i]);
            }

        System.out.println(query(arr1,7)-query(arr1,5));
	}*/
}