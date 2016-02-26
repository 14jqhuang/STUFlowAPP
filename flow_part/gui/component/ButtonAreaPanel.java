package gui.component;

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

import gui.account_dialog.AddAccountDialog;
import gui.alarm_dialog.AlarmSettingDialog;
import gui.alarm_dialog.PlayMusic;
import gui.mainfraim.FlowAppMainFrame;
import other.bean.Account;
import other.tool.FlowLogRequest;
import resource.loadconfig.LoadConfig;
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
	private LoadConfig config;
	
	//��¼�ǲ��ǳ�ʼ����ʱ��ļ������Զ�ѡ��
	public 	int i=0;
	
	public ButtonAreaPanel(){
		
		try {
			config = new LoadConfig();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		initAccount();
		
		drawGui();

		setListener();
		
		startTimer();
		
	}
	
	//��ʼ���˺���Ϣ
	private void initAccount()
	{
		try {
			Account=new Account();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "��ȡ�ļ�ʧ��");
		}		
	}
	
	//��ʼ��gui����
	private void drawGui()
	{
		this.setLayout(new GridLayout(3, 2));
		alarmButton=new JButton("��������");
		addUser=new JButton("����˻�");
		loginButton=new JButton("��¼");
		autoLoginChBox=new JCheckBox("�Զ���¼");
		autoSelectChBox=new JCheckBox("�Զ��л�");
		accountSelectCombo=new JComboBox<String>(Account.accountArrary);
		this.setBorder(new TitledBorder("������"));
		this.setLayout(new GridLayout(3, 2));
		this.add(alarmButton);
		this.add(addUser);
		this.add(accountSelectCombo);
		this.add(loginButton);
		this.add(autoLoginChBox);
		this.add(autoSelectChBox);
	}
	
	//����ʱ����
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
	
	//���ô�����
	private void setListener()
	{
		alarmButton.addActionListener(this);
		
		//����˺ŵĹ���
		addUser.addActionListener(e->{
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
		});
		
		//��¼��ť��ʵ��
		loginButton.addActionListener(e -> {
			if(!FlowAppMainFrame.webStatus.WebLost)
			{	
				try {
					String temp=((String) accountSelectCombo.getSelectedItem()).trim();		
					if(temp!=null&&FlowAppMainFrame.webStatus.loginStatus==FlowAppMainFrame.webStatus.OUT)
					{	
						params=Account.hashMap.get(temp);
						try {
							FlowLogRequest.login(ResourcePath.SERVERPATH	, params);
							}catch (IOException e1) {
							JOptionPane.showMessageDialog(null, "���͵�¼��Ϣʧ��");
						}
					}
					else JOptionPane.showMessageDialog(null, "�����˳��ѵ�¼���˺�");
				} catch (NullPointerException e1) {
					JOptionPane.showMessageDialog(this, "��ѡ���˺�");
				}
			}
		});
		accountSelectCombo.addActionListener(this);
		autoLoginChBox.addItemListener(this);
		autoSelectChBox.addItemListener(this);
	}
	
	//ʵ�ְ�ť��ʱ�����Ĵ�����
	public void actionPerformed(ActionEvent e) {
		String action=e.getActionCommand();
		if(action==null)
			action="none";
		
		//��ʼ���Զ���¼��ť
		if(FlowAppMainFrame.autologin)
			autoSelectChBox.setEnabled(true);
		else autoSelectChBox.setEnabled(false);
		
		//��ʼ���Զ��л���ť
		if(FlowAppMainFrame.autoSelect)
			autoSelectChBox.setSelected(true);
		else autoSelectChBox.setSelected(false);
		
		//�������ѵļ��
		if(!FlowAppMainFrame.webStatus.WebLost&&(alarmhasSet)&&
				(	FlowAppMainFrame.webStatus.flowStringToNumber(FlowAppMainFrame.webStatus.usedAmount)/1000000
				>=AlarmSettingDialog.alarmAmount))
		{
			music=new PlayMusic(AlarmSettingDialog.musicPath,true);
			music.play();
			music.showControlPanel(null, "��������");
		}
		
		
		//��������,����֮�����ȡ����������
		//ʱ����ִ�е�ʱ�򲻻ἤ���¼�
		if(action.equals("��������"))
		{	FlowAppMainFrame.inside=true;
			try {
				new AlarmSettingDialog(
						""+FlowAppMainFrame.webStatus.flowStringToNumber(FlowAppMainFrame.webStatus.totalAmount));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}			
		}
		if(alarmhasSet)
		{	alarmButton.setText("ȡ������");
			alarmButton.setActionCommand("ȡ������");
		}
		if(action.equals("ȡ������")||!alarmhasSet)
		{	alarmButton.setText("��������");
			alarmhasSet=false;
			alarmButton.setActionCommand("��������");
		}
		
		
		
		//��¼��ť�ļ��
		if(!FlowAppMainFrame.webStatus.WebLost)
		{	if(FlowAppMainFrame.webStatus.loginStatus==1)
			{	loginButton.setEnabled(false);
			}
			else {
				loginButton.setEnabled(true);
			}
		}
		
		//�Զ���¼�ļ��,����ǳ���,�Զ����͵�¼��Ϣ
		if(!FlowAppMainFrame.webStatus.WebLost&&autoLoginChBox.isSelected()
				&&FlowAppMainFrame.webStatus.loginStatus==0)
		{
			String defaultAccount = config.getDefaultLoginAccount();
			if(defaultAccount==null)
				config.getLoginAccount();
			
			params=Account.hashMap.get(defaultAccount);
			try {
				FlowLogRequest.login(ResourcePath.SERVERPATH	, params);
			}catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "���͵�¼��Ϣʧ��");	
			}
		}
		
	}

	//ʵ��comboBox�Ĵ�����
	public void itemStateChanged(ItemEvent e) {
		
		//�Զ���¼�Ĺ��ܼ�¼
		if(autoLoginChBox.isSelected())
		{	autoLogin=true;								//����Ϊ����flowdisplay�İ�ť������	,����ȫ����Ϣ		
			if(FlowAppMainFrame.webStatus.loginStatus==1)
				if(FlowAppMainFrame.autologin)
				{	
					config.setLoginAccount(FlowAppMainFrame.webStatus.userName);
				}
			//�Զ��л�
			if(autoSelectChBox.isSelected())
			{	
				config.setProperty("autoselect", "yes");
			}
			else {		
				config.setProperty("autoselect", "no");
			}
		}
		else 	
			{
				autoLogin=false;	
				config.setProperty("autologin", "no");
			}
	}
}
