public class Demo{
	public static void main(String[] args){
		People viet = new Vietnamese();// define là class People nhưng khởi tạo instance của class Vietnamese.vì class Vietnamese đã extends class Pepple.
		viet.showNationality();//gọi fun showNationality() of class Vietnamese.vì instance của nó là Vietnamese extends class People.fun của class sub này thực thi chi tiết cho fun ở lớp super nó extends.
	}
}