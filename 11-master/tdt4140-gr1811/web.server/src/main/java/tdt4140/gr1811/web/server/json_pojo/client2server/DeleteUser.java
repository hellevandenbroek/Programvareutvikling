package tdt4140.gr1811.web.server.json_pojo.client2server;

public class DeleteUser extends ClientJson {

	private boolean deleteData;

	public DeleteUser(Integer clientId, boolean deleteData) {
		this.msgType = ClientMsgType.DELETE_USER;
		this.clientId = clientId;
		this.deleteData = deleteData;
	}

	public boolean isDeleteData() {
		return deleteData;
	}

}
