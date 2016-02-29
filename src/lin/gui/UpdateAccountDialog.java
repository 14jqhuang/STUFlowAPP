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
 * ��������޸ĵ�ѡ���ʱ����ʾ�����
 */
public class UpdateAccountDialog extends JDialog implements ActionListener{
	public  JComboBox<String> accountSelectCombo;
	JButton sureButton;
	private JButton cancelButton;
	public UpdateAccountDialog(JFrame jframe)   {
		this.setLayout(new GridLayout(2, 1));
		this.setTitle("ѡ���˺�");	
		accountSelectCombo=new JComboBox<String>(ButtonAreaPanel.readAccount.accountArrary);
		this.add(accountSelectCombo);
		JPanel tempPanel=new JPanel();
		sureButton=new JButton("ȷ��");
		cancelButton=new JButton("ȡ��");
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
	//��ʾ�޸����ݵ���������µ�����
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