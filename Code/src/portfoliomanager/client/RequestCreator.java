package portfoliomanager.client;

import java.util.HashMap;
import java.util.Map;

/**
 * The request creator class
 * @author Colby
 * @version Spring 2025
 */
public class RequestCreator {
	public static final String TYPE = "type";
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String CONFIRM_PASSWORD = "confirmPassword";
	public static final String TOKEN = "token";

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
	
	/**
	 * Creates the auth request.
     *
     * @param requestMade the request to be created
     * @param username the username
     * @param password the password
     * @param confirmPassword the confirm password, if request is signUp
     * 
     * @return the request created
     */
	public Map<String, String> createAuthRequest(Requests requestMade, String username, String password, String confirmPassword) {
        Map<String, String> request = new HashMap<>();
        request.put(TYPE, requestMade.toString());
        request.put(USERNAME, username);
        request.put(PASSWORD, password);
        
        if (confirmPassword != null) {
            request.put(CONFIRM_PASSWORD, confirmPassword);
        }
        
        return request;
    }
	
	/**
	 * Creates the logout request.
	 *
	 * @param requestMade the request to be created
	 * @param token the token to be used
	 * 
	 * @return the request created
	 */
	public Map<String, String> createLogoutRequest(Requests requestMade, String token) {
        Map<String, String> request = new HashMap<>();
        request.put(TYPE, requestMade.toString());
        request.put(TOKEN, token);
        
        return request;
    }
}
