package tdt4140.gr1811.web.server.protocol;

import com.google.gson.Gson;

import tdt4140.gr1811.app.dao.DataProviderDao;
import tdt4140.gr1811.app.db.CredentialsFactory;
import tdt4140.gr1811.app.db.DataSourceBuilder;
import tdt4140.gr1811.web.server.json_pojo.client2server.RequestAllInfo;
import tdt4140.gr1811.web.server.json_pojo.server2client.RequestAllInfoResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import javax.sql.DataSource;

public class RequestAllInfoProtocol extends Protocol {

	private RequestAllInfo request;
	
	public RequestAllInfoProtocol(RequestAllInfo request) {
		this.request = request;
	}
	
	@Override
	public String handleRequest() {
		int userID = request.getClientId();
		
		DataSource ds = new DataSourceBuilder(CredentialsFactory.get()).build();
		DataProviderDao dao = new DataProviderDao(ds);
		RequestAllInfoResponse responseObj = new RequestAllInfoResponse();
		try {
			responseObj.setData(dao.getAllDataOfOne(userID));
		} catch (SQLException e) {
			e.printStackTrace();
			// Send the stacktrace to the client
			// TODO: Proper error handling
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			String stacktrace = sw.toString();
			stacktrace = stacktrace.replaceAll("(\\r\\n|\\r|\\n)", "<newline>");
			pw.close();
			responseObj.setErrorMessage(stacktrace);
		}
		return new Gson().toJson(responseObj);
	}

}
