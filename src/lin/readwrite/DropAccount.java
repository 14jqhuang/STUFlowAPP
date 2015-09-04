package lin.readwrite;

import java.io.File;
import java.io.IOException;

import lin.component.ButtonAreaPanel;

public class DropAccount {
	private File file;
	public DropAccount(int index) {
		// TODO Auto-generated constructor stub
		drop(index);
	}
	
	public boolean deleteFile()
	{
		file=new File(ResourcePath.ACCOUNTPATH);
		if(file.exists())
			return file.delete();
		else return true;
	}
	public void drop(int removeInt)
	{
		ButtonAreaPanel.readAccount.drop(removeInt);
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
