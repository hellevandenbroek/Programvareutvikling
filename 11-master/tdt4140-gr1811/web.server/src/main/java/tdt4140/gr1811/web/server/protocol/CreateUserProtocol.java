package tdt4140.gr1811.web.server.protocol;

import java.sql.Date;

import javax.sql.DataSource;
import com.google.gson.Gson;
import tdt4140.gr1811.app.dao.DataProviderDao;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.db.DataSourceBuilder;
import tdt4140.gr1811.app.pojo.Sex;
import tdt4140.gr1811.web.server.json_pojo.client2server.CreateUser;
import tdt4140.gr1811.web.server.json_pojo.server2client.CreateUserResponse;

public class CreateUserProtocol extends Protocol {
	private CreateUser request;
	private String ssn;
	private String firstName;
	private String lastName;
	private Date birthDate;
	private Sex sex;
	private Double weight;
	private Double height;
	private String email;
	private String phoneNum;

	public CreateUserProtocol(CreateUser request) {
		this.request = request;
	}

	@Override
	public String handleRequest() {
		Boolean trouble = false;

		DataSource ds = new DataSourceBuilder(CredentialsFactory.get()).build();
		DataProviderDao dao = new DataProviderDao(ds);
		try {
			this.ssn = request.getSsn();
			this.firstName = request.getFirstName();
			this.lastName = request.getLastName();
			this.birthDate = request.getBirthDate();
			this.sex = request.getSex();
			this.weight = request.getWeight();
			this.height = request.getHeight();
			this.email = request.getEmail();
			this.phoneNum = request.getPhoneNum();

			dao.createDataprovider(ssn, firstName, lastName, birthDate, sex, weight, height, email, phoneNum);
		} catch (Exception e) {
			e.printStackTrace();
			trouble = true;
		}
		CreateUserResponse responseObj = new CreateUserResponse();
		if (trouble) {
			responseObj.setMessage("Error: Could not create user.");
		} else {
			responseObj.setMessage("User successfully added to database.");
		}

		return new Gson().toJson(responseObj);

	}

}
