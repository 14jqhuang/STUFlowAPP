package lin.readwrite;

import java.io.File;
import java.io.IOException;

import lin.component.ButtonAreaPanel;

public class UpdateAccount {
	/*
	 * ˼·�Ǹ���ReadAccount�е�����,Ȼ���ԭ�����ļ�ɾ��,���½���һ���ļ�
	 */
	private File file;
	public UpdateAccount(int index,String name,String password) {
		// TODO Auto-generated constructor stub
		update(index, name, password);
	}
	
	public boolean deleteFile()
	{
		file=new File(ResourcePath.ACCOUNTPATH);
		if(file.exists())
			return file.delete();
		else return true;
	}
	public void update(int removeInt,String name,String password)
	{
		ButtonAreaPanel.readAccount.Update(removeInt,name,password);
		if(this.deleteFile())
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 WriteAccount wa=new WriteAccount();
		for(String key:ButtonAreaPanel.readAccount.accountArrary)
		{
			wa.writeAccount(wa.out,ButtonAreaPanel.readAccount.hashMap.get(key));
		}
		wa.out.close();
		try {
			ButtonAreaPanel.readAccount=new ReadAccount();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
