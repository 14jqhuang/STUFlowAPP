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
import lin.gui.FlowAppMainFrame;

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
		try {
			ReadStatus.setWebLost();
			timer=new Timer(1000, this);
			FlowAppMainFrame.controller.addTimer(timer);
			if(!WebLost)
			{	
				timer.start();		
				input=this.getStringBuilder(openStream(ResourcePath.SERVERPATH));
			}
		} catch (Exception e) {
			e.printStackTrace();
			loginStatus=OUT;
			ReadStatus.setNull();
		}
	}
	
	/**
	 * 获取网页的关于流量的数据,并赋给相应的成员变量
	 * @param input 网页的源代码数据
	 * @throws IOException
	 */
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
	
	/**
	 * 全部关于流量的成员变量赋为""
	 */
	public  static void setNull()
	{
		userName="";
		usedAmount="";
		totalAmount="";
		remainAmount="";
	}	
	/**
	 * 获取网页的字符流
	 * @param pathname 网页的URL
	 * @return 返回一个BufferReader对象
	 * @throws IOException 如果抛出该异常则认为是断网,停止所有的时间器
	 */
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
			FlowAppMainFrame.controller.stopAllController();
		}		
		return br;
		
	}
	/**
	 * 从文本文件里面获取数据,仅供测试使用
	 */
	@Deprecated
	public  BufferedReader openFile(String path) throws FileNotFoundException
	{
		BufferedReader br=null;
		File f=new File(path);
		InputStream in=new FileInputStream(f);
		br=new BufferedReader(new InputStreamReader(in));		
		return br;
	}
	/**
	 * 获取BufferReader流中的数据
	 * @param br 输入一个BufferReader对象,从中读取网页的数据
	 * @return 返回一个SringBuilder对象
	 * @throws IOException
	 */
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
	
	/**
	 * 截取输入范围内的流量数据
	 * @param input 网页的源代码数据
	 * @param pattern 输入正则表达式缩小匹配流量数据的范围
	 * @return 返回输入中符合流量格式的字符串
	 */
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
//			timer.stop();
			ReadStatus.setNull();
		}
//System.out.println("temp1="+temp1);
		return temp1;
		
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
	
	/**
	 * 通过正则表达式获取总的流量
	 * @param input 网页获取的源代码的文本
	 * @return 总流量的字符串
	 */
	public  String getTotalAmount(StringBuilder input)
	{
		return cutNumberByMacther(input, "<td class=\\\"text3\\\" id=\\\"tb\\\">.+?</td>");
	}
	
	/**
	 * 通过正则表达式获取已使用的流量
	 * @param input 网页获取源代码的文本
	 * @return 已使用流量的字符串
	 */
	public  String getUsedAmount(StringBuilder input)
	{
		return cutNumberByMacther(input, "<td class=\\\"text3\\\" id=\\\"ub\\\">.+?</td>");		
	}
	/**
	 * 通过已使用和总的流量计算剩余的流量
	 * @param 
	 * @return 返回流量的字符串000,000,000
	 */
	public  String getRemainAmount() throws IOException
	{
//		this.setUseOut();
		this.setLoginStatus(input);
		try {
			if(useOut)
				return ""+0;				
			else if(loginStatus==IN) {
				int tempTotal[] = this.getSplitData(this.getTotalAmount(input).split(","));
				  int tempUsed[];
				if ((this.getUsedAmount(input).split(",")).length==tempTotal.length)
				 tempUsed= this.getSplitData(this.getUsedAmount(input).split(","));
				else tempUsed=this.getSplitData(("0,"+this.getUsedAmount(input)).split(","));
				tempTotal[tempTotal.length-2]-=1;
//	for(int i:tempTotal)
//		System.out.print(i+" ");
//	System.out.println();
//	for(int i:tempUsed)
//		System.out.print(i+" ");
//	System.out.println();
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
			e.printStackTrace();
			return "";
		}
		return  "";
	}
	
	/**
	 * 将字符串数组转换成整型数组
	 * @param temp 数字字符串数组
	 * @return 返回一个整型数组
	 * @exception 发生错误的话返回输入的数组长度的整型数组值
	 */
	public int[] getSplitData(String[] temp)
	{
		try {
			int[] temp0=new int[temp.length];
			for(int i=0;i<temp.length;i++)
				temp0[i]=Integer.parseInt(temp[i]);		
			return temp0;
		} catch (NumberFormatException e) {
			int temp1[]=new int[temp.length];
			return temp1; 
		}
		
		
	}

	/**
	 *用已使用流量和总流量对比,看是不是用完了流量<br>
	 *将给结果反馈到Useout变量
	 */
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
	/**
	 * 设置登录的状态,IN指的是登录成功,ERROR指的是密码或用户名错误<br>
	 * OUT指的是还没有发送登录信息
	 * @param input 网页源代码的内容
	 * @return 赋给loginStatus字段相应的值
	 */
	 public  void setLoginStatus(StringBuilder input)
	{
		if(!WebLost)
		{try {
				this.refreshInput();
				if(input.indexOf("Welcome")!=-1)
					loginStatus=1;
				else if(loginStatus!=ERROR&&input.indexOf("display:none")!=-1)
						loginStatus=0;
			} catch (NullPointerException e) {
				loginStatus=IN;
			}
//System.out.println("loginstatus="+loginStatus);
		}
	}

	 /**
	  * 刷新网页的数据的内容<br>
	  * 将值赋给成员变量input
	  */
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
	
	/**
	 * 设置loginStatus和useOut的值
	 */
	public void setWebStatus()
	{
		ReadStatus.setNull();
		setWebLost();
		this.setLoginStatus(input);
		this.setUseOut();
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
	
	/**
	 * 截取第一个逗号前面的流量数据,用来显示在精简面板里面<br>
	 * 即得到单位是M的整数数据
	 * @param num 一个以逗号","分隔的数字字符串
	 * @return 返回第一个逗号之前的数字字符串
	 */
	public static String subNum(String num)
	{
		String temp=num;
		int index=-1;
		for(int i=0;i<2;i++)
		{	index=temp.lastIndexOf(",");
			temp=temp.substring(0, index);
		}
System.out.println(temp);
		return temp;
	}
	
	public void actionPerformed(ActionEvent e) {
		setWebStatus();
		try {
			if(loginStatus==IN)
				setStatus(input);
		} catch (IOException e1) {
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
