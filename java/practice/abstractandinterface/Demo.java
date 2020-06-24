public class Demo{
	public static void main(String[] args){
		Vietnamese vie = new LG();
		vie.address();//call method override 
		vie.eyes();//call method override,chứ không call method của super class.
		vie.color = "red";
		System.out.println("vietnamese color : "+vie.color);
		
		
		People peo = new LG();
		peo.look();
		peo.run();
		//peo.dance(); lỗi compile 
		People.dance();
		System.out.println(peo.value);
	}
	
	public static class LG extends Vietnamese implements People{
		void address(){//override method abstract of abstract class.
			System.out.println("LG address ....");
		}
		
		public void run(){//override method của interface
			System.out.println("LG run....");
		}
		
		public void eyes(){//override method non abstract của abstract class
			System.out.println("LG eyes ....");
		}
		
	}
	
	public static abstract class Vietnamese{
		protected String color="";
		abstract void address();
	
		public void eyes(){
			System.out.println("Vietnamese eyes ....");
		}
	
	}
	
	public static interface People{
		public static final String value = "interface people";
		void run();
		/*
			- từ java 8 có thêm static và default trong interface
			- static thuộc quản lí của class.(object không thể call được method này lí do thì em không biết:) ).
			- default thuộc quản lí object.
		*/
		static public void dance(){
			System.out.println("people dance...");
		}
		
		default public void look(){
			System.out.println("people look...");
		}
	}
}