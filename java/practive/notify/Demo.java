public class Demo {
	public static void main(String args[]){
		Bank bank = new Bank(100);
		MyThread t1 = new MyThread(bank){
			public void run(){
				this.getBank().withDraw();
			}
		};
		
		MyThread t2 = new MyThread(bank){
			public void run(){
				this.getBank().accessWithDraw();
			}
		};
		
		MyThread t3 = new MyThread(bank){
			public void run(){
				this.getBank().getMoney();
			}
		};
		
		t1.start();
		t2.start();
		t3.start();
		
	}
	
}

class MyThread extends Thread{
	private Bank bank;
	
	public MyThread(Bank bank){
		this.bank =  bank;
	}
	
	public Bank getBank(){
		return this.bank;
	}
}

class Bank{
	private int money;
	
	public Bank(int money){
		this.money = money;
	}
	synchronized public void withDraw(){
		System.out.println("chuan bi ru tien....");
		try{
			wait();
		}catch(Exception e){
			
		}
		this.money = 90;
		System.out.println("rut tien thanh cong....");
		notify();
	}
	synchronized public void accessWithDraw(){
		System.out.println("cho phep rut tien... delay 2,5s");
		for(int i=0;i<5;i++){
			try{
				Thread.sleep(500);
			}catch(Exception ex){
				
			}
		}
		notify();
	}
	
	synchronized public void getMoney(){
		try{
			wait();
		}catch(Exception e){
			
		}
		System.out.println("so tien du la: "+this.money);
		System.out.println("complete!");
	}
}