package tdt4140.gr1811.web.server.json_pojo.client2server;

public class RequestAllInfo extends ClientJson {

	public RequestAllInfo(Integer clientId) {
		this.msgType = ClientMsgType.REQUEST_ALL_INFO;
		this.clientId = clientId;
	}

}
