package tdt4140.gr1811.web.server.protocol;

import java.sql.SQLException;

//Tilpassing
import javax.sql.DataSource;

import com.google.gson.Gson;

import tdt4140.gr1811.app.dao.DataProviderDao;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.db.DataSourceBuilder;
import tdt4140.gr1811.app.pojo.DataProvider;
import tdt4140.gr1811.web.server.json_pojo.client2server.ModifyUser;
import tdt4140.gr1811.web.server.json_pojo.server2client.ModifyUserResponse;

public class ModifyUserProtocol extends Protocol {

	private ModifyUser request;
	private DataSource ds = new DataSourceBuilder(CredentialsFactory.get()).build();

	public ModifyUserProtocol(ModifyUser request) {
		this.request = request;
	}

	@Override
	public String handleRequest() {
		int id = request.getClientId();
		try {
			DataProviderDao dao = new DataProviderDao(ds);
			DataProvider dp = dao.getById(id);
			String email = request.getEmail();
			String phoneNum = request.getPhoneNum();
			Integer height = request.getHeight();
			Integer weight = request.getWeight();
			if (email != null) {
				dp.setEmail(email);
			}
			if (phoneNum != null) {
				dp.setPhoneNum(phoneNum);
			}
			if (height != null) {
				dp.setHeight(height);
			}
			if (weight != null) {
				dp.setWeight(weight);
			}
			dao.saveDataprovider(dp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ModifyUserResponse response = new ModifyUserResponse();
		response.setMessage("Modified user successfully.");
		return new Gson().toJson(response);
	}
}
