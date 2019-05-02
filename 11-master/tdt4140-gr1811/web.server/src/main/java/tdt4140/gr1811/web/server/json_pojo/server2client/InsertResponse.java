package tdt4140.gr1811.web.server.json_pojo.server2client;

public class InsertResponse extends ServerJson{

	private String message;

	public InsertResponse() {
		this.msgType = ServerMsgType.INSERT_ACK;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
