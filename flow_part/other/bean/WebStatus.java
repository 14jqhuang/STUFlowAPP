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

import gui.mainfraim.FlowAppMainFrame;
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
		
		timer=new Timer(FlowAppMainFrame.controller.interval, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					setWebStatus();
					if(loginStatus==IN)
						setStatus(input);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "断网！"+this.getClass().getName());
					timer.stop();//断网
					loginStatus = OUT;
					WebLost = true;
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
			e.printStackTrace();
			//处理断网
			WebLost=true;
			this.setNull();
			//timer.stop();
		}
		timer.start();
		return timer;		
	}
	
	//给各个变量赋值
	private  void setStatus(StringBuilder input) throws IOException
	{
		if(loginStatus==IN)
		{	userName=setUserName();
			usedAmount=setUsedAmount();
			totalAmount=setTotalAmount();
			remainAmount=setRemainAmount();
		}
//System.out.println(" line 78 in WebStatus "
//		+"\nuser: "+userName
//		+"\nuesd: "+usedAmount
//		+"\ntotal: "+totalAmount
//		+"\nremain: "+remainAmount);
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
	//在与服务器断开连接的时候会出错 断网的时候在调用getStringBuilder中处理了
	/**
	 * 读取网页的内容
	 * @param pathname 网址
	 * @return BufferReader对象
	 * @throws IOException
	 */
	private BufferedReader openStream(String pathname) throws IOException
	{
		BufferedReader br=null;
		URL url=new URL(pathname);
		URLConnection con=url.openConnection();
		// TODO Auto-generated catch block
		InputStream in=con.getInputStream();//java.net.ConnectException: Connection timed out: connect
		br=new BufferedReader(new InputStreamReader(in));
		return br;
		
	}
	
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
	
	//用index来首搜索流量数据
	//label的格式应该是<></>
	public String cutDataInLabel(StringBuilder input , String preLabel,String lasLabel)
	{
		String data = "";
		int preIndex=-1,lasIndex=-1;
		preIndex= input.indexOf(preLabel);
		lasIndex = input.indexOf(lasLabel,preIndex);
		if( preIndex != -1 && lasIndex !=-1)
		{
			data=input.substring(preIndex+preLabel.length(), lasIndex);
		}
		return data;
	}
	
	private  String setUserName()
	{
		return cutDataInLabel(input,"<td width=\"262\" class=\"text3\">", "</td>");
	}
	
	public  String setTotalAmount()
	{
		return cutDataInLabel(input,"<td class=\"text3\" id=\"tb\">", "</td>");
	}
	
	public  String setUsedAmount()
	{
		return cutDataInLabel(input,"<td class=\"text3\" id=\"ub\">", "</td>");
	}
	
	/**
	 * 将截取的数据转换成整型数字
	 * @param str
	 * @return
	 * @throws NullPointerException
	 * @throws NumberFormatException
	 */
	public int flowStringToNumber(String str) throws NullPointerException,NumberFormatException
	{
		String temp[];
		temp=str.split(",");
		
		String intString = "";
		if(temp!= null)
			for(String part : temp)
				intString+=part;
		int tempInt = 0;
		tempInt= Integer.parseInt(intString);
		return tempInt;
	}

	//计算剩余流量的算法
	private  String setRemainAmount() throws IOException
	{
		this.setLoginStatus(input);
		
		if(useOut)
			return ""+0;
		else if(loginStatus==IN) {
			int tempRemaining=flowStringToNumber(totalAmount)-flowStringToNumber(usedAmount);
			int length=totalAmount.length();
			return String.format("%0"+(length-(tempRemaining+"").length())+"d" , tempRemaining);
		}	
		return  "";
	}


	/**
	 * 判断是否用完的标识
	 * @throws IOException
	 */
	private  void setUseOut() throws IOException
	{
		if(!WebLost)
		{	this.setLoginStatus(this.input);
			if(loginStatus==IN)
			{	
				try {
					if(flowStringToNumber(usedAmount)>=flowStringToNumber(totalAmount))
						useOut=true;			
					else useOut=false;
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
				} catch (NullPointerException e) {
					// TODO Auto-generated catch block
				}
			}
		}
	}
	
	//这个是用来截取状态的 但是没有办法截取到密码错误的情况
	//可能还是一个正则表达式的问题 坑的一笔
	 public  void setLoginStatus(StringBuilder input) throws IOException
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

	public void refreshInput() throws IOException
	{
		input=null;
		input=getStringBuilder(ResourcePath.DATAPATH);
	}
	
	public void setWebStatus() throws IOException
	{
		this.setNull();
		this.setLoginStatus(input);
		this.setUseOut();
	}

	
	public static void main(String[] args) throws IOException {
//		WebStatus re=new WebStatus();		
//		re.startConnection();
//		System.out.println("usname= "+re.cutDataInLabel("<td width=\"262\" class=\"text3\">", "</td>"));
//		System.out.println( "ub= "+re.cutDataInLabel("<td class=\"text3\" id=\"ub\">", "</td>"));
//		System.out.println("tb= "+ re.cutDataInLabel("<td class=\"text3\" id=\"tb\">", "</td>"));
//		System.out.println("remain= "+ re.remainAmount);
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
	}
}
