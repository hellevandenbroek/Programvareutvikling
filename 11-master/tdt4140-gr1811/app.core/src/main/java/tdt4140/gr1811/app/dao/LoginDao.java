package tdt4140.gr1811.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import tdt4140.gr1811.app.db.QueryImporter;

public class LoginDao {
	
	private DataSource ds;

	public LoginDao(DataSource ds) {
		if (ds == null) {
			throw new IllegalArgumentException("Datasource was null");
		}
		this.ds = ds;
	}
	
	/*
	 * Returns the hashed password for the given username or email
	 */
	
	/*
	 * Returns the hashed password for the given email
	 */
	
	public String getHashedPasswordFromUsername(String username) throws SQLException {
		String query = QueryImporter.getQuery("getHashedPassword_Username.sql");
		String hashed_password = null;
		try (Connection conn = ds.getConnection()) {
			try (PreparedStatement stm = conn.prepareStatement(query)) {
				stm.setString(1, username);
				try (ResultSet rs = stm.executeQuery()) {
					if(rs.next()) {	
						if(username.equals(rs.getString("Username"))) {
							hashed_password = rs.getString("PasswordHash");
						}						
					}
				}
			}
		}
		return hashed_password;
	}
	
	
	/*
	 * Returns the hashed password for the given email
	 */
	
	public String getHashedPasswordFromEmail(String email) throws SQLException {
		String query = QueryImporter.getQuery("getHashedPassword_Email.sql");
		String hashed_password = null;
		try (Connection conn = ds.getConnection()) {
			try (PreparedStatement stm = conn.prepareStatement(query)) {
				stm.setString(1, email);
				try (ResultSet rs = stm.executeQuery()) {
					while(rs.next()) {
						if(email.equals(rs.getString("Email"))) {
							hashed_password = rs.getString("PasswordHash");
						}						
					}
				}
			}
		}
		return hashed_password;
	}
	
	

}
