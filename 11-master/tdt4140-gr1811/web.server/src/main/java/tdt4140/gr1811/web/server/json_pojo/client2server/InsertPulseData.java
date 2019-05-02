package tdt4140.gr1811.web.server.json_pojo.client2server;

import java.sql.Timestamp;
import java.util.Map;

public class InsertPulseData extends ClientJson {
	private Map<Timestamp, Integer> data;

	public InsertPulseData(Integer clientId, Map<Timestamp, Integer> data) {
		this.msgType = ClientMsgType.INSERT_PULSE_DATA;
		this.clientId = clientId;
		this.data = data;
	}

	public Map<Timestamp, Integer> getPulseData() {
		return data;
	}

}
