package gui.simplify_dialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

import gui.mainfraim.FlowAppMainFrame;

@SuppressWarnings("serial")
public class SimplifyDialog extends JDialog implements WindowListener,ActionListener {
	public JTextField usedText,remainText;//������ʾ��������ֵ
	public JLabel statusLabel;
	private GridBagLayout gridbag;
	private GridBagConstraints constraints;
	private JPanel jPanel;
	public SimplifyDialog(boolean asComponent,Timer timer) {
		
			drawGui(asComponent);
	}
	
	private void drawGui(boolean asComponent)
	{
		jPanel=new JPanel();
    	jPanel.setBorder(new TitledBorder("����"));
    	gridbag=new GridBagLayout();
    	jPanel.setLayout(gridbag);
    	
    	constraints=new GridBagConstraints(0, 1, 1, 1, 1, 1,GridBagConstraints.CENTER
    				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	jPanel.add(new JLabel("��ʹ��:"),constraints);    	
    	constraints=new GridBagConstraints(1, 1, 1, 1, 3, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	usedText=new JTextField();
    	usedText.setEditable(false);
    	jPanel.add(usedText, constraints);
    	constraints=new GridBagConstraints(2, 1, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	jPanel.add(new JLabel("M"), constraints);
    	
    	constraints=new GridBagConstraints(0, 2, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	jPanel.add(new JLabel("ʣ��:"), constraints);    	
    	constraints=new GridBagConstraints(1, 2, 1, 1, 3, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	remainText=new JTextField();
    	remainText.setForeground(Color.red);
    	remainText.setEditable(false);
    	jPanel.add(remainText, constraints);    	
    	constraints=new GridBagConstraints(2, 2, 1, 1, 1, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	jPanel.add(new JLabel("M"), constraints);
    	
    	constraints=new GridBagConstraints(0, 3, 2, 1, GridBagConstraints.REMAINDER
    			, 1,GridBagConstraints.CENTER
				, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
    	statusLabel=new JLabel("δ��¼",JLabel.CENTER);
    	statusLabel.setForeground(Color.red);
    	jPanel.add(statusLabel, constraints);
    	
    	if(FlowAppMainFrame.webStatus.loginStatus==1)
    		this.setTitle(FlowAppMainFrame.webStatus.userName);
    	this.setSize(120, 120);
    	Dimension d=getToolkit().getScreenSize();
    	this.setLocation(d.width-150,0);
    	this.add(jPanel);
    	
    	this.setLoginStatus(FlowAppMainFrame.webStatus.loginStatus, FlowAppMainFrame.webStatus.useOut);
    	
    	if(asComponent)
    		this.addWindowListener(this);
    	else this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        	
    	this.setVisible(true);
	}
	
	private void setLoginStatus(int loginstatus,boolean useOut)
    {
		if(!FlowAppMainFrame.webStatus.WebLost)
    	{	if(loginstatus==1)
    		{	statusLabel.setForeground(Color.green);
    		statusLabel.setText("�ѵ�¼");
    		}
    		else if(loginstatus==0)
    		{
    			statusLabel.setForeground(Color.red);
    			statusLabel.setText("δ��¼");
    		}else {
    			statusLabel.setForeground(Color.blue);
    		statusLabel.setText("�û������������");
    	}
    	if(useOut)
    	{
    		statusLabel.setForeground(Color.blue);
    		statusLabel.setText("����������");
    	}}
		else {
			statusLabel.setForeground(Color.blue);
			statusLabel.setText("�Ѷ���");
		}	
    }

	public void setTexts(String name,String used,String remain,int status)
    {	
		if(!FlowAppMainFrame.webStatus.WebLost)
    	{	this.setTitle(name);
	    	this.usedText.setText(used);
	    	this.remainText.setText(remain);
	    	this.setLoginStatus(status,FlowAppMainFrame.webStatus.useOut);
    	}else
    	{
    		this.setTitle("");
	    	this.usedText.setText("");
	    	this.remainText.setText("");
	    	this.setLoginStatus(status,FlowAppMainFrame.webStatus.useOut);
    	}
    }
	
	public static void main(String[] args) {
		new SimplifyDialog(false,null);
	}

	public void updateOnTop(boolean infront)
	{
		this.setAlwaysOnTop(infront);
		this.setVisible(true);
	}
	
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		if(FlowAppMainFrame.chekboxItem[1].isSelected())
			FlowAppMainFrame.chekboxItem[1].setSelected(false);
	}

	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowStateChanged(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
}