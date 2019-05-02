package tdt4140.gr1811.web.server.json_pojo.server2client;

public class UndoDataDeletionResponse extends ServerJson {
	
	private String message;
	
	public UndoDataDeletionResponse() {
		this.msgType = ServerMsgType.UNDO_DATA_DELETION_ACK;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
