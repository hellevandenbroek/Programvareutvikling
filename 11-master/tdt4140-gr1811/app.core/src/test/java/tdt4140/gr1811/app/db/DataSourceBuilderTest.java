package tdt4140.gr1811.app.db;

import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.junit.Test;

public class DataSourceBuilderTest {

	@Test
	public void testShouldBuild() {
		Credentials c = new Credentials("serverName", 40000, "dbname", "user", "password");
		
		DataSourceBuilder builder = new DataSourceBuilder(c);
		assertNotNull(builder);
		
		DataSource ds = builder.build();
		assertNotNull(ds);
	}

	@Test
	public void testMultiConnectionOption() {
		Credentials c = new Credentials("serverName", 40000, "dbname", "user", "password");

		DataSource without = new DataSourceBuilder(c).build();
		DataSource with = new DataSourceBuilder(c).allowMultiQueries().build();

		assertNotNull(with);
		assertNotNull(without);
		assertNotEquals(with, without);
	}

}
