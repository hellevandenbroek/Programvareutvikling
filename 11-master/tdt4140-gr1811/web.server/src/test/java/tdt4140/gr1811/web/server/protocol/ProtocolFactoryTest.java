package tdt4140.gr1811.web.server.protocol;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import tdt4140.gr1811.web.server.json_pojo.server2client.ErrorType;

public class ProtocolFactoryTest {

	@Test
	public void badJsonShouldReturnErrorProtocol() {
		String jsonString = "This isn't valid json at all...";

		ProtocolFactory factory = new ProtocolFactory(jsonString);
		Protocol response = factory.get();

		assertTrue(response instanceof ErrorProtocol);
		String errorSerialized = response.handleRequest();
		assertTrue(errorSerialized.toLowerCase().contains(ErrorType.BAD_JSON.name().toLowerCase()));
	}

	@Test
	public void badHeaderTypeShouldReturnErrorProtocol() {
		String jsonString = "{\"msgType\":[]}";

		ProtocolFactory factory = new ProtocolFactory(jsonString);
		Protocol response = factory.get();

		assertTrue(response instanceof ErrorProtocol);
		String errorSerialized = response.handleRequest();
		assertTrue(errorSerialized.toLowerCase().contains(ErrorType.BAD_REQUEST.name().toLowerCase()));
	}

	@Test
	public void missingHeaderShouldReturnErrorProtocol() {
		String jsonString = "{}";

		ProtocolFactory factory = new ProtocolFactory(jsonString);
		Protocol response = factory.get();

		assertTrue(response instanceof ErrorProtocol);
		String errorSerialized = response.handleRequest();
		assertTrue(errorSerialized.toLowerCase().contains(ErrorType.INVALID_HEADER.name().toLowerCase()));
	}

	@Test
	public void unknownHeaderShouldReturnErrorProtocol() {
		String jsonString = "{\"msgType\":\"this_isnt_valid_header\"}";

		ProtocolFactory factory = new ProtocolFactory(jsonString);
		Protocol response = factory.get();

		assertTrue(response instanceof ErrorProtocol);
		String errorSerialized = response.handleRequest();

		assertTrue(errorSerialized.toLowerCase().contains(ErrorType.INVALID_HEADER.name().toLowerCase()));
	}

	@Test
	public void echoMeProtocolFactory() {
		String jsonString = "{\"msgType\":\"ECHO_ME\",\"clientId\":1, \"echoThis\":\"Testmsg\"}";

		ProtocolFactory factory = new ProtocolFactory(jsonString);
		Protocol response = factory.get();

		assertTrue(response instanceof EchoProtocol);
	}

	@Test
	public void requestAllInfoProtocolFactory() {
		String jsonString = "{\"msgType\":\"REQUEST_ALL_INFO\",\"clientId\":1}";

		ProtocolFactory factory = new ProtocolFactory(jsonString);
		Protocol response = factory.get();

		assertTrue(response instanceof RequestAllInfoProtocol);
	}

	@Test
	public void deleteDataProtocolFactory() {
		String jsonString = "{\"msgType\":\"DELETE_DATA\",\"clientId\":1}";

		ProtocolFactory factory = new ProtocolFactory(jsonString);
		Protocol response = factory.get();

		assertTrue(response instanceof DeleteDataProtocol);
	}

	@Test
	public void insertBloodsugarDataProtocolFactory() {
		String jsonString = "{\"msgType\":\"INSERT_BLOODSUGAR_DATA\",\"clientId\":1,"
				+ "\"data\":{\"1990-12-24 05:10:20\":3.6}" + "}";

		ProtocolFactory factory = new ProtocolFactory(jsonString);
		Protocol response = factory.get();

		assertTrue(response instanceof InsertBloodsugarProtocol);
	}

	@Test
	public void insertPulseDataProtocolFactory() {
		String jsonString = "{\"msgType\":\"INSERT_PULSE_DATA\",\"clientId\":1,"
				+ "\"data\":{\"1992-12-10 02:10:20\":3}" + "}";

		ProtocolFactory factory = new ProtocolFactory(jsonString);
		Protocol response = factory.get();

		assertTrue(response instanceof InsertPulseProtocol);
	}

	@Test
	public void deleteUserProtocolFactory() {
		String jsonString = "{\"msgType\":\"DELETE_USER\",\"clientId\":1," + "\"deleteData\":false" + "}";

		ProtocolFactory factory = new ProtocolFactory(jsonString);
		Protocol response = factory.get();

		assertTrue(response instanceof DeleteUserProtocol);
	}

	@Test
	public void createUserProtocolFactory() {
		String jsonString = "{\"msgType\":\"CREATE_USER\"}";

		ProtocolFactory factory = new ProtocolFactory(jsonString);
		Protocol response = factory.get();

		assertTrue(response instanceof CreateUserProtocol);
	}

}
