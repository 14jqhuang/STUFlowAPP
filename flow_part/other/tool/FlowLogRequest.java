package other.tool;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.swing.JOptionPane;

import resource.webserver.ResourcePath;

public class FlowLogRequest {
	private static HttpURLConnection con;
	private static OutputStreamWriter out;
	private static URL url;
	
	public FlowLogRequest() throws Exception {
		throw new Exception("��Ҫʵ����SendLoginRequest");
	}
	
	public static int login(String serverpath,String params) throws IOException
	{
		try {
			url=new URL(serverpath);
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(null, "������������ɽ����?��ȫ�Ҳ���");
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
			con.disconnect();
			return con.getResponseCode();
		} catch (SocketTimeoutException e) {
			JOptionPane.showMessageDialog(null, "�Ѷ���");
			return 404;
		}


	}
	
	public static int logout(String serverpath) throws IOException
	{
		String params="logout=";//������㷢��ʲô�����˳���¼
		try {			
			url=new URL(serverpath);
		} catch (MalformedURLException e) {
			JOptionPane.showMessageDialog(null, "������������ɽ����?��ȫ�Ҳ���");
		}	
		try {
			con=(HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setUseCaches(false); 
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//post��ʽ
			con.setRequestProperty("Content-Length", String.valueOf(params.length()));
			con.connect();
			out=new OutputStreamWriter(con.getOutputStream(), "utf-8");
			out.write(params);
			out.flush();
			out.close();
			con.disconnect();
			return con.getResponseCode();
		} catch (SocketTimeoutException e) {
			JOptionPane.showMessageDialog(null, "�Ѷ���");
			return 404;
		}
	}
	
	public static void main(String[] args) throws IOException {
		FlowLogRequest.logout(ResourcePath.SERVERPATH);
	}

}
