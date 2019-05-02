package tdt4140.gr1811.web.server.protocol;

import com.google.gson.Gson;

import tdt4140.gr1811.web.server.json_pojo.server2client.ErrorResponse;
import tdt4140.gr1811.web.server.json_pojo.server2client.ErrorType;

public class ErrorProtocol extends Protocol {
	
	private ErrorType errorType;
	
	public ErrorProtocol(ErrorType errorType) {
		this.errorType = errorType;
	}

	@Override
	public String handleRequest() {
		ErrorResponse response = new ErrorResponse(this.errorType);
		return new Gson().toJson(response);
	}

}
