import java.util.*;

public class Demo{
	public static void main(String[] args){
		System.out.println("nhap so fibinacci : ");
		int n = Integer.parseInt(new Scanner(System.in).nextLine());
		System.out.println("result : " + algorithm(n));
		System.out.println("result : " + recursive(n));
	
	}
	
	public static int algorithm(int n){
		if(n==0||n==1){
			return n;
		}else{ 
			int index;
			int numberOne=0;
			int numberTwo=1;
			int result=0;
			for(index=2;index<=n;index++){
				result = numberOne+numberTwo;
				numberOne = numberTwo;
				numberTwo = result;
			}
			return result;
		}
	}
	
	public static int recursive(int n){
		if(n==0||n==1){
			return n;
		}else{
			return recursive(n-1)+recursive(n-2);
		}
	}
}