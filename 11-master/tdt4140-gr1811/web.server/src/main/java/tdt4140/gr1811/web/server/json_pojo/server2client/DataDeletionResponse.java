package tdt4140.gr1811.web.server.json_pojo.server2client;

public class DataDeletionResponse extends ServerJson {

	private String message;

	public DataDeletionResponse(String message) {
		this.msgType = ServerMsgType.DATA_DELETION_ACK;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
