package tdt4140.gr1811.app.security;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import javax.sql.DataSource;
import tdt4140.gr1811.app.dao.LoginDao;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.db.DataSourceBuilder;

public class VerifyPassword {
	
	
	/*
	 * function to check whether password matches
	 */
	public static boolean verifyPassword(String typed_username, String typed_password) {
		
		if (CredentialsFactory.get() != null && typed_username != null && typed_password != null) {
			try {
				DataSource ds = new DataSourceBuilder(CredentialsFactory.get()).build();
				LoginDao dao = new LoginDao(ds);
				String stored_ph = null;
				
				// Check if typed_username matches with any usernames in the db
				stored_ph = dao.getHashedPasswordFromUsername(typed_username);
				if(stored_ph != null) {
					if(SHA_512.verifyPassword(typed_password, stored_ph)) {
						return true;
					}
				}
				
				// Check if typed_username matches with any email in the db
				stored_ph = dao.getHashedPasswordFromEmail(typed_username);
				if(stored_ph != null) {
					if(SHA_512.verifyPassword(typed_password, stored_ph)) {
						return true;
					}
				}
				
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		return false;
	}

}
