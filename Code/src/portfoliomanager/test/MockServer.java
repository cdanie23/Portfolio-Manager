package portfoliomanager.test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.zeromq.ZMQ;

public class MockServer {
	private ZMQ.Context context;
	private ZMQ.Socket socket;

	public void mockServer(String bindingPort) {
		this.context = ZMQ.context(1);
		this.socket = this.context.socket(ZMQ.REP);
		this.socket.bind(bindingPort);
		
		while(!Thread.currentThread().isInterrupted()) {
			byte[] request = socket.recv(0);
			if (request == null) {
				break;
			}
			String requestString = new String(request, ZMQ.CHARSET);
			JSONObject jsonRequest = new JSONObject(requestString);
			JSONObject jsonResponse = new JSONObject();
			if (jsonRequest.getString("type").equals("btcPrice")) {
				jsonResponse.put("success code", 1);
				jsonResponse.put("Price", new BigDecimal("91.26"));
			} else if (jsonRequest.getString("type").equals("btcHistory")) {
				Map<String, BigDecimal>history = new HashMap<>();
				history.put("2025-03-21", new BigDecimal("56.5654"));
				history.put("2024-03-22", new BigDecimal("22.65"));
				history.put("2025-03-23", new BigDecimal("156.56"));
				jsonResponse.put("success code", 1);
				jsonResponse.put("History", history);
			} else if (jsonRequest.getString("type").equals("signUp")) {
				jsonResponse.put("success code", 1);
				jsonResponse.put("token", "abs");
			} else if (jsonRequest.getString("type").equals("login")) {
				jsonResponse.put("success code", 1);
				jsonResponse.put("token", "abd");
			} else if (jsonRequest.getString("type").equals("exit")) {
				jsonResponse.put("success code", -1);
				jsonResponse.put("type", "exit");
			} else {
				return;
			}
			this.socket.send(jsonResponse.toString().getBytes(ZMQ.CHARSET), 0);
		}
		this.stopServer();
	}
	
	public void stopServer() {
		System.out.println("Stopping server");
		this.socket.close();
		this.context.close();
	}


}
