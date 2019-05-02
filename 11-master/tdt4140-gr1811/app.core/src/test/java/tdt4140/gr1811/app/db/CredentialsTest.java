package tdt4140.gr1811.app.db;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.*;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

@RunWith(MockitoJUnitRunner.class)
public class CredentialsTest {

	private static Credentials prevCreds = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		prevCreds = CredentialsFactory.saveCredentials(null);
	}

	@AfterClass
	public static void restoreCreds() {
		CredentialsFactory.saveCredentials(prevCreds);
	}

	@Test
	public void testSaveCredentials() {
		Credentials myCreds = new Credentials("a", 1234, "dbn", "us", "psw");
		CredentialsFactory.saveCredentials(myCreds);
		assertEquals(myCreds, CredentialsFactory.get());
	}

	@Test
	public void testListeners() {
		BooleanProperty sawChange = new SimpleBooleanProperty(false);
		CredentialsListener myListener = new CredentialsListener() {
			@Override
			public void credentialsChanged(Credentials oldCredentials, Credentials newCredentials) {
				sawChange.setValue(true);
			}
		};

		// register listener
		CredentialsFactory.addListener(myListener);
		// change value
		CredentialsFactory.saveCredentials(new Credentials("n", null, "d", "s", "p"));

		assertTrue(sawChange.get());
	}

	@Test
	public void testLocalFileSave() throws IOException {
		Credentials fromFile = null;
		try {
			// first, backup file if possible
			if (CredentialsFactory.fileExists()) {
				CredentialsFactory.saveCredentials(null);
				fromFile = CredentialsFactory.get();
			}

			// Create new credentials
			Credentials a = new Credentials("a", 23552, "dbname", "usr", "pw");
			// save it
			CredentialsFactory.saveCredentialsLocalFile(a);
			// load it
			CredentialsFactory.saveCredentials(null);
			Credentials b = CredentialsFactory.get();

			// should be the same
			assertEquals(a.getServerName(), b.getServerName());
			assertEquals(a.getDatabaseName(), b.getDatabaseName());
			assertEquals(a.getPort(), b.getPort());
			assertEquals(a.getUsername(), b.getUsername());
			assertEquals(a.getPassword(), b.getPassword());
		} finally {
			// restore old file
			CredentialsFactory.deleteFromDisk();
			if (fromFile != null) {
				CredentialsFactory.saveCredentialsLocalFile(fromFile);
			}
		}
	}

	@Test
	public void testReadyness() {
		boolean old = CredentialsFactory.setReady(true);
		try {
			assertTrue(CredentialsFactory.isReady());
			CredentialsFactory.setReady(false);
			assertFalse(CredentialsFactory.isReady());
		} finally {
			CredentialsFactory.setReady(old);
		}
	}

	@Test
	public void credentialsSetterShouldReturnOldFile() {
		File f = new File("test.txt");
		File oldFile = CredentialsFactory.setCredentialsFile(f);
		File orig = CredentialsFactory.setCredentialsFile(oldFile);
		assertEquals(f, orig);
	}

	@Test
	public void testInstancing() {
		CredentialsFactory cf = new CredentialsFactory();
		assertNotNull(cf);
	}

	@Test(expected = IOException.class)
	public void failureToCreateFolderShouldThrow() throws Exception {
		File file = mock(File.class);
		File parent = mock(File.class);
		when(file.getParentFile()).thenReturn(parent);
		when(parent.exists()).thenReturn(false);
		when(parent.mkdirs()).thenReturn(false); // simulate a failure to create folder

		File oldFile = CredentialsFactory.setCredentialsFile(file);
		try {
			CredentialsFactory.saveCredentialsLocalFile(mock(Credentials.class));
		} finally {
			CredentialsFactory.setCredentialsFile(oldFile);
		}
	}

	@Test
	public void nonexistantCredentialsFileShouldReturnNull() {
		File mock = mock(File.class);
		when(mock.exists()).thenReturn(false);
		Credentials oldCreds = CredentialsFactory.saveCredentials(null);
		File oldFile = CredentialsFactory.setCredentialsFile(mock);
		try {
			Credentials shouldBeNull = CredentialsFactory.get();
			assertNull(shouldBeNull);
		} finally {
			CredentialsFactory.saveCredentials(oldCreds);
			CredentialsFactory.setCredentialsFile(oldFile);
		}
	}

	@Test(expected=RuntimeException.class)
	public void noFileFoundShouldThrowRuntimeException() {
		File spy = spy(new File("doesnotexist.txt"));
		when(spy.exists()).thenReturn(true);
		Credentials oldCreds = CredentialsFactory.saveCredentials(null);
		File oldFile = CredentialsFactory.setCredentialsFile(spy);
		try {
			CredentialsFactory.get();
		} finally {
			CredentialsFactory.saveCredentials(oldCreds);
			CredentialsFactory.setCredentialsFile(oldFile);
		}
	}

	@Test
	public void testFileExistsIsDirectory() {
		File mock = mock(File.class);
		when(mock.exists()).thenReturn(true);
		when(mock.isDirectory()).thenReturn(true);
		File oldFile = CredentialsFactory.setCredentialsFile(mock);
		try {
			assertFalse(CredentialsFactory.fileExists());
		} finally {
			CredentialsFactory.setCredentialsFile(oldFile);
		}
	}

	@Test
	public void testFileExistsIsNotDirectory() {
		File mock = mock(File.class);
		when(mock.exists()).thenReturn(true);
		when(mock.isDirectory()).thenReturn(false);
		File oldFile = CredentialsFactory.setCredentialsFile(mock);
		try {
			assertTrue(CredentialsFactory.fileExists());
		} finally {
			CredentialsFactory.setCredentialsFile(oldFile);
		}
	}
}
