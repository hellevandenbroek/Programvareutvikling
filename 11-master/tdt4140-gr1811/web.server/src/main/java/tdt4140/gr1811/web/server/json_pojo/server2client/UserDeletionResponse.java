package tdt4140.gr1811.web.server.json_pojo.server2client;

public class UserDeletionResponse extends ServerJson{
	private String message;

	public UserDeletionResponse() {
		this.msgType = ServerMsgType.USER_DELETION_ACK;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
