package tdt4140.gr1811.web.server.json_pojo.server2client;

import tdt4140.gr1811.app.pojo.DataProvider;

public class RequestAllInfoResponse extends ServerJson {

	private String errorMessage;
	private DataProvider dataToClient;

	public RequestAllInfoResponse() {
		this.msgType = ServerMsgType.ALL_INFO;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String message) {
		this.errorMessage = message;
	}

	public DataProvider getData() {
		return dataToClient;
	}

	public void setData(DataProvider dataToClient) {
		this.dataToClient = dataToClient;
	}

}
