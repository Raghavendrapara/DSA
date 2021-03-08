/*input
16
*/
#include <bits/stdc++.h>
using namespace std;
int main(){
	int a;
	cin>>a;
	int flag=0;
	do{
	if(a<21){
		cout<<"A"<<endl;
		flag=1;
	}
	if(a==20){
		cout<<"B"<<endl;
		flag++;
		if(flag==2)break;
	}
	else{
		cout<<"C"<<endl;
		break;

	}
	}while(true);
}