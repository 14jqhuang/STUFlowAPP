package lin.component;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import function.config_auto_file.ConfigAutoLogin;
import function.config_auto_file.ConfigAutoSelect;
import gui.account_dialog.AddAccountDialog;
import gui.account_dialog.SetDefaultLoginAccount;
import gui.alarm_dialog.AlarmSettingDialog;
import gui.alarm_dialog.PlayMusic;
import gui.mainfraim.FlowAppMainFrame;
import other.bean.Account;
import other.tool.SendLogRequest;
import resource.webserver.ResourcePath;

@SuppressWarnings("serial")
public class ButtonAreaPanel extends JPanel implements ActionListener, ItemListener {

	public  JComboBox<String> accountSelectCombo;
	public  static JButton loginButton;
	public  JButton addUser;
	public  JCheckBox autoLoginChBox;
	public  JCheckBox autoSelectChBox;
	private JButton alarmButton;
	public AddAccountDialog accountDialog;
	public static boolean autoLogin=false;
	public static boolean hasDefault;
	public static Account Account;
	public static boolean alarmhasSet=false;
	public String params;
	public Timer timer;
	public PlayMusic music;
	public SetDefaultLoginAccount setDefaultLoginAccount;
	
	//记录是不是初始化的时候的激发的自动选择
	public 	int i=0;
	
	public ButtonAreaPanel() {
		
		initAccount();
		
		drawGui();

		setListener();
		
		startTimer();
		
	}
	
	//初始化账号信息
	private void initAccount()
	{
		try {
			Account=new Account();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "读取文件失败");
		}		
	}
	
	//初始化gui界面
	private void drawGui()
	{
		this.setLayout(new GridLayout(3, 2));
		alarmButton=new JButton("设置提醒");
		addUser=new JButton("添加账户");
		loginButton=new JButton("登录");
		autoLoginChBox=new JCheckBox("自动登录");
		autoSelectChBox=new JCheckBox("自动切换");
		accountSelectCombo=new JComboBox<String>(Account.accountArrary);
		this.setBorder(new TitledBorder("功能区"));
		this.setLayout(new GridLayout(3, 2));
		this.add(alarmButton);
		this.add(addUser);
		this.add(accountSelectCombo);
		this.add(loginButton);
		this.add(autoLoginChBox);
		this.add(autoSelectChBox);
	}
	
	//启动时间器
	private void startTimer()
	{
		
		timer=new Timer(FlowAppMainFrame.controller.interval, this);
		FlowAppMainFrame.controller.setButtonPanelTimer(timer);
		if(!FlowAppMainFrame.webStatus.WebLost)
			timer.start();
		else loginButton.setEnabled(false);
		if(FlowAppMainFrame.webStatus.loginStatus==FlowAppMainFrame.webStatus.IN)
			loginButton.setEnabled(false);		
				
	}
	
	//设置触发器
	private void setListener()
	{
		alarmButton.addActionListener(this);
		addUser.addActionListener(this);
		loginButton.addActionListener(this);
		accountSelectCombo.addActionListener(this);
		autoLoginChBox.addItemListener(this);
		autoSelectChBox.addItemListener(this);
	}
	
	//实现按钮和时间器的触发器
	public void actionPerformed(ActionEvent e) {
		String action=e.getActionCommand();
		if(action==null)
			action="none";
		
		//初始化自动登录按钮
		if(FlowAppMainFrame.autologin>0)
			autoSelectChBox.setEnabled(true);
		else autoSelectChBox.setEnabled(false);
		
		//初始化自动切换按钮
		if(FlowAppMainFrame.autoSelect)
			autoSelectChBox.setSelected(true);
		else autoSelectChBox.setSelected(false);
		
		//流量提醒的检查
		if(!FlowAppMainFrame.webStatus.WebLost&&(alarmhasSet)&&
				(	FlowAppMainFrame.webStatus.flowStringToNumber(FlowAppMainFrame.webStatus.usedAmount)/1000000
				>=AlarmSettingDialog.alarmAmount))
		{
			music=new PlayMusic(AlarmSettingDialog.musicPath,true);
			music.play();
			music.showControlPanel(null, "流量警告");
		}
		
		
		//设置提醒,设置之后会变成取消提醒字样
		if(action.equals("设置提醒"))//这里如果用getAccom..会报错 为毛啊?因为时间器执行的时候没有激发事件
		{	FlowAppMainFrame.inside=true;
			try {
				new AlarmSettingDialog(
						""+FlowAppMainFrame.webStatus.flowStringToNumber(FlowAppMainFrame.webStatus.totalAmount));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}			
		}
		if(alarmhasSet)
		{	alarmButton.setText("取消提醒");
			alarmButton.setActionCommand("取消提醒");
		}
		if(action.equals("取消提醒")||!alarmhasSet)
		{	alarmButton.setText("设置提醒");
			alarmhasSet=false;
			alarmButton.setActionCommand("设置提醒");
		}
		
		//添加账号的功能
		if(e.getSource()==addUser)
		{	FlowAppMainFrame.inside=true;
			try {
				new AddAccountDialog();
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			if(accountSelectCombo!=null)
				accountSelectCombo.removeAllItems();
			for(String item:Account.accountArrary)
				accountSelectCombo.addItem(item);
		}
		
		
		//登录功能
		if(!FlowAppMainFrame.webStatus.WebLost&&e.getSource()==loginButton)
		{	
//			try {
//				FlowAppMainFrame.controller.setDelay(10*1000);
//				FlowAppMainFrame.controller.restartAll();
//			} catch (NullPointerException e2) {
//			}
			
			String temp=((String) accountSelectCombo.getSelectedItem()).trim();		
			if(temp!=null&&FlowAppMainFrame.webStatus.loginStatus==FlowAppMainFrame.webStatus.OUT)
			{	
				params=Account.hashMap.get(temp);
				try {
					SendLogRequest.login(ResourcePath.SERVERPATH	, params);
					}catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "发送登录信息失败");
				}
			}
			else JOptionPane.showMessageDialog(null, "请先退出已登录的账号");
		}
		
		//登录按钮的检查
		if(!FlowAppMainFrame.webStatus.WebLost)
		{	if(FlowAppMainFrame.webStatus.loginStatus==1)
			{	loginButton.setEnabled(false);
			}
			else {
				loginButton.setEnabled(true);
			}
		}
		
		//自动登录的检查,如果登出了,自动发送登录信息
		if(!FlowAppMainFrame.webStatus.WebLost&&autoLoginChBox.isSelected()&&FlowAppMainFrame.webStatus.loginStatus==0)
		{
			String defaultAccount = null;
			try {
				defaultAccount=new ConfigAutoLogin().readName();
				if(defaultAccount==null)
					setDefaultLoginAccount=new SetDefaultLoginAccount(Account.accountArrary, new ConfigAutoLogin());
				params=Account.hashMap.get(defaultAccount);
				try {
					SendLogRequest.login(ResourcePath.SERVERPATH	, params);
					}catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "发送登录信息失败");	}
				} catch (IOException e1) {
				e1.printStackTrace();}
		}
		
	}

	//实现comboBox的触发器
	public void itemStateChanged(ItemEvent e) {
		
		//自动登录的功能记录
		if(autoLoginChBox.isSelected())
		{	autoLogin=true;//这是为了让flowdisplay的按钮不可用	,传递全局信息		
			if(FlowAppMainFrame.webStatus.loginStatus==1)
				try {
					if(FlowAppMainFrame.autologin<=0)
					{	new ConfigAutoLogin().write2Name(FlowAppMainFrame.webStatus.userName);
						FlowAppMainFrame.autologin=2;
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			
			//写入是否自动切换的资料 不要在这里记录文件,因为初始化的时候会先有未选中的状态
//			autoSelectChBox.setSelected(true);
			if(autoSelectChBox.isSelected())
			{	
				if (i!=0) {
					FlowAppMainFrame.autoSelect = true;
					//新建一个记录的文件
					try {
						new ConfigAutoSelect().writeY();
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
				}
				i++;

			}
			else {		
				if (i!=0) {
					//没有选中自动切换的话就写入 n
//System.out.println("didn't select");
					try {
						new ConfigAutoSelect().writeN();
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					}
					FlowAppMainFrame.autoSelect = false;
					autoSelectChBox.setSelected(false);
				}
				i++;
			}
//System.out.println(autoLogin);
		}
		else 	
			{
				autoLogin=false;	
				FlowAppMainFrame.autologin=-1;
			}
	}
}
