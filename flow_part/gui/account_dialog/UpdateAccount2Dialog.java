package gui.account_dialog;

import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import function.account_operate.AccountOperator;
import function.account_operate.UpdateUser;
import gui.component.ButtonAreaPanel;

/*
 * 2�����
 */
@SuppressWarnings("serial")
public class UpdateAccount2Dialog extends JDialog implements ActionListener {
	private JTextField userNameInput;
	private JPasswordField passwordInput;
	private JButton sureButton;
	private JButton cancalButton;
	private  int index;
	private AccountOperator updateAccount;
	
	public UpdateAccount2Dialog(int index)  {

		this.setAlwaysOnTop(true);
		this.setTitle("�޸��˺�");
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		this.setLocationRelativeTo(null);
		this.setSize(180, 180);
		this.index=index;

		JPanel panel1=new JPanel();		
		panel1.add(new JLabel("�û���:"));
		panel1.add((userNameInput=new JTextField(15)));
		
		JPanel panel2=new JPanel();
		panel2.add(new JLabel("����:"));
		panel2.add((passwordInput=new JPasswordField(15)));
		
		JPanel panel3=new JPanel();
		sureButton=new JButton("ȷ��");
		cancalButton=new JButton("ȡ��");
		sureButton.addActionListener(this);
		cancalButton.addActionListener(this);
		panel3.add(sureButton);
		panel3.add(cancalButton);
		
		JPanel panel0=new JPanel(new GridLayout(3, 1));
		panel0.add(panel1);
		panel0.add(panel2);
		panel0.add(panel3);
		this.add(panel0);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);		
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand()=="ȡ��")
			this.dispose();
		if(e.getSource()==sureButton)
		{
			if(userNameInput.getText().trim().length()>=4&&
					(new String(passwordInput.getPassword())).trim().length()!=0)
			{	
				try {
					updateAccount= new UpdateUser(ButtonAreaPanel.Account,
							index, userNameInput.getText(),
							new String(passwordInput.getPassword()));
				} catch (UnsupportedEncodingException e2) {
					e2.printStackTrace();
				}
				this.dispose();
				try {
					ButtonAreaPanel.Account= updateAccount.operate();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(this, "�޸ĳɹ�");
			}
			else JOptionPane.showMessageDialog(this, "��������ȷ������");
		}
	}
}
