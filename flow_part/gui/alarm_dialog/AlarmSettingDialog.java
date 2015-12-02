package gui.alarm_dialog;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
//import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import lin.component.ButtonAreaPanel;
import other.bean.Musics;
import resource.webserver.ResourcePath;

@SuppressWarnings("serial")
public class AlarmSettingDialog extends JDialog implements ActionListener {
	public JSpinner alarmSpin;
	public JComboBox<String> alarmCombo;
	public JButton tryLisButton;
	public JButton sureButton;
	private JButton cancelButton;
	private SpinnerNumberModel model;
	private int maxFlow=800;//��¼��������,Ĭ��800M
	public File musicFile;
	private Musics musicList;
	private PlayMusic music;
	
	public static String musicPath;
	public static int alarmAmount;
	
	public AlarmSettingDialog(String maxFlow) throws UnsupportedEncodingException {
		
		//�������
		this.setTitle("��������");
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setBounds(450, 250, 200, 200);
		this.setAlwaysOnTop(true);
		this.setLayout(new GridLayout(4, 1));
		this.maxFlow=Integer.parseInt(maxFlow);
//		musicChooser=new MusicChooser();		
		model=new SpinnerNumberModel((this.maxFlow-100)/100*100, 0, this.maxFlow, 50);
		alarmSpin=new JSpinner(model);
		
		JPanel temppanel=new JPanel();
		temppanel.add(new JLabel("���Ѷ��:"));
		temppanel.add(alarmSpin);
		temppanel.add(new JLabel("M"));
		this.add(temppanel);
		
		this.add(new JLabel("��������:"));
		JPanel panel=new JPanel();
		musicList=new Musics(URLDecoder.decode(ResourcePath.JARPATH, "utf-8"));
		if(musicList.strMusicList.length==0)
		{	JOptionPane.showMessageDialog(this, "��ǰ�ļ���û��(.wav)�����ļ�");
			this.dispose();
		}	
		else
		{	alarmCombo=new JComboBox<String>(musicList.strMusicList);
			panel.add(alarmCombo);
			tryLisButton=new JButton("����");
			panel.add(tryLisButton);
			tryLisButton.addActionListener(this);
			alarmCombo.setPreferredSize(new Dimension(120, 28));
			this.add(panel);
			JPanel panel1=new JPanel();
			sureButton=new JButton("ȷ��");
			sureButton.addActionListener(this);
			cancelButton=new JButton("ȡ��");
			cancelButton.addActionListener(this);
			panel1.add(sureButton);
			panel1.add(cancelButton);
			this.add(panel1);
			this.setResizable(false);
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			this.setVisible(true);
		}
	}
	
	public void setMaxFlow(int maxFlow)
	{
		this.maxFlow=maxFlow;
		model.setMaximum(this.maxFlow);
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("ȡ��"))
			this.dispose();
		if(e.getActionCommand().equals("����"))
		{
			String name=(String) alarmCombo.getSelectedItem();
			if(name!=null)
			{	try {
				music=new PlayMusic(musicList.hashMap.get(name),true);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
				music.play();
				music.showControlPanel(this, "�밴ֹͣ�ر�����");
			}
		}
		if(e.getActionCommand().equals("ȷ��"))
		{
			musicPath=(String) alarmCombo.getSelectedItem();
			try{	alarmAmount=(Integer) alarmSpin.getValue();}
			catch(NumberFormatException ex) { JOptionPane.showMessageDialog(this, "�������");		};
			this.dispose();
			ButtonAreaPanel.alarmhasSet=true;
		}	
	}

}