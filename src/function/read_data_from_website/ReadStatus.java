package function.read_data_from_website;

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

import gui.mainfraim.FlowAppMainFrame;
import resource.webserver.ResourcePath;

public class ReadStatus implements ActionListener {
	/*
	 * 1.�ǵ��˳���ʱ���ʱ��ص�
	 * 2.��֪��Ϊʲô�����ܽ�ȡ�������������
	 */
	public static final int IN=1;
	public static final int OUT=0;
	public static final int ERROR=-1;//��ȡ��������ʱ��������
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
			FlowAppMainFrame.controller.addTimer(timer);
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
//			JOptionPane.showMessageDialog(null, "�Ѷ���~��ú���Ϣ,��");
//			timer.stop();
			WebLost=true;
			ReadStatus.setNull();
			FlowAppMainFrame.controller.stopAllController();
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
		try {//���ڵ���˳���ʱ��ش����������
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
			JOptionPane.showMessageDialog(null, "ʣ�������������");
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
			int temp1[]= {000,000,000};
			return temp1; 
		}
		
		
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
			input=getStringBuilder(openStream(ResourcePath.DATAPATH));
			System.gc();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "inputˢ��ʧ��");
			timer.stop();
		}
	}
	
	public void setWebStatus()
	{
		ReadStatus.setNull();
		setWebLost();
		this.setLoginStatus(input);
		this.setUseOut();
	}
	
	//����ʱ��Ķ�������
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
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		setWebStatus();
//System.out.println("loginStatus="+loginStatus);
		try {
			if(loginStatus==IN)
				setStatus(input);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "��ҳ�򲻿���..��������\n"+this.getClass().getName());
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
