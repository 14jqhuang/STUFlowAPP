package lin.component;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import lin.function.SendLoginRequest;
import lin.gui.FlowAppMainFrame;
import lin.readwrite.ConfigAutoLogin;
import lin.readwrite.ReadStatus;
import lin.readwrite.ResourcePath;

@SuppressWarnings("serial")
public class FlowDisplayPanel extends JPanel implements ActionListener {
	/*
	 * 记得退出的时候把时间关掉
	 */
	public JTextField usedText,totalText,remainText,nameText;//用来显示流量的数值
	public JLabel statusLabel;
	public JButton logoutButton;
	private GridBagLayout gridbag;
	private GridBagConstraints constraints;
	public  Timer timer;
	private ReadStatus readStatus;//这个不能删除
	
	//参数显示要不要精简模式,测试时用false
    public FlowDisplayPanel(boolean simplify) {
    	//流量展示区
    	this.setBorder(new TitledBorder("流量"));
    	gridbag=new GridBagLayout();
    	this.setLayout(gridbag);
    	
    	constraints=new GridBagConstraints(0, 0, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	this.add(new JLabel("用户名:"), constraints);    	
    	constraints=new GridBagConstraints(1, 0, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	nameText=new JTextField();
    	nameText.setEditable(false);
    	this.add(nameText, constraints);
    	
    	constraints=new GridBagConstraints(0, 2, 1, 1, 1, 1,GridBagConstraints.CENTER
    				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	this.add(new JLabel("已使用流量:"),constraints);    	
    	constraints=new GridBagConstraints(1, 2, 1, 1, 3, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	usedText=new JTextField();
    	usedText.setForeground(Color.blue);
    	usedText.setEditable(false);
    	this.add(usedText, constraints);    	
    	constraints=new GridBagConstraints(2, 2, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	this.add(new JLabel("Byte"),constraints);
    	
    	constraints=new GridBagConstraints(0, 1, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	this.add(new JLabel("总流量:"), constraints);    	
    	constraints=new GridBagConstraints(1, 1, 1, 1, 3, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	totalText=new JTextField();
    	totalText.setForeground(Color.ORANGE);
    	totalText.setEditable(false);
    	this.add(totalText, constraints);    	
    	constraints=new GridBagConstraints(2, 1, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	this.add(new JLabel("Byte"),constraints);
    	
    	constraints=new GridBagConstraints(0, 3, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	this.add(new JLabel("剩余流量:"), constraints);
    	
    	constraints=new GridBagConstraints(1, 3, 1, 1, 3, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	remainText=new JTextField();
    	remainText.setForeground(Color.red);
    	remainText.setEditable(false);
    	this.add(remainText, constraints);
    	
    	constraints=new GridBagConstraints(2, 3, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	this.add(new JLabel("Byte"),constraints);
    	
    	constraints=new GridBagConstraints(0, 4, 3, 1, GridBagConstraints.REMAINDER
    			, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	statusLabel=new JLabel("未登录",JLabel.CENTER);
    	statusLabel.setForeground(Color.red);
    	this.add(statusLabel, constraints);
    	
    	constraints=new GridBagConstraints(0, 5, 3, 1, GridBagConstraints.REMAINDER
    			, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	logoutButton=new JButton("退出登录");
    	logoutButton.addActionListener(this);
//    	logoutButton.setEnabled(false);
    	this.add(logoutButton, constraints);
    	
    	if (!ReadStatus.WebLost) {
			//获取网页的信息
			try {
				readStatus = new ReadStatus();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "初始化readStatus失败");
				//			if(timer!=null)
				//				timer.stop();
			}
			timer = new Timer(1000, this);
			if (ReadStatus.loginStatus == 1)
				timer.setDelay(readStatus.timer.getDelay());
			timer.start();
		}else
		{
//System.out.println("weblost");
			statusLabel.setForeground(Color.blue);
			statusLabel.setText("已断网");
//			logoutButton.setEnabled(false);			
		}
	}
    
    //设置显示的数据
    public void setTexts(String name,String used,String total,String remain,int status)
    {	if(!ReadStatus.WebLost)
    	{
    		this.nameText.setText(name);
    		this.usedText.setText(used);
    		this.totalText.setText(total);
    		this.remainText.setText(remain);
    		this.setLoginStatus(status,ReadStatus.useOut);
    	}else {
    		this.nameText.setText("");
    		this.usedText.setText("");
    		this.totalText.setText("");
    		this.remainText.setText("");
    		this.setLoginStatus(status,ReadStatus.useOut);
    	}
    }
    
    //设置状态标签显示的内容 包括已登录,未登录,用户名或密码错误,流量已用完,已断网
    public void setLoginStatus(int loginstatus,boolean useOut)
    {
    	if(!ReadStatus.WebLost)	
		{	if(loginstatus==1)
			{	statusLabel.setForeground(Color.green);
				statusLabel.setText("已登录");
				logoutButton.setEnabled(true);
			}
			else if(loginstatus==0)
			{
				statusLabel.setForeground(Color.red);
				statusLabel.setText("未登录");
				logoutButton.setEnabled(false);
			}else {
				statusLabel.setForeground(Color.blue);
				statusLabel.setText("用户名或密码错误");
				logoutButton.setEnabled(false);
			}
			if(useOut)
			{
				statusLabel.setForeground(Color.blue);
				statusLabel.setText("流量已用完");
				logoutButton.setEnabled(false);
			}
			if(ButtonAreaPanel.autoLogin)
			{	logoutButton.setEnabled(false);
			}
			else {	logoutButton.setEnabled(true);
						ButtonAreaPanel.autoLogin=false;
			}
		}
		else {
			statusLabel.setForeground(Color.blue);
			statusLabel.setText("已断网");
//			logoutButton.setEnabled(false);
//			ButtonAreaPanel.loginButton.setEnabled(false);
			
		}
    }
   
	public void actionPerformed(ActionEvent e) {
		
		//设置面板的数据
		this.setLoginStatus(ReadStatus.loginStatus, ReadStatus.useOut);
		//自动切换账号登录
		if(ReadStatus.useOut&&FlowAppMainFrame.autoSelect)
		{	
			int time=0;//记录试了几个账号
			//如果是登录着,发送退出的信息
			if(ReadStatus.loginStatus==1)
				try {
					new SendLoginRequest().logout(ResourcePath.SERVERPATH);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			//用别的账号登录
			if(ReadStatus.loginStatus==0)
				while(time<ButtonAreaPanel.readAccount.accountList.size())		
				{
					String key=ButtonAreaPanel.readAccount.accountList.get(time);
System.out.println(time);
					try {
						new SendLoginRequest().login(ResourcePath.SERVERPATH, 
								ButtonAreaPanel.readAccount.hashMap.get(key));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					//如果全部账号用完了,还是没登上,可能是用户名或密码错误
					if(ReadStatus.loginStatus==0&&time==ButtonAreaPanel.readAccount.accountList.size())
					{		JOptionPane.showMessageDialog(this, "已经没有可用的账号了");
							FlowAppMainFrame.autoSelect=false;}
					
					//如果全部账号用完了,还是没流量
					if(ReadStatus.useOut&&time==ButtonAreaPanel.readAccount.accountList.size())
					{	JOptionPane.showMessageDialog(this, "你拥有的账号流量全部用完了");
						FlowAppMainFrame.autoSelect=false;
					}
					//如果登上了而且该账号流量没用完的话,就退出
					if(!ReadStatus.useOut&&ReadStatus.loginStatus==1)
						break;
					time++;
				}			
		}
		
		//更新精简面板的数据
		if(FlowAppMainFrame.simplifyDialog!=null)
			FlowAppMainFrame.simplifyDialog.setTexts(ReadStatus.userName,ReadStatus.usedAmount, 
					ReadStatus.remainAmount,ReadStatus.loginStatus);
		this.setTexts(ReadStatus.userName,ReadStatus.usedAmount, 
				ReadStatus.totalAmount, ReadStatus.remainAmount,ReadStatus.loginStatus);
		
		//退出登录的处理动作
		if(e.getSource()==logoutButton)
			try {
				if(FlowAppMainFrame.autologin!=1&&FlowAppMainFrame.autologin!=2)
				{	new ConfigAutoLogin().write_1Name(ReadStatus.userName);
					FlowAppMainFrame.autologin=-1;
				}
				new SendLoginRequest().logout(ResourcePath.SERVERPATH);		
				//更新显示的数据
				this.setTexts(ReadStatus.userName,ReadStatus.usedAmount, 
						ReadStatus.totalAmount, ReadStatus.remainAmount,ReadStatus.loginStatus);
				ButtonAreaPanel.loginButton.setEnabled(true);			
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "发送退出信息失败");
			}
		
		//检查退出登录的按钮
		if(ReadStatus.loginStatus==0)
			logoutButton.setEnabled(false);
	}
	
	 public static void main(String args[])
	    {
	    	JFrame jf=new JFrame();
	    	jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    	jf.setSize(300, 300);
	    	jf.add(new FlowDisplayPanel(false));
	    	jf.setVisible(true);
	    }

}
