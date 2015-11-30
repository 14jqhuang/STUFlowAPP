package gui.mainfraim;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;

import function.config_auto_file.ConfigAutoLogin;
import function.config_auto_file.ConfigAutoSelect;
import gui.account_dialog.DropAccountDialog;
import gui.account_dialog.SetDefaultLoginAccount;
import gui.account_dialog.UpdateAccountDialog;
import gui.simplify_dialog.SimplifyDialog;
import gui.verysimplfy_dialog.VerySimpleDialog;
import lin.component.ButtonAreaPanel;
import lin.component.FlowDisplayPanel;
import other.bean.TimerController;
import other.bean.WebStatus;

@SuppressWarnings("serial")
public class FlowAppMainFrame extends JFrame implements ActionListener, ItemListener, WindowListener{
	
	private ButtonAreaPanel buttonPanel;
	private FlowDisplayPanel displayPanel;
	private VerySimpleDialog verySimpleDialog;
	
	private JSplitPane split;
	private JMenuBar menubar;
	private JMenu menu[];
	private JMenuItem menuItem[];
	public static SimplifyDialog simplifyDialog;
	
	public static TimerController controller=new TimerController();
	public static WebStatus webStatus=new WebStatus();
	
	public String[] strMenu= {"�˺Ź���","����"};
	public String[] strMenuItem={"�޸�","ɾ��","�����Ե��˺�","���"};
	public String[] strCheckboxItem={"������ǰ","�������","�������"};
	public static JCheckBoxMenuItem chekboxItem[];
	
	public static final int LOGNO=2;//�������Զ���¼����û�������˺�
	public static final int SET=1;//�Ѿ��������Ե��˺�
	public static final int LAST=-1;//���ϴ��Զ���¼���ܵ�¼���˺�
	public static final int INIT=0;//�ļ�����ʲô��û��	
	
	public static int  autologin;
	public static boolean autoSelect;
	public static boolean inside=false;//�ڲ������
	public boolean infront=true;
	
	
	public FlowAppMainFrame() {
		
		drawGui();

		//�Ѷ�ȡ�������ӵ�ʱ��������ʱ��������
		controller.setWebConnectionTimer(webStatus.startConnection());
		
		//����2��״̬
		this.setAutoLogin();
		this.setAutoSelect();
		this.setVisible(true);
	}
	
	
	private void drawGui()
	{
		//GUI����
		this.setTitle("����");
		this.setBounds(400, 200, 230, 330);
		this.setResizable(false);
		
		menubar=new JMenuBar();
		this.setJMenuBar(menubar);
		menu=new JMenu[strMenu.length];
		for(int i=0;i<strMenu.length;i++)
		{	menu[i]=new JMenu(strMenu[i]);
			menubar.add(menu[i]);
		}
		menuItem=new JMenuItem[strMenuItem.length];
		for(int i=0;i<strMenuItem.length;i++)
		{
			menuItem[i]=new JMenuItem(strMenuItem[i]);
			menuItem[i].addActionListener(this);
			if(i==3)
				menu[0].addSeparator();
			menu[0].add(menuItem[i]);
		}
		chekboxItem=new JCheckBoxMenuItem[strCheckboxItem.length];
		for(int i=0;i<strCheckboxItem.length;i++)
		{
			chekboxItem[i]=new JCheckBoxMenuItem(strCheckboxItem[i]);
			chekboxItem[i].addItemListener(this);
			menu[1].add(chekboxItem[i]);
		}
		chekboxItem[0].setSelected(true);
		
		//����չʾ����
		displayPanel=new FlowDisplayPanel(true);
		split=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split.add(displayPanel);
		split.setOneTouchExpandable(true);
		//����İ�ť����
		buttonPanel=new ButtonAreaPanel();
		split.add(buttonPanel);	
		this.add(split);		
		
		this.setAlwaysOnTop(true);
		this.addWindowListener(this);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
				
	}

	public static void main(String[] args) {
		new FlowAppMainFrame();
	}


	
	//��ȡ�Զ���¼��״̬
	public  void setAutoLogin()
	{
		int temp = 0;
		try {
			temp=new ConfigAutoLogin().readModel();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		autologin=temp;
		if(temp==1||temp==2)
		{	buttonPanel.autoLoginChBox.setSelected(true);
			ButtonAreaPanel.autoLogin=true;
		}	
	}

	//��ȡ�Զ��л�״̬
	public void setAutoSelect()
	{
		autoSelect=new ConfigAutoSelect().readModel();
		if(autoSelect)
			buttonPanel.autoSelectChBox.setSelected(true);
		else buttonPanel.autoSelectChBox.setSelected(false);

//System.out.println("autoSelect="+autoSelect);
	}

	public void actionPerformed(ActionEvent e) {	
		
		//�����Զ���¼���˺�
		if(e.getActionCommand().equals("�����Ե��˺�")) {
			if (ButtonAreaPanel.Account.accountList.size()!=0) {
				try {
					new SetDefaultLoginAccount(this,ButtonAreaPanel.Account.accountArrary, new ConfigAutoLogin());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}else JOptionPane.showMessageDialog(this, "��������˺�");
		}
		
		//�޸�ѡ��
		if(e.getActionCommand().equals("�޸�"))
		{	
			if (ButtonAreaPanel.Account.accountList.size()!=0) {
				new UpdateAccountDialog(this);
				buttonPanel.accountSelectCombo.removeAllItems();
				for (String c : ButtonAreaPanel.Account.accountList)
					buttonPanel.accountSelectCombo.addItem(c);
			}else JOptionPane.showMessageDialog(this, "û�пɸ��ĵ��˺�");
		}
		
		//ɾ��ѡ��
		if(e.getActionCommand().equals("ɾ��"))
		{
			if (ButtonAreaPanel.Account.accountList.size()!=0) {
				new DropAccountDialog(this);
				buttonPanel.accountSelectCombo.removeAllItems();
				for (String c : ButtonAreaPanel.Account.accountList)
					buttonPanel.accountSelectCombo.addItem(c);
			}else JOptionPane.showMessageDialog(this, "û�п�ɾ�����˺�");
		}
		
		//���
		if(e.getActionCommand().equals("���"))
		{
			if (ButtonAreaPanel.Account.accountList.size()!=0) {
				if (JOptionPane.showConfirmDialog(this, "ȷ��Ҫ���?", "���",
						JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
					File f = new File(this.getClass().getClassLoader().getResource("").getPath() + "account.txt");
					if (f.exists())
						if (f.delete())
							try {
								f.createNewFile();
								buttonPanel.accountSelectCombo.removeAllItems();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(this, "����˺�ʧ��");
							}
				} 
			}else JOptionPane.showMessageDialog(this, "û�п���ɾ�����˺�");
		}
	}

	public void itemStateChanged(ItemEvent e) {
		//ʼ����ǰ���ܵļ��
		if(e.getSource()==chekboxItem[0]&&chekboxItem[0].isSelected())
		{//	this.addWindowFocusListener(this);
			this.setAlwaysOnTop(true);
			this.setVisible(true);
		}else {			
//			this.removeWindowFocusListener(this);
			this.setAlwaysOnTop(false);
			this.setVisible(true);
		}
		
		if(chekboxItem[0].isSelected())
			infront=true;
		else infront=false;
		
		//��ʾ�������
		if(chekboxItem[1].isSelected()&&simplifyDialog==null)
		{		simplifyDialog=new SimplifyDialog(true,displayPanel.timer);				
System.out.println(infront);
				simplifyDialog.updateOnTop(infront);
				this.setVisible(false);
		}
		if(!chekboxItem[1].isSelected()&&simplifyDialog!=null)
			{	simplifyDialog.dispose();
				simplifyDialog=null;
				this.setAlwaysOnTop(true);
			}
		
		//��ʾ���������
		if(chekboxItem[2].isSelected())
		{	verySimpleDialog=new VerySimpleDialog(displayPanel.timer);
			verySimpleDialog.updateOnTop(infront);
			this.setVisible(false);
		}
	}

	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
	}

	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosing(WindowEvent arg0) {
		//û�������Զ���¼��ʱ���Զ���¼��ǰ���˺���Ϊ
		//û������Ĭ���˺ŵ�ʱ���¼���˺�
		try {
			if(autologin<=0)
			{	new ConfigAutoLogin().write_1Name(webStatus.userName);}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub		
	}


	
}
