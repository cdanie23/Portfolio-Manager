package portfoliomanager.client;

import java.util.Map;
import org.zeromq.ZMQ;
import org.json.JSONObject;

/**
 * Sets up client to interact with the server
 * 
 * @author Group 2
 * @version Spring 2025
 */
public final class Client extends Thread {
	private static final String PROTOCOL_IP = "tcp://127.0.0.1:";
	private static final String DEFAULT_PORT = "5555";
	private static String serverPort;
	private static ZMQ.Context context;
    private static ZMQ.Socket socket;
	
	private RequestCreator requestCreator;
	private Map<String, String> request;
	private Map<String, Object> response;
	
	private Client() {
		this.request = null;
		this.response = null;
		this.requestCreator = new RequestCreator();
		
		if (serverPort == null) {
			serverPort = DEFAULT_PORT;
		}
		context = ZMQ.context(1);
		socket = context.socket(ZMQ.REQ);
		this.run();
	}
	
	/**
	 * Make a request to the server
	 * @pre request != null
	 * @post this.request != null
	 * @param request the request to make
	 * @throws IllegalArgumentException
	 */
	public void makeRequest(Requests request) {
		if (request == null) {
			throw new IllegalArgumentException("request cannot be null");
		}
		
		this.request = this.requestCreator.createRequest(request);
		this.sendRequest();
	}
	
	/**
	 * Make an auth request to the server.
	 *
	 * @pre request != null
	 * @pre username != null
	 * @pre password != null
	 * @post this.request != null
	 * @param request the request
	 * @param username the username
	 * @param password the password
	 * @param confirmPassword the confirm password, if request is signUp
	 * @throws IllegalArgumentException
	 */
	public void makeAuthRequest(Requests request, String username, String password, String confirmPassword) {
	    if (request == null || username == null || password == null) {
	        throw new IllegalArgumentException("Request, username, and password cannot be null");
	    }

	    this.request = this.requestCreator.createAccountRequest(request, username, password, confirmPassword);
	    this.sendRequest();
	}
	
	/**
	 * Sends a request to the server to get the holding of an account
	 * @param auth the token associated with the account
	 * @post this.request == the request needed to get the holdings from the server
	 */
	
	public void makeGetHoldingRequest(String auth) {
		this.request = this.requestCreator.createGetAccountPropertiesRequest(Requests.getHoldings, auth);
		this.sendRequest();
	}
	
	/**
	 * Sends a request to the server to get the funds of the account
	 * @param auth the auth associated with the account
	 * @post this.request == the request needed to get the funds of the account from the server
	 */
	public void makeGetFundsRequest(String auth) {
		this.request = this.requestCreator.createGetAccountPropertiesRequest(Requests.getFunds, auth);
		this.sendRequest();
	}
	/**
	 * Makes a request to the server to modify a holding
	 * @param crypto the type of crypto the holding is
	 * @param amount the amount of holding to change i.e. remove/add
	 * @param auth the authorization used 
	 * @param totalCost totalCost to be added to the funds of the user if sold
	 * 					totalCost to be subtracted from the funds of the user if bought
	 * @param buyRequest buyRequest if true
	 * 					sellRequest if false
	 * 
	 * 
	 * @post this.request == the appropriate request to send to the server
	 */
	
	public void makeModifyTradeRequest(CryptoCurrencies crypto, double amount, String auth, double totalCost, Boolean buyRequest) {
		if (buyRequest) {
			this.request = this.requestCreator.createHoldingRequest(Requests.buyCrypto, crypto, amount, auth, totalCost);
		} else {
			this.request = this.requestCreator.createHoldingRequest(Requests.sellCrypto, crypto, amount, auth, totalCost);
		}
		this.sendRequest();
	}
	
	/**
	 * Adds funds to the specified users account
	 * @param auth the token corresponding to the account
	 * @param amount the amount to add
	 */
	public void makeAddFundsRequest(String auth, double amount) {
		this.request = this.requestCreator.createAddFundsRequest(Requests.addFunds, auth, amount);
		this.sendRequest();
	}
	
	/**
	 * Make a logout request to the server.
	 *
	 * @pre request != null
	 * @pre token != null
	 * @post this.request != null
	 * @param request the request
	 * @param token the token
	 * @throws IllegalArgumentException
	 */
	public void makeLogoutRequest(Requests request, String token) {
		if (request == null || token == null) {
	        throw new IllegalArgumentException("Request and token cannot be null");
	    }
		
		this.request = this.requestCreator.createLogoutRequest(request, token);
		this.run();
	}
	
	/**
	 * Returns the request made to the server
	 * @return the request made to the server
	 */
	
	public Map<String, String> getRequest() {
		return this.request;
	}
	
	/**
	 * Gets the response received from the server
	 * @return the response received from the server in string form
	 */
	public Map<String, Object> getResponse() {
		return this.response;
	}
	
	@Override
	public void run() {
       System.out.println("Connecting to server");
       socket.connect(PROTOCOL_IP + Integer.parseInt(Client.serverPort)); 
        
	}
	
	/**
	 * Sends a request to the server
	 * @pre this.request != null
	 * @post this.response == the response from the server
	 */
	public void sendRequest() {
		System.out.println("Client - Sending" + this.request);
        JSONObject request = new JSONObject(this.request);
        socket.send(request.toString());
        byte[] reply = socket.recv(0);
        String response = new String(reply, ZMQ.CHARSET);
        System.out.println(response);
        JSONObject jsonResponse = new JSONObject(response);
        this.response = jsonResponse.toMap();
		System.out.println("Client - Received " + this.response);

		if (this.response.containsValue("exit")) {
        	socket.close();
            context.term();
            System.out.println("Client - Closing due to server exit");
        	return;
        }
	}
	
	private static final class Holder {
		private static Client client = new Client();
	}
	
	/**
	 * Gets a singleton instance of the client
	 * @return the client
	 */
	public static Client getInstance() {
		return Client.getInstance(null);
	}
	
	/**
	 * Overloads to connect to a different port (Used for testing)
	 * 
	 * @param customPort client connection port
	 * @return the client
	 */
	public static Client getInstance(String customPort) {
		if (customPort == null || customPort.isEmpty()) {
			Client.serverPort = DEFAULT_PORT;
		} else {
			Client.serverPort = customPort;
		}

		if (Holder.client != null) {
			return Holder.client;
		}
		
		return new Client();
	}
	
	/**
	 * Gets the serverPort
	 * @return the serverPort
	 */
	public String getPort() {
		return Client.serverPort;
	}
	
	/**
	 * Sets the client back to null mainly for testing purposes
	 * @pre Client != null
	 * @post Client == null
	 */
	public void resetClient() {
		Holder.client = null;
	}
	
}