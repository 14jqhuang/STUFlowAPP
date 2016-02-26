package all;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import lin.readwrite.TestDecode;
import other.bean.TestAccount;
import other.bean.TestWebStatue;
import other.tool.TestWriteAccount;

@RunWith(Suite.class)
@SuiteClasses(value= {
		TestDecode.class,
		TestWebStatue.class,
		TestWriteAccount.class,
		TestAccount.class
		})
public class TestAll {


}
