package lin.readwrite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

public class WriteAccount {
	public  PrintWriter out;
	/*
	 * �����Ҫ�ǵùر���
	 */
	public WriteAccount()  {
		// TODO Auto-generated constructor stub

		out=this.openStream(ResourcePath.ACCOUNTPATH);
	}
	
	public PrintWriter openStream(String path)
	{
		PrintWriter out=null;
		try {
			File f=new File(path);//��仰�������½��ļ�
			OutputStream in=new FileOutputStream(f,true);//���ļ�����׷������,������仰�����Զ��½���һ��
			out=new PrintWriter(in);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "���ļ�����ʧ��ʧ��\n"+this.getClass().getName());
		}
		return out;		
	}

	public void writeAccount(PrintWriter out,String name,String password)
	{
		String str="";
		str+="AuthenticateUser="+name+"&"+"AuthenticatePassword="+password+"&shit";
		out.append(str+"\r\n");
		out.close();
	}
	public void writeAccount(PrintWriter out,String params)
	{
		out.append(params+"\r\n");
	}
}
