import java.util.*;
public class Demo{
	public static void main(String[] a){
		System.out.println("input n: ");
		int n = new Scanner(System.in).nextInt();
		System.out.println("Result:" + recursion(n));
	}
	
	public static int recursion(int n){
		/*
		- liệt kê các phép toán +  chỉ có n=0 và n=1 góp mặt.
		- kết quả là tổng nhiều lần n=0 và n=1
		*/
		if(n==1||n==0){
			return n;//đến đây chương trình sẽ thực hiện tính toán sau khoảng thời gian sẽ xuất kết quả.
		}else{
			return recursion(n-1)+recursion(n-2);//call lại giảm dần n cho tới khi có giá trị cụ thể thì thôi
		}
	}
}