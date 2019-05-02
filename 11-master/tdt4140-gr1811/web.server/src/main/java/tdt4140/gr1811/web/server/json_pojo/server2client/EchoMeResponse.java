package tdt4140.gr1811.web.server.json_pojo.server2client;

public class EchoMeResponse extends ServerJson {

	private String message;

	public EchoMeResponse() {
		this.msgType = ServerMsgType.ECHO_ACK;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
