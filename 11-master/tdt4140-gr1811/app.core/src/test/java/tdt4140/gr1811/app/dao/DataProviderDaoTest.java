package tdt4140.gr1811.app.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mysql.jdbc.PreparedStatement;

import tdt4140.gr1811.app.pojo.DataProvider;
import tdt4140.gr1811.app.pojo.FilterAttributes;
import tdt4140.gr1811.app.pojo.Sex;
import tdt4140.gr1811.app.pojo.Status;

@RunWith(MockitoJUnitRunner.class)
public class DataProviderDaoTest {

	@Mock
	private DataSource ds;

	@Mock
	private Connection c;

	@Mock
	private Statement stmt;

	@Mock
	private PreparedStatement pstmt;

	@Mock
	private ResultSet rs;

	@Test
	public void testGetData() throws Exception {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstmt);
		when(pstmt.executeQuery()).thenReturn(rs);

		// simulate a user
		DataProvider user = new DataProvider();
		user.setId(1);
		user.setFirstName("Bob");
		user.setSex(Sex.M);
		user.setStatus(Status.ACTIVE);

		// insert data set
		HashMap<Timestamp, Double> blood_data = new HashMap<>();
		Timestamp key = new Timestamp(5);
		Double val = new Double(10);
		blood_data.put(key, val);
		user.getBlood_data().putAll(blood_data);

		when(rs.getInt("DataproviderID")).thenReturn(user.getId());
		when(rs.getString("FirstName")).thenReturn(user.getFirstName());
		when(rs.getTimestamp("blood_time")).thenReturn(key);
		when(rs.getDouble("Level")).thenReturn(val);
		when(rs.getString("Sex")).thenReturn(Sex.M.name());
		when(rs.getString("Status")).thenReturn(Status.ACTIVE.name());

		when(rs.next()).thenReturn(true, true, false);
		when(rs.wasNull()).thenReturn(false);

		DataProviderDao dao = new DataProviderDao(ds);
		DataProvider processed = dao.getAllDataOfOne(1);

		assertEquals(user.getId(), processed.getId());
		assertEquals(user.getFirstName(), processed.getFirstName());

		assertEquals(user.getSex(), processed.getSex());
		assertEquals(user.getStatus(), processed.getStatus());

		// compare data point
		assertEquals(user.getBlood_data().get(key), processed.getBlood_data().get(key), 2d);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullInConstructorShouldThrow() {
		new DataProviderDao(null);
	}

	@Test(expected = SQLException.class)
	public void SqlErrorShouldReturnThrow() throws Exception {
		SQLException mock = mock(SQLException.class);
		when(ds.getConnection()).thenThrow(mock);
		DataProviderDao dao = new DataProviderDao(ds);
		dao.getAllDataOfOne(1);
	}

	@Test
	public void testDeleteMethod() throws Exception {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstmt);
		when(pstmt.executeQuery()).thenReturn(rs);

		DataProviderDao dao = new DataProviderDao(ds);
		dao.deleteData(1);
	}

	@Test(expected = SQLException.class)
	public void sqlExceptionShouldThrow() throws Exception {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstmt);
		when(pstmt.executeUpdate()).thenThrow(new SQLException("Should throw."));

		DataProviderDao dao = new DataProviderDao(ds);
		dao.deleteData(1);
	}

	@Test
	public void testGetIdByFilter() throws Exception {
		when(ds.getConnection()).thenReturn(c);
		when(c.createStatement()).thenReturn(stmt);
		when(stmt.executeQuery(anyString())).thenReturn(rs);
		when(rs.next()).thenReturn(true, true, true, false);
		when(rs.getInt("DataproviderID")).thenReturn(3, 4, 5);

		DataProviderDao dao = new DataProviderDao(ds);
		List<Integer> result = dao.getIdsByFilter(new FilterAttributes());

		assertEquals(3, (int) result.get(0));
		assertEquals(4, (int) result.get(1));
		assertEquals(5, (int) result.get(2));
	}

	@Test(expected = SQLException.class)
	public void testGetIdByFilterBubblesException() throws Exception {
		when(ds.getConnection()).thenThrow(new SQLException());

		DataProviderDao dao = new DataProviderDao(ds);
		dao.getIdsByFilter(new FilterAttributes());
	}

	@Test(expected = SQLException.class)
	public void sqlErrorOnGetTableDataShouldThrow() throws Exception {
		SQLException mock = mock(SQLException.class);
		when(ds.getConnection()).thenThrow(mock);
		DataProviderDao dao = new DataProviderDao(ds);
		dao.getAllDatagivers_tableData();
	}

	@Test
	public void testGetDataproviderById() throws SQLException {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstmt);
		when(pstmt.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true).thenReturn(true).thenReturn(false);
		DataProviderDao dao = new DataProviderDao(ds);
		dao.getById(1);
	}

	@Test
	public void testDeleteUser() throws SQLException {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstmt);
		when(pstmt.executeQuery()).thenReturn(rs);

		DataProviderDao dao = new DataProviderDao(ds);
		dao.deleteUser(20);
	}
	
	@Test
	public void testDeactivateUser() throws SQLException {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstmt);
		when(pstmt.executeQuery()).thenReturn(rs);

		DataProviderDao dao = new DataProviderDao(ds);
		dao.deactivateUser(20);
	}

	@Test
	public void testCreateUser() throws SQLException, ParseException {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstmt);
		when(pstmt.executeQuery()).thenReturn(rs);

		DataProviderDao dao = new DataProviderDao(ds);
		java.util.Date utilDate;
		utilDate = new SimpleDateFormat("dd MMM yyyy").parse("01 NOVEMBER 2012");
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		dao.createDataprovider("123", "Per", "Andersen", sqlDate, Sex.M, 180.0, 80.0, "per@andersen.no", "96969696");
	}

	@Test
	public void testAddToDeleted() throws SQLException {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstmt);
		when(pstmt.executeQuery()).thenReturn(rs);

		DataProviderDao dao = new DataProviderDao(ds);
		Boolean setDeleted = true;
		int dataproviderID = 4;
		dao.setDeleteState(setDeleted, dataproviderID);
	}

	@Test
	public void testRemoveFromDeleted() throws SQLException {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstmt);
		when(pstmt.executeQuery()).thenReturn(rs);

		DataProviderDao dao = new DataProviderDao(ds);
		Boolean setDeleted = false;
		int dataproviderID = 4;
		dao.setDeleteState(setDeleted, dataproviderID);
	}

	@Test
	public void testDeleteCachedData() throws SQLException {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstmt);
		when(pstmt.executeQuery()).thenReturn(rs);

		DataProviderDao dao = new DataProviderDao(ds);
		int dataproviderID = 4;
		dao.deleteCachedData(dataproviderID);
	}

	@Test
	public void testGetDeletedDataInfo() throws SQLException {
		when(ds.getConnection()).thenReturn(c);
		Statement stmt = mock(Statement.class);
		when(c.createStatement()).thenReturn(stmt);
		when(stmt.executeQuery(anyString())).thenReturn(rs);
		when(rs.next()).thenReturn(true, false);

		DataProviderDao dao = new DataProviderDao(ds);
		dao.getDeletedDataInfo();
	}
	
	@Test 
	public void saveDataprovider() throws SQLException, Exception {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstmt);
		when(pstmt.executeQuery()).thenReturn(rs);
		
		DataProviderDao dao = new DataProviderDao(ds);
		DataProvider dp = new DataProvider();
		dp.setFirstName("Test");
		dp.setLastName("Testerson");
		java.util.Date utilDate;
		utilDate = new SimpleDateFormat("dd MMM yyyy").parse("01 NOVEMBER 2012");
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		dp.setBirthday(sqlDate);
		dp.setEmail("test@testson.test");
		dp.setHeight(190);
		dp.setWeight(80);
		dp.setPhoneNum("91919191");
		dp.setSsn("111111111");
		dp.setStatus(Status.ACTIVE);
		dp.setSex(Sex.M);
		dp.setStartDate(sqlDate);
		dp.setId(100);
		dao.saveDataprovider(dp);
	}
	
	@Test
	public void testGetStatus() throws SQLException {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstmt);
		when(pstmt.executeQuery()).thenReturn(rs);

		when(rs.next()).thenReturn(true, false);
		String active = Status.ACTIVE.name();
		when(rs.getString(anyString())).thenReturn(active);
		when(rs.getString(anyInt())).thenReturn(active);

		DataProviderDao dao = new DataProviderDao(ds);
		Status s = dao.getStatus(1);

		assertEquals(Status.ACTIVE, s);
	}

	@Test
	public void testGetStatusNullReturnsNull() throws SQLException {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstmt);
		when(pstmt.executeQuery()).thenReturn(rs);

		when(rs.next()).thenReturn(true, false);
		String active = null;
		when(rs.getString(anyString())).thenReturn(active);
		when(rs.getString(anyInt())).thenReturn(active);

		DataProviderDao dao = new DataProviderDao(ds);
		Status s = dao.getStatus(1);

		assertNull(s);
	}

	@Test(expected = RuntimeException.class)
	public void testGetUnknownStatusThrows() throws SQLException {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstmt);
		when(pstmt.executeQuery()).thenReturn(rs);

		when(rs.next()).thenReturn(true, false);
		String active = "ThisIsAnUnknownEnumValue";
		when(rs.getString(anyString())).thenReturn(active);
		when(rs.getString(anyInt())).thenReturn(active);

		DataProviderDao dao = new DataProviderDao(ds);
		dao.getStatus(1);
	}

	@Test
	public void testGetStatusEmptyResultReturnsNull() throws SQLException {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstmt);
		when(pstmt.executeQuery()).thenReturn(rs);

		when(rs.next()).thenReturn(false);

		DataProviderDao dao = new DataProviderDao(ds);
		Status s = dao.getStatus(1);
		assertNull(s);
	}

	@Test
	public void testChangeStatus() throws SQLException {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstmt);
		when(pstmt.executeQuery()).thenReturn(rs);

		DataProviderDao dao = new DataProviderDao(ds);
		dao.changeStatus(1, Status.ACTIVE);

		verify(pstmt).executeUpdate();
	}
}
