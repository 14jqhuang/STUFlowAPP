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

import interfaces.timer.UseTimer;
import resource.loadconfig.LoadConfig;
import resource.webserver.ResourcePath;

public class WebStatus implements UseTimer {
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
		LoadConfig config = null;
		try {
			config = new LoadConfig();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		int interval = Integer.parseInt(config.readProperty("interval"));
		timer=new Timer(interval, new ActionListener() {
			
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
	
	@Override
	public void startTimer()
	{
		//�������Ļ������Ƕ�����
		try {
			input=this.openWebsite(ResourcePath.SERVERPATH);
		} catch (IOException e) {
			e.printStackTrace();
			//�������
			WebLost=true;
			this.setNull();
			//timer.stop();
		}
		timer.start();
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
	
	
	/**
	 * ��ȡ��ҳ������
	 * ��ȡ��ҳ����
	 * ����������Ͽ����ӵ�ʱ������ 
	 * ������ʱ���ڵ���getStringBuilder�д�����
	 * @param pathname ��ַ
	 * @return BufferReader����
	 * @throws IOException
	 */
	public StringBuilder openWebsite(String pathname)
			throws IOException
	{
		BufferedReader br=null;
		URL url=new URL(pathname);
		URLConnection con=url.openConnection();
		InputStream in=con.getInputStream();//java.net.ConnectException: Connection timed out: connect
		br=new BufferedReader(new InputStreamReader(in));
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
	 * ��index����������������
	 * label�ĸ�ʽӦ����<></>
	 * @param input
	 * @param preLabel ǰ���ǩ������
	 * @param lasLabel �����ǩ������
	 * @return ���ر�ǩ�е�����
	 */
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

	/**
	 * ����ʣ�������ķ���
	 * @return
	 * @throws IOException
	 */
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
				} catch (NullPointerException e) {
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
		input=openWebsite(ResourcePath.DATAPATH);
	}
	
	public void setWebStatus() throws IOException
	{
		this.setNull();
		this.setLoginStatus(input);
		this.setUseOut();
	}
	
	@Override
	public void setDelay(int delay) {
		this.timer.setDelay(delay);
		
	}

	@Override
	public void stopTimer() {
		this.timer.stop();
	}
}
