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
	 * 1.�ǵ��˳���ʱ���ʱ��ص�
	 * 2.��֪��Ϊʲô�����ܽ�ȡ�������������
	 */
	public final int IN=1;
	public final int OUT=0;
	public final int ERROR=-1;//�������������
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
					JOptionPane.showMessageDialog(null, "��ҳ�򲻿���..��������\n"+this.getClass().getName());
				}
			}
		});
	}
	
	public Timer startConnection()
	{
		//�������Ļ������Ƕ�����
		try {
			input=this.getStringBuilder(ResourcePath.SERVERPATH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//�������
			e.printStackTrace();
		}
		timer.start();
		return timer;		
	}
	
	//������������ֵ
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
	
	//ȫ����Ϊ"" �ڳ����ʱ����
	public void setNull()
	{
		userName="";
		usedAmount="";
		totalAmount="";
		remainAmount="";
	}	
	
	//��ȡ��ҳ����
	//����������Ͽ����ӵ�ʱ������
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
	
	//��ȡ��ҳ�����ݷ���һ��StringBuilder�в�����
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
	
	//��������ʽ(���ⲿ����)���ַ����л�ȡ���ֵ�����,�����е����� ����������볬��800M���������
	//�������Ҫ��һ�� ��Ҫ��������ʽ��
	//��Ҫ���������Ķ������ж��㷨
	private  String cutNumberByMacther(StringBuilder input,String pattern)
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
			this.setNull();
		}
		return temp1;
		
	}
	
	//��������ʽ����ȡ�û��� ������������ʽ
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
	
	
	//����ʣ���������㷨
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
			JOptionPane.showMessageDialog(null, "ʣ�������������");
			return "";
		}
		return  "";
	}
	
	//���Զ��ŷָ��������ַ���ת��������
	//��Ȼ������������ܲ����� �������ҲҪ�ĵ�
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

	
	//���Ҳ�������������� �����亦
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
	
	//�����������ȡ״̬�� ����û�а취��ȡ�������������
	//���ܻ���һ��������ʽ������ �ӵ�һ��
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
			JOptionPane.showMessageDialog(null, "inputˢ��ʧ��");
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
