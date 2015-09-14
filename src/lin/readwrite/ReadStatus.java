package lin.readwrite;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.Timer;

public class ReadStatus implements ActionListener {
	/*
	 * 1.记得退出的时候把时间关掉
	 * 2.不知道为什么并不能截取到密码错误的情况
	 */
	public static final int IN=1;
	public static final int OUT=0;
	public static final int ERROR=-1;//获取服务器的时候发生错误
	public static String userName;
	public static String usedAmount; 
	public static String remainAmount;
	public static String totalAmount;
	public static boolean WebLost;
	public StringBuilder input;
	public static int loginStatus;
	public static boolean useOut=false;
	public  Timer timer;
	
	public ReadStatus() throws IOException {
		// TODO Auto-generated constructor stub
		try {
			ReadStatus.setWebLost();
//System.out.println("WebLost="+WebLost);
			timer=new Timer(1000, this);
			if(!WebLost)
			{	
				timer.start();		
				input=this.getStringBuilder(openStream(ResourcePath.SERVERPATH));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loginStatus=OUT;
			ReadStatus.setNull();
		}
	}
	
	public  void setStatus(StringBuilder input) throws IOException
	{
		if(loginStatus==IN)
		{	userName=getUserName(input);
			usedAmount=getUsedAmount(input);
			totalAmount=getTotalAmount(input);
			remainAmount=getRemainAmount();
		}
System.out.println("user: "+userName
		+"\nuesd: "+usedAmount
		+"\ntotal: "+totalAmount
		+"\nremain: "+remainAmount);
	}
	
	public  static void setNull()
	{
		userName="";
		usedAmount="";
		totalAmount="";
		remainAmount="";
	}	
	
	public BufferedReader openStream(String pathname) throws IOException
	{
		BufferedReader br=null;
		try {
			URL url=new URL(pathname);
			URLConnection con=url.openConnection();
			InputStream in=con.getInputStream();
			br=new BufferedReader(new InputStreamReader(in));
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			JOptionPane.showMessageDialog(null, "已断网~请好好休息,晚安");
//			timer.stop();
			WebLost=true;
			ReadStatus.setNull();
		}		
		return br;
		
	}
	
	public  BufferedReader openFile(String path) throws FileNotFoundException
	{
		BufferedReader br=null;
		File f=new File(path);
		InputStream in=new FileInputStream(f);
		br=new BufferedReader(new InputStreamReader(in));		
		return br;
	}
	
	public StringBuilder getStringBuilder(BufferedReader br) throws IOException
	{
		if (!WebLost) {
			String line;
			StringBuilder b = new StringBuilder();
			while ((line = br.readLine()) != null) {
				b.append(line);
			}
			br.close();
			return b;
		}else return new StringBuilder("");
	}
	
	public  String cutNumberByMacther(StringBuilder input,String pattern)
	{
		String temp1 = null;
		try {//这在点击退出的时候回触发这个错误
			String temp = null;
			Pattern p=Pattern.compile(pattern);
			Matcher m=p.matcher(input);
			while(m.find())
			{
				temp=input.substring(m.start(), m.end());
			}
			p=Pattern.compile("\\d{0,3},?\\d{1,3},\\d{1,3}");
			m=p.matcher(temp);
			temp1 = "没有找到内容";
			while(m.find())
			{
				temp1=temp.substring(m.start(), m.end());
			}
		} catch (NullPointerException e) {
//			timer.stop();
			ReadStatus.setNull();
		}
//System.out.println("temp1="+temp1);
		if(temp1.length()<=7)
			return "0,"+temp1;
		else return temp1;
	}
	
	public  String getUserName(StringBuilder input)
	{
		if (!WebLost) {
			String temp = "";
			Pattern p = Pattern.compile("<td width=\\\"262\\\" class=\\\"text3\\\">.+?</td>");
			Matcher m = p.matcher(input);
			while (m.find())
				temp += input.substring(m.start(), m.end());
			p = Pattern.compile(">.+?<");
			m = p.matcher(temp);
			String temp1 = "";
			while (m.find())
				temp1 += temp.substring(m.start(), m.end());
			if (temp != "")
				return temp1.substring(1, temp1.length() - 1);
			else
				return "";
		}else return "";
	}
	
	public  String getTotalAmount(StringBuilder input)
	{
		return cutNumberByMacther(input, "<td class=\\\"text3\\\" id=\\\"tb\\\">.+?</td>");
	}
	
	public  String getUsedAmount(StringBuilder input)
	{
		return cutNumberByMacther(input, "<td class=\\\"text3\\\" id=\\\"ub\\\">.+?</td>");		
	}
	
	public  String getRemainAmount() throws IOException
	{
//		this.setUseOut();
		this.setLoginStatus(input);
		try {
			if(useOut)
				return ""+0;				
			else if(loginStatus==IN) {
				int tempTotal[] = this.getSplitData(this.getTotalAmount(input).split(","));
				int tempUsed[] = this.getSplitData(this.getUsedAmount(input).split(","));
				tempTotal[tempTotal.length-2]-=1;
	//for(int i:tempTotal)
//		System.out.print(i+" ");
	//System.out.println();
	//for(int i:tempUsed)
//		System.out.print(i+" ");
	//System.out.println();
				int tempResult[] = new int[tempTotal.length];
				String result = "";
				for (int i = tempTotal.length-1; i >= 0; i--) {
					if (tempTotal[i] - tempUsed[i] >= 0)
						tempResult[i] = tempTotal[i] - tempUsed[i];
					else {
						tempTotal[i - 1] -= 1;
						tempResult[i] = tempTotal[i]+1000 - tempUsed[i];
					}
				}
				for (int i : tempResult)
					if((i+"").length()<3)
						result += "0" + i + ",";
					else if((i+"").length()<2)
						result += "00"+ i + ",";
					else
						result += "" + i + ",";
				return result.substring(0, result.length() - 1);
				}
			}catch (Exception e) {
			// TODO Auto-generated catch block
//			JOptionPane.showMessageDialog(null, "剩余流量计算错误");
			return "";
		}
		return  "";
	}
	
	public int[] getSplitData(String[] temp)
	{
		try {
			int[] temp0=new int[temp.length];
			for(int i=0;i<temp.length;i++)
				temp0[i]=Integer.parseInt(temp[i]);		
			return temp0;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
//			JOptionPane.showMessageDialog(null, "服务器不稳定无法获取信息,请稍后重试");
//			loginStatus=-1;
//			timer.stop();
//			WebLost=true;
			int temp1[]= {000,000,000};
			return temp1; 
		}
		
		
	}

	@Deprecated
	public void setLoginStatus()
	{
		if(totalAmount==""||userName=="")
			loginStatus=OUT;
		else
			loginStatus=IN;
	}
	
	public  void setUseOut()
	{
		if(!WebLost)
		{	this.setLoginStatus(this.input);
			if(loginStatus==IN)
			{	int tempTotal[] = this.getSplitData(getTotalAmount(input).split(","));
				int tempUsed[] = this.getSplitData(getUsedAmount(input).split(","));
				if(tempTotal[0]==tempUsed[0]
						&&tempTotal[1]==tempUsed[1]
								&&tempTotal[2]<tempUsed[2])
					useOut=true;			
				else useOut=false;
			}
		}
	}
	
	 public  void setLoginStatus(StringBuilder input)
	{
		if(!WebLost)
		{try {
				this.refreshInput();
				Pattern p=Pattern.compile("<h3><center><font color=\\\"red\\\" style=\\\"display:[a-z]*?\\\">");
				Matcher m=p.matcher(input);
				String temp="none";
				while(m.find())
					temp=input.substring(m.start(),m.end());
//System.out.println("temp="+temp);
				String temp2=null;
				int index=temp.indexOf("display:");
				if(index!=-1)
					temp2=temp.substring(index+8, index+9);
//System.out.println("temp2="+temp2);
				if(temp2.equals("n"))
					loginStatus=OUT;
					else if(temp2.equals("i"))
						loginStatus=ERROR;
					else 	loginStatus=IN;
//System.out.println("loginStatus="+loginStatus);
			} catch (NullPointerException e) {
				// TODO Auto-generated catch block
				loginStatus=IN;
			}
		}
	}

	public void refreshInput()
	{
		try {
			input=null;
			input=getStringBuilder(openStream(ResourcePath.DATAPATH));
			System.gc();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "input刷新失败");
			timer.stop();
		}
	}
	
	public void setWebStatus()
	{
		ReadStatus.setNull();
		setWebLost();
		this.setLoginStatus(input);
		this.setUseOut();
//System.out.println("login= "+loginStatus);
//System.out.println("useOut="+useOut);
	}
	
	//采用时间的断网机制
	@Deprecated
	public static void setWebLost()
	{
//		long time=System.currentTimeMillis();
//		SimpleDateFormat str=new SimpleDateFormat("hh:mm:ss");
//System.out.println("down is: "+str.format(new Date(0-8*60*60*1000)));
//System.out.println(1440609223319l/60/60/1000/24/365);
//System.out.println(6*60*60*1000+30*60*1000);
//System.out.println("now is:  "+str.format(new Date(time)));
//System.out.println("up is  :  "+str.format(new Date(0-8*60*60*1000+6*60*60*1000+30*60*1000)));
//		if(time>=(0-8*60*60*1000)&&time<=6*60*60*1000+30*60*1000)
//			WebLost=true;
//		else WebLost=false;
		WebLost=false;
	}
	
	public static String subNum(String num)
	{
		int index=num.indexOf(",");
		if(index!=-1)
			return num.substring(0, index);
		else return num;
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		setWebStatus();
//System.out.println("loginStatus="+loginStatus);
		try {
			if(loginStatus==IN)
				setStatus(input);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "网页打不开啊..隔壁老王\n"+this.getClass().getName());
		}
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
//		ReadStatus re=new ReadStatus();		
//		re.setWebStatus();
//		re.setLoginStatus(re.input);
//System.out.println("loginStatus="+ReadStatus.loginStatus);
//		re.setUseOut();
//System.out.println("UseOut?"+useOut);
//System.out.println("Input=null?"+(re.input==null));
//System.out.println("input="+re.input);
//System.out.println("UserName"+re.getUserName(re.input));
//System.out.println("TotalAmount="+re.getTotalAmount(re.input));
//System.out.println("UsedAmpunt="+re.getUsedAmount(re.input));
//System.out.println("RemainAmount="+re.getRemainAmount());
//		ReadStatus.setNull();
//System.out.println("\n\nAftersetNull\n"
//		+ "loginStatus="+loginStatus
//		+"\nUseOut?"+useOut
//		+ "\nUserName"+re.getUserName(re.input)
//		+ "\nTotalAmount="+re.getTotalAmount(re.input)
//		+ "\nUsedAmpunt="+re.getUsedAmount(re.input)
//		+ "\nRemainAmount="+re.getRemainAmount());
//		re.setWebStatus();
	}
}
