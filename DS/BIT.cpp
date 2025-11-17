/*input
5
1 2 3 4 5
*/
#include<iostream>
#include<cstring>
using namespace std;

void update(int* arr, int* BIT, int index, int val, int size)
{
	for(;index<=size;index+=(index & (-index)))
	{
		BIT[index] = BIT[index] + val;
	}

}
int query(int* BIT, int index){
	int sum=0;
	for(;index>0;index-=(index & (-index)))
	sum=sum+BIT[index];
	return sum;
}

int main()
{
	int n;
	cin>>n;
	int* arr = new int[n+1];

	int* BIT = new int[n+1];
	for(int i=1;i<=n;i++)
	{
		cin>>arr[i];
		update(arr,BIT,i,arr[i],n);
	}
	cout<<query(BIT,4);
}