/*input 
5 -1 -1 5 5 5 0 0 0 0
*/
#include<bits/stdc++.h>
using namespace std;

void swap(int arr[],int i,int j){
	int temp=arr[i];
	arr[i]=arr[j];
	arr[j]=temp;
}
  // Performs Hoare partition algorithm for quicksort
 /* private static int partition(int[] ar, int lo, int hi) {
    int pivot = ar[lo];
    int i = lo - 1, j = hi + 1;
    while (true) {
      do {
        i++;
      } while (ar[i] < pivot);
      do {
        j--;
      } while (ar[j] > pivot);
      if (i < j) swap(ar, i, j);
      else return j;
    }
  }*/
int findpivot(int s[],int l,int h){
int i;
/* counter */
int p;
/* pivot element index */
int firsthigh;
/* divider position for pivot element */
p = h;
firsthigh = l;
for (i=l; i<h; i++)
if (s[i] < s[p]) {
swap(s,i,firsthigh);
firsthigh++;
}
swap(s,firsthigh,p);
return(firsthigh);
}

void quicksort(int arr[], int i, int j){
		
	if(i>=j){
		return;
	}
	/*for(int k=0;k<10;k++){
		cout<<arr[k]<<setw(3);	
	    }
	    cout<<endl;
	cout<<"pivot"<<pivot<<endl;
	for(int k=0;k<10;k++){
		cout<<arr[k]<<setw(3);	
	    }
	    cout<<endl;
	 */
	int pivot=findpivot(arr,i,j);
	
	quicksort(arr,i,pivot-1);
	quicksort(arr,pivot+1,j);
}

int main(){
	int* arr=new int[10];
	for(int i=0;i<10;i++)
	cin>>arr[i];
	
	quicksort(arr,0,9);
	for(int i=0;i<10;i++){
		cout<<arr[i]<<setw(3);
	}
}