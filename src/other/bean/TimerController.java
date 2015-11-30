package other.bean;

import javax.swing.Timer;

public class TimerController {
	
	private Timer buttonPanelTimer,
					 	 flowPanelTimer,
						 webConnectionTimer;

	public Timer getButtonPanelTimer() {
		return buttonPanelTimer;
	}

	public void setButtonPanelTimer(Timer buttonPanelTimer) {
		this.buttonPanelTimer = buttonPanelTimer;
	}

	public Timer getFlowPanelTimer() {
		return flowPanelTimer;
	}

	public void setFlowPanelTimer(Timer flowPanelTimer) {
		this.flowPanelTimer = flowPanelTimer;
	}

	public Timer getWebConnectionTimer() {
		return webConnectionTimer;
	}

	public void setWebConnectionTimer(Timer webConnectionTimer) {
		this.webConnectionTimer = webConnectionTimer;
	}
	
	
}
