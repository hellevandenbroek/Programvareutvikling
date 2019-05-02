package tdt4140.gr1811.web.server.protocol;

public abstract class Protocol {

	/**
	 * Handle a request
	 * 
	 * @return {@code null} if no response, json-formatted string otherwise
	 */
	public abstract String handleRequest();

}
