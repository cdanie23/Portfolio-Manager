package portfoliomanager.client;

import java.util.Map;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;
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
		this.run();
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

	    this.request = this.requestCreator.createAuthRequest(request, username, password, confirmPassword);
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
        Context context = ZMQ.context(1);

        System.out.println("Connecting to server");
        Socket socket = context.socket(ZMQ.REQ);
        socket.connect(PROTOCOL_IP + Integer.parseInt(Client.serverPort));
        
        System.out.println("Client - Sending" + this.request);
        JSONObject request = new JSONObject(this.request);
        socket.send(request.toString());
        
        byte[] reply = socket.recv(0);
        String response = new String(reply, ZMQ.CHARSET);
        System.out.println(response);
        JSONObject jsonResponse = new JSONObject(response);
        this.response = jsonResponse.toMap();
		System.out.println("Client - Received " + this.response);

		if (this.response.containsValue("Exit request received.")) {
        	socket.close();
            context.term();
            System.out.println("Client - Closing due to server exit");
        	return;
        }
        socket.close();
        context.term();
	}
	
	private static final class Holder {
		static final Client CLIENT = new Client();
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
		return Holder.CLIENT;
		
	}
	
	/**
	 * Gets the serverPort
	 * @return the serverPort
	 */
	public String getPort() {
		return Client.serverPort;
	}
	
}

