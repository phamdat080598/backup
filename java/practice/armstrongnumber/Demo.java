import java.util.*;
public class Demo{
	public static void main(String[] a){
		System.out.print("input n: ");
		int n = new Scanner(System.in).nextInt();
		System.out.println("Is Armstrong: "+ isArmstrong(n));
	}
	
	public static boolean isArmstrong(int n){
		int result=0;//gán result = 0 dùng để so sánh với n
		int temp = n;// lưu bản sao n để lấy các chữ số tồn tại trong n.
		while(true){
			int surplus = temp%10;//lấy ra từng số 1 trong n bắt đầu từ hàng đơn vị.
			result += surplus*surplus*surplus;//tính tổng
			if((temp/=10)==0){// mỗi lần lấy ra 1 số ta sẽ giảm đi 1 đơn vị(3 chữ số -> 2 chữ số).đến khi n chỉ còn 1 chữ số.
				break;//exist
			}
		}
		if(result==n){// so sánh để xem có phải là Armstrong không?
			return true;
		}
		return false;
	}
}