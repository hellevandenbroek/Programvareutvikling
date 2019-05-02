package tdt4140.gr1811.web.server.json_pojo.client2server;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import tdt4140.gr1811.web.server.json_pojo.client2server.ClientMsgType;
import tdt4140.gr1811.web.server.json_pojo.client2server.DeleteData;
import tdt4140.gr1811.web.server.json_pojo.client2server.DeleteUser;
import tdt4140.gr1811.web.server.json_pojo.client2server.EchoMe;
import tdt4140.gr1811.web.server.json_pojo.client2server.HeaderOnly;
import tdt4140.gr1811.web.server.json_pojo.client2server.InsertBloodsugarData;
import tdt4140.gr1811.web.server.json_pojo.client2server.InsertPulseData;
import tdt4140.gr1811.web.server.json_pojo.client2server.RequestAllInfo;

public class ClientServerPojoTest {

	@Test
	public void testDeleteDataPojo() {
		Integer clientId = 5;

		DeleteData data = new DeleteData(clientId);

		assertEquals(clientId, data.getClientId());
		assertEquals(ClientMsgType.DELETE_DATA, data.getMessageType());
	}

	@Test
	public void testDeleteUserPojo() {
		Integer clientId = 5;

		DeleteUser data = new DeleteUser(clientId, false);

		assertEquals(clientId, data.getClientId());
		assertEquals(ClientMsgType.DELETE_USER, data.getMessageType());
	}
	
	@Test
	public void testEchoMePojo() {
		Integer clientId = 5;
		String message = "Hello world!";

		EchoMe data = new EchoMe(clientId, message);

		assertEquals(clientId, data.getClientId());
		assertEquals(message, data.getEchoThis());
		assertEquals(ClientMsgType.ECHO_ME, data.getMessageType());
	}

	@Test
	public void testHeaderOnlyPojo() {
		HeaderOnly data = new HeaderOnly();
		assertNull(data.getClientId());
		assertNull(data.getMessageType());
	}

	@Test
	public void testInsertBloodsugarDataPojo() {
		Integer clientId = 5;
		Map<Timestamp, Double> bloodsugarData = new HashMap<>();

		InsertBloodsugarData data = new InsertBloodsugarData(clientId, bloodsugarData);

		assertEquals(clientId, data.getClientId());
		assertEquals(bloodsugarData, data.getBloodsugarData());
		assertEquals(ClientMsgType.INSERT_BLOODSUGAR_DATA, data.getMessageType());
	}

	@Test
	public void testInsertPulseDataPojo() {
		Integer clientId = 5;
		Map<Timestamp, Integer> pulseData = new HashMap<>();

		InsertPulseData data = new InsertPulseData(clientId, pulseData);

		assertEquals(clientId, data.getClientId());
		assertEquals(pulseData, data.getPulseData());
		assertEquals(ClientMsgType.INSERT_PULSE_DATA, data.getMessageType());
	}

	@Test
	public void testRequestAllInfoPojo() {
		Integer clientId = 5;

		RequestAllInfo data = new RequestAllInfo(clientId);

		assertEquals(clientId, data.getClientId());
		assertEquals(ClientMsgType.REQUEST_ALL_INFO, data.getMessageType());
	}
	
}
