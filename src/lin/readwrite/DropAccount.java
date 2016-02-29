package lin.readwrite;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import lin.component.ButtonAreaPanel;

public class DropAccount implements ResourcePath{
	private File file;
	public DropAccount(int index) throws UnsupportedEncodingException {
		// TODO Auto-generated constructor stub
		drop(index);
	}
	
	public boolean deleteFile() throws UnsupportedEncodingException
	{
		file=new File(decode(ResourcePath.ACCOUNTPATH));
		if(file.exists())
			return file.delete();
		else return true;
	}
	public void drop(int removeInt) throws UnsupportedEncodingException
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

	@Override
	public String decode(String path) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return URLDecoder.decode(path, "utf-8");
	}
}
