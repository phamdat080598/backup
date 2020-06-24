import java.io.*;

public class MyFile{
	private String path;
	private File mFile;
	
	public File getFile(){
		return mFile;
	}
	
	public String getPath(){
		return path;
	}
	
	public void setPath(String path){
		this.path = path;
	}
	
	public void createFile(){
		try{
			mFile = new File(path);
			if(mFile.exists()){
				System.out.println("File is exists!");
			}else{
				mFile.createNewFile();
				System.out.println("Create file successfull!");
			}
		}catch(IOException ex){
			System.out.println("Error File!" );
		}
	}
	
	public void writeFile(String s){
		FileOutputStream fos=null;
		try{
			fos = new FileOutputStream(mFile,true);
			byte[] content = s.getBytes();
			fos.write(content);
			fos.flush();
		}catch(IOException ex){
			System.out.println("Error File output stream: ");
		}
		finally{
			try{
				if(fos!=null){
					fos.close();
				}
			}catch(IOException ex){
				System.out.println("output close file error!");
			}
		}
	}
	
	public String readFile(){
		String list = "";
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(mFile);
			int word;
			while((word = fis.read())!=-1){
				list+=((char)word+"");
			}
		}catch(IOException ex){
			System.out.println("Error file stream input ! ");
		}
		finally{
			try{
				if(fis!=null){
					fis.close();
				}
			}catch(IOException ex){
				System.out.println("Error file input close! ");
			}finally{
				return list;
			}
		}
	}
}