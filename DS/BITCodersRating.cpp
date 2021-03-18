/*input
6
1 5 4 8 7 9 8 5 2 6 1 6
*/
#include<bits/stdc++.h>
using namespace std;
#define f(i,n) for(int i=0;i<n;i++)

class Coder{
public:
	int a,b;
	int index;
};

int compare(Coder x,Coder y){
	return (x.a>y.a)?0:(x.a==y.a)?(x.b>y.b)?0:1:1;
}
int main(){
int n;
cin>>n;
Coder* arr = new Coder[n];
int* BIT = new int[1001];
f(i,n){
	int a,b;
	cin>>a;
	cin>>b;
	arr[i].a=a;arr[i].b=b;arr[i].index=i;
}
sort(arr,arr+n,compare);
f(i,n){
	cout<<arr[i].a<<" "<<arr[i].b<<" "<<endl;
}


}
