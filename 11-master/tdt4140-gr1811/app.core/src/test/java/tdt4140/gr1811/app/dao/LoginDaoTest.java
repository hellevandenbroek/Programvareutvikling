package tdt4140.gr1811.app.dao;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoginDaoTest {

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
		new LoginDao(null);
	}
	
	@Test
	public void testGetHashedPasswordFromUsername() throws SQLException {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstm);
		when(pstm.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true, true, false);
		LoginDao loginDao = new LoginDao(ds);
		loginDao.getHashedPasswordFromUsername("admin");
	}
	
	@Test
	public void testGetHashedPasswordFromEmail() throws SQLException {
		when(ds.getConnection()).thenReturn(c);
		when(c.prepareStatement(anyString())).thenReturn(pstm);
		when(pstm.executeQuery()).thenReturn(rs);
		when(rs.next()).thenReturn(true, true, false);
		LoginDao loginDao = new LoginDao(ds);
		loginDao.getHashedPasswordFromEmail("admin@d1abytes.com");
	}

	


}


