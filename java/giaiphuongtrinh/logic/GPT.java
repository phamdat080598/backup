package logic;
import java.util.Scanner;
public class GPT{
	/*
		giải phương trình bậc 2 ax^2+bx+c=0
	*/
	private Double a,b,c;
	
	public GPT(Double a,Double b,Double c){
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	public void inputMember(){
		Scanner keyboard = new Scanner(System.in);
		this.a = Double.parseDouble(keyboard.nextLine());
		this.b = Double.parseDouble(keyboard.nextLine());
		this.c = Double.parseDouble(keyboard.nextLine());
	}
	
	public void outputStructure(){
		System.out.println(this.a+"x^2"+" + "+this.b + "x"+ " + "+this.c+" = 0");
	}
	
	public void calculate(){
		if(a==0.0d && b!=0.0d && c!=0.0d){
			System.out.println("phuong trinh co 1 nghiem duy nhat: "+ (-c/b));
		}else if(a==0.0d && b==0.0d && c==0.0d){
			System.out.println("phuong trinh co vo so nghiem!");
		}else if(a==0.0d && b==0.0d && c!=0.0d){
			System.out.println("phuong trinh vo nghiem!");
		}else{
			Double delta = b*b - 4*a*c;
			if(delta<0){
				System.out.println("phuong trinh vo nghiem!");
			}else if(delta==0.0d){
				System.out.println("phuong trinh co nghiem kep : x = "+ -b/(2*a));
			}else {
				System.out.println("phuong trinh co 2 nghiem phan biet x1, x2 : ");
				Double x1 = (-b-Math.sqrt(delta))/(2*a);
				Double x2 = (-b+Math.sqrt(delta))/(2*a);
				
				System.out.println("x1 = "+ x1);
				System.out.println("x2 = "+ x2);
			}
		}
	}
}