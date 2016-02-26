package resource.loadconfig;

import java.io.IOException;
import java.util.Properties;

public class LoadConfig {

	private Properties properties;
	public LoadConfig() throws IOException {
		properties = new Properties();
		properties.load(this.getClass().getResourceAsStream("/config.properties"));
	}
	
	public String readProperty(String key)
	{
		return properties.getProperty(key);
	}
	
	public void setProperty(String key,String value)
	{
		properties.setProperty(key, value);
	}
	
	public boolean isAutoLogin() {
		if(readProperty("autologin").equals("yes"))
			return true;
		else return false;
	}
	
	public boolean isAutoSelect() {
		if(readProperty("autoselect").equals("yes"))
			return true;
		else return false;
	}
	
	public String getDefaultLoginAccount() {
		return readProperty("defaultLoginAccount");
	}
	public void setDefaultLoginAccount(String username) {
		properties.setProperty("defaultLoginAccount", username);
	}
	public String getLoginAccount() {
		return readProperty("loginAccount");
	}
	
	public void setLoginAccount(String username) {
		properties.setProperty("loginAccount", username);
	}

	
}
