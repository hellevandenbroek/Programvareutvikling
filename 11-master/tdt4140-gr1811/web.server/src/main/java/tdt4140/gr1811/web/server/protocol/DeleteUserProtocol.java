package tdt4140.gr1811.web.server.protocol;

import java.sql.SQLException;
import javax.sql.DataSource;
import com.google.gson.Gson;
import tdt4140.gr1811.app.dao.DataProviderDao;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.db.DataSourceBuilder;
import tdt4140.gr1811.web.server.json_pojo.client2server.DeleteUser;
import tdt4140.gr1811.web.server.json_pojo.server2client.UserDeletionResponse;

public class DeleteUserProtocol extends Protocol {

	private DeleteUser request;

	public DeleteUserProtocol(DeleteUser request) {
		this.request = request;
	}

	@Override
	public String handleRequest() {
		Boolean trouble = false;
		int id = request.getClientId();
		DataSource ds = new DataSourceBuilder(CredentialsFactory.get()).build();
		DataProviderDao dao = new DataProviderDao(ds);
		if (request.isDeleteData()) {
			try {
				dao.deleteData(id);
				dao.deleteUser(id);
			} catch (SQLException e) {
				e.printStackTrace();
				trouble = true;
			}
		} else {
			try {
				dao.deactivateUser(id);
			} catch (SQLException e) {
				e.printStackTrace();
				trouble = true;
			}
		}

		UserDeletionResponse responseObj = new UserDeletionResponse();
		if (trouble) {
			responseObj.setMessage("Error: Could not delete.");
		} else if (trouble && request.isDeleteData()) {
			responseObj.setMessage("Error: Could not deactivate user.");
		} else if (request.isDeleteData()) {
			responseObj.setMessage("Deactivated user with id " + id);
		} else {
			responseObj.setMessage("Deleted user with id " + id);
		}
		return new Gson().toJson(responseObj);

	}

}
