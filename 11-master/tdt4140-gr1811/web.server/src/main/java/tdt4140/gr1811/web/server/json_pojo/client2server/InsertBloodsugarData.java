package tdt4140.gr1811.web.server.json_pojo.client2server;

import java.sql.Timestamp;
import java.util.Map;

public class InsertBloodsugarData extends ClientJson {
	private Map<Timestamp, Double> data;
	
	public InsertBloodsugarData(Integer clientId, Map<Timestamp, Double> data) {
		this.msgType = ClientMsgType.INSERT_BLOODSUGAR_DATA;
		this.clientId = clientId;
		this.data = data;
	}

	public Map<Timestamp, Double> getBloodsugarData() {
		return data;
	}

}
