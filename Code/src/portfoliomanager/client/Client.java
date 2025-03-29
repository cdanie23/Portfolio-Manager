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
	private RequestCreator requestCreator;
	private Map<String, String> request;
	private Map<String, Object> response;
	
	private Client() {
		this.request = null;
		this.response = null;
		this.requestCreator = new RequestCreator();
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
        socket.connect("tcp://127.0.0.1:5555");
        
        System.out.println("Client - Sending" + this.request);
        JSONObject request = new JSONObject(this.request);
        socket.send(request.toString());
        if (this.request.get(RequestCreator.TYPE).equals(Requests.exit.toString())) {
        	socket.close();
            context.term();
            System.out.println("Client - Closing due to server exit");
        	return;
        }
        byte[] reply = socket.recv(0);
        String response = new String(reply, ZMQ.CHARSET);
        JSONObject jsonResponse = new JSONObject(response);
        this.response = jsonResponse.toMap();
		System.out.println("Client - Received " + this.response);

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
		return Holder.CLIENT;
	}
	
}

