import java.util.Scanner;

public class Demo{
	public static void main(String[] args){
		Scanner scanner =  new Scanner(System.in);
		System.out.println("nhập số a: ");
		int n = Integer.parseInt(scanner.nextLine());
		double x = (float)n;
		System.out.println("result a: "+ x);
	}
}	