package gui.component;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import function.config_auto_file.ConfigAutoLogin;
import gui.mainfraim.FlowAppMainFrame;
import other.tool.FlowLogRequest;
import resource.webserver.ResourcePath;

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
	
	//参数显示要不要精简模式,测试时用false
    public FlowDisplayPanel(boolean simplify) {
    	
    	drawGui();
    	
    	startTimer();
	}
    
    public void startTimer()
    {
    	timer= new Timer(FlowAppMainFrame.controller.interval, this);
    	FlowAppMainFrame.controller.setFlowPanelTimer(timer);
    	timer.start();
    }
    
    //设置显示的数据
    private void setTexts(String name,String used,String total,String remain,int status)
    {	if(!FlowAppMainFrame.webStatus.WebLost)
    	{
    		this.nameText.setText(name);
    		this.usedText.setText(used);
    		this.totalText.setText(total);
    		this.remainText.setText(remain);
    		this.setLoginStatus(status,FlowAppMainFrame.webStatus.useOut);
    	}
    	else {//断网
    		this.nameText.setText("");
    		this.usedText.setText("");
    		this.totalText.setText("");
    		this.remainText.setText("");
    		this.setLoginStatus(status,FlowAppMainFrame.webStatus.useOut);
    	}
    }
    
    //设置状态标签显示的内容 包括已登录,未登录,用户名或密码错误,流量已用完,已断网,还有时间器的delay设置
    private void setLoginStatus(int loginstatus,boolean useOut)
    {
    	if(!FlowAppMainFrame.webStatus.WebLost)	
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
		}
    }
   
    /**
     * 绘制gui,绑定logoutbutton的处理逻辑
     */
    private void drawGui()
    {
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
    	this.add(new JLabel("M"),constraints);
    	
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
    	this.add(new JLabel("M"),constraints);
    	
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
    	this.add(new JLabel("M"),constraints);
    	
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
    	
    	//退出登录的处理动作
    	logoutButton.addActionListener(e->{
    		if(e.getSource()==logoutButton)
    			try {
    				
    				try {
    					FlowAppMainFrame.controller.setDelay(1);//设置为1毫秒的极速反应
    				} catch (NullPointerException e1) {
    				}
    				
    				if(FlowAppMainFrame.autologin!=1&&FlowAppMainFrame.autologin!=2)
    				{	new ConfigAutoLogin().write_1Name(FlowAppMainFrame.webStatus.userName);
    					FlowAppMainFrame.autologin=-1;
    				}
    				FlowLogRequest.logout(ResourcePath.SERVERPATH);		
    				//更新显示的数据
    				this.setTexts(FlowAppMainFrame.webStatus.userName,FlowAppMainFrame.webStatus.usedAmount, 
    						FlowAppMainFrame.webStatus.totalAmount, FlowAppMainFrame.webStatus.remainAmount,FlowAppMainFrame.webStatus.loginStatus);
    				ButtonAreaPanel.loginButton.setEnabled(true);			
    			} catch (IOException e1) {
    				JOptionPane.showMessageDialog(null, "发送退出信息失败");
    			}
    	});
    	this.add(logoutButton, constraints);
    }

    public void actionPerformed(ActionEvent e) {
		
		//更新面板的数据
		this.setLoginStatus(FlowAppMainFrame.webStatus.loginStatus, 
				FlowAppMainFrame.webStatus.useOut);
		
		//自动切换账号登录
		if(FlowAppMainFrame.webStatus.useOut&&FlowAppMainFrame.autoSelect)
		{	
			int time=0;//记录试了几个账号
			//如果是登录着,发送退出的信息
			if(FlowAppMainFrame.webStatus.loginStatus==1)
				try {
					FlowLogRequest.logout(ResourcePath.SERVERPATH);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			
			//暂停1秒
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//用别的账号登录
			if(FlowAppMainFrame.webStatus.loginStatus==0)
				while(time<ButtonAreaPanel.Account.accountList.size())		
				{
					String key=ButtonAreaPanel.Account.accountList.get(time);
					try {
						FlowLogRequest.login(ResourcePath.SERVERPATH, 
								ButtonAreaPanel.Account.hashMap.get(key));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					//暂停1秒
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					//如果全部账号用完了,还是没登上,可能是用户名或密码错误
					if(FlowAppMainFrame.webStatus.loginStatus==0&&time==ButtonAreaPanel.Account.accountList.size())
					{		JOptionPane.showMessageDialog(this, "用户名或密码错误,已经没有可用的账号了");
							FlowAppMainFrame.autoSelect=false;}
					
					//如果全部账号用完了,还是没流量
					if(FlowAppMainFrame.webStatus.useOut&&time==(ButtonAreaPanel.Account.accountList.size()-1))
					{	JOptionPane.showMessageDialog(this, "你拥有的账号流量全部用完了");
						FlowAppMainFrame.autoSelect=false;
					}
					//如果登上了而且该账号流量没用完的话,就退出循环
					if(!FlowAppMainFrame.webStatus.useOut&&FlowAppMainFrame.webStatus.loginStatus==1)
						break;
					time++;
				}			
		}
		
		try {
			//更新精简面板的数据
			String username = FlowAppMainFrame.webStatus.userName;
			
			String used = ""+ FlowAppMainFrame.webStatus.flowStringToNumber(
							FlowAppMainFrame.webStatus.usedAmount)/1000000;
			
			String total = "" + FlowAppMainFrame.webStatus.flowStringToNumber(
					FlowAppMainFrame.webStatus.totalAmount)/1000000;
			
			String remain = ""+FlowAppMainFrame.webStatus.flowStringToNumber(
					FlowAppMainFrame.webStatus.remainAmount)/1000000;
			
			int status = FlowAppMainFrame.webStatus.loginStatus;
			
			if(FlowAppMainFrame.simplifyDialog!=null)
				FlowAppMainFrame.simplifyDialog.setTexts(username,used,remain,status);
			
			//更新自己面板的数据
			this.setTexts(username,used,total,remain,status);
		} catch (NullPointerException e2) {
			//还没有数据的时候就会激发这个错误
		}catch (NumberFormatException e2) {
			
			if(FlowAppMainFrame.simplifyDialog!=null)
				FlowAppMainFrame.simplifyDialog.setTexts( "", "" ,"" , 
						FlowAppMainFrame.webStatus.loginStatus);
			
			this.setTexts( "" , "" , "" , "" , 
					FlowAppMainFrame.webStatus.loginStatus);
		}
		
		//检查退出登录的按钮
		if(FlowAppMainFrame.webStatus.loginStatus==0)
			logoutButton.setEnabled(false);
	}
}
