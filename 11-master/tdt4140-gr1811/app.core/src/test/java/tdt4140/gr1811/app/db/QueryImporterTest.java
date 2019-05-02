package tdt4140.gr1811.app.db;

import static org.junit.Assert.*;

import org.junit.Test;

public class QueryImporterTest {

	@Test(expected=RuntimeException.class)
	public void nonexistentFileShouldThrowRuntimeException() {
		QueryImporter.getQuery("nonexistentFile");
	}

	@Test
	public void testImport() {
		String query = "";
		query = QueryImporter.getQuery("sampleQuery.sql");
		assertEquals("SELECT * FROM test;\n", query);
	}
	
	@Test
	public void testInstancing() {
		QueryImporter queryImporter = new QueryImporter();
		assertNotNull(queryImporter);
	}

}
