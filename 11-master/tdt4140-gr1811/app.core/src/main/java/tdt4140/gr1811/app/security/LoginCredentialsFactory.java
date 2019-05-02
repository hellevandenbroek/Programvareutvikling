package tdt4140.gr1811.app.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class LoginCredentialsFactory {
	
	private static final String APP_FOLDER_NAME = ".diabytes";
	private static final String LOGIN_PROPERTIES_NAME = "login.properties";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_PASSWORD = "password";
	
	private static LoginCredentials logincredentials = null;
	private static File logincredFile = null;
	private static boolean ready = true;
	

	/**
	 * @return {@code null} if login credentials couldn't be fetched
	 */
	public static LoginCredentials getLoginCredentials() {
		if (logincredentials == null) {
			File logincredFile = getLoginCredentialsFile();
			if (!logincredFile.exists()) {
				return null;
			}
			Properties p = new Properties();
			try (InputStream is = new FileInputStream(logincredFile)) {
				p.load(is);
				String username = p.getProperty(KEY_USERNAME);
				String hashed_password = p.getProperty(KEY_PASSWORD);
				logincredentials = new LoginCredentials(username, hashed_password);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return logincredentials;
	}
	
	/**
	 * Save new credentials, and returns the old credentials
	 * <p>
	 * Useful for e.g. testing purposes
	 * 
	 * @param c new credentials
	 * @return old credentials
	 */
	public static LoginCredentials saveLoginCredentials(LoginCredentials c) {
		LoginCredentials oldLoginCredentials = logincredentials;
		logincredentials = c;
		
//		// notify listeners
//		for (CredentialsListener cl : listeners) {
//			cl.credentialsChanged(oldLoginCredentials, c);
//		}
		
		return oldLoginCredentials;
	}
	
	public static void saveLoginCredentialsLocalFile(LoginCredentials c) throws IOException {
		// Create app dir and property file
		File logincredFile = getLoginCredentialsFile();
		File parent = logincredFile.getParentFile(); 
		if (parent != null && !parent.exists()) {
			if (!parent.mkdirs()) {
				throw new IOException("Couldn't create directory");
			}
		}
		
		// Create property file output stream
		try (FileOutputStream fos = new FileOutputStream(logincredFile)){
			Properties p = new Properties();
			p.setProperty(KEY_USERNAME, c.getUsername());
			p.setProperty(KEY_PASSWORD, c.getPassword());
			p.store(fos, " Login credentials\n WARNING: Password not encrypted! Keep this file secret.");
		}
		
		saveLoginCredentials(c);
	}
	
	public static void deleteFromDisk() {
		File cFile = getLoginCredentialsFile();
		if (cFile.exists() && !cFile.isDirectory()) {
			cFile.delete();
		}
	}
	
	public static boolean fileExists() {
		File cFile = getLoginCredentialsFile();
		return cFile.exists() && !cFile.isDirectory();
	}
	
	private static File getLoginCredentialsFile() {
		if (logincredFile == null) {
			String userhome = System.getProperty("user.home");
			Path appFolder = Paths.get(userhome, APP_FOLDER_NAME);
			logincredFile = appFolder.resolve(LOGIN_PROPERTIES_NAME).toFile();
		}
		return logincredFile;
	}
	
	public static File setLoginCredentialsFile(File newFile) {
		File oldFile = logincredFile;
		logincredFile = newFile;
		return oldFile;
	}
	
//	public static void addListener(CredentialsListener cl) {
//		listeners.add(cl);
//	}
	
	/**
	 * Courtesy check to see if the CredentialsFactory is ready to be queried.
	 * 
	 * @return true if the factory is ready to produce credentials, false otherwise
	 */
	public static boolean isReady() {
		return ready;
	}
	
	/**
	 * Sets the readiness of the factory. Should only be called externally if
	 * there's a need to turn of database connectivity
	 * 
	 * @param ready the new readiness value
	 * @return the old readiness value
	 */
	public static boolean setReady(boolean ready) {
		boolean oldReady = LoginCredentialsFactory.ready;
		LoginCredentialsFactory.ready = ready;
		return oldReady;
	}

}
