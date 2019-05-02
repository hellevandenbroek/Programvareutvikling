package tdt4140.gr1811.web.server.protocol;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.google.gson.Gson;

import tdt4140.gr1811.app.dao.DataProviderDao;
import tdt4140.gr1811.app.db.Credentials;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.db.DataSourceBuilder;
import tdt4140.gr1811.app.pojo.DataProvider;
import tdt4140.gr1811.web.server.json_pojo.client2server.UndoDeleteData;
import tdt4140.gr1811.web.server.json_pojo.server2client.ServerMsgType;
import tdt4140.gr1811.web.server.json_pojo.server2client.UndoDataDeletionResponse;

public class UndoDataDeletionProtocol extends Protocol {

	
	private UndoDeleteData request;
	private DataSource ds = new DataSourceBuilder(CredentialsFactory.get()).build();
	
	public  UndoDataDeletionProtocol(UndoDeleteData request) {
		this.request = request;
	}
	
	
	@Override
	public String handleRequest() {
		int id = request.getClientId();
		Boolean status = request.getStatus();
		Boolean trouble = false;
	
		DataProviderDao dao = new DataProviderDao(ds);
		try {
			dao.setDeleteState(status, id);
		} catch (SQLException e) {
			e.printStackTrace();
			trouble = true;
		}
		
		UndoDataDeletionResponse responseObj = new UndoDataDeletionResponse();
		if (trouble) {
			responseObj.setMessage("Error: Could not add/undo deleted data");
		} else {
			responseObj.setMessage("OK");
		}
		return new Gson().toJson(responseObj);
	}
	
}
