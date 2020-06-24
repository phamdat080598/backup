public class Demo{
	public static void main(String[] args){
		People peo = new People();
		
		MyThread1 t1 = new MyThread1(peo,5);
		MyThread1 t2 = new MyThread1(peo,10);
		
		t1.start();
		t2.start();
	}
}
class People{
	synchronized public void source(int n){
		for(int i=0;i<n;i++){
			System.out.println("result: "+ i*n);
		}
	}
}

class MyThread1 extends Thread{
	private People peo;
	private int n;
	public MyThread1(People peo,int n){
		this.peo = peo;
		this.n = n;
	}
	
	public void run(){
		peo.source(n);
	}
}

