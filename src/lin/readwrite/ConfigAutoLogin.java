package lin.readwrite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ConfigAutoLogin implements ResourcePath{
	private String jarPath;
	private BufferedReader br;
	private PrintWriter pw;
	public ConfigAutoLogin() throws IOException {
		// TODO Auto-generated constructor stub
		jarPath=this.decode(ResourcePath.CONFIGPATH);
	}
	/*
	 * 使用局部的file变量会自动创建,没有使用的话也不会创建
	 * 但是不知道为什么使用成员变量没办法创建文件?
	 */
	
	public BufferedReader openFile() throws IOException
	{	
		try {
			File f=new File(jarPath);
			if(!f.exists())
				f.createNewFile();
			br=new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return br;
	}
	
	public int readModel()
	{
		try {
			openFile();
			String temp="none";
			temp=br.readLine();
			br.close();
			if(temp==null)
				return 0;
			String model=temp.substring(0, 1);
			if(model.equals("!"))
				return 1;
			else if(model.equals("?"))
				return -1;
			else if(model.equals("#"))
				return 2;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}
	
	public String readName() throws IOException
	{
		openFile();
		String temp=br.readLine();
		br.close();
		return temp.substring(1);
	}
	//写入?代表没有开启自动登录的功能,这个账号是退出账号的时候写入的
	public void write_1Name(String name)
	{
		try {
			File f=new File(jarPath);
			pw=new PrintWriter(new FileOutputStream(f));//没有开启追加功能,会覆盖,意味着文件里只能有一个账号
			pw.println("?"+name);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//这个写入的是!号,表示1号  open 指的是有没有开启自动切换账号
	public void write1Name(String name)
	{
		try {
			File f=new File(jarPath);
			pw=new PrintWriter(new FileOutputStream(f));
			pw.println("!"+name);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//这个写#代表2	open 指的是有没有开启自动切换账号
	public void write2Name(String name)
	{
		try {
			File f=new File(jarPath);
			pw=new PrintWriter(new FileOutputStream(f));
			pw.println("#"+name);
			pw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Deprecated
	public char writeSeletMark(boolean open)
	{
		if(open)
			return 'Y';
		else return 'N';
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new ConfigAutoLogin();
	}
	@Override
	public String decode(String path) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return URLDecoder.decode(path, "utf-8");
	}

}
