package other.tool;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import resource.webserver.ResourcePath;

public class TestFlowLogRequest {

	@Test
	public void testLogin() throws IOException {
		assertEquals(200, FlowLogRequest.login(
				ResourcePath.SERVERPATH,
"AuthenticateUser=14sxlin"
+ "&AuthenticatePassword=Lsx120&shit"));
	}
	
	@Test
	public void testLogout() throws Exception {
		assertEquals(200, FlowLogRequest.logout(
				ResourcePath.SERVERPATH));
	}

}
