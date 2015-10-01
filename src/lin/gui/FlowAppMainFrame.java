package lin.gui;

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

import lin.component.ButtonAreaPanel;
import lin.component.FlowDisplayPanel;
import lin.function.TimerController;
import lin.readwrite.ConfigAutoLogin;
import lin.readwrite.ConfigAutoSelect;
import lin.readwrite.ReadStatus;

@SuppressWarnings("serial")
public class FlowAppMainFrame extends JFrame implements ActionListener, ItemListener, WindowListener{
	private ButtonAreaPanel buttonPanel;
	private FlowDisplayPanel displayPanel;
	private VerySimpleDialog verySimpleDialog;
	
	private JSplitPane split;
	private JMenuBar menubar;
	private JMenu menu[];
	private JMenuItem menuItem[];
	public boolean infront=true;
	
	public String[] strMenu= {"账号管理","功能"};
	public String[] strMenuItem={"修改","删除","设置自登账号","清空"};
	public String[] strCheckboxItem={"保持最前","精简面板","超极面板"};
	public static TimerController controller;
	public static final int LOGNO=2;//设置了自动登录但是没有设置账号
	public static final int SET=1;//已经设置了自登账号
	public static final int LAST=-1;//有上次自动记录的能登录的账号
	public static final int INIT=0;//文件里面什么都没有	
	public static JCheckBoxMenuItem chekboxItem[];
	public static SimplifyDialog simplifyDialog;
	public static int  autologin;
	public static boolean autoSelect;
	public static boolean inside=false;//内部的组件
	public FlowAppMainFrame() {
		controller=new TimerController();
		//GUI界面
		this.setTitle("流量");
		this.setBounds(400, 200, 200, 330);
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
		
		//流量展示区域
		displayPanel=new FlowDisplayPanel(true);
		split=new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split.add(displayPanel);
		split.setOneTouchExpandable(true);
		//下面的按钮区域
		buttonPanel=new ButtonAreaPanel();
		split.add(buttonPanel);	
		this.add(split);
		

		//设置2个状态
		this.getAutoLogin();
		this.getAutoSelect();
		
		this.setAlwaysOnTop(true);
		this.addWindowListener(this);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
//System.out.println("Class= "+this.getClass().getResource("").getPath());
//System.out.println("ClassLoad="+this.getClass().getClassLoader().getResource("").getPath());
//System.out.println("SystemLoad="+ClassLoader.getSystemResource("").getPath());
/*
 * 导出的jar包输出结果
 * Class= lin/gui/
	ClassLoad=
	SystemLoad=/C:/Users/think/Desktop/
 */
	}
	
	public static void main(String[] args) {
		new FlowAppMainFrame();
	}

//	public void windowGainedFocus(WindowEvent e) {
//	}
//
//	public void windowLostFocus(WindowEvent e) {
////JOptionPane.showMessageDialog(null, "lostFoucs");
//		if(inside)
//		{	this.setAlwaysOnTop(false);
////JOptionPane.showMessageDialog(this, "inside check!");
//		}
//		else this.setAlwaysOnTop(true);
//		this.setVisible(true);
//		inside=false;
//	}
	
	//获取自动登录的状态
	public  void getAutoLogin()
	{
		int temp = 0;
		try {
			temp=new ConfigAutoLogin().readModel();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		autologin=temp;
		if(temp==1||temp==2)
		{	buttonPanel.autoLoginChBox.setSelected(true);
			ButtonAreaPanel.autoLogin=true;
		}	
//System.out.println("AUTOlOGIN="+autologin);
	}

	//获取自动切换状态
	public void getAutoSelect()
	{
		autoSelect=new ConfigAutoSelect().readModel();
		if(autoSelect)
			buttonPanel.autoSelectChBox.setSelected(true);
		else buttonPanel.autoSelectChBox.setSelected(false);

//System.out.println("autoSelect="+autoSelect);
	}

	public void actionPerformed(ActionEvent e) {	
		
		//设置自动登录的账号
		if(e.getActionCommand().equals("设置自登账号")) {
			if (ButtonAreaPanel.readAccount.accountList.size()!=0) {
				try {
					new SetDefaultLoginAccount(this,ButtonAreaPanel.readAccount.accountArrary, new ConfigAutoLogin());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
			}else JOptionPane.showMessageDialog(this, "请先添加账号");
		}
		
		//修改选项
		if(e.getActionCommand().equals("修改"))
		{	
			if (ButtonAreaPanel.readAccount.accountList.size()!=0) {
				new UpdateAccountDialog(this);
				buttonPanel.accountSelectCombo.removeAllItems();
				for (String c : ButtonAreaPanel.readAccount.accountList)
					buttonPanel.accountSelectCombo.addItem(c);
			}else JOptionPane.showMessageDialog(this, "没有可更改的账号");
		}
		
		//删除选项
		if(e.getActionCommand().equals("删除"))
		{
			if (ButtonAreaPanel.readAccount.accountList.size()!=0) {
				new DropAccountDialog(this);
				buttonPanel.accountSelectCombo.removeAllItems();
				for (String c : ButtonAreaPanel.readAccount.accountList)
					buttonPanel.accountSelectCombo.addItem(c);
			}else JOptionPane.showMessageDialog(this, "没有可删除的账号");
		}
		
		//清空
		if(e.getActionCommand().equals("清空"))
		{
			if (ButtonAreaPanel.readAccount.accountList.size()!=0) {
				if (JOptionPane.showConfirmDialog(this, "确定要清空?", "清空",
						JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION) {
					File f = new File(this.getClass().getClassLoader().getResource("").getPath() + "account.txt");
					if (f.exists())
						if (f.delete())
							try {
								f.createNewFile();
								buttonPanel.accountSelectCombo.removeAllItems();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(this, "清空账号失败");
							}
				} 
			}else JOptionPane.showMessageDialog(this, "没有可以删除的账号");
		}
	}

	public void itemStateChanged(ItemEvent e) {
		//始终在前功能的检查
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
		
		//显示精简面板
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
		
		//显示超精简面板
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
		// TODO Auto-generated method stub
		//没有设置自动登录的时候自动记录当前的账号作为
		//没有设置默认账号的时候登录的账号
		try {
			if(autologin<=0)
			{	new ConfigAutoLogin().write_1Name(ReadStatus.userName);}
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
