package other.tool;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestWriteAccount {

	private String param1 ="AuthenticateUser=xiaoming&AuthenticatePassword=1234&shit";
	private String param2 ="@xiaoming&1234";
	private final String result=
			"AuthenticateUser=xiaoming&AuthenticatePassword=1234&shit";

	@Test
	public void testSetupParam() {
		String result1 = WriteAccount.setupParam("xiaoming", "1234");
		String result2 = WriteAccount.processParam(param1);
		String result3 = WriteAccount.processParam(param2);
		assertEquals(result, result1);
		assertEquals(result, result2);
		assertEquals(result, result3);
	}

}
