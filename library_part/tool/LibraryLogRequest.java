package tool;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;



public  class LibraryLogRequest {

//	private static final String LibraryServerPath="https://sso.stu.edu.cn/login?service="
//			+ "http://202.192.155.48:83/opac/login.aspx?ReturnUrl=/opac/user/bookborrowed.aspx";
	private static final String CreditServerPath=
			"http://credit.stu.edu.cn/web/stulogin.aspx";
	
	
	public static void login(String param) throws IOException
	{
//		URL url= new URL(LibraryServerPath);
		URL url= new URL(CreditServerPath);
		HttpURLConnection con= (HttpURLConnection) url.openConnection();
		OutputStreamWriter out;
		
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setUseCaches(false);
		con.setConnectTimeout(4000);//设置超时?有什么卵用?
		con.connect();
		out=new OutputStreamWriter(con.getOutputStream(), "UTF-8");
		out.write(param);
		out.flush();
		out.close();
		con.getResponseCode();//这句话是跟服务器说我发给你的内容结束了
		con.disconnect();
	}
	
	public static void login(String username,String password) throws IOException
	{
//		String param="username="+username+"&"+"password="+password
//				+"&It=LT-155674-yVRXsbaDuP0Ux0Fvwe3Hda1UYN0dkh"
//				+"&execution=e1s1"
//				+"&_eventId:submit";
		String param="__EVENTTARGET=/WebResource.axd?d=4mYbeHchO6XTweUrEAzlyx2XGgM4MSFknh79yQ7LOHefWVhQDiOUaKjIkCpEkOo-81beDCSlnc7D4sYb6RS4GvwXRq01&amp;t=635589723570790873 "
+ "&__EVENTARGUMENT= "
+ "&__VIEWSTATE=/wEPDwUKMTM1MzI1Njg5N2Rk47x7/EAaT/4MwkLGxreXh8mHHxA="
+ "&__VIEWSTATEGENERATOR=FBAF4793"
+ "&__EVENTVALIDATION=/wEWBAKo25zdBALT8dy8BQLG8eCkDwKk07qFCRXt1F3RFYVdjuYasktKIhLnziqd"
+ "&txtUserID=14sxlin"
+ "&txtUserPwd=Lsx120"
+ "&btnLogon=";
		login(param);
	}
	
	public static void logout()
	{
		
	}
	
	public static void main(String [] args)
	{
		try {
			LibraryLogRequest.login("14sxlin", "Lsx120");
			System.out.println("已发送登录请求");
			System.out.println(LibDataCutter.getPageString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
