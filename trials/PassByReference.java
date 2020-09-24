package algo;

public class PassByReference {

	public static void main(String args[])
	{
		
		  int arr[]= {2,3,4,5}; PassByReference ob=new PassByReference();
		  ob.reference(arr); System.out.println(arr[2]);
		 
	}
	void reference(int arr[])
	{
		
		arr[2]=3;
		
		
	}
}
