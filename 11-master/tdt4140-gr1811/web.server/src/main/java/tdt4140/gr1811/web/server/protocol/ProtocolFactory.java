package tdt4140.gr1811.web.server.protocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import tdt4140.gr1811.web.server.json_pojo.client2server.ChangeStatus;
import tdt4140.gr1811.web.server.json_pojo.client2server.ClientMsgType;
import tdt4140.gr1811.web.server.json_pojo.client2server.CreateUser;
import tdt4140.gr1811.web.server.json_pojo.client2server.DeleteData;
import tdt4140.gr1811.web.server.json_pojo.client2server.DeleteUser;
import tdt4140.gr1811.web.server.json_pojo.client2server.EchoMe;
import tdt4140.gr1811.web.server.json_pojo.client2server.HeaderOnly;
import tdt4140.gr1811.web.server.json_pojo.client2server.InsertBloodsugarData;
import tdt4140.gr1811.web.server.json_pojo.client2server.InsertPulseData;
import tdt4140.gr1811.web.server.json_pojo.client2server.ModifyUser;
import tdt4140.gr1811.web.server.json_pojo.client2server.RequestAllInfo;
import tdt4140.gr1811.web.server.json_pojo.client2server.UndoDeleteData;
import tdt4140.gr1811.web.server.json_pojo.server2client.ErrorType;

public class ProtocolFactory {

	private String clientMessage;

	public ProtocolFactory(String message) {
		this.clientMessage = message;
	}

	public Protocol get() {
		// First, verify message
		JsonElement el;
		try {
			el = new JsonParser().parse(clientMessage);
		} catch (JsonSyntaxException e) {
			// bad syntax, return error
			return new ErrorProtocol(ErrorType.BAD_JSON);
		}

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

		ClientMsgType type = null;

		try {
			type = gson.fromJson(el, HeaderOnly.class).getMessageType();
		} catch (JsonSyntaxException e) {
			return new ErrorProtocol(ErrorType.BAD_REQUEST);
		}

		if (type == null) {
			return new ErrorProtocol(ErrorType.INVALID_HEADER);
		}

		switch (type) {
		case STATUS_CHANGE:
			try {
				ChangeStatus jsonObj = gson.fromJson(el, ChangeStatus.class);
				return new StatusProtocol(jsonObj);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				return new ErrorProtocol(ErrorType.BAD_REQUEST);
			}
		case ECHO_ME:
			try {
				EchoMe jsonObj = gson.fromJson(el, EchoMe.class);
				return new EchoProtocol(jsonObj);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				return new ErrorProtocol(ErrorType.BAD_REQUEST);
			}
		case INSERT_BLOODSUGAR_DATA:
			try {
				InsertBloodsugarData jsonObj = gson.fromJson(el, InsertBloodsugarData.class);
				return new InsertBloodsugarProtocol(jsonObj);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				return new ErrorProtocol(ErrorType.BAD_REQUEST);
			}
		case INSERT_PULSE_DATA:
			try {
				InsertPulseData jsonObj = gson.fromJson(el, InsertPulseData.class);
				return new InsertPulseProtocol(jsonObj);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				return new ErrorProtocol(ErrorType.BAD_REQUEST);
			}
		case DELETE_DATA:
			try {
				DeleteData jsonObj = gson.fromJson(el, DeleteData.class);
				return new DeleteDataProtocol(jsonObj);
			} catch (JsonSyntaxException e) {
				return new ErrorProtocol(ErrorType.BAD_REQUEST);
			}
		case REQUEST_ALL_INFO:
			try {
				RequestAllInfo jsonObj = gson.fromJson(el, RequestAllInfo.class);
				return new RequestAllInfoProtocol(jsonObj);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				return new ErrorProtocol(ErrorType.BAD_REQUEST);
			}
		case DELETE_USER:
			try {
				DeleteUser jsonObj = gson.fromJson(el, DeleteUser.class);
				return new DeleteUserProtocol(jsonObj);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				return new ErrorProtocol(ErrorType.BAD_REQUEST);
			}
		case CREATE_USER:
			try {
				CreateUser jsonObj = gson.fromJson(el, CreateUser.class);
				return new CreateUserProtocol(jsonObj);
			} catch (Exception e) {
				e.printStackTrace();
				return new ErrorProtocol(ErrorType.BAD_REQUEST);
			}
		case SET_DELETE_STATE:
			try {
				UndoDeleteData jsonObj = gson.fromJson(el, UndoDeleteData.class);
				return new UndoDataDeletionProtocol(jsonObj);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				return new ErrorProtocol(ErrorType.BAD_REQUEST);
			}
			
		case MODIFY_USER:
			try {
				ModifyUser jsonObj = gson.fromJson(el, ModifyUser.class);
				return new ModifyUserProtocol(jsonObj);
			} catch (Exception e) {
				e.printStackTrace();
				return new ErrorProtocol(ErrorType.BAD_REQUEST);
			}
			
			
			
		default:
			return new ErrorProtocol(ErrorType.INVALID_HEADER);
		}
	}
}
