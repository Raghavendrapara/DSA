
import java.util.*;
public class SegmentTree
{
public static void main(String[] args) {
	
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt();
        int arr[]=new int[n];
        for(int i=0;i<n;i++)
        arr[i]=sc.nextInt();


         int t[]=new int[n*4];
         build(arr,1,0,n-1,t);
         System.out.println(sum(1,0,n-1,1,4,t));
         update(1,0,n-1,2,12,t);
         System.out.println(sum(1,0,n-1,1,4,t));
         update(1,0,n-1,6,-2,t);
         System.out.println(sum(1,0,n-1,1,6,t));
         update(1,0,n-1,2,-12,t);
         System.out.println(sum(1,0,n-1,0,3,t));
         update(1,0,n-1,0,12,t);
         System.out.println(sum(1,0,n-1,0,3,t));
}
static void build(int arr[],int v,int tl,int tr,int t[])
{
if(tl==tr)
t[v]=arr[tl];
else{
int tm=(tl+tr)/2;
build(arr,v*2,tl,tm,t);
build(arr,v*2+1,tm+1,tr,t);
t[v]=t[v*2]+t[v*2+1];
}
}
static int sum(int v,int tl,int tr,int l,int r,int t[])
{
if(l>r)
return 0;
if(l==tl && r==tr)
return t[v];
int tm=(tl+tr)/2;
return(sum(v*2,tl,tm,l,Math.min(r,tm),t)+sum(v*2+1, tm+1, tr, Math.max(l,tm+1),r,t));


}
static void update(int v,int tl,int tr,int pos,int new_val,int t[])
{
if (tl == tr) {
        t[v] = new_val;
    } else {
        int tm = (tl + tr) / 2;
        if (pos <= tm)
            update(v*2, tl, tm, pos, new_val,t);
        else
            update(v*2+1, tm+1, tr, pos, new_val,t);
        t[v] = t[v*2] + t[v*2+1];
    }
}
}
