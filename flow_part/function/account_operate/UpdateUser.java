package function.account_operate;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import other.bean.Account;
import other.tool.WriteAccount;
import resource.webserver.ResourcePath;

public class UpdateUser implements ResourcePath,AccountOperator{
	/*
	 * 思路是更改ReadAccount中的数据,然后把原来的文件删除,重新建立一个文件
	 */
	private File file;
	private int index;
	private String name;
	private String password;
	private Account account;
	
	public UpdateUser(Account account,int index,String name,String password) throws UnsupportedEncodingException {
		this.index = index;
		this.name = name;
		this.password = password;
		this.account = account;
	}
	
	private boolean deleteFile() throws UnsupportedEncodingException
	{
		file=new File(decode(ResourcePath.ACCOUNTPATH));
		if(file.exists())
			return file.delete();
		else return true;
	}
	private Account update() throws UnsupportedEncodingException
	{
		account.Update( index , name , password);
		if(this.deleteFile())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

		for(String key:account.accountArrary)
		{
			WriteAccount.writeAccount(account.hashMap.get(key));
		}
		
		return this.account;
	}

	@Override
	public String decode(String path) throws UnsupportedEncodingException {
		return URLDecoder.decode(path, "utf-8");
	}

	@Override
	public Account operate() throws Exception {
		return update();
	}
}
