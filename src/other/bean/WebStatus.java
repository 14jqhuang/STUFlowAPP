package other.bean;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import resource.webserver.ResourcePath;

public class WebStatus {
	/*
	 * 1.记得退出的时候把时间关掉
	 * 2.不知道为什么并不能截取到密码错误的情况
	 */
	public final int IN=1;
	public final int OUT=0;
	public final int ERROR=-1;//密码错误发生错误
	public String userName;
	public String usedAmount; 
	public String remainAmount;
	public String totalAmount;
	public boolean WebLost;
	public StringBuilder input;
	public int loginStatus;
	public boolean useOut=false;
	private Timer timer;
	
	public WebStatus() {
		
		timer=new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
	System.out.println("invoke 43 WebStatus");
				setWebStatus();
				try {
					if(loginStatus==IN)
						setStatus(input);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "网页打不开啊..隔壁老王\n"+this.getClass().getName());
				}
			}
		});
	}
	
	public Timer startConnection()
	{
		//这里出错的话可能是断网了
		try {
			input=this.getStringBuilder(ResourcePath.SERVERPATH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//处理断网
			e.printStackTrace();
		}
		timer.start();
		return timer;		
	}
	
	//给各个变量赋值
	private  void setStatus(StringBuilder input) throws IOException
	{
		if(loginStatus==IN)
		{	userName=getUserName();
			usedAmount=getUsedAmount();
			totalAmount=getTotalAmount();
			remainAmount=getRemainAmount();
		}
System.out.println("user: "+userName
		+"\nuesd: "+usedAmount
		+"\ntotal: "+totalAmount
		+"\nremain: "+remainAmount);
	}
	
	//全部设为"" 在出错的时候用
	public void setNull()
	{
		userName="";
		usedAmount="";
		totalAmount="";
		remainAmount="";
	}	
	
	//获取网页的流
	//在与服务器断开连接的时候会出错
	private BufferedReader openStream(String pathname) throws IOException
	{
		BufferedReader br=null;
		try {
			URL url=new URL(pathname);
			URLConnection con=url.openConnection();
			InputStream in=con.getInputStream();
			br=new BufferedReader(new InputStreamReader(in));
		} catch (Exception e) {
			WebLost=true;
			this.setNull();
		}		
		return br;
		
	}
	
//	public  BufferedReader openFile(String path) throws FileNotFoundException
//	{
//		BufferedReader br=null;
//		File f=new File(path);
//		InputStream in=new FileInputStream(f);
//		br=new BufferedReader(new InputStreamReader(in));		
//		return br;
//	}
	
	//读取网页的数据放在一个StringBuilder中并返回
	private StringBuilder getStringBuilder(String pathname) throws IOException
	{
		BufferedReader br=this.openStream(pathname);
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
	
	//用正则表达式(从外部输入)从字符串中获取数字的数据,但是有点问题 这个不适用与超过800M流量的情况
	//所以这个要改一下 不要用正则表达式了
	//还要根据流量的多少来判断算法
	private  String cutNumberByMacther(StringBuilder input,String pattern)
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
			p=Pattern.compile("(\\d{0,3},){0,5}\\d{1,3}");
			m=p.matcher(temp);
			temp1 = "000,000,000";
			while(m.find())
			{
				temp1=temp.substring(m.start(), m.end());
			}
			if(temp1.length()<=3)
				return "000,000,"+temp1;
			else if(temp1.length()<=7)
						return "000,"+temp1;
					else return temp1;
		} catch (NullPointerException e) {
			this.setNull();
		}
		return temp1;
		
	}
	
	//用正则表达式来获取用户名 内置了正则表达式
	private  String getUserName()
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
	
	public  String getTotalAmount()
	{
		return cutNumberByMacther(input, "<td class=\\\"text3\\\" id=\\\"tb\\\">.+?</td>");
	}
	
	public  String getUsedAmount()
	{
		return cutNumberByMacther(input, "<td class=\\\"text3\\\" id=\\\"ub\\\">.+?</td>");		
	}
	
	
	//计算剩余流量的算法
	public  String getRemainAmount() throws IOException
	{
		this.setLoginStatus(input);
		try {
			if(useOut)
				return ""+0;				
			else if(loginStatus==IN) {
				int tempTotal[] = this.getSplitData(this.getTotalAmount().split(","));
				int tempUsed[] = this.getSplitData(this.getUsedAmount().split(","));
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
			JOptionPane.showMessageDialog(null, "剩余流量计算错误");
			return "";
		}
		return  "";
	}
	
	//将以逗号分隔的数字字符串转换成数组
	//显然有数组来计算很不方便 所以这个也要改掉
	public int[] getSplitData(String[] temp)
	{
		try {
			int[] temp0=new int[temp.length];
			for(int i=0;i<temp.length;i++)
				temp0[i]=Integer.parseInt(temp[i]);		
			return temp0;
		} catch (NumberFormatException e) {
			int temp1[]= {000,000,000};
			return temp1; 
		}
		
		
	}

	
	//这个也是用数组来计算 深受其害
	public  void setUseOut()
	{
		if(!WebLost)
		{	this.setLoginStatus(this.input);
			if(loginStatus==IN)
			{	int tempTotal[] = this.getSplitData(getTotalAmount().split(","));
				int tempUsed[] = this.getSplitData(getUsedAmount().split(","));
				if(tempTotal[0]==tempUsed[0]
						&&tempTotal[1]==tempUsed[1]
								&&tempTotal[2]<tempUsed[2])
					useOut=true;			
				else useOut=false;
			}
		}
	}
	
	//这个是用来截取状态的 但是没有办法截取到密码错误的情况
	//可能还是一个正则表达式的问题 坑的一笔
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
				String temp2=null;
				int index=temp.indexOf("display:");
				if(index!=-1)
					temp2=temp.substring(index+8, index+9);
				if(temp2.equals("n"))
					loginStatus=OUT;
					else if(temp2.equals("i"))
						loginStatus=ERROR;
					else 	loginStatus=IN;
			} catch (NullPointerException e) {
				loginStatus=IN;
			}
		}
	}

	public void refreshInput()
	{
		try {
			input=null;
			input=getStringBuilder(ResourcePath.DATAPATH);
			System.gc();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "input刷新失败");
			timer.stop();
		}
	}
	
	public void setWebStatus()
	{
		this.setNull();
		this.setLoginStatus(input);
		this.setUseOut();
	}
	
	
	public String subNum(String num)
	{
		String temp=num;
		int index=-1;
		for(int i=0;i<2;i++)
		{	index=temp.lastIndexOf(",");
			if(index!=-1)
				temp=temp.substring(0, index);
		}
System.out.println(temp);
		return temp;
	}
	
//	public static void main(String[] args) throws IOException {
//		WebStatus re=new WebStatus();		
//		re.timer.start();
//		re.setWebStatus();
//		re.setLoginStatus(re.input);
//System.out.println("loginStatus="+re.loginStatus);
//		re.setUseOut();
//System.out.println("UseOut?"+re.useOut);
//System.out.println("Input=null?"+(re.input==null));
//System.out.println("input="+re.input);
//System.out.println("UserName"+re.getUserName());
//System.out.println("TotalAmount="+re.getTotalAmount());
//System.out.println("UsedAmpunt="+re.getUsedAmount());
//try {
//	System.out.println("RemainAmount="+re.getRemainAmount());
//} catch (IOException e) {
//	e.printStackTrace();
//}
//		re.setNull();
//	System.out.println("\n\nAftersetNull\n"
//			+ "loginStatus="+re.loginStatus
//			+"\nUseOut?"+re.useOut
//			+ "\nUserName"+re.getUserName()
//			+ "\nTotalAmount="+re.getTotalAmount()
//			+ "\nUsedAmpunt="+re.getUsedAmount()
//			+ "\nRemainAmount="+re.getRemainAmount());
//		re.setWebStatus();
//	}
}
