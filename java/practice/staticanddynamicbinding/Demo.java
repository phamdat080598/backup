public class Demo{
	public static void main(String[] args){
		Vietnamese vie = new Vietnamese();// binding static : type of "vie" được quyết định tại compile time
		vie.run();// xác định được class sẽ call fun này tại compile time
		People people = new Vietnamese();// dynamic static : 
		people.run();//compiler ko thể biết được people là type nào.vì people vừa là 1 thể hiện của class Vietnamese và People.Trong lúc runtime chương trình sẽ xác định được.
		people.eyes();// do chưa xác định được type lên sẽ lỗi compile time.
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