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

public class DataProvider_PulseDao {
	private DataSource ds;

	public DataProvider_PulseDao(DataSource ds) {
		if (ds == null) {
			throw new IllegalArgumentException("Datasource was null");
		}
		this.ds = ds;
	}

	public List<DataProvider> getAllDataProviders_Pulse() throws SQLException {
		String query = QueryImporter.getQuery("getDatagivers_PulseData.sql");
		List<DataProvider> datagivers = new ArrayList<>();
		try (Connection conn = ds.getConnection()) {
			try (Statement stm = conn.createStatement()) {
				try (ResultSet rs = stm.executeQuery(query)) {

					// Create list over all ID's that have submitted data
					ArrayList<Integer> ID_list = new ArrayList<>();
					while (rs.next()) {
						int id = rs.getInt("p.DataproviderID");
						if (!ID_list.contains(id)) {
							ID_list.add(id);
						}
					}
					rs.beforeFirst();

					// For each ID, create object and add data
					for (int id : ID_list) {
						DataProvider datagiver = new DataProvider();
						HashMap<Timestamp, Integer> pulse_data = new HashMap<>();
						datagiver.setId(id);
						while (rs.next()) {
							if (id == rs.getInt("p.DataproviderID")) {
								pulse_data.put(rs.getTimestamp("p.Timestamp"), rs.getInt("BPM"));
								datagiver.getPulse_data().putAll(pulse_data);
								datagiver.setFirstName(rs.getString("d.FirstName"));
								datagiver.setLastName(rs.getString("d.LastName"));
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

	public void savePulse(int datagiverID, Map<Timestamp, Integer> data) throws SQLException {
		String pulseQuery = QueryImporter.getQuery("insertPulseData.sql");
		try (Connection conn = ds.getConnection()) {
			try (PreparedStatement stm = conn.prepareStatement(pulseQuery)) {
				for (Map.Entry<Timestamp, Integer> entry : data.entrySet()) {
					stm.setInt(1, datagiverID);
					stm.setTimestamp(2, entry.getKey());
					stm.setInt(3, entry.getValue());
					stm.executeUpdate();
				}
			}
		}
	}

	public DataProvider getDataProvider_Pulse(int dataproviderId) throws SQLException {
		String query = QueryImporter.getQuery("getDatagiverByID_Pulse.sql");
		DataProvider datagiver = null;
		HashMap<Timestamp, Integer> pulse_data = new HashMap<>();
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
						pulse_data.put(rs.getTimestamp("Timestamp"), rs.getInt("BPM"));
					}
					if (datagiver != null) {
						datagiver.getPulse_data().putAll(pulse_data);
					}
				}
			}
		}
		return datagiver;
	}
}
