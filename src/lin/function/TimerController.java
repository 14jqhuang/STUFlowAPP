package lin.function;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Timer;

public class TimerController {
	private ArrayList<Timer> member;
	
	public TimerController() {
		// TODO Auto-generated constructor stub
		member=new ArrayList<>();
	}
	
	public void addTimer(Timer member)
	{
		this.member.add(member);
	}
	
	public void stopAllController()
	{
		Iterator<Timer> it=member.iterator();
		Timer temp;
		while(it.hasNext())
			{
			     temp=it.next();
		    	if(temp.isRunning())
				  temp.stop();
			}
	}
	public void continueAll()
	{
		Iterator<Timer> it=member.iterator();
		Timer temp;
		while(it.hasNext())
		{
		   temp=it.next();
		   temp.start();
		   if(!temp.isRunning())
			 temp.restart();
		}
	}
}
