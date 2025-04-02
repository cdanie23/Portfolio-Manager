package portfoliomanager.test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class MockServer {

	public static void mockServer(Context context, Socket socket, boolean running) {
		context = ZMQ.context(1);
		socket = context.socket(ZMQ.REP);
		socket.bind("tcp://127.0.0.1:6586");


		try {
			while(running) {
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
				}
				socket.send(jsonResponse.toString().getBytes(ZMQ.CHARSET), 0);
			} 

		} finally {
			socket.close();
			context.term();
		}
	}
}
