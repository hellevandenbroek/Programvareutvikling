package tdt4140.gr1811.web.server.protocol;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.google.gson.Gson;

import tdt4140.gr1811.app.dao.DataProviderDao;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.db.DataSourceBuilder;
import tdt4140.gr1811.app.pojo.Status;
import tdt4140.gr1811.web.server.json_pojo.client2server.ChangeStatus;
import tdt4140.gr1811.web.server.json_pojo.server2client.ChangeStatusResponse;


public class StatusProtocol extends Protocol{

	private ChangeStatus ChangeStatus;
	
	public StatusProtocol(ChangeStatus ChangeStatus) {
		this.ChangeStatus = ChangeStatus;
	}
	@Override
	public String handleRequest() {
		DataSource ds = new DataSourceBuilder(CredentialsFactory.get()).build();
		DataProviderDao dao = new DataProviderDao(ds);
		Status state;
		int datagiverId = ChangeStatus.getClientId();
		Status recievedStatus = ChangeStatus.status;
		ChangeStatusResponse response = new ChangeStatusResponse();
		
		try {
			state = dao.getStatus(datagiverId);
			if((state==Status.INACTIVE && recievedStatus== Status.ACTIVE)&& !(state==Status.ACTIVE && recievedStatus== Status.ACTIVE)) {
					dao.changeStatus(datagiverId, recievedStatus);
					response.setMessage("Success, activated user");
					System.out.println(response);
					return new Gson().toJson(response);					
			}
			else if((state==Status.ACTIVE && recievedStatus== Status.INACTIVE) && !(state==Status.INACTIVE && recievedStatus== Status.INACTIVE)) {
				dao.changeStatus(datagiverId, recievedStatus);
				response.setMessage("Success, user deactivated");
				System.out.println(response);
				return new Gson().toJson(response);
			}
			else if((state==Status.DEACTIVATED && recievedStatus== Status.INACTIVE) || (state==Status.DEACTIVATED && recievedStatus== Status.ACTIVE)) {
				response.setMessage("Bad request");
				System.out.println(response);
				return new Gson().toJson(response);
			}
			else {
				response.setMessage("No need for change");
				System.out.println(response);
				return new Gson().toJson(response);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// return json error TODO
			response.setMessage("Something went wrong");
			return new Gson().toJson(response);
		}
	}
}
