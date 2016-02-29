package lin.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import lin.readwrite.DropAccount;

@SuppressWarnings("serial")
public class DropAccountDialog extends UpdateAccountDialog implements ActionListener {
	public DropAccountDialog(JFrame jframe) {
		// TODO Auto-generated constructor stub
		super(jframe);
		this.sureButton.removeActionListener(sureButton.getAction());
		this.sureButton.addActionListener(this);	
	}
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("确定"))
		{	
			try {
				new DropAccount(this.accountSelectCombo.getSelectedIndex());
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
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
