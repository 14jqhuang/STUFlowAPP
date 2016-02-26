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
	 * �ǵ��˳���ʱ���ʱ��ص�
	 */
	public JTextField usedText,totalText,remainText,nameText;//������ʾ��������ֵ
	public JLabel statusLabel;
	public JButton logoutButton;
	private GridBagLayout gridbag;
	private GridBagConstraints constraints;
	public  Timer timer;
	
	//������ʾҪ��Ҫ����ģʽ,����ʱ��false
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
    
    //������ʾ������
    private void setTexts(String name,String used,String total,String remain,int status)
    {	if(!FlowAppMainFrame.webStatus.WebLost)
    	{
    		this.nameText.setText(name);
    		this.usedText.setText(used);
    		this.totalText.setText(total);
    		this.remainText.setText(remain);
    		this.setLoginStatus(status,FlowAppMainFrame.webStatus.useOut);
    	}
    	else {//����
    		this.nameText.setText("");
    		this.usedText.setText("");
    		this.totalText.setText("");
    		this.remainText.setText("");
    		this.setLoginStatus(status,FlowAppMainFrame.webStatus.useOut);
    	}
    }
    
    //����״̬��ǩ��ʾ������ �����ѵ�¼,δ��¼,�û������������,����������,�Ѷ���,����ʱ������delay����
    private void setLoginStatus(int loginstatus,boolean useOut)
    {
    	if(!FlowAppMainFrame.webStatus.WebLost)	
		{	if(loginstatus==1)
			{	statusLabel.setForeground(Color.green);
				statusLabel.setText("�ѵ�¼");
				logoutButton.setEnabled(true);
			}
			else if(loginstatus==0)
			{
				statusLabel.setForeground(Color.red);
				statusLabel.setText("δ��¼");
				logoutButton.setEnabled(false);
			}else {
				statusLabel.setForeground(Color.blue);
				statusLabel.setText("�û������������");
				logoutButton.setEnabled(false);
			}
			if(useOut)
			{
				statusLabel.setForeground(Color.blue);
				statusLabel.setText("����������");
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
			statusLabel.setText("�Ѷ���");
		}
    }
   
    /**
     * ����gui,��logoutbutton�Ĵ����߼�
     */
    private void drawGui()
    {
    	//����չʾ��
    	this.setBorder(new TitledBorder("����"));
    	gridbag=new GridBagLayout();
    	this.setLayout(gridbag);
    	
    	constraints=new GridBagConstraints(0, 0, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	this.add(new JLabel("�û���:"), constraints);    	
    	constraints=new GridBagConstraints(1, 0, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	nameText=new JTextField();
    	nameText.setEditable(false);
    	this.add(nameText, constraints);
    	
    	constraints=new GridBagConstraints(0, 2, 1, 1, 1, 1,GridBagConstraints.CENTER
    				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 5, 5);
    	this.add(new JLabel("��ʹ������:"),constraints);    	
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
    	this.add(new JLabel("������:"), constraints);    	
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
    	this.add(new JLabel("ʣ������:"), constraints);
    	
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
    	statusLabel=new JLabel("δ��¼",JLabel.CENTER);
    	statusLabel.setForeground(Color.red);
    	this.add(statusLabel, constraints);
    	
    	constraints=new GridBagConstraints(0, 5, 3, 1, GridBagConstraints.REMAINDER
    			, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	logoutButton=new JButton("�˳���¼");
    	
    	//�˳���¼�Ĵ�����
    	logoutButton.addActionListener(e->{
    		if(e.getSource()==logoutButton)
    			try {
    				
    				try {
    					FlowAppMainFrame.controller.setDelay(1);//����Ϊ1����ļ��ٷ�Ӧ
    				} catch (NullPointerException e1) {
    				}
    				
    				if(FlowAppMainFrame.autologin!=1&&FlowAppMainFrame.autologin!=2)
    				{	new ConfigAutoLogin().write_1Name(FlowAppMainFrame.webStatus.userName);
    					FlowAppMainFrame.autologin=-1;
    				}
    				FlowLogRequest.logout(ResourcePath.SERVERPATH);		
    				//������ʾ������
    				this.setTexts(FlowAppMainFrame.webStatus.userName,FlowAppMainFrame.webStatus.usedAmount, 
    						FlowAppMainFrame.webStatus.totalAmount, FlowAppMainFrame.webStatus.remainAmount,FlowAppMainFrame.webStatus.loginStatus);
    				ButtonAreaPanel.loginButton.setEnabled(true);			
    			} catch (IOException e1) {
    				JOptionPane.showMessageDialog(null, "�����˳���Ϣʧ��");
    			}
    	});
    	this.add(logoutButton, constraints);
    }

    public void actionPerformed(ActionEvent e) {
		
		//������������
		this.setLoginStatus(FlowAppMainFrame.webStatus.loginStatus, 
				FlowAppMainFrame.webStatus.useOut);
		
		//�Զ��л��˺ŵ�¼
		if(FlowAppMainFrame.webStatus.useOut&&FlowAppMainFrame.autoSelect)
		{	
			int time=0;//��¼���˼����˺�
			//����ǵ�¼��,�����˳�����Ϣ
			if(FlowAppMainFrame.webStatus.loginStatus==1)
				try {
					FlowLogRequest.logout(ResourcePath.SERVERPATH);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			
			//��ͣ1��
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//�ñ���˺ŵ�¼
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
					
					//��ͣ1��
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					//���ȫ���˺�������,����û����,�������û������������
					if(FlowAppMainFrame.webStatus.loginStatus==0&&time==ButtonAreaPanel.Account.accountList.size())
					{		JOptionPane.showMessageDialog(this, "�û������������,�Ѿ�û�п��õ��˺���");
							FlowAppMainFrame.autoSelect=false;}
					
					//���ȫ���˺�������,����û����
					if(FlowAppMainFrame.webStatus.useOut&&time==(ButtonAreaPanel.Account.accountList.size()-1))
					{	JOptionPane.showMessageDialog(this, "��ӵ�е��˺�����ȫ��������");
						FlowAppMainFrame.autoSelect=false;
					}
					//��������˶��Ҹ��˺�����û����Ļ�,���˳�ѭ��
					if(!FlowAppMainFrame.webStatus.useOut&&FlowAppMainFrame.webStatus.loginStatus==1)
						break;
					time++;
				}			
		}
		
		try {
			//���¾�����������
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
			
			//�����Լ���������
			this.setTexts(username,used,total,remain,status);
		} catch (NullPointerException e2) {
			//��û�����ݵ�ʱ��ͻἤ���������
		}catch (NumberFormatException e2) {
			
			if(FlowAppMainFrame.simplifyDialog!=null)
				FlowAppMainFrame.simplifyDialog.setTexts( "", "" ,"" , 
						FlowAppMainFrame.webStatus.loginStatus);
			
			this.setTexts( "" , "" , "" , "" , 
					FlowAppMainFrame.webStatus.loginStatus);
		}
		
		//����˳���¼�İ�ť
		if(FlowAppMainFrame.webStatus.loginStatus==0)
			logoutButton.setEnabled(false);
	}
}
