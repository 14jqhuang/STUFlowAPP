package lin.function;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.swing.JOptionPane;

import lin.readwrite.ResourcePath;

public class SendLoginRequest {
	private HttpURLConnection con;
	private OutputStreamWriter out;
	URL url;
	public SendLoginRequest() throws IOException {}
	
	public void login(String serverpath,String params) throws IOException
	{
		try {
			url=new URL(serverpath);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "服务器哪里捡的山塞货?完全找不到\n"+this.getClass().getName());
		}
		try {
			con=(HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setConnectTimeout(4000);
			con.connect();
			out=new OutputStreamWriter(con.getOutputStream(), "UTF-8");
			out.write(params);
			out.flush();
			out.close();
			con.getResponseCode();//这句话是跟服务器说我发给你的内容结束了
			con.disconnect();
//System.out.println(con.getResponseCode());//就算是断开连接了也能得到什么,而且两次的结果是一样的
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "已断网");
		}


	}
	
	public void logout(String serverpath) throws IOException
	{
		String params="logout=";//好像随便发点什么就能退出登录
		try {			
			url=new URL(serverpath);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "服务器哪里捡的山塞货?完全找不到\n"+this.getClass().getName());
		}	
		try {
			con=(HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false); 
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			con.setRequestProperty("Content-Length", String.valueOf(params.length()));
			con.connect();
			out=new OutputStreamWriter(con.getOutputStream(), "utf-8");
			out.write(params);
			out.flush();
			out.close();
			con.getResponseCode();
			con.disconnect();
		} catch (SocketTimeoutException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "已断网");
		}
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		SendLoginRequest a=new SendLoginRequest();
		a.login(ResourcePath.SERVERPATH,"AuthenticateUser=14sxlin&AuthenticatePassword=pw146348");
//		a.logout(ResourcePath.SERVERPATH);
		//如果读在写之前可能也会报connection reset的错误
	}

}
