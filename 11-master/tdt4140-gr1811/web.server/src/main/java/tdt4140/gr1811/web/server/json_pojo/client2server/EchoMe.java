package tdt4140.gr1811.web.server.json_pojo.client2server;

public class EchoMe extends ClientJson {
	
	private String echoThis;

	public EchoMe(Integer clientId, String message) {
		this.msgType = ClientMsgType.ECHO_ME;
		this.clientId = clientId;
		this.echoThis = message;
	}

	public String getEchoThis() {
		return echoThis;
	}

}
