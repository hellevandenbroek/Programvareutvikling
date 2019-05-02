package tdt4140.gr1811.web.server.json_pojo.server2client;

public class ErrorResponse extends ServerJson {

	private ErrorType errorType;

	public ErrorResponse(ErrorType errorType) {
		this.msgType = ServerMsgType.ERROR;
		this.errorType = errorType;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

}
