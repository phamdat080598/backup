package innerpackage;
public class InnerPackage{
	public InnerPackage(){
	}
	public void runPublic(){
		System.out.println("runPublic");
	}
	
	protected void runProtected(){
		System.out.println("runProtected");
	}
	void runDefault(){
		System.out.println("runDefault");
	}
	
	private void runPrivate(){
		System.out.println("runPrivate");
	}
}