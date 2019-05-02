package tdt4140.gr1811.app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import tdt4140.gr1811.app.db.QueryImporter;
import tdt4140.gr1811.app.pojo.DataProvider;

public class DataProvider_BloodsugarDao {
	private DataSource ds;

	public DataProvider_BloodsugarDao(DataSource ds) {
		if (ds == null) {
			throw new IllegalArgumentException("Datasource was null");
		}
		this.ds = ds;
	}

	public List<DataProvider> getAllDataProviders_Bloodsugar() throws SQLException {
		String query = QueryImporter.getQuery("getDatagivers_BloodData.sql");
		List<DataProvider> datagivers = new ArrayList<>();
		try (Connection conn = ds.getConnection()) {
			try (Statement stm = conn.createStatement()) {
				try (ResultSet rs = stm.executeQuery(query)) {

					// Create list over all ID's that have submitted data
					ArrayList<Integer> ID_list = new ArrayList<>();
					while (rs.next()) {
						int id = rs.getInt("DataProviderID");
						if (!ID_list.contains(id)) {
							ID_list.add(id);
						}
					}
					rs.beforeFirst();

					// For each ID, create object and add data
					for (int id : ID_list) {
						DataProvider datagiver = new DataProvider();
						HashMap<Timestamp, Double> blood_data = new HashMap<>();
						datagiver.setId(id);
						while (rs.next()) {
							if (id == rs.getInt("DataProviderID")) {
								blood_data.put(rs.getTimestamp("Timestamp"), rs.getDouble("Level"));
								datagiver.getBlood_data().putAll(blood_data);
								datagiver.setFirstName(rs.getString("FirstName"));
								datagiver.setLastName(rs.getString("LastName"));
							}
						}
						rs.beforeFirst();
						datagivers.add(datagiver);
					}
				}
			}
		}
		return datagivers;
	}
	
	public void saveBloodsugar(int dataproviderID, Map<Timestamp, Double> data) throws SQLException {
		String bloodsugarQuery = QueryImporter.getQuery("insertBloodsugarData.sql");
		try (Connection conn = ds.getConnection()) {
			try (PreparedStatement stm = conn.prepareStatement(bloodsugarQuery)) {
				for (Map.Entry<Timestamp, Double> entry : data.entrySet()) {
					stm.setInt(1, dataproviderID);
					stm.setTimestamp(2, entry.getKey());
					stm.setDouble(3, entry.getValue());
					stm.executeUpdate();
				}
			}
		}
	}

	public DataProvider getDataProvider_Bloodsugar(int dataproviderId) throws SQLException {
		String query = QueryImporter.getQuery("getDatagiverByID_Blood.sql");
		DataProvider datagiver = null;
		HashMap<Timestamp, Double> blood_data = new HashMap<>();
		try (Connection conn = ds.getConnection()){
			try (PreparedStatement stm = conn.prepareStatement(query)) {
				stm.setInt(1, dataproviderId);
				try (ResultSet rs = stm.executeQuery()){								
					while(rs.next()) {
						if(datagiver == null) {
							datagiver = new DataProvider();
							datagiver.setId(dataproviderId);
							datagiver.setFirstName(rs.getString("FirstName"));
							datagiver.setLastName(rs.getString("LastName"));
						}
						blood_data.put(rs.getTimestamp("Timestamp"), rs.getDouble("Level"));
					}
					if (datagiver != null) {
						datagiver.getBlood_data().putAll(blood_data);
					}
				}
			}
		}
		return datagiver;
	}
}
