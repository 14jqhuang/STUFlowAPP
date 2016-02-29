package other.bean;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import resource.webserver.ResourcePath;

public class TestWebStatue {

	private StringBuilder param1 = new StringBuilder("<label>ÄÚÈÝ</label>");
	private WebStatus webstatus;
	@Before
	public void setUp() throws Exception {
		webstatus = new WebStatus();
	}

	@Test
	public void testCutDataLabel() {
		String result = webstatus.cutDataInLabel(param1,"<label>", "</label>");
		assertEquals("ÄÚÈÝ", result);
	}
	
	@Test
	public void testflowStringtoNumber() {
		int result1 = webstatus.flowStringToNumber("233,334,555");
		int result2 = webstatus.flowStringToNumber("23,231,222");
		int result3= webstatus.flowStringToNumber("23,031,022");
		assertEquals(233334555, result1);
		assertEquals(23231222, result2);
		assertEquals(23031022, result3);
	}
	
	@Test
	public void testOpenWebsiteWork() throws IOException {
		StringBuilder sb = 
				webstatus.openWebsite(ResourcePath.DATAPATH);
		assertNotEquals("", sb.toString());
	}

}
