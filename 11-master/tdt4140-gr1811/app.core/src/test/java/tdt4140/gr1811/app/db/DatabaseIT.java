package tdt4140.gr1811.app.db;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import tdt4140.gr1811.app.dao.DataProviderDao;
import tdt4140.gr1811.app.dao.DataProvider_BloodsugarDao;
import tdt4140.gr1811.app.dao.DataProvider_PulseDao;
import tdt4140.gr1811.app.db.Credentials;
import tdt4140.gr1811.app.pojo.DataProvider;
import tdt4140.gr1811.app.pojo.FilterAttributes;

public class DatabaseIT {
	
	private static DataSource ds;
	private static Credentials oldCredentials = null;
	private static Credentials testCredentials = new Credentials("mysql-it", null, "IntegrationTest", "root", "abcd");

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ds = new DataSourceBuilder(testCredentials)
				.allowMultiQueries()
				.build();
		oldCredentials = CredentialsFactory.saveCredentials(testCredentials);
		
		// Setup database schema and example values
		try (Connection conn = ds.getConnection()){
			try (Statement stmt = conn.createStatement()){
				stmt.executeUpdate(QueryImporter.getQuery("tdt4140_gruppe11_schema.sql"));
				stmt.executeUpdate(QueryImporter.getQuery("tdt4140_gruppe11_example_values.sql"));
			}
		}
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		try (Connection conn = ds.getConnection()){
			try (Statement stmt = conn.createStatement()) {
				stmt.executeUpdate(QueryImporter.getQuery("tdt4140_gruppe11_schema_drop.sql"));
			}
		}
		CredentialsFactory.saveCredentials(oldCredentials);
	}

	@Test
	public void testConnection() throws Exception {
		try (Connection c = ds.getConnection()){
			// do nothing with it here
		}
	}
	
	@Test
	public void test_getAllDataProviders_Bloodsugar() throws Exception {
		DataProvider_BloodsugarDao dao = new DataProvider_BloodsugarDao(ds);
		List<DataProvider> stuff = dao.getAllDataProviders_Bloodsugar();
		assertNotNull(stuff);
	}
	
	@Test
	public void test_getAllDataProviders_Pulse() throws Exception {
		DataProvider_PulseDao dao = new DataProvider_PulseDao(ds);
		List<DataProvider> stuff = dao.getAllDataProviders_Pulse();
		assertNotNull(stuff);
	}
	
	@Test
	public void test_getTableData() throws Exception {
		DataProviderDao dao = new DataProviderDao(ds);
		List<DataProvider> stuff = dao.getAllDatagivers_tableData();
		assertNotNull(stuff);
	}
	
	@Test
	public void checkDatabaseFiltering() throws SQLException {
		
		// Setup database schema and example values
		try (Connection conn = ds.getConnection()){
			try (Statement stmt = conn.createStatement()){		
				stmt.executeUpdate("INSERT INTO DataProvider VALUES (98, '15065012356', 'Testa1', 'Testason1',"
						+ " '1950-06-15', 'F', 40, 100, 'active', '2018-03-21', 'test@testmail.com', '91676385');");
				stmt.executeUpdate("INSERT INTO DataProvider VALUES (99, '15065012356', 'Testa2', 'Testason2',"
						+ " '1950-06-15', 'F', 80, 100, 'active', '2018-03-21', 'test@testmail.com', '91676385');");
				stmt.executeUpdate("INSERT INTO DataProvider VALUES (100, '15065012356', 'Testa3', 'Testason3',"
						+ " '1950-06-15', 'F', 120, 100, 'active', '2018-03-21', 'test@testmail.com', '91676385');");
			}
		}
		DataProviderDao  dao = new DataProviderDao(ds);
		FilterAttributes fa = new FilterAttributes();
		fa.setMinWeight(50);
		fa.setMaxWeight(200);
		List<Integer> result = dao.getIdsByFilter(new FilterAttributes());
		assertTrue(result.size() >= 2);
	}

}
