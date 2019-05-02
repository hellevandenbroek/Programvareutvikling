package tdt4140.gr1811.app.db;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DataSourceBuilder {
	
	private final MysqlDataSource dataSource;

	public DataSourceBuilder(Credentials c) {
		dataSource = new MysqlDataSource();
		dataSource.setServerName(c.getServerName());
		dataSource.setDatabaseName(c.getDatabaseName());
		dataSource.setUser(c.getUsername());
		dataSource.setPassword(c.getPassword());
		if (c.getPort() != null) {
			dataSource.setPort(c.getPort());
		}
	}
	
	public DataSourceBuilder allowMultiQueries() {
		dataSource.setAllowMultiQueries(true);
		return this;
	}
	
	public DataSource build() {
		return this.dataSource;
	}

}
