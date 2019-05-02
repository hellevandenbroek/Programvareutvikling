package tdt4140.gr1811.app.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CredentialsFactory {
	
	private static final List<CredentialsListener> listeners = new ArrayList<>();

	private static final String APP_FOLDER_NAME = ".diabytes";
	private static final String DB_PROPERTIES_NAME = "db.properties";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_DATABASENAME = "databasename";
	private static final String KEY_PORT = "port";
	private static final String KEY_SERVERNAME = "servername";
	
	private static Credentials credentials = null;
	private static File credFile = null;
	private static boolean ready = true;
	

	/**
	 * @return {@code null} if credentials couldn't be fetched
	 */
	public static Credentials get() {
		if (credentials == null) {
			File credFile = getCredentialsFile();
			if (!credFile.exists()) {
				return null;
			}
			Properties p = new Properties();
			try (InputStream is = new FileInputStream(credFile)) {
				p.load(is);
				String serverName = p.getProperty(KEY_SERVERNAME);
				String portString = p.getProperty(KEY_PORT, null);
				Integer port = null;
				if (portString != null){
					port = Integer.parseInt(portString);
				}
				String databaseName = p.getProperty(KEY_DATABASENAME);
				String username = p.getProperty(KEY_USERNAME);
				String password = p.getProperty(KEY_PASSWORD);
				credentials = new Credentials(serverName, port, databaseName, username, password);
			} catch (IOException e) {
				throw new RuntimeException("Couldn't load database credentials file.", e);
			}
		}
		return credentials;
	}
	
	/**
	 * Save new credentials, and returns the old credentials
	 * <p>
	 * Useful for e.g. testing purposes
	 * 
	 * @param c new credentials
	 * @return old credentials
	 */
	public static Credentials saveCredentials(Credentials c) {
		Credentials oldCredentials = credentials;
		credentials = c;
		
		// notify listeners
		for (CredentialsListener cl : listeners) {
			cl.credentialsChanged(oldCredentials, c);
		}
		
		return oldCredentials;
	}
	
	public static void saveCredentialsLocalFile(Credentials c) throws IOException {
		// Create app dir and property file
		File credFile = getCredentialsFile();
		File parent = credFile.getParentFile();
		if (parent != null && !parent.exists()) {
			if (!parent.mkdirs()) {
				throw new IOException("Couldn't create directory");
			}
		}
		
		// Create property file output stream
		try (FileOutputStream fos = new FileOutputStream(credFile)){
			Properties p = new Properties();
			p.setProperty(KEY_SERVERNAME, c.getServerName());
			p.setProperty(KEY_DATABASENAME, c.getDatabaseName());
			if (c.getPort() != null) {
				p.setProperty(KEY_PORT, c.getPort().toString());
			}
			p.setProperty(KEY_USERNAME, c.getUsername());
			p.setProperty(KEY_PASSWORD, c.getPassword());
			p.store(fos, "Database credentials");
		}
		
		saveCredentials(c);
	}
	
	public static void deleteFromDisk() {
		File cFile = getCredentialsFile();
		if (cFile.exists() && !cFile.isDirectory()) {
			cFile.delete();
		}
	}
	
	public static boolean fileExists() {
		File cFile = getCredentialsFile();
		return cFile.exists() && !cFile.isDirectory();
	}
	
	private static File getCredentialsFile() {
		if (credFile == null) {
			String userhome = System.getProperty("user.home");
			Path appFolder = Paths.get(userhome, APP_FOLDER_NAME);
			credFile = appFolder.resolve(DB_PROPERTIES_NAME).toFile();
		}
		return credFile;
	}
	
	public static File setCredentialsFile(File newFile) {
		File oldFile = credFile;
		credFile = newFile;
		return oldFile;
	}
	
	public static void addListener(CredentialsListener cl) {
		listeners.add(cl);
	}
	
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
		boolean oldReady = CredentialsFactory.ready;
		CredentialsFactory.ready = ready;
		return oldReady;
	}
}
