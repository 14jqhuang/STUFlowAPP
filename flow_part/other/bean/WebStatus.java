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
		
		timer=new Timer(FlowAppMainFrame.controller.interval, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					setWebStatus();
					if(loginStatus==IN)
						setStatus(input);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "������"+this.getClass().getName());
					timer.stop();//����
					loginStatus = OUT;
					WebLost = true;
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
			e.printStackTrace();
			//�������
			WebLost=true;
			this.setNull();
			//timer.stop();
		}
		timer.start();
		return timer;		
	}
	
	//������������ֵ
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
	
	//ȫ����Ϊ"" �ڳ����ʱ����
	public void setNull()
	{
		userName="";
		usedAmount="";
		totalAmount="";
		remainAmount="";
	}	
	
	//��ȡ��ҳ����
	//����������Ͽ����ӵ�ʱ������ ������ʱ���ڵ���getStringBuilder�д�����
	/**
	 * ��ȡ��ҳ������
	 * @param pathname ��ַ
	 * @return BufferReader����
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
	
	//��index����������������
	//label�ĸ�ʽӦ����<></>
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
	 * ����ȡ������ת������������
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

	//����ʣ���������㷨
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
	 * �ж��Ƿ�����ı�ʶ
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
	
	//�����������ȡ״̬�� ����û�а취��ȡ�������������
	//���ܻ���һ��������ʽ������ �ӵ�һ��
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
