package tdt4140.gr1811.web.server.protocol;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import javax.sql.DataSource;

import com.google.gson.Gson;

import tdt4140.gr1811.app.dao.DataProviderDao;
import tdt4140.gr1811.app.dao.DataProvider_PulseDao;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.db.DataSourceBuilder;
import tdt4140.gr1811.app.pojo.Status;
import tdt4140.gr1811.web.server.json_pojo.client2server.InsertPulseData;
import tdt4140.gr1811.web.server.json_pojo.server2client.InsertResponse;

public class InsertPulseProtocol extends Protocol{

	private InsertPulseData insertPulseData;

	public InsertPulseProtocol(InsertPulseData insertData) {
		this.insertPulseData = insertData;
	}

	@Override
	public String handleRequest() {
		DataSource ds = new DataSourceBuilder(CredentialsFactory.get()).build();
		DataProvider_PulseDao dao = new DataProvider_PulseDao(ds);
		DataProviderDao dao2 = new DataProviderDao(ds);
		Integer id = insertPulseData.getClientId();
		Map<Timestamp, Integer> data = insertPulseData.getPulseData();
		try {
			if(Status.INACTIVE==dao2.getStatus(id)) {
				dao2.changeStatus(id, Status.ACTIVE);
				dao.savePulse(id, data);
			}
			if(Status.DEACTIVATED ==dao2.getStatus(id)){
				InsertResponse response = new InsertResponse();
				response.setMessage("Bad Request");
				return new Gson().toJson(response);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO: If something wrong happened, return error
		}
		InsertResponse response = new InsertResponse();
		response.setMessage("Added: " + insertPulseData.toString());
		return new Gson().toJson(response);
	}
}
