package tdt4140.gr1811.web.server.protocol;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.google.gson.Gson;

import tdt4140.gr1811.app.dao.DataProviderDao;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.db.DataSourceBuilder;
import tdt4140.gr1811.web.server.json_pojo.client2server.DeleteData;
import tdt4140.gr1811.web.server.json_pojo.server2client.DataDeletionResponse;


public class DeletionProtocol extends Protocol{

	private DeleteData request;
	
	public DeletionProtocol(DeleteData request) {
		this.request = request;
	}
	
	@Override
	public String handleRequest() {
		
		int id = request.getClientId();
		
		DataSource ds = new DataSourceBuilder(CredentialsFactory.get()).build();
		DataProviderDao dao = new DataProviderDao(ds);
		try {
			dao.deleteData(id);
		} catch (SQLException e) {
			e.printStackTrace();
			// TODO: If something wrong happened, return failure message
		}
		
		DataDeletionResponse responseObj = new DataDeletionResponse("OK");
		return new Gson().toJson(responseObj);
	
	}
}
