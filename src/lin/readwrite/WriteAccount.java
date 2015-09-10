package lin.readwrite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.swing.JOptionPane;

public class WriteAccount implements ResourcePath{
	public  PrintWriter out;
	/*
	 * 用完后要记得关闭流
	 */
	public WriteAccount() throws UnsupportedEncodingException  {
		// TODO Auto-generated constructor stub

		out=this.openStream(decode(ACCOUNTPATH));
	}
	
	public PrintWriter openStream(String path)
	{
		PrintWriter out=null;
		try {
			File f=new File(path);//这句话并不会新建文件
			OutputStream in=new FileOutputStream(f,true);//让文件可以追加内容,而且这句话这里自动新建了一个
			out=new PrintWriter(in);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "打开文件或流失败失败\n"+this.getClass().getName());
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

	@Override
	public String decode(String path) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return URLDecoder.decode(path, "utf-8");
	}
}
