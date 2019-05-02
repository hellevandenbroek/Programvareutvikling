package tdt4140.gr1811.web.server.protocol;

import com.google.gson.Gson;

import tdt4140.gr1811.web.server.json_pojo.client2server.EchoMe;
import tdt4140.gr1811.web.server.json_pojo.server2client.EchoMeResponse;

public class EchoProtocol extends Protocol {
	
	private EchoMe request;
	
	public EchoProtocol(EchoMe request) {
		this.request = request;
	}

	@Override
	public String handleRequest() {
		EchoMeResponse responseObj = new EchoMeResponse();
		responseObj.setMessage(request.getEchoThis());
		return new Gson().toJson(responseObj);
	}

}
