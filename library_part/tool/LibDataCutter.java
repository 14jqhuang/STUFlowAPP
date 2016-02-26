package tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class LibDataCutter {
	private static String pagePath="http://credit.stu.edu.cn/portal/STUMainPage.aspx";
	
	
	public static String getPageString() throws IOException
	{
		String tempString="";
		URL url= new URL(pagePath);
		URLConnection con = url.openConnection();
		BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line="";
		while(( line=br.readLine()) != null)
			tempString+=line;
		return tempString;
	}
	
}
