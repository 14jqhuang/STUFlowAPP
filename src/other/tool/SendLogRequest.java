package other.tool;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import javax.swing.JOptionPane;

import resource.webserver.ResourcePath;

public class SendLogRequest {
	private static HttpURLConnection con;
	private static OutputStreamWriter out;
	private static URL url;
	
	public SendLogRequest() throws Exception {
		throw new Exception("��Ҫʵ����SendLoginRequest");
	}
	
	public static void login(String serverpath,String params) throws IOException
	{
		try {
			url=new URL(serverpath);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "������������ɽ����?��ȫ�Ҳ���");
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
			con.getResponseCode();//��仰�Ǹ�������˵�ҷ���������ݽ�����
			con.disconnect();
//System.out.println(con.getResponseCode());//�����ǶϿ�������Ҳ�ܵõ�ʲô,�������εĽ����һ����
		} catch (SocketTimeoutException e) {
			JOptionPane.showMessageDialog(null, "�Ѷ���");
		}


	}
	
	public static void logout(String serverpath) throws IOException
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
			JOptionPane.showMessageDialog(null, "�Ѷ���");
		}
	}
	public static void main(String[] args) throws IOException {
		SendLogRequest.login(ResourcePath.SERVERPATH,"AuthenticateUser=14sxlin&AuthenticatePassword=pw146348");
//		a.logout(ResourcePath.SERVERPATH);
		//�������д֮ǰ����Ҳ�ᱨconnection reset�Ĵ���
	}

}