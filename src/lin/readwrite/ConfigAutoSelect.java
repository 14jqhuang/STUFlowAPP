package lin.readwrite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ConfigAutoSelect {

	private BufferedReader br;
	private PrintWriter pw;
	
	public ConfigAutoSelect() {
		// TODO Auto-generated constructor stub
	}
	
	public BufferedReader openFile() throws IOException
	{	
		try {
			File f=new File(ResourcePath.CONFIG1PATH);
//System.out.println("exist="+f.exists());
			if(!f.exists())
				f.createNewFile();
			br=new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return br;
	}
	
	public boolean readModel()
	{
		try {
			openFile();
			String temp="n";
			temp=br.readLine();
System.out.println(temp);
			if(temp.equals("")||temp==null)
			{
				br.close();
				return  false;
			}
			String model=temp.substring(0, 1);
			if(model.equals("y"))
			{	br.close();
				return true;
			}
			else {
				br.close();
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void writeY()
	{
		try {
			File f=new File(ResourcePath.CONFIG1PATH);
			pw=new PrintWriter(new FileOutputStream(f));
			pw.println("y");
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeN()
	{
		try {
			File f=new File(ResourcePath.CONFIG1PATH);
			pw=new PrintWriter(new FileOutputStream(f));
			pw.println("n");
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Deprecated
	public boolean delete()
	{
		File f=new File(ResourcePath.CONFIG1PATH);
		if(f.exists())
			return f.delete();
		else return true;
					
	}
	
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
