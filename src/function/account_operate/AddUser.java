package function.account_operate;

import java.io.IOException;

import other.bean.Account;
import other.tool.WriteAccount;

public class AddUser implements AccountOperator {

	private String param;
	
	public AddUser(Account account, String param) {
		this.param=param;
	}
	
	//ֱ�Ӵ��ļ��ж�ȡ��
	private Account addUser() throws IOException
	{
		WriteAccount.writeAccount(param);
		return new Account();
	}
	@Override
	public Account operate() throws Exception {
		return addUser();
	}

}
