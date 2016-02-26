package other.tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.swing.JOptionPane;

import resource.webserver.ResourcePath;

public class WriteAccount{

	private WriteAccount() throws Exception  {
		throw new Exception("��Ҫʵ���������");
	}
	
	private static PrintWriter openStream(String path)
	{
		PrintWriter out=null;
		try {
			File f=new File(URLDecoder.decode(ResourcePath.ACCOUNTPATH, "utf-8"));//��仰�������½��ļ�
			OutputStream in=new FileOutputStream(f,true);//���ļ�����׷������,������仰�����Զ��½���һ��
			out=new PrintWriter(in);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "���ļ�����ʧ��ʧ��");
		}
		return out;		
	}

	public static  void writeAccount(String name,String password)
	{
		PrintWriter out=openStream(ResourcePath.ACCOUNTPATH);
		String str="";
		setupParam(name, password);
		out.append(str+"\r\n");
		out.close();
	}	
	
	public static String setupParam(String name,String password) {
		return "AuthenticateUser="+name+"&"+"AuthenticatePassword="+password+"&shit";
	}
	
	public static String processParam(String param)
	{
		if(param.indexOf("AuthenticateUser=") != -1 )
		{
			return param;
		}else {
			String name = null,password = null;
			name=param.substring(param.indexOf("@")+"@".length(), param.indexOf("&"));
			int i=param.indexOf("&")+1;
			password= param.substring( i );
			return setupParam(name, password);
		}
	}
	/**
	 * д��
	 * @param param
	 */
	public static void writeAccount(String param)
	{
		PrintWriter out=openStream(ResourcePath.ACCOUNTPATH);
		out.append(processParam(param)+"\r\n");
	}
}
