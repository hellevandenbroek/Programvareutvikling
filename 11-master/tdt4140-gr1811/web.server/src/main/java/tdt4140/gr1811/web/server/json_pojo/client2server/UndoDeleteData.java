package tdt4140.gr1811.web.server.json_pojo.client2server;

public class UndoDeleteData extends ClientJson {
	
	private Boolean status; 
	
	public UndoDeleteData(Integer id, Boolean status) {
		this.msgType = ClientMsgType.SET_DELETE_STATE;
		this.clientId = id;
		this.status = status;
	}
	
	public Boolean getStatus() {
		return status;
	}
}
