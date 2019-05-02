package tdt4140.gr1811.web.server.json_pojo.client2server;

public class DeleteData extends ClientJson{
	
	public DeleteData(Integer id) {
		this.msgType = ClientMsgType.DELETE_DATA;
		this.clientId = id;
	}

	
}
