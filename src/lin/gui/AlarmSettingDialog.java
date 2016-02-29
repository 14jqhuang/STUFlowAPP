package lin.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
import lin.function.PlayMusic;
//import lin.component.MusicChooser;
import lin.readwrite.GetMusicFile;
import lin.readwrite.ResourcePath;

@SuppressWarnings("serial")
public class AlarmSettingDialog extends JDialog implements ActionListener {
	public JSpinner alarmSpin;
	public JComboBox<String> alarmCombo;
	public JButton tryLisButton;
	public JButton sureButton;
	private JButton cancelButton;
	private SpinnerNumberModel model;
//	private MusicChooser musicChooser;
	private int maxFlow=800;//记录最大的流量,默认800M
	public File musicFile;
	private GetMusicFile musicList;
	private PlayMusic music;
	public static String musicPath;
	public static int alarmAmount;
	public AlarmSettingDialog(String maxFlow) throws UnsupportedEncodingException {
		// TODO Auto-generated constructor stub
		//界面设计
		this.setTitle("设置提醒");
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setBounds(450, 250, 200, 200);
		this.setAlwaysOnTop(true);
		this.setLayout(new GridLayout(4, 1));
		this.maxFlow=Integer.parseInt(maxFlow);
//		musicChooser=new MusicChooser();		
		model=new SpinnerNumberModel((this.maxFlow-100)/100*100, 0, this.maxFlow, 50);
		alarmSpin=new JSpinner(model);
		
		JPanel temppanel=new JPanel();
		temppanel.add(new JLabel("提醒额度:"));
		temppanel.add(alarmSpin);
		temppanel.add(new JLabel("M"));
		this.add(temppanel);
		
		this.add(new JLabel("提醒铃声:"));
		JPanel panel=new JPanel();
		musicList=new GetMusicFile(ResourcePath.JARPATH);
		if(musicList.strMusicList.length==0)
		{	JOptionPane.showMessageDialog(this, "当前文件夹没有(.wav)音乐文件");
			this.dispose();
		}	
		else
		{	alarmCombo=new JComboBox<String>(musicList.strMusicList);
			panel.add(alarmCombo);
			tryLisButton=new JButton("试听");
			panel.add(tryLisButton);
			tryLisButton.addActionListener(this);
//		alarmSpin.setMaximumSize(new Dimension(280, 50));
//		alarmSpin.setPreferredSize(new Dimension(50, 28));
		alarmCombo.setPreferredSize(new Dimension(120, 28));
			this.add(panel);
			JPanel panel1=new JPanel();
			sureButton=new JButton("确定");
			sureButton.addActionListener(this);
			cancelButton=new JButton("取消");
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
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("取消"))
			this.dispose();
		if(e.getActionCommand().equals("试听"))
		{
			String name=(String) alarmCombo.getSelectedItem();
			if(name!=null)
			{	try {
				music=new PlayMusic(musicList.hashMap.get(name),true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				music.play();
				music.showControlPanel(this, "请按停止关闭音乐");
			}
		}
		if(e.getActionCommand().equals("确定"))
		{
			musicPath=(String) alarmCombo.getSelectedItem();
//System.out.println(musicIndex);
			try{	alarmAmount=(Integer) alarmSpin.getValue();}
			catch(NumberFormatException ex) { JOptionPane.showMessageDialog(this, "输入错误");		};
			this.dispose();
			ButtonAreaPanel.alarmhasSet=true;
		}	
	}

}
