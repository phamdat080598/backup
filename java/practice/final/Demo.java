public class Demo{
	private final int value =1;
	public static void main(String[] args){
		value = 2 ;// error v� final n�n value chi duoc g�n gi� tri duy nhat 1 lan
		
	}
	
	
	private static class People{// static v� method main is static method
		public final void run(){
			System.out.println("people run....");
		}
	}
	
	private static final class VietNamese extends People{// static v� method main is static method
		public void run(){// error v� final ko cho ph�p Sub class overriding fun of Super class
			System.out.println("vietnamese run .....");
		}	
	}
	
	private static class LG extends VietNamese{// error not extends final class.
		
	}
}