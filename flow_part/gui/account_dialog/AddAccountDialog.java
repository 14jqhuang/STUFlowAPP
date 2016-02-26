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
import function.account_operate.AddUser;
import gui.component.ButtonAreaPanel;

@SuppressWarnings("serial")
public class AddAccountDialog extends JDialog implements ActionListener {
	public JTextField userNameInput;
	public JPasswordField passwordInput;
	private JButton sureButton;
	private JButton cancalButton;
	public  AccountOperator addAccount;
	public AddAccountDialog() throws UnsupportedEncodingException  {
		this.setAlwaysOnTop(true);
		this.setTitle("����˺�");
		this.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		this.setBounds(400, 200, 180, 180);

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
			//���û���������д���ļ��������˺��б�
			if(userNameInput.getText().trim().length()>=4&&
					(new String(passwordInput.getPassword())).trim().length()!=0)
			{	addAccount=new AddUser(ButtonAreaPanel.Account,
					"@"+	userNameInput.getText()+"&"+new String(passwordInput.getPassword()));
				try {
					ButtonAreaPanel.Account=addAccount.operate();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				this.dispose();
			}
			else JOptionPane.showMessageDialog(this, "��������ȷ������");
		}
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		@SuppressWarnings("unused")
		AddAccountDialog jf=new AddAccountDialog();
	}

}
