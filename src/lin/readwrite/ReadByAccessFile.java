package lin.readwrite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.JOptionPane;
@Deprecated
public class ReadByAccessFile {
	private String jarPath;
	private RandomAccessFile randomaccessfile;
	public ReadByAccessFile() throws IOException {
		// TODO Auto-generated constructor stub
		this.setJarPath();
		randomaccessfile=openAccessFile(jarPath);
//		this.update(3, "13blank", "ppppp");
//this.printData(randomaccessfile);
	}
	
	public RandomAccessFile openAccessFile(String path) throws IOException
	{
		File f=new File(path);
		RandomAccessFile randomfile = null;
		try {
			randomfile=new RandomAccessFile(f, "rw");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "�ļ�û���ҵ�");
		}
		return randomfile;
	}
	
	public void setJarPath()
	{//��ȡ�ⲿjar��������
		this.jarPath= this.getClass().getClassLoader().getResource("").getPath()+"account.txt";
	}
	public void skip(int times)
	{
		try {
		if(randomaccessfile.length()-randomaccessfile.getFilePointer()>=new Long(times))
			for(int i=1;i<=times;i++)
				randomaccessfile.readLine();
		else JOptionPane.showMessageDialog(null, "��תλ�ó����ı�������");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "��Ծʧ��");
		}
	}
	public void printData(RandomAccessFile randomaccessfile)
	{
			try {
				skipTo(0);
				while(randomaccessfile.getFilePointer()!=randomaccessfile.length())
					System.out.println(randomaccessfile.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public void  update(int index,String name,String password)
	{
		try {
			skipTo(index);
			randomaccessfile.writeBytes("AuthenticateUser="+name+"&"+"AuthenticatePassword="+password+"&shit=");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "�޸�ʧ��");
		}
	}
	public void skipTo(int index)
	{
		try {
			this.randomaccessfile.seek(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "����RandomAccessFIleʧ��");
		}
		skip(index);
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new ReadByAccessFile();
	}

}
