public class Demo{
	public static void main(String[] args){
		Vietnamese vie = new Vietnamese();
		People peo = vie;//upcasting
		peo.run();
		
		People peoTwo = new Vietnamese();
		Vietnamese vieTwo = (Vietnamese)peoTwo;//downcasting
		vieTwo.eyes();
	
		
	}
	
	static class People{
		public void run(){
			System.out.println("people run...");
		}
		
	}
	
	static class Vietnamese extends People{
		public void run(){
			System.out.println("vietnamese run ...");
		}
		
		public void eyes(){
			System.out.println("vietnamese eyes..");
		}
	}
}