import java.util.Scanner;
public class Demo{
	public static void main(String[] args){
		MyFile myFile = new MyFile();
		myFile.setPath("content.txt");
		myFile.createFile();
		String content = new Scanner(System.in).nextLine();
		myFile.writeFile(content);
		System.out.println("Content of file : "+ myFile.readFile());
	}
}