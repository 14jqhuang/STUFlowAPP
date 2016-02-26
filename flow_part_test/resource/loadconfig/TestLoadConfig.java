package resource.loadconfig;

import static org.junit.Assert.*;

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
		assertEquals("", config.getLoginAccount());
		assertEquals("", config.getDefaultLoginAccount());
	}

}
