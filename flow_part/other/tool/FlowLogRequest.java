package other.tool;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.swing.JOptionPane;

public class FlowLogRequest {
	private static HttpURLConnection con;
	private static OutputStreamWriter out;
	private static URL url;
	
	public FlowLogRequest() throws Exception {
		throw new Exception("不要实例化SendLoginRequest");
	}
	
	public static void login(String serverpath,String params) throws IOException
	{
		try {
			url=new URL(serverpath);
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(null, "服务器哪里捡的山塞货?完全找不到");
		}
		try {
			con=(HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false);
			con.setConnectTimeout(1000);
			con.setReadTimeout(2000);
			con.connect();
			out=new OutputStreamWriter(con.getOutputStream(), "UTF-8");
			out.write(params);
			out.flush();
			out.close();
//			System.out.println(con.getResponseCode());
			con.disconnect();
//System.out.println(con.getResponseCode());//就算是断开连接了也能得到什么,而且两次的结果是一样的
		} catch (SocketTimeoutException e) {
			JOptionPane.showMessageDialog(null, "已断网");
		}


	}
	
	public static void logout(String serverpath) throws IOException
	{
		String params="logout=";//好像随便发点什么就能退出登录
		try {			
			url=new URL(serverpath);
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(null, "服务器哪里捡的山塞货?完全找不到");
		}	
		try {
			con=(HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false); 
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//post方式
			con.setRequestProperty("Content-Length", String.valueOf(params.length()));
			con.connect();
			out=new OutputStreamWriter(con.getOutputStream(), "utf-8");
			out.write(params);
			out.flush();
			out.close();
//			System.out.println(con.getResponseCode());
			con.disconnect();
		} catch (SocketTimeoutException e) {
			JOptionPane.showMessageDialog(null, "已断网");
		}
	}
	
//	public static void main(String[] args) throws IOException {
//		FlowLogRequest.login(ResourcePath.SERVERPATH,"AuthenticateUser=14sxlin&AuthenticatePassword=pw146348");
//	}

}
