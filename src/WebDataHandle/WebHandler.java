package WebDataHandle;

public interface WebHandler extends Runnable{
	
	void startService();
	
	void stopService();
	
	int getLoginStatus();
	
	boolean getUseOut();
	boolean getWebLost();
	
	String getRemainFlow();
	
	String getTotalFlow();
	
	String getUsedFlow();	
	
	String getUserName();
	
}
