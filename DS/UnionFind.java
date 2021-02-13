/*input 

*/
import java.util.*;
import java.io.*;
import static java.lang.System.out;
class UnionFind{

private int link[];
private int size[];

UnionFind(int n){

	link = new int[n];
	size = new int[n];

}

void union(int a,int b){
 
 	a=find(a);
 	b=find(b);
 	if(size[a] < size[b]){

 		int temp=a;
 		a=b;
 		b=temp;
 	}
 	size[a]+=size[b];
 	link[b] = a;

}

int find(int x){

	while(x != link[x]){
		x=link[x];
	}
	return x;

}

boolean same(int x,int y){
	
	if(find(x) == find(y)){
	
		return true;
	
	}
	
 	return false;
}

int[] getLink(){

return link;

}
int[] getSize(){

return size;

}
void setLink(int arr[]){

link=arr;
}
void setSize(int arr[]){
size=arr;
}

public static void main(String args[]){
	int n=10;
	UnionFind ob = new UnionFind(n);
	for(int i=1;i<=10;i++)
		ob.link[i-1]=i-1;

	ob.union(2,4);
	ob.union(2,7);
	ob.union(3,9);
	out.println(ob.find(4)+"   "+ob.find(7)+"  "+ob.same(2,7));
}


}
	

