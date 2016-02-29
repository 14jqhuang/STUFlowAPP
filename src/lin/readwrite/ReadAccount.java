package lin.readwrite;

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

public class ReadAccount implements ResourcePath{
	
	public  String jarPath;
	public HashMap<String,String> hashMap;
	public ArrayList<String> accountList;
	public String[] accountArrary;
	private BufferedReader br;
	public ReadAccount() throws IOException {
		// TODO Auto-generated constructor stub
		accountList=new ArrayList<String>();
		hashMap=new HashMap<String,String>();
		try {
			File f=new File(decode(ResourcePath.ACCOUNTPATH));
			if(!f.exists())
				f.createNewFile();
			br=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			this.setHashMap(br);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
//			JOptionPane.showMessageDialog(null, "首次运行请先添加账户");
			accountList.add("请添加账户");
		}
		accountArrary=new String[accountList.size()];
		accountList.toArray(accountArrary);
		br.close();
	}
	
	public void setHashMap(BufferedReader br) throws IOException
	{
		try {
			String line;
			while((line=br.readLine())!=null)
			{	
					hashMap.put(this.getAccountName(line).trim(),line.trim());
					accountList.add(this.getAccountName(line).trim());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "找不到指定的文件\n"+this.getClass().getName());
		}
		
	}
	public String getAccountName(String line)
	{
		String temp[]=line.split("&");
		int index=temp[0].indexOf("=");
		return temp[0].substring(index+1);
	}
	
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

	@Override
	public String decode(String path) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return URLDecoder.decode(path, "utf-8");
	}
}
