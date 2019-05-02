package tdt4140.gr1811.web.server.protocol;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Map;

import javax.sql.DataSource;

import com.google.gson.Gson;

import tdt4140.gr1811.app.dao.DataProviderDao;
import tdt4140.gr1811.app.dao.DataProvider_BloodsugarDao;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.db.DataSourceBuilder;
import tdt4140.gr1811.app.pojo.Status;
import tdt4140.gr1811.web.server.json_pojo.client2server.InsertBloodsugarData;
import tdt4140.gr1811.web.server.json_pojo.server2client.InsertResponse;

public class InsertBloodsugarProtocol extends Protocol {

	private InsertBloodsugarData insertBloodsugarData;

	public InsertBloodsugarProtocol(InsertBloodsugarData insertData) {
		this.insertBloodsugarData = insertData;
	}

	@Override
	public String handleRequest() {
		DataSource ds = new DataSourceBuilder(CredentialsFactory.get()).build();
		DataProvider_BloodsugarDao dao = new DataProvider_BloodsugarDao(ds);
		DataProviderDao dao2 = new DataProviderDao(ds);
		Integer id = insertBloodsugarData.getClientId();
		Map<Timestamp, Double> data = insertBloodsugarData.getBloodsugarData();
		try {
			if(Status.INACTIVE==dao2.getStatus(id)) {
				dao2.changeStatus(id, Status.ACTIVE);
				dao.saveBloodsugar(id, data);
			}
			if(Status.DEACTIVATED ==dao2.getStatus(id)){
				InsertResponse response = new InsertResponse();
				response.setMessage("Bad Request");
				return new Gson().toJson(response);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO: If something wrong happened, return error message
		}
		InsertResponse response = new InsertResponse();
		response.setMessage("Added: " + insertBloodsugarData.getBloodsugarData());
		return new Gson().toJson(response);
	}

}
