package lin.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import lin.component.ButtonAreaPanel;
import lin.function.UpdateAccount2Dialog;

@SuppressWarnings("serial")
/*
 * 主面板点击修改的选项的时候显示的面板
 */
public class UpdateAccountDialog extends JDialog implements ActionListener{
	public  JComboBox<String> accountSelectCombo;
	JButton sureButton;
	private JButton cancelButton;
	public UpdateAccountDialog(JFrame jframe)   {
		this.setLayout(new GridLayout(2, 1));
		this.setTitle("选择账号");	
		accountSelectCombo=new JComboBox<String>(ButtonAreaPanel.readAccount.accountArrary);
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
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	//显示修改数据的面板添加新的数据
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==sureButton)
		{	new UpdateAccount2Dialog(accountSelectCombo.getSelectedIndex());
			this.dispose();
		}
		if(e.getSource()==cancelButton)
			this.dispose();
	}

}
