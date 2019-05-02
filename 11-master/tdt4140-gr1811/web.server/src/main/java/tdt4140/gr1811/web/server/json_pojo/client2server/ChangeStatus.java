package tdt4140.gr1811.web.server.json_pojo.client2server;

import tdt4140.gr1811.app.pojo.Status;

public class ChangeStatus extends ClientJson {
	
		public Status status;
		
		public ChangeStatus (int datagiverID, Status status) {
			this.msgType = ClientMsgType.STATUS_CHANGE;
			this.clientId = datagiverID;
			this.status = status;
		}
}
