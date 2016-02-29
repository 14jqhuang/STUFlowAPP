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
<<<<<<< HEAD:flow_part/gui/verysimplfy_dialog/VerySimpleDialog.java
			if (!useOut)
				return "����:" +
					(int) (FlowAppMainFrame.webStatus.flowStringToNumber(FlowAppMainFrame.webStatus.usedAmount)*100.0/ 
							FlowAppMainFrame.webStatus.flowStringToNumber(FlowAppMainFrame.webStatus.totalAmount) ) + "%";
=======
		    int result = 0;
			String total_temp=ReadStatus.subNum(total);
			String used_temp=ReadStatus.subNum(used);
System.out.println("total: "+total_temp);
System.out.println("used: "+used_temp);
			int index=total_temp.indexOf(",");
			if (!useOut)
			{	if(index==-1)
				{  try {
					result=(int) (Double.parseDouble(used_temp) / Double.parseDouble(total_temp)*100);
System.out.println(result);
					} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					return "δ��¼";
					}
					return "����:" + result+ "%";
				}
				else {
					double to=Integer.parseInt(total_temp.substring(0, index))*1000+
							Integer.parseInt(total_temp.substring(index+1));
					index=used_temp.indexOf(",");
					double us;
					if (index!=-1) {
						us = Integer.parseInt(used_temp.substring(0, index)) * 1000
								+ Integer.parseInt(used_temp.substring(index + 1));
					}
					else us=Integer.parseInt(used_temp);
					try {
						result=(int)(us/to*100);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						return "�������";
					}
					return  "����:" + (int) 
							(us/to* 100) + "%";
				}
			}
>>>>>>> d94da75913dd6b8f495ae2229709a38c1fb0ba37:src/lin/gui/VerySimpleDialog.java
			else
				return "�ѱ�:" + "100%";
		}else
		{
			return "����(��o��)";
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
