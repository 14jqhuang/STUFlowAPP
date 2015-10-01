package lin.function;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Timer;

public class TimerController {
	private ArrayList<Timer> member;
	
	public TimerController() {
		// TODO Auto-generated constructor stub
	}
	
	public void addTimer(Timer member)
	{
		this.member.add(member);
	}
	
	public void stopAllController()
	{
		Iterator<Timer> it=member.iterator();
		while(it.hasNext())
			it.next().stop();
	}
}
