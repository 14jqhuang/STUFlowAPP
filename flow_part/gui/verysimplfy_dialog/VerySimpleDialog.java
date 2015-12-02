package gui.verysimplfy_dialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.Timer;

import gui.mainfraim.FlowAppMainFrame;

@SuppressWarnings("serial")
public class VerySimpleDialog extends JDialog implements ActionListener, WindowListener {
	private Timer timer;
	
	public VerySimpleDialog(Timer timer) {
		this.timer=timer;
		this.timer.addActionListener(this);
//		this.setResizable(false);
		Dimension d=getToolkit().getScreenSize();
    	this.setLocation(d.width-150,0);
    	this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    	this.addWindowListener(this);
    	this.setVisible(true);
	}
	
	private String getRateData(boolean weblost,String used,String total,boolean useOut)
	{
		if (!weblost) {
			if (!useOut)
				return "ÒÑÓÃ:" +
					(int) (FlowAppMainFrame.webStatus.flowStringToNumber(FlowAppMainFrame.webStatus.usedAmount)*100.0/ 
							FlowAppMainFrame.webStatus.flowStringToNumber(FlowAppMainFrame.webStatus.totalAmount) ) + "%";
			else
				return "ÒÑ±¬:" + "100%";
		}else
		{
			return "¶ÏÍø(¡Ño¡Ñ)";
		}
	}
	
	public void updateOnTop(boolean infront)
	{
		this.setAlwaysOnTop(infront);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.setTitle(getRateData(FlowAppMainFrame.webStatus.WebLost,FlowAppMainFrame.webStatus.usedAmount, 
				FlowAppMainFrame.webStatus.totalAmount, FlowAppMainFrame.webStatus.useOut));
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		FlowAppMainFrame.chekboxItem[2].setSelected(false);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
