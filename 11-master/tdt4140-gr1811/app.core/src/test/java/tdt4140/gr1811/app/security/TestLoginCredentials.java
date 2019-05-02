package tdt4140.gr1811.app.security;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class TestLoginCredentials {
	
	private static LoginCredentials prevCreds = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		prevCreds = LoginCredentialsFactory.saveLoginCredentials(null);
	}

	@AfterClass
	public static void restoreCreds() {
		LoginCredentialsFactory.saveLoginCredentials(prevCreds);
	}
	
	@Test
	public void testLoginCredClass() {
		LoginCredentials logincred = new LoginCredentials("", "");
		logincred.getUsername();
		logincred.getPassword();
	}

	@Test
	public void testSaveCredentials() {
		LoginCredentials myCreds = new LoginCredentials("username", "password");
		LoginCredentialsFactory.saveLoginCredentials(myCreds);
		assertEquals(myCreds, LoginCredentialsFactory.getLoginCredentials());
	}
	
	@Test
	public void testLocalFileSave() throws IOException {
		LoginCredentials fromFile = null;
		try {
			// first, backup file if possible
			if (LoginCredentialsFactory.fileExists()) {
				LoginCredentialsFactory.saveLoginCredentials(null);
				fromFile = LoginCredentialsFactory.getLoginCredentials();
			}

			// Create new credentials
			LoginCredentials a = new LoginCredentials("username", "password");
			// save it
			LoginCredentialsFactory.saveLoginCredentialsLocalFile(a);
			// load it
			LoginCredentialsFactory.saveLoginCredentials(null);
			LoginCredentials b = LoginCredentialsFactory.getLoginCredentials();

			// should be the same
			assertEquals(a.getUsername(), b.getUsername());
			assertEquals(a.getPassword(), b.getPassword());
		} finally {
			// restore old file
			LoginCredentialsFactory.deleteFromDisk();
			if (fromFile != null) {
				LoginCredentialsFactory.saveLoginCredentialsLocalFile(fromFile);
			}
		}
	}

}
