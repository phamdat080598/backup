import java.io.*;  
public class Demo{
	public static void main(String[] args){
		
		try{
			int a=0;
			if(a==0){
				throw new NotEnoughAgeException("em chua 18");
			}
		}catch(NotEnoughAgeException ex){
			System.out.println(ex);
		}
		//thowsEx();
		//throwEx();
		//mutilCatch();
	}
	
	public static void mutilCatch(){
		try{
			/*
			int[] a = {1,2,3,4};
			int b = a[10];
			String phone=null;
			boolean temp = phone.equals("a");//null pointer
			String name = "pvd";
			int number = Integer.parseInt(name);// number format*/
			try{
				int error = 5/0;//arithmetic
			}catch(Exception ex){
				System.out.println(ex);
			}finally{
				System.out.println("finally 1");
			}
		}catch(ArithmeticException ex){
			System.out.println(ex);
		}catch(NumberFormatException ex){
			System.out.println(ex);
		}catch(NullPointerException ex){
			System.out.println(ex);
		}catch(ArrayIndexOutOfBoundsException ex){
			System.out.println(ex);
		}finally{
			System.out.println("finally 2");
		}	
	}
	
	public static void throwEx(){
		throw new ArithmeticException("chua du 18 tuoi");
	}
	
	public static void thowsEx() throws IOException{
		System.out.println("hello world");
	}
}

class NotEnoughAgeException extends Exception{
	public NotEnoughAgeException(String a){
		super(a);
	}
}