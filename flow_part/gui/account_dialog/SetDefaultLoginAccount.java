package gui.account_dialog;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import function.config_auto_file.ConfigAutoLogin;
import gui.mainfraim.FlowAppMainFrame;

@SuppressWarnings("serial")
public class SetDefaultLoginAccount extends JDialog implements ActionListener{
	private JComboBox<String> accountSelectCombo;
	private JButton sureButton,cancelButton;
	private ConfigAutoLogin config;//config是用来把账户信息写进文件的 只需要new 就行
	public SetDefaultLoginAccount(JFrame jframe,String[] accouts,ConfigAutoLogin config) {
		// TODO Auto-generated constructor stub
		this.setLayout(new GridLayout(2, 1));
		this.config=config;
		accountSelectCombo=new JComboBox<String>(accouts);
		this.add(accountSelectCombo);
		JPanel tempPanel=new JPanel();
		sureButton=new JButton("确定");
		cancelButton=new JButton("取消");
		sureButton.addActionListener(this);
		cancelButton.addActionListener(this);
		tempPanel.add(sureButton);
		tempPanel.add(cancelButton);
		this.add(tempPanel);		
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(jframe);
		this.setSize(120, 100);
		this.setResizable(false);
		this.setTitle("自登账号");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	
	public SetDefaultLoginAccount(String[] accouts,ConfigAutoLogin config) {
		// TODO Auto-generated constructor stub
		this.setLayout(new GridLayout(2, 1));
		this.config=config;
		accountSelectCombo=new JComboBox<String>(accouts);
		this.add(accountSelectCombo);
		JPanel tempPanel=new JPanel();
		sureButton=new JButton("确定");
		cancelButton=new JButton("取消");
		sureButton.addActionListener(this);
		cancelButton.addActionListener(this);
		tempPanel.add(sureButton);
		tempPanel.add(cancelButton);
		this.add(tempPanel);		
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(null);
		this.setSize(120, 100);
		this.setResizable(false);
		this.setTitle("自登账号");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String a=e.getActionCommand();
		if(a.equals("确定"))
		{	config.write1Name((String)accountSelectCombo.getSelectedItem());
			FlowAppMainFrame.autologin=1;//防止写成自动登录的账号
			this.dispose();
		}
		if(a.equals("取消"))
			this.dispose();
	}
}
