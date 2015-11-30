package other.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import resource.webserver.ResourcePath;

//作为一个bean来使用,静态声明
public class Account implements ResourcePath{
	
	public  String jarPath;
	public HashMap<String,String> hashMap;
	public ArrayList<String> accountList;
	public String[] accountArrary;
	private BufferedReader br;
	
	/**
	 * 构造的时候就已经从文件中读取了
	 * @throws IOException
	 */
	public Account() throws IOException {
		
		accountList=new ArrayList<String>();
		hashMap=new HashMap<String,String>();
		readAccount();
	}
	
	//读取文件中的用户名
	private void readAccount() throws IOException
	{
		try {
			File f=new File(decode(ResourcePath.ACCOUNTPATH));
			if(!f.exists())
				f.createNewFile();
			br=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			this.setHashMap(br);
		} catch (FileNotFoundException e) {
			accountList.add("请添加账户");
		}
		accountArrary=new String[accountList.size()];
		accountList.toArray(accountArrary);
		br.close();
	}
	
	//设置用户名与密码对应的hash表
	private void setHashMap(BufferedReader br) throws IOException
	{
		try {
			String line;
			while((line=br.readLine())!=null)
			{	
					hashMap.put(this.getAccountName(line).trim(),line.trim());
					accountList.add(this.getAccountName(line).trim());
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "找不到指定的文件\n"+this.getClass().getName());
		}
		
	}
	
	//获取用户名,从流中读取到的字符串获取用户名
	private String getAccountName(String line)
	{
		String temp[]=line.split("&");
		int index=temp[0].indexOf("=");
		return temp[0].substring(index+1);
	}
	
	//更改账号
	public void Update(int removeInt,String newname,String newpassword)
	{
		if (accountList.size()!=0) {
			accountList.remove(removeInt);
			accountList.add(removeInt, newname);
			hashMap.remove(removeInt);
			hashMap.put(newname, "AuthenticateUser=" + newname + "&" + "AuthenticatePassword=" + newpassword + "&shit");
			accountArrary = null;
			accountArrary = new String[accountList.size()];
			accountList.toArray(accountArrary);
		}
	}
	
	//删除账号
	public void drop(int removeInt)
	{
		if (accountList.size()!=0) {
			accountList.remove(removeInt);
			hashMap.remove(removeInt);
			if (accountList == null)
				accountList.add("请添加账号");
			else {
				accountArrary = null;
				accountArrary = new String[accountList.size()];
				accountList.toArray(accountArrary);
			} 
		}
	}

	//对路径进行编码
	@Override
	public String decode(String path) throws UnsupportedEncodingException {
		return URLDecoder.decode(path, "utf-8");
	}
}
