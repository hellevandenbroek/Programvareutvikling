package tdt4140.gr1811.app.db;

public class Credentials {
	
	private String serverName;
	private Integer port;
	private String databaseName;
	private String username;
	private String password;
	
	public Credentials(String serverName, Integer port, String databaseName, String username, String password) {
		this.serverName = serverName;
		this.port = port;
		this.databaseName = databaseName;
		this.username = username;
		this.password = password;
	}

	public String getServerName() {
		return serverName;
	}

	public Integer getPort() {
		return port;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}
