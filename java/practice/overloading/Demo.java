public class Demo{
	public static void main(String[] args){
		System.out.println("sum:"+ sumTwoNumber(1,1));
		System.out.println("sum:"+ sumTwoNumber(1,1f));
		System.out.println("sum:"+ sumTwoNumber(1,1d));
		System.out.println("sum:"+ sumTwoNumber(1,1,1));
	}
	
	public static int sumTwoNumber(int a,int b){
		return a+b;
	}
	
	public static int sumTwoNumber(int a,int b,int c){
		return a+b+c;
	}
	
	public static float sumTwoNumber(int a,float b){
		return a+b;
	}
	
	public static double sumTwoNumber(int a,double b){
		return a+b;
	}
}