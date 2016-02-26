package other.bean;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class TestAccount {

	private Account account;
	private ArrayList<String> resultlist = new ArrayList<>();
	@Before
	public void setUp() throws IOException {
		
		account = new Account();
		resultlist.add("user1");
		resultlist.add("newname");
		resultlist.add("user3");
		
		ArrayList<String> usernames = new ArrayList<>();
		usernames.add("user1");
		usernames.add("user2");
		usernames.add("user3");

		HashMap<String, String> map = new HashMap<>();
		map.put("user1", "AuthenticateUser=user1&AuthenticatePassword=p1&shit");
		map.put("user2", "AuthenticateUser=user2&AuthenticatePassword=p2&shit");
		map.put("user3", "AuthenticateUser=user3&AuthenticatePassword=p3&shit");
		
		account.accountList = usernames;
		account.hashMap = map;
	}
	
	@Test
	public void testUpdateAccount() {
		//1实际上是第二项
		account.Update(1, "newname", "1234");
		assertArrayEquals(resultlist.toArray(), account.accountList.toArray());
		assertEquals("AuthenticateUser=newname&AuthenticatePassword=1234&shit"
				,account.hashMap.get("newname"));
	}

	@Test
	public void testDropAccount() {
		account.drop(1);
		assertEquals(2, account.accountList.size());
		assertThat(account.hashMap,not(hasKey("user2")));
	}
}
