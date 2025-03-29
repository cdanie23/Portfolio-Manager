package portfoliomanager.client;

import java.util.HashMap;
import java.util.Map;

/**
 * The request creator class
 * @author Colby
 * @version Spring 2025
 */
public class RequestCreator {
	private static final String TYPE = "type";

	/**
	 * Creates a request 
	 * @param requestMade the request to be created
	 * @return the request created
	 */
	public Map<String, String> createRequest(Requests requestMade) {
		Map<String, String> request = new HashMap<String, String>();
		request.put(TYPE, requestMade.toString());
		return request;
	}
}
