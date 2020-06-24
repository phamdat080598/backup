public class Demo{
	private final int value =1;
	public static void main(String[] args){
		value = 2 ;// error vì final nên value chi duoc gán giá tri duy nhat 1 lan
		
	}
	
	
	private static class People{// static vì method main is static method
		public final void run(){
			System.out.println("people run....");
		}
	}
	
	private static final class VietNamese extends People{// static vì method main is static method
		public void run(){// error vì final ko cho phép Sub class overriding fun of Super class
			System.out.println("vietnamese run .....");
		}	
	}
	
	private static class LG extends VietNamese{// error not extends final class.
		
	}
}