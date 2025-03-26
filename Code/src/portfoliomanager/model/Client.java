package portfoliomanager.model;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

/**
 * Sets up client to interact with the server
 * 
 * @author Group 2
 * @version Spring 2025
 */
public class Client extends Thread {
	private String request;
	
	/**
	 * Creates a client object
	 * @param request the request to be made to the server
	 */
	public Client(String request) {
		if (request == null || request.isEmpty() || request.isBlank()) {
			throw new IllegalArgumentException("request must not be null or empty.");
		}
		this.request = request;
	}
	
	/**
	 * Returns the request made to the server
	 * @return the request made to the server
	 */
	public String getRequest() {
		return this.request;
	}
	
	@Override
	public void run() {
        Context context = ZMQ.context(1);

        //  Socket to talk to server
        System.out.println("Connecting to server");
        Socket socket = context.socket(ZMQ.REQ);
        socket.connect("tcp://127.0.0.1:5555");
        
        String jsonRequest = "{\"type\": \"" + this.request + "\"}";   
        System.out.println("Client - Sending" + jsonRequest);
        
        socket.send(jsonRequest.getBytes(ZMQ.CHARSET), 0);
        
        byte[] reply = socket.recv(0);
        String response = new String(reply, ZMQ.CHARSET);
		System.out.println("Client - Received " + response);
		
        socket.close();
        context.term();
	}

}

