package tdt4140.gr1811.web.server.json_pojo.server2client;

import static org.junit.Assert.*;

import org.junit.Test;

import tdt4140.gr1811.app.pojo.DataProvider;
import tdt4140.gr1811.web.server.json_pojo.server2client.DataDeletionResponse;
import tdt4140.gr1811.web.server.json_pojo.server2client.EchoMeResponse;
import tdt4140.gr1811.web.server.json_pojo.server2client.ErrorResponse;
import tdt4140.gr1811.web.server.json_pojo.server2client.ErrorType;
import tdt4140.gr1811.web.server.json_pojo.server2client.InsertResponse;
import tdt4140.gr1811.web.server.json_pojo.server2client.RequestAllInfoResponse;
import tdt4140.gr1811.web.server.json_pojo.server2client.ServerMsgType;
import tdt4140.gr1811.web.server.json_pojo.server2client.UserDeletionResponse;

public class ServerClientPojoTest {

	@Test
	public void testDataDeletionResponsePojo() {
		String message = "Works fine on my machine!";

		DataDeletionResponse data = new DataDeletionResponse(message);

		assertEquals(message, data.getMessage());
		assertEquals(ServerMsgType.DATA_DELETION_ACK, data.getMsgType());
	}

	@Test
	public void testEchoMeResponsePojo() {
		String message = "Echo... echo... echo...";

		EchoMeResponse data = new EchoMeResponse();
		data.setMessage(message);

		assertEquals(message, data.getMessage());
		assertEquals(ServerMsgType.ECHO_ACK, data.getMsgType());
	}

	@Test
	public void testErrorResponsePojo() {
		ErrorType errorType = ErrorType.BAD_JSON;

		ErrorResponse data = new ErrorResponse(errorType);

		assertEquals(errorType, data.getErrorType());
		assertEquals(ServerMsgType.ERROR, data.getMsgType());
	}

	@Test
	public void testInsertResponsePojo() {
		String message = "Something";

		InsertResponse data = new InsertResponse();
		data.setMessage(message);

		assertEquals(message, data.getMessage());
		assertEquals(ServerMsgType.INSERT_ACK, data.getMsgType());
	}

	@Test
	public void testRequestAllInfoResponsePojo() {
		String message = "Something";
		DataProvider provider = new DataProvider();

		RequestAllInfoResponse data = new RequestAllInfoResponse();
		data.setErrorMessage(message);
		data.setData(provider);

		assertEquals(message, data.getErrorMessage());
		assertEquals(provider, data.getData());
		assertEquals(ServerMsgType.ALL_INFO, data.getMsgType());
	}

	@Test
	public void testUserDeletionResponsePojo() {
		String message = "Something";

		UserDeletionResponse data = new UserDeletionResponse();
		data.setMessage(message);

		assertEquals(message, data.getMessage());
		assertEquals(ServerMsgType.USER_DELETION_ACK, data.getMsgType());
	}
}
