/*input
5 44 3 2 1 89 0 2 4 9
*/
#include<bits/stdc++.h>
using namespace std;
int* array = new int[10];
void merge(int* arr,int l,int r){

	int mid=(l+r)/2;
	int i,j;
	int* a=new int[mid-l+1];
	int* b=new int[r-mid];
	for(i=l;i<=mid;i++){
		a[i-l]=arr[i];
	}
	for(j=mid+1;j<=r;j++){
		b[j-mid-1]=arr[j];
	}
	int* c=new int[10];
	int cnt=l;
	i=0;j=0;
	for(;i<(mid-l+1) && j<(r-mid);){

		if(a[i]>b[j])
			arr[cnt]=a[i++];
		else
			arr[cnt]=b[j++];
		cnt++;
	}
	for(int k=i;k<mid-l+1;k++)
		{arr[cnt]=a[k];cnt++;}
	for(int k=j;k<r-mid;k++)
		{arr[cnt]=b[k];cnt++;}

}
void mergesort(int* arr,int left,int right){

	if(left>=right)
		return;
	int mid=(left+right)/2;
	mergesort(arr,left,mid);
	mergesort(arr,mid+1,right);
	merge(arr, left, right);
}

int sum1(int a,int b){
	return a+b;
}
int sum(int a,int b){
	return sum1(a,b);
}


int main(){
	int arr[10];
	for(int i=0;i<10;i++)
		cin>>arr[i];
	mergesort(arr,0,9);
	for(int i=0;i<=9;i++)
	cout<<arr[i]<<" ";

}


