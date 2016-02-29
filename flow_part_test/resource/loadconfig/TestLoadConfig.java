package resource.loadconfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;


public class TestLoadConfig {

	private LoadConfig config;
	@Before
	public void setUp() throws Exception {
		config = new LoadConfig();
	}

	@Test
	public void testReadPropertiesWell() {
		assertNotNull(config.isAutoLogin());
		assertNotNull(config.isAutoSelect());
		config.setDefaultLoginAccount("14sxlin");
		assertEquals("14sxlin", config.getDefaultLoginAccount());
		config.setLoginAccount("xiaoming");
		assertEquals("xiaoming", config.getLoginAccount());
		assertNotNull(Integer.
				parseInt(config.readProperty("interval")));
	}

}
