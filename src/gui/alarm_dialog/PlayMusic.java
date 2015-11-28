package gui.alarm_dialog;

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import lin.component.ButtonAreaPanel;
import sun.audio.*;
public class PlayMusic implements ActionListener, WindowListener {

	public AudioStream as;	
	public InputStream in;
	private JDialog controlPanel;
	private boolean asCompenont;
	public PlayMusic(File f,boolean asCompenont) throws IOException {
		// TODO Auto-generated constructor stub
		this.asCompenont=asCompenont;
		in=new FileInputStream(f);
		as=new AudioStream(in);
	}
	public PlayMusic(InputStream musicStream) throws IOException {
		// TODO Auto-generated constructor stub
		as=new AudioStream(musicStream);
	}
	public PlayMusic(String musicPath,boolean asCompenont)
	{
		this.asCompenont=asCompenont;
		try {
			InputStream in=this.getClass().getClassLoader().getResourceAsStream(musicPath);
			as=new AudioStream(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void play()
	{
		AudioPlayer.player.start(as);
	}
	public void close() throws IOException
	{
		AudioPlayer.player.stop(as);
		as.close();
	}
	public void showControlPanel(JDialog dialog,String message)
	{
		controlPanel=new JDialog();
		controlPanel.setAlwaysOnTop(true);
		controlPanel.setModalityType(ModalityType.TOOLKIT_MODAL);
		controlPanel.setLocationRelativeTo(dialog);
		controlPanel.setLayout(new GridLayout(2, 1));
		JLabel temp=new JLabel(message);
		temp.setForeground(Color.red);
		controlPanel.add(temp);
		JButton button=new JButton("ֹͣ");
		button.addActionListener(this);
		controlPanel.add(button);
		controlPanel.addWindowListener(this);
		controlPanel.setSize(40, 90);
		
		controlPanel.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("ֹͣ"))
		{	try {				
				if(asCompenont)
					ButtonAreaPanel.alarmhasSet=false;
				this.close();
				controlPanel.dispose();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		try {
			if(asCompenont)
				ButtonAreaPanel.alarmhasSet=false;
			this.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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

}
