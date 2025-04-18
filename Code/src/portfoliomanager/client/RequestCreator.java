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

	public static final String NAME = "name";
	public static final String AMOUNT = "amount";

	public static final String AUTH = "token";

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
	public Map<String, String> createAccountRequest(Requests requestMade, String username, String password, String confirmPassword) {
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
	 * @param authtoken the token to be used
	 * 
	 * @return the request created
	 */
	public Map<String, String> createLogoutRequest(Requests requestMade, String authtoken) {
        Map<String, String> request = new HashMap<>();
        request.put(TYPE, requestMade.toString());
        request.put(AUTH, authtoken);
        
        return request;
    }

	/**
	 * Creates a request for a holding of an authorized account
	 * @param requestMade type of request 
	 * @param crypto type of crypto
	 * @param amount the amount of crypto
	 * @param authtoken the authorization token used
	 * @return a request for the client to send to the server
	 */

	public Map<String, String> createHoldingRequest(Requests requestMade, CryptoCurrencies crypto, double amount, String authtoken) {
		Map<String, String> request = new HashMap<>();
		request.put(TYPE, requestMade.toString());
		request.put(NAME, crypto.toString());
		request.put(AMOUNT, String.valueOf(amount));
		request.put(AUTH, authtoken);
		return request;
	}
	
	/**
	 * Creates a request to add funds to an account
	 * @param requestMade the type of request
	 * @param auth the token associated with the account you want to add funds to
	 * @param amount the amount to add
	 * @return the appropriate response for the server
	 */
	public Map<String, String> createAddFundsRequest(Requests requestMade, String auth, double amount) {
		Map<String, String> request = new HashMap<String, String>();
		request.put(TYPE, requestMade.toString());
		request.put(AUTH, auth);
		request.put(AMOUNT, String.valueOf(amount));
		return request;
	}
	
	/**
	 * Creates a request to get a property associated with the account
	 * @param requestMade the type of request to make
	 * @param auth the auth associated with the account
	 * @return the request needed to get a response from the server to return a property associated with the account
	 */
	public Map<String, String> createGetAccountPropertiesRequest(Requests requestMade, String auth) {
		Map<String, String> request = new HashMap<String, String>();
		request.put(TYPE, requestMade.toString());
		request.put(AUTH, auth);
		return request;
	}

	/**
	 * Creates a request to get a property associated with the account
	 * @param requestMade the type of request to make
	 * @return the request needed to get a response from the server to return a property associated with the account
	 */
	public Map<String, String> createPriceRequestByCrypto(Requests requestMade, String cryptoName) {

		System.out.println(requestMade.toString() + cryptoName);
		Map<String, String> request = new HashMap<String, String>();
		request.put(TYPE, requestMade.toString());
		request.put("cryptoName", cryptoName);
		return request;
	}
	
}


