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
		throw new Exception("不要实例化这个类");
	}
	
	private static PrintWriter openStream(String path)
	{
		PrintWriter out=null;
		try {
			File f=new File(URLDecoder.decode(ResourcePath.ACCOUNTPATH, "utf-8"));//这句话并不会新建文件
			OutputStream in=new FileOutputStream(f,true);//让文件可以追加内容,而且这句话这里自动新建了一个
			out=new PrintWriter(in);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "打开文件或流失败失败");
		}
		return out;		
	}

	public static  void writeAccount(String name,String password)
	{
		PrintWriter out=openStream(ResourcePath.ACCOUNTPATH);
		String str="";
		str+="AuthenticateUser="+name+"&"+"AuthenticatePassword="+password+"&shit";
		out.append(str+"\r\n");
		out.close();
	}	
	
	/**
	 * 写入
	 * @param param
	 */
	public static void writeAccount(String param)
	{
		if(param.indexOf("AuthenticateUser=") != -1 )
		{
			PrintWriter out=openStream(ResourcePath.ACCOUNTPATH);
			out.append(param+"\r\n");
			out.close();
		}else {
			String name = null,password = null;
			name=param.substring(param.indexOf("@")+"@".length(), param.indexOf("&"));
			int i=param.indexOf("&")+1;
			password= param.substring( i );
			String str="";
			str+="AuthenticateUser="+name+"&"+"AuthenticatePassword="+password+"&shit";
			PrintWriter out=openStream(ResourcePath.ACCOUNTPATH);
			out.append(str+"\r\n");
			out.close();
		}
	}
}
