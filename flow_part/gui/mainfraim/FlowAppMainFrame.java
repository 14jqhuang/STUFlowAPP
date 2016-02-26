package gui.mainfraim;

import java.awt.Dimension;
import java.awt.Toolkit;
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

import gui.account_dialog.DropAccountDialog;
import gui.account_dialog.SetDefaultLoginAccount;
import gui.account_dialog.UpdateAccountDialog;
import gui.component.ButtonAreaPanel;
import gui.component.FlowDisplayPanel;
import gui.simplify_dialog.SimplifyDialog;
import gui.verysimplfy_dialog.VerySimpleDialog;
import other.bean.TimerController;
import other.bean.WebStatus;
import resource.loadconfig.LoadConfig;

@SuppressWarnings("serial")
public class FlowAppMainFrame extends JFrame implements ActionListener, ItemListener, WindowListener{
	
	private LoadConfig config;
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
	
	public String[] strMenu= {"账号管理","功能"};
	public String[] strMenuItem={"修改","删除","设置自登账号","清空"};
	public String[] strCheckboxItem={"保持最前","精简面板","超极面板"};
	public static JCheckBoxMenuItem chekboxItem[];
	
	public static boolean  autologin;
	public static boolean autoSelect;
	public static boolean inside=false;//内部的组件
	public boolean infront=true;
	
	
	public FlowAppMainFrame() {
		
		drawGui();

		//把读取网络连接的时间器交给时间器管理
		controller.setWebConnectionTimer(webStatus.startConnection());
		
		//设置2个状态
		this.setAutoLogin();
		this.setAutoSelect();
		this.setVisible(true);
	}
	
	private void drawGui()
	{
		//GUI界面
		this.setTitle("流量");
		Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(dim.width*2/5, dim.height/4,dim.width/6, dim.height*9/20);
		this.setResizable(true);
		
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
		
		this.setAlwaysOnTop(true);
		this.addWindowListener(this);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
				
	}

	public static void main(String[] args) {
		new FlowAppMainFrame();
	}


	
	//获取自动登录的状态
	public  void setAutoLogin()
	{
		autologin=config.isAutoLogin();
		buttonPanel.autoLoginChBox.setSelected(autologin);
	}

	//获取自动切换状态
	public void setAutoSelect()
	{
		autoSelect=config.isAutoSelect();
		buttonPanel.autoSelectChBox.setSelected(autoSelect);
	}

	public void actionPerformed(ActionEvent e) {	
		
		//设置自动登录的账号
		if(e.getActionCommand().equals("设置自登账号")) {
			if (ButtonAreaPanel.Account.accountList.size()!=0) {
				new SetDefaultLoginAccount(this,ButtonAreaPanel.Account.accountArrary,config); 
			}else JOptionPane.showMessageDialog(this, "请先添加账号");
		}
		
		//修改选项
		if(e.getActionCommand().equals("修改"))
		{	
			if (ButtonAreaPanel.Account.accountList.size()!=0) {
				new UpdateAccountDialog(this);
				buttonPanel.accountSelectCombo.removeAllItems();
				for (String c : ButtonAreaPanel.Account.accountList)
					buttonPanel.accountSelectCombo.addItem(c);
			}else JOptionPane.showMessageDialog(this, "没有可更改的账号");
		}
		
		//删除选项
		if(e.getActionCommand().equals("删除"))
		{
			if (ButtonAreaPanel.Account.accountList.size()!=0) {
				new DropAccountDialog(this);
				buttonPanel.accountSelectCombo.removeAllItems();
				for (String c : ButtonAreaPanel.Account.accountList)
					buttonPanel.accountSelectCombo.addItem(c);
			}else JOptionPane.showMessageDialog(this, "没有可删除的账号");
		}
		
		//清空
		if(e.getActionCommand().equals("清空"))
		{
			if (ButtonAreaPanel.Account.accountList.size()!=0) {
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
		//加入当前正在使用的账号
		config.setLoginAccount(FlowAppMainFrame.webStatus.userName);
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
