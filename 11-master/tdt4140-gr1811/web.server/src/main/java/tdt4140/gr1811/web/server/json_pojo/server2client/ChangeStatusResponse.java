package tdt4140.gr1811.web.server.json_pojo.server2client;


public class ChangeStatusResponse extends ServerJson{
	private String message;

	public ChangeStatusResponse() {
		this.msgType = ServerMsgType.CHANGE_STATUS_ACK;
	}

	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

}
