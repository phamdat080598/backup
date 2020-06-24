public class Demo{
	public static void main(String[] args){
		DemoThis demoThis = new DemoThis();
		System.out.println(demoThis.getValue());
		
		DemoSuper demoSuper = new DemoSuper();
		System.out.println(demoSuper.getValue());
	}
	
}

class DemoThis{
	/*
	- this tham chiếu tới instance hiện tại của class.
	- call field,method,constructor.
	*/
	protected String value;
	
	static{
		System.out.println("prepare init....");//call trước constructor,call đầu tiên trước khi khởi tạo,
	}
	public DemoThis(){
		this("DemoThis");
		//this.setValue("->DemoThis");
	}
	private DemoThis(String value){
		this.value = value;
	}
	public void setValue(String value){
		this.value = value ;// call biến trong class,dể phân biệt khi tên biến class trùng với tham số của method.
	}
	
	public String getValue(){
		return this.value;
	}
}
class DemoSuper extends DemoThis{
	/*
		- tham chiếu thới super class gần nhất.
		- call field,method,constructor của super class.
	*/
	public DemoSuper(){
		super();
	}
	
	public String getValue(){
		return super.getValue();
	}
}