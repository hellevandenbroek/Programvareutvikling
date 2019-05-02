package tdt4140.gr1811.web.server.json_pojo.server2client;

public class CreateUserResponse extends ServerJson {
	private String message;
	
	public CreateUserResponse() {
		this.msgType = ServerMsgType.USER_CREATION_ACK;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
