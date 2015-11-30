package gui.account_dialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import function.account_operate.AccountOperator;
import function.account_operate.DropUser;
import lin.component.ButtonAreaPanel;

@SuppressWarnings("serial")
public class DropAccountDialog extends UpdateAccountDialog implements ActionListener {
	
	private AccountOperator dropUser;
	
	public DropAccountDialog(JFrame jframe) {
		super(jframe);
		this.sureButton.removeActionListener(sureButton.getAction());
		this.sureButton.addActionListener(this);	
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("确定"))
		{	
			try {
				dropUser= new DropUser(ButtonAreaPanel.Account
						,this.accountSelectCombo.getSelectedIndex());
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			
			try {
				ButtonAreaPanel.Account= dropUser.operate();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			JOptionPane.showMessageDialog(this, "删除成功");
			super.dispose();
		}else {
			super.dispose();
		}
	}
	public static void main(String args[])
	{
		new DropAccountDialog(null);
	}
}
