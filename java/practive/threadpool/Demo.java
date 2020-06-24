import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
public class Demo{
	public static void main(String[] args){
		ExecutorService executor = Executors.newFixedThreadPool(5);
		for(int i=0;i<10;i++){
			MyRunnable work = new MyRunnable(i+"");
			executor.execute(work);
		}
		executor.shutdown();
	}
}

class MyRunnable implements Runnable{
	private String name;
	
	public MyRunnable(String name){
		this.name = name;
	}
	public void run(){
		System.out.println(Thread.currentThread()  +"work start...."+ name);
		try{
			Thread.sleep(1000);
		}catch(Exception ex){
			
		}
		System.out.println(Thread.currentThread()  +"work end...." + name);
	}
}