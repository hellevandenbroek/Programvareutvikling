package tdt4140.gr1811.web.server.json_pojo.server2client;

public class UserModificationResponse extends ServerJson{
	private String message;

	public UserModificationResponse() {
		this.msgType = ServerMsgType.USER_MODIFICATION_ACK;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
