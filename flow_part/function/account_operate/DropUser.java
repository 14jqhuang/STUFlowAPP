package function.account_operate;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import other.bean.Account;
import other.tool.WriteAccount;
import resource.webserver.ResourcePath;

public class DropUser implements ResourcePath, AccountOperator{
	
	private File file;
	private Account account;
	private int index;
	
	public DropUser(Account account , int index) throws UnsupportedEncodingException {
		this.index = index;
		this.account = account;
	}
	
	private boolean deleteFile() throws UnsupportedEncodingException
	{
		file=new File(decode(ResourcePath.ACCOUNTPATH));
		if(file.exists())
			return file.delete();
		else return true;
	}
	
	private Account drop() throws UnsupportedEncodingException
	{
		this.account.drop(this.index);
		
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
		
		return account;
	}

	@Override
	public String decode(String path) throws UnsupportedEncodingException {
		return URLDecoder.decode(path, "utf-8");
	}

	@Override
	public Account operate() throws Exception {
		return drop();
	}
}
