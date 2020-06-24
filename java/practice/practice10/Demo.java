import innerpackage.*;
public class Demo extends InnerPackage{
	/*
	- demo chủ yếu về method,với Class và Field cũng tương tự như vậy
	*/
	public static void main(String[] args){
		InnerPackage inner = new InnerPackage();
		inner.runPublic();//inner.runDefault(); error default không có permissiton truy cập ngoài package. inner.runPrivate() cũng vậy.
		
		Demo outer = new Demo();
		outer.runProtected(); // có thể call method không cùng package
		
	}
}