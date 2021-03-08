#include<bits/stdc++.h>
using namespace std;

void build(int arr[],int* tree,int i_start,int j_end,int segtree_index){
	if(i_start==j_end){
		tree[segtree_index] = arr[i_start];

		return;
	}
	int mid=(i_start + j_end)/2;
	build(arr, tree, i_start, mid, 2*segtree_index);
	build(arr, tree, mid+1, j_end, 2*segtree_index+1);
	tree[segtree_index]=tree[2*segtree_index]+tree[2*segtree_index+1];
}

void update(int arr[],int* tree,int i_start,int j_end,int segtree_index,int idx,int val){

	if(i_start==j_end){
		arr[i_start] = val;
		tree[segtree_index] = val;
		return;
	}
	int mid=(i_start + j_end)/2;
	if(idx>=i_start && idx<=j_end)
	update(arr, tree, i_start, mid, 2*segtree_index, idx, val);
	else
	update(arr, tree, mid+1, j_end, 2*segtree_index+1, idx, val);
	tree[segtree_index]=tree[2*segtree_index]+tree[2*segtree_index+1];

}

int query(int* tree, int left, int right, int i_start, int j_end, int segtree_index){
	
	if(i_start>=left && j_end<=right)
		return tree[segtree_index];
	if(left>j_end || right<i_start)
		return 0;
	int mid=(i_start + j_end)/2;
	int q1,q2;
	q1 = query(tree, left, right, i_start, mid, 2*segtree_index);

	q2 = query(tree, left, right, mid+1, j_end, 2*segtree_index+1);

	return q1+q2;
	
}


 int main(){


 int arr[8]={10,4,1,2,3,4,8,6};
 //int tree[17]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
 int* segtree=new int[2*8];
 build(arr, segtree, 0, 7, 1);

 update(arr, segtree, 0, 7, 1, 0, 20);
 
 for(int i=1;i<=15;i++)
 cout<<segtree[i]<<endl;
 cout<<"=================================="<<endl;
 int q=query(segtree,1,4,0,7,1);
 cout<<q<<endl;
 cout<<"==================================="<<endl;


}
