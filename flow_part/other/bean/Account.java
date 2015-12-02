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

//��Ϊһ��bean��ʹ��,��̬����
public class Account implements ResourcePath{
	
	public  String jarPath;
	public HashMap<String,String> hashMap;
	public ArrayList<String> accountList;
	public String[] accountArrary;
	private BufferedReader br;
	
	/**
	 * �����ʱ����Ѿ����ļ��ж�ȡ��
	 * @throws IOException
	 */
	public Account() throws IOException {
		
		accountList=new ArrayList<String>();
		hashMap=new HashMap<String,String>();
		readAccount();
	}
	
	//��ȡ�ļ��е��û���
	private void readAccount() throws IOException
	{
		try {
			File f=new File(decode(ResourcePath.ACCOUNTPATH));
			if(!f.exists())
				f.createNewFile();
			br=new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			this.setHashMap(br);
		} catch (FileNotFoundException e) {
			accountList.add("������˻�");
		}
		accountArrary=new String[accountList.size()];
		accountList.toArray(accountArrary);
		br.close();
	}
	
	//�����û����������Ӧ��hash��
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
			JOptionPane.showMessageDialog(null, "�Ҳ���ָ�����ļ�\n"+this.getClass().getName());
		}
		
	}
	
	//��ȡ�û���,�����ж�ȡ�����ַ�����ȡ�û���
	private String getAccountName(String line)
	{
		String temp[]=line.split("&");
		int index=temp[0].indexOf("=");
		return temp[0].substring(index+1);
	}
	
	//�����˺�
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
	
	//ɾ���˺�
	public void drop(int removeInt)
	{
		if (accountList.size()!=0) {
			accountList.remove(removeInt);
			hashMap.remove(removeInt);
			if (accountList == null)
				accountList.add("������˺�");
			else {
				accountArrary = null;
				accountArrary = new String[accountList.size()];
				accountList.toArray(accountArrary);
			} 
		}
	}

	//��·�����б���
	@Override
	public String decode(String path) throws UnsupportedEncodingException {
		return URLDecoder.decode(path, "utf-8");
	}
}
