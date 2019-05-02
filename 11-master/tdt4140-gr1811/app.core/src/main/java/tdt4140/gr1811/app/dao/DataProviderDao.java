package tdt4140.gr1811.app.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;
import javax.sql.DataSource;

import tdt4140.gr1811.app.db.FilterQueryBuilder;
import tdt4140.gr1811.app.db.QueryImporter;
import tdt4140.gr1811.app.pojo.DataProvider;
import tdt4140.gr1811.app.pojo.FilterAttributes;
import tdt4140.gr1811.app.pojo.Sex;
import tdt4140.gr1811.app.pojo.Status;

public class DataProviderDao {

	private DataSource ds;

	public DataProviderDao(DataSource ds) {
		if (ds == null) {
			throw new IllegalArgumentException("Datasource was null");
		}
		this.ds = ds;
	}

	public DataProvider getAllDataOfOne(int dataProviderID) throws SQLException {
		String query = QueryImporter.getQuery("getAllDataAboutOneById.sql");
		DataProvider allDataForUser;
		try (Connection conn = ds.getConnection()) {
			try (PreparedStatement pstm = conn.prepareStatement(query)) {
				pstm.setInt(1, dataProviderID);
				try (ResultSet rs = pstm.executeQuery()) {
					DataProvider user = new DataProvider();

					if (rs.next()) {
						user.setId(rs.getInt("DataproviderID"));
						user.setFirstName(rs.getString("FirstName"));
						user.setLastName(rs.getString("LastName"));
						user.setBirthday(rs.getDate("BirthDate"));
						String sex = rs.getString("Sex");
						user.setSex(sex == null ? null : Sex.valueOf(sex));
						user.setHeight(rs.getInt("Height"));
						user.setWeight(rs.getInt("Weight"));
						String status = rs.getString("Status");
						user.setStatus(status == null ? null : Status.valueOf(status.toUpperCase()));
						user.setStartDate(rs.getDate("StartDate"));
						user.setEmail(rs.getString("Email"));
						user.setPhoneNum(rs.getString("PhoneNum"));
					}
					rs.beforeFirst();
					HashMap<Timestamp, Double> blood_data = new HashMap<>();
					HashMap<Timestamp, Integer> pulse_data = new HashMap<>();
					while (rs.next()) {
						// data
						Timestamp t = rs.getTimestamp("blood_time");
						if (!rs.wasNull()) {
							blood_data.put(t, rs.getDouble("Level"));
						}
						t = rs.getTimestamp("pulse_time");
						if (!rs.wasNull()) {
							pulse_data.put(t, rs.getInt("BPM"));
						}
					}
					user.getPulse_data().putAll(pulse_data);
					user.getBlood_data().putAll(blood_data);
					allDataForUser = user;
				}
			}
		}
		return allDataForUser;
	}

	/**
	 * Setting the delete state of the data preceeding the current time.
	 * @param setDelete set delete state to either true or false
	 *                  True: to be deleted later
	 *                  False: not to be deleted (default for incoming data)
	 * @param dataProviderID ID of the data provider
	 * @throws SQLException
	 */
	public void setDeleteState(Boolean setDelete, int dataProviderID) throws SQLException {
		try (Connection conn = ds.getConnection()) {
			String insertDeletedData = QueryImporter.getQuery("insertDeletedData.sql");
			String removeDeletedData = QueryImporter.getQuery("removeDeletedData.sql");

			if (setDelete) {
				// Set to deleted
				try (PreparedStatement pstm = conn.prepareStatement(insertDeletedData)) {
					Timestamp ts = new Timestamp(System.currentTimeMillis());
					pstm.setTimestamp(1, ts);
					pstm.setInt(2, dataProviderID);
					pstm.executeUpdate();
				}
			} else {
				// remove from deleted
				try (PreparedStatement pstm = conn.prepareStatement(removeDeletedData)) {
					pstm.setInt(1, dataProviderID);
					pstm.executeUpdate();
				}
			}
		}
	}

	/**
	 * Used by scheduler to remove data that has been in
	 * DeletedData for longer than one week.
	 * @param datagiverID ID of datagiver.
	 * @throws SQLException Problems with connection or input may occur
	 */
	public void deleteCachedData(int datagiverID) throws SQLException {
		// Delete bloodsugar and pulse data
		String deletionCachedBloodsugar = QueryImporter.getQuery("deleteCachedBloodsugar.sql");
		String deletionCachedPulse = QueryImporter.getQuery("deleteCachedPulse.sql");
		try (Connection conn = ds.getConnection()) {
			try (PreparedStatement pstm = conn.prepareStatement(deletionCachedBloodsugar)) {
				pstm.setInt(1, datagiverID);
				pstm.executeUpdate();
			}
			try (PreparedStatement pstm = conn.prepareStatement(deletionCachedPulse)) {
				pstm.setInt(1, datagiverID);
				pstm.executeUpdate();
			}
		}
	}


	/**
	 * Permanently delete data from the database.
	 * @param datagiverID
	 * @throws SQLException
	 */
	public void deleteData(int datagiverID) throws SQLException {
		// Delete bloodsugar and pulse data
		String deletionBloodsugar = QueryImporter.getQuery("deleteBloodsugar.sql");
		String deletionPulse = QueryImporter.getQuery("deletePulse.sql");
		try (Connection conn = ds.getConnection()) {
			try (PreparedStatement pstm = conn.prepareStatement(deletionBloodsugar)) {
				pstm.setInt(1, datagiverID);
				pstm.executeUpdate();
			}
			try (PreparedStatement pstm = conn.prepareStatement(deletionPulse)) {
				pstm.setInt(1, datagiverID);
				pstm.executeUpdate();
			}
		}
	}

	/**
	 * Delete user from database.
	 * @param dataProviderId
	 * @throws SQLException
	 */
	public void deleteUser(int dataProviderId) throws SQLException {
		String deleteDatagiver = QueryImporter.getQuery("deleteDataProvider.sql");
		try (Connection conn = ds.getConnection()) {
			try (PreparedStatement pstm = conn.prepareStatement(deleteDatagiver)) {
				pstm.setInt(1, dataProviderId);
				pstm.executeUpdate();
			}
		}
	}

	/**
	 * Get entries from DeletedData table in database.
	 * @return
	 * @throws SQLException
	 */
	public Map<Integer, Timestamp> getDeletedDataInfo() throws SQLException {
		String deletedDataInfo = QueryImporter.getQuery("getDeletedDataInfo.sql");
		try (Connection conn = ds.getConnection()) {
			try (Statement stmt = conn.createStatement()) {
				try (ResultSet rs = stmt.executeQuery(deletedDataInfo)) {
					Map<Integer, Timestamp> data = new HashMap<>();
					while (rs.next()) {
						int dataProviderID = rs.getInt("DataProviderID");
						Timestamp ts = rs.getTimestamp("Timestamp");
						data.put(dataProviderID, ts);
					}
					return data;
				}
			}
		}
	}


	public Status getStatus(int DatagiverID) throws SQLException {
		String query = QueryImporter.getQuery("getStatus.sql");
		try (Connection conn = ds.getConnection()) {
			try (PreparedStatement pstm = conn.prepareStatement(query)) {
				pstm.setInt(1, DatagiverID);
				try (ResultSet rs = pstm.executeQuery()) {
					if (rs.next()) {
						String status = rs.getString("status");
						if (status == null) {
							return null;
						}
						try {
							return Status.valueOf(status);
						} catch (IllegalArgumentException e) {
							throw new RuntimeException("Unknown state: " + status, e);
						}
					}
				}
			}
		}
		return null;
	}

	public void changeStatus(int datagiver, Status status) throws SQLException{
		String query = QueryImporter.getQuery("statusChange.sql");
		try (Connection conn = ds.getConnection()){
			try (PreparedStatement pstm = conn.prepareStatement(query)){
				String sqlStatus = status.name();
				pstm.setString(1, sqlStatus);
				pstm.setInt(2, datagiver);
				pstm.executeUpdate();
			}
		}
	}
	
	public void deactivateUser(int dataProviderId) throws SQLException {
		String deactivateUser = QueryImporter.getQuery("deactivateDataprovider.sql");
		try (Connection conn = ds.getConnection()) {
			try (PreparedStatement pstm = conn.prepareStatement(deactivateUser)) {
				pstm.setInt(1, dataProviderId);
				pstm.executeUpdate();
			}
		}
	}
	public List<Integer> getIdsByFilter(FilterAttributes fa) throws SQLException {
		final List<Integer> ids = new ArrayList<>();
		String query = new FilterQueryBuilder().queryBuilder(fa);
		try (Connection conn = ds.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				Integer id = rs.getInt("DataproviderID");
				ids.add(id);
			}
		}
		return ids;
	}

	public List<DataProvider> getAllDatagivers_tableData() throws SQLException {
		List<DataProvider> data = new ArrayList<>();
		String query1 = QueryImporter.getQuery("getDatagiver_TableData.sql");
		try (Connection conn = ds.getConnection()) {
			try (Statement stm = conn.createStatement()) {
				try (ResultSet rs = stm.executeQuery(query1)) {
					while (rs.next()) {
						DataProvider datagiver = new DataProvider();
						Integer id = rs.getInt("DataproviderID");
						datagiver.setId(id);
						datagiver.setSsn(rs.getString("SSN"));
						datagiver.setFirstName(rs.getString("Firstname"));
						datagiver.setLastName(rs.getString("Lastname"));
						String sex = rs.getString("Sex");
						datagiver.setSex(sex == null ? null : Sex.valueOf(sex));
						String status = rs.getString("Status");
						datagiver.setStatus(status == null ? null : Status.valueOf(status.toUpperCase()));
						datagiver.setBirthday(rs.getDate("BirthDate"));
						datagiver.setWeight(rs.getInt("Weight"));
						datagiver.setHeight(rs.getInt("Height"));
						data.add(datagiver);
					}
				}
			}
		}
		return data;
	}

	
	public DataProvider getById(int id) throws SQLException {
		String query = QueryImporter.getQuery("getDataproviderById.sql");
		DataProvider dataprovider = null;
		try (Connection conn = ds.getConnection(); PreparedStatement pstm = conn.prepareStatement(query)) {
			pstm.setInt(1, id);
			try (ResultSet rs = pstm.executeQuery()) {
				if (rs.next()) {
					dataprovider = new DataProvider();
					dataprovider.setId(rs.getInt("DataproviderID"));
					dataprovider.setSsn(rs.getString("SSN"));
					dataprovider.setFirstName(rs.getString("FirstName"));
					dataprovider.setLastName(rs.getString("LastName"));
					dataprovider.setBirthday(rs.getDate("BirthDate"));
					String sex = rs.getString("Sex");
					String status = rs.getString("Status");
					dataprovider.setSex(sex == null ? null : Sex.valueOf(sex));
					dataprovider.setStatus(status == null ? null : Status.valueOf(status));
					dataprovider.setWeight(rs.getInt("Weight"));
					dataprovider.setHeight(rs.getInt("Height"));
					dataprovider.setEmail(rs.getString("Email"));
					dataprovider.setPhoneNum(rs.getString("PhoneNum"));
				}
			}
		}
		return dataprovider;
	}

	public void createDataprovider(String ssn, String firstName, String lastName, Date birthDate, Sex sex,
			Double weight, Double height, String email, String phoneNum) throws SQLException {
		String query = QueryImporter.getQuery("createUser.sql");
		try (Connection conn = ds.getConnection(); PreparedStatement pstm = conn.prepareStatement(query)) {
			Calendar currenttime = Calendar.getInstance();
			Date now = new Date((currenttime.getTime()).getTime());
			pstm.setString(1, ssn);
			pstm.setString(2, firstName);
			pstm.setString(3, lastName);
			pstm.setDate(4, birthDate);
			pstm.setString(5, sex == null ? null : sex.toString());
			pstm.setDouble(6, weight);
			pstm.setDouble(7, height);
			pstm.setString(8, email);
			pstm.setString(9, phoneNum);
			pstm.setDate(10, now);
			pstm.executeUpdate();
		}
	}
	public void saveDataprovider(DataProvider dp) throws SQLException {
		String query = QueryImporter.getQuery("saveDataprovider.sql");
		try (Connection conn = ds.getConnection(); PreparedStatement pstm = conn.prepareStatement(query)) {
			pstm.setString(1, dp.getSsn());
			pstm.setString(2, dp.getFirstName());
			pstm.setString(3, dp.getLastName());
			pstm.setDate(4, dp.getBirthday());
			pstm.setString(5,dp.getSex() == null ? null : dp.getSex().toString());
			pstm.setDouble(6, dp.getWeight());
			pstm.setDouble(7, dp.getHeight());
			pstm.setString(8, dp.getStatus() == null ? null : dp.getStatus().toString());
			pstm.setString(9, dp.getEmail());
			pstm.setString(10, dp.getPhoneNum());
			pstm.setInt(11, dp.getId());
			pstm.executeUpdate();
		}
	}
}
