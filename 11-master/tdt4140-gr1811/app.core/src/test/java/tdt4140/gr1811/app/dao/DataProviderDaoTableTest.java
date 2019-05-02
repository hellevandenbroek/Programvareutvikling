package tdt4140.gr1811.app.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.mysql.jdbc.PreparedStatement;

import tdt4140.gr1811.app.pojo.DataProvider;
import tdt4140.gr1811.app.pojo.Sex;

@RunWith(MockitoJUnitRunner.class)
public class DataProviderDaoTableTest {

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

	private DataProvider a;
	private DataProvider b;

	@Test
	public void testGetAllDatagivers_tableData() throws Exception {
		when(ds.getConnection()).thenReturn(c);
		when(c.createStatement()).thenReturn(stmt);
		when(stmt.executeQuery(anyString())).thenReturn(rs);

		// simulate two datagivers
		a = new DataProvider();
		b = new DataProvider();
		a.setId(1);
		b.setId(2);
		a.setFirstName("Alice");
		b.setFirstName("Bert");

		when(rs.getInt("DataproviderID")).thenReturn(1, 2);
		when(rs.getString("Firstname")).thenReturn("Alice", "Bert");
		when(rs.getInt("id")).thenReturn(1, 2);
		when(rs.getString("Sex")).thenReturn(Sex.F.name(), Sex.M.name());

		when(rs.next()).thenReturn(true, true, false, true, true, false);
		DataProviderDao dao = new DataProviderDao(ds);
		List<DataProvider> l = dao.getAllDatagivers_tableData();
		DataProvider first = l.get(0);
		DataProvider second = l.get(1);

		assertEquals(a.getId(), first.getId());
		assertEquals(a.getFirstName(), first.getFirstName());

		assertEquals(b.getId(), second.getId());
		assertEquals(b.getFirstName(), second.getFirstName());
	}

}
