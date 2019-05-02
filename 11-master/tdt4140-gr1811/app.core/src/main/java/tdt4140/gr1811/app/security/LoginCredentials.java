package tdt4140.gr1811.app.security;

public class LoginCredentials {
	
	private String username;
	private String hashed_password;
	
	public LoginCredentials(String username, String encoded_password) {
		this.username = username;
		this.hashed_password = encoded_password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return hashed_password;
	}
	
	

}
