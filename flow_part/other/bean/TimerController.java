package other.bean;

import java.util.ArrayList;
import java.util.List;

import interfaces.timer.UseTimer;

public class TimerController {
	
   private List<UseTimer> timerList; 
   
   public TimerController() {
	timerList =new ArrayList<>();
}
   
   public void addTimer(UseTimer timer) {
	   timerList.add(timer);
   }
   
	public void startAllTimer() {
		
		for(UseTimer timer:timerList)
		{
			timer.startTimer();
		}
	}
	
	public void setDelay(int delay)
	{
		for(UseTimer timer:timerList)
		{
			timer.setDelay(delay);
		}
	}
	
	public void restartAll()
	{
		for(UseTimer timer:timerList)
		{
			timer.stopTimer();;
		}
	}
}
