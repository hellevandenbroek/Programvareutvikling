package tdt4140.gr1811.app.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import tdt4140.gr1811.app.db.QueryImporter;
import tdt4140.gr1811.app.pojo.DataProvider;

@RunWith(MockitoJUnitRunner.class)
public class DataProvider_BloodsugarDaoTest {

	@Mock
	private DataSource ds;

	@Mock
	private Connection c;

	@Mock
	private Statement stmt;

	@Mock
	private PreparedStatement pstm;

	@Mock
	private ResultSet rs;

	@Test(expected = IllegalArgumentException.class)
	public void nullInConstructorShouldThrow() {
		new DataProvider_BloodsugarDao(null);
	}

	@Test
	public void testGetAllDataProviders_Bloodsugar() throws Exception {
		when(ds.getConnection()).thenReturn(c);
		when(c.createStatement()).thenReturn(stmt);
		when(stmt.executeQuery(anyString())).thenReturn(rs);

		when(rs.next()).thenReturn(true, true, true, false, true, true, true, false);
		when(rs.getInt("DataProviderID")).thenReturn(1, 2, 3, 1, 2, 3);

		DataProvider_BloodsugarDao dao = new DataProvider_BloodsugarDao(ds);
		List<DataProvider> result = dao.getAllDataProviders_Bloodsugar();
		assertEquals(3, result.size());
		for (int i = 0; i < result.size(); i++)
			assertEquals(i + 1, result.get(i).getId().intValue());
	}

	@Test
	public void testSaveBloodsugar() throws Exception {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(QueryImporter.getQuery("insertBloodsugarData.sql"))).thenReturn(pstm);

		DataProvider_BloodsugarDao dao = new DataProvider_BloodsugarDao(ds);
		Map<Timestamp, Double> data = new HashMap<>();
		Date date = new Date();
		Timestamp t = new Timestamp(date.getTime());
		Double level = 12.0;
		data.put(t, level);
		dao.saveBloodsugar(20, data);
	}
	
	@Test(expected=SQLException.class)
	public void sqlErrorOnSaveShouldThrow() throws Exception {
		SQLException mock = mock(SQLException.class);
		when(ds.getConnection()).thenThrow(mock);
		DataProvider_BloodsugarDao dao = new DataProvider_BloodsugarDao(ds);
		Map<Timestamp, Double> data = new HashMap<>();
		dao.saveBloodsugar(1, data);
	}

	@Test(expected=SQLException.class)
	public void sqlErrorOnGetShouldThrow() throws Exception {
		SQLException mock = mock(SQLException.class);
		when(ds.getConnection()).thenThrow(mock);
		DataProvider_BloodsugarDao dao = new DataProvider_BloodsugarDao(ds);
		dao.getAllDataProviders_Bloodsugar();
	}

	@Test
	public void testGetById() throws Exception {
		String fname = "Alice";
		String lname = "Bolder";
		int id = 25;
		Timestamp time1 = new Timestamp(5);
		Timestamp time2 = new Timestamp(20);
		double val1 = 20;
		double val2 = 40;

		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstm);
		when(pstm.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true, true, false);
		when(rs.getString("FirstName")).thenReturn(fname);
		when(rs.getString("LastName")).thenReturn(lname);
		when(rs.getTimestamp("Timestamp")).thenReturn(time1, time2);
		when(rs.getDouble("Level")).thenReturn(val1, val2);

		DataProvider_BloodsugarDao dao = new DataProvider_BloodsugarDao(ds);
		DataProvider result = dao.getDataProvider_Bloodsugar(id);

		assertEquals(fname, result.getFirstName());
		assertEquals(lname, result.getLastName());
		assertEquals(id, (int) result.getId());
		assertTrue(result.getBlood_data().containsKey(time1));
		assertTrue(result.getBlood_data().containsKey(time2));
		assertEquals(val1, (double) result.getBlood_data().get(time1), 1d);
		assertEquals(val2, (double) result.getBlood_data().get(time2), 1d);
	}

	@Test(expected=SQLException.class)
	public void getByIdSqlExceptionShouldThrow() throws Exception {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenThrow(new SQLException());

		DataProvider_BloodsugarDao dao = new DataProvider_BloodsugarDao(ds);
		dao.getDataProvider_Bloodsugar(4);
	}

}
