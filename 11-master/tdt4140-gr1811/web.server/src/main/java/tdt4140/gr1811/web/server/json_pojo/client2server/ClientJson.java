package tdt4140.gr1811.web.server.json_pojo.client2server;

public abstract class ClientJson {
	
	protected ClientMsgType msgType;
	protected Integer clientId;

	public ClientMsgType getMessageType() {
		return msgType;
	}

	public Integer getClientId() {
		return clientId;
	}

}
