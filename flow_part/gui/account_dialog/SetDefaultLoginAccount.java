package gui.account_dialog;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import resource.loadconfig.LoadConfig;

@SuppressWarnings("serial")
public class SetDefaultLoginAccount extends JDialog implements ActionListener{
	private JComboBox<String> accountSelectCombo;
	private JButton sureButton,cancelButton;
	private LoadConfig config;
	
	public SetDefaultLoginAccount(JFrame jframe,String[] accounts,LoadConfig config) {
		this(accounts,config);
		this.setLocationRelativeTo(jframe);
		this.setVisible(true);
	}

	
	public SetDefaultLoginAccount(String[] accounts,LoadConfig config) {
		this.config = config;
		this.setLayout(new GridLayout(2, 1));
		accountSelectCombo=new JComboBox<String>(accounts);
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
		String a=e.getActionCommand();
		if(a.equals("确定"))
		{	
			config.setDefaultLoginAccount((String)accountSelectCombo.getSelectedItem());
			this.dispose();
		}
		if(a.equals("取消"))
			this.dispose();
	}
}
