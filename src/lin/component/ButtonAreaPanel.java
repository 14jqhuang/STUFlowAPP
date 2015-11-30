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
import function.read_data_from_website.ReadStatus;
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
	
	//��¼�ǲ��ǳ�ʼ����ʱ��ļ������Զ�ѡ��
	public 	int i=0;
	
	public ButtonAreaPanel() {
		
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
		
		timer=new Timer(1000, this);
		FlowAppMainFrame.controller.addTimer(timer);
		if(!ReadStatus.WebLost)
			timer.start();
		else loginButton.setEnabled(false);
		if(ReadStatus.loginStatus==ReadStatus.IN)
			loginButton.setEnabled(false);		
				
	}
	
	//���ô�����
	private void setListener()
	{
		alarmButton.addActionListener(this);
		addUser.addActionListener(this);
		loginButton.addActionListener(this);
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
		if(FlowAppMainFrame.autologin>0)
			autoSelectChBox.setEnabled(true);
		else autoSelectChBox.setEnabled(false);
		
		//��ʼ���Զ��л���ť
		if(FlowAppMainFrame.autoSelect)
			autoSelectChBox.setSelected(true);
		else autoSelectChBox.setSelected(false);
		
		//�������ѵļ��
		if(!ReadStatus.WebLost&&(alarmhasSet)&&(Integer.parseInt(ReadStatus.subNum(ReadStatus.usedAmount))
				>=AlarmSettingDialog.alarmAmount))
		{
			music=new PlayMusic(AlarmSettingDialog.musicPath,true);
			music.play();
			music.showControlPanel(null, "��������");
		}
		
		
		//��������,����֮�����ȡ����������
		if(action.equals("��������"))//���������getAccom..�ᱨ�� Ϊë��?��Ϊʱ����ִ�е�ʱ��û�м����¼�
		{	FlowAppMainFrame.inside=true;
			try {
				new AlarmSettingDialog(ReadStatus.subNum(ReadStatus.totalAmount));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
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
		
		//����˺ŵĹ���
		if(e.getSource()==addUser)
		{	FlowAppMainFrame.inside=true;
			try {
				new AddAccountDialog();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(accountSelectCombo!=null)
				accountSelectCombo.removeAllItems();
			for(String item:Account.accountArrary)
				accountSelectCombo.addItem(item);
		}
		
		
		//��¼����
		if(!ReadStatus.WebLost&&e.getSource()==loginButton)
		{	
			String temp=((String) accountSelectCombo.getSelectedItem()).trim();		
			if(temp!=null&&ReadStatus.loginStatus==ReadStatus.OUT)
			{	
				params=Account.hashMap.get(temp);
				try {
					SendLogRequest.login(ResourcePath.SERVERPATH	, params);
					}catch (IOException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "���͵�¼��Ϣʧ��");
				}
			}
			else JOptionPane.showMessageDialog(null, "�����˳��ѵ�¼���˺�");
		}
		
		//��¼��ť�ļ��
		if(!ReadStatus.WebLost)
		{	if(ReadStatus.loginStatus==1)
			{	loginButton.setEnabled(false);
	//			accountSelectCombo.setEditable(false);
			}
			else {
				loginButton.setEnabled(true);
	//			accountSelectCombo.setEditable(true);
			}
		}
		
		//�Զ���¼�ļ��,����ǳ���,�Զ����͵�¼��Ϣ
		if(!ReadStatus.WebLost&&autoLoginChBox.isSelected()&&ReadStatus.loginStatus==0)
		{
			String defaultAccount = null;
			try {
				defaultAccount=new ConfigAutoLogin().readName();
//System.out.println("defaultAccount=   "+defaultAccount);
				if(defaultAccount==null)
					setDefaultLoginAccount=new SetDefaultLoginAccount(Account.accountArrary, new ConfigAutoLogin());
				params=Account.hashMap.get(defaultAccount);
				try {
					SendLogRequest.login(ResourcePath.SERVERPATH	, params);
					}catch (IOException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(null, "���͵�¼��Ϣʧ��");	}
				} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();}
		}
		
	}

	//ʵ��comboBox�Ĵ�����
	public void itemStateChanged(ItemEvent e) {
		
		//�Զ���¼�Ĺ��ܼ�¼
		if(autoLoginChBox.isSelected())
		{	autoLogin=true;//����Ϊ����flowdisplay�İ�ť������	,����ȫ����Ϣ		
			if(ReadStatus.loginStatus==1)
				try {
					if(FlowAppMainFrame.autologin<=0)
					{	new ConfigAutoLogin().write2Name(ReadStatus.userName);
						FlowAppMainFrame.autologin=2;
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			//д���Ƿ��Զ��л������� ��Ҫ�������¼�ļ�,��Ϊ��ʼ����ʱ�������δѡ�е�״̬
//			autoSelectChBox.setSelected(true);
			if(autoSelectChBox.isSelected())
			{	
				if (i!=0) {
					FlowAppMainFrame.autoSelect = true;
					//�½�һ����¼���ļ�
					try {
						new ConfigAutoSelect().writeY();
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//System.out.println("select");
				}
				i++;

			}
			else {		
				if (i!=0) {
					//û��ѡ���Զ��л��Ļ���д�� n
//System.out.println("didn't select");
					try {
						new ConfigAutoSelect().writeN();
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
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
