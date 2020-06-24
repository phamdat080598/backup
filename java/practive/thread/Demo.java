public class Demo{
	public static void main(String args[]){
		/*createThread();
		createThread();
		createRunnable();
		createRunnable();*/
		
		People peo = new People();
		peo.setName("dat");
		
		MyThread t1 = new MyThread(peo,10);
		MyThread t2 = new MyThread(peo,12);
		
		t1.start();
		try{  
			t1.join();  
		}catch(Exception e){System.out.println(e);}  
	
		
		t2.start();
	}
	
	public static void createThread(){
		Thread t1 = new Thread(){
			public void run(){
				for(int i=0;i<10;i++){
					System.out.println("Thread running: "+i);
					try{Thread.sleep(500);}catch(InterruptedException e){System.out.println(e);}  
				}
			}
		};
		t1.start();
	}
	
	public static void createRunnable(){
		Thread t2 = new Thread(new Runnable(){
			public void run(){
				for(int i=0;i<10;i++){
					System.out.println("Runnable running: "+i);
					try{Thread.sleep(500);}catch(InterruptedException e){System.out.println(e);}  
				}
			}
		});
		t2.start();
	}
}

class People{
	private String name;

	public void setName(String name){
		this.name = name;
	}
	
	public void getName(){
		System.out.println(this.name);
	}
}

class MyThread extends Thread{
	private People peo;
	private int n;
	
	public MyThread(People peo,int n){
		this.peo = peo;
		this.n = n;
	}
	
	public void run(){
		peo.getName();
		for(int i=0;i<n;i++){
			peo.setName("Name: "+ i);
			System.out.println(Thread.currentThread()+" "+i);
		}
		peo.getName();
	}
}