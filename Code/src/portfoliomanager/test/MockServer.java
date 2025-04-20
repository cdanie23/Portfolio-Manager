package portfoliomanager.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.zeromq.ZMQ;

import portfoliomanager.client.CryptoCurrencies;
import portfoliomanager.model.Account;

public class MockServer {
	private ZMQ.Context context;
	private ZMQ.Socket socket;
	public static List<Account> ACCOUNTS = new ArrayList<>(List.of(new Account("user", "pass123", "$123")));

	public void mockServer(String bindingPort) {
		this.context = ZMQ.context(1);
		this.socket = this.context.socket(ZMQ.REP);
		this.socket.bind(bindingPort);

		while (!Thread.currentThread().isInterrupted()) {
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
				Map<String, BigDecimal> history = new HashMap<>();
				history.put("21/03/24", new BigDecimal("56.5654"));
				history.put("22/03/24", new BigDecimal("22.65"));
				history.put("23/03/24", new BigDecimal("156.56"));
				jsonResponse.put("success code", 1);
				jsonResponse.put("History", history);
			} else if (jsonRequest.getString("type").equals("signUp")) {
				jsonResponse.put("success code", 1);
				jsonResponse.put("token", "abs");
			} else if (jsonRequest.getString("type").equals("login")) {
				jsonResponse.put("success code", 1);
				jsonResponse.put("token", "abd");

			} else if (jsonRequest.getString("type").equals("addFunds")) {
				jsonResponse.put("success code", 1);
				jsonResponse.put("token", "$123");
				jsonResponse.put("amount", 10);
			} else if (jsonRequest.getString("type").equals("getFunds")) {
				jsonResponse.put("success code", 1);
				jsonResponse.put("token", "$123");
				jsonResponse.put("amount", 10.00);
			} else if (jsonRequest.getString("type").equals("buyCrypto")) {
				jsonResponse.put("success code", 1);
				jsonResponse.put("auth", "$123");
				jsonResponse.put("amount", new BigDecimal(5.00001));
				jsonResponse.put("funds", new BigDecimal(954.50001));
				jsonResponse.put("name", CryptoCurrencies.Bitcoin);
			} else if (jsonRequest.getString("type").equals("sellCrypto")) {
				jsonResponse.put("success code", 1);
				jsonResponse.put("auth", "$123");
				double amount = Double.parseDouble(jsonRequest.getString("amount"));
				if (amount == 2 ) {
					jsonResponse.put("amount", new BigDecimal(8.00001));
					jsonResponse.put("funds", new BigDecimal(2000.00001));
				} if (amount == 10) {
					jsonResponse.put("amount", new BigDecimal(10.00001));
					jsonResponse.put("funds", new BigDecimal(10000.00001));
				}
				jsonResponse.put("name", CryptoCurrencies.Bitcoin);
			} else if (jsonRequest.getString("type").equals("getHoldings")) {
				jsonResponse.put("success code", 1);
				jsonResponse.put("token", "$123");
				List<Map<String, Object>> holdingDict = new ArrayList<Map<String, Object>>();
				Map<String, Object> holdingsMap = new HashMap<String, Object>();
				holdingsMap.put("amount", new BigDecimal(2.00001));
				holdingsMap.put("name", "Bitcoin");
				holdingDict.add(holdingsMap);
				jsonResponse.put("holdings", holdingDict);
			} else if (jsonRequest.getString("type").equals("getData")) {
				HashMap<String, HashMap<String, BigDecimal>> data = new HashMap<String, HashMap<String, BigDecimal>>();
				HashMap<String, BigDecimal> historicalData = new HashMap<String, BigDecimal>();
				historicalData.put("01/01/25", new BigDecimal(0.59845));
				historicalData.put("02/01/25", new BigDecimal(1.025));
				historicalData.put("03/01/25", new BigDecimal(4.5685));
				data.put("Bitcoin", historicalData);
				jsonResponse.put("success code", 1);
				jsonResponse.put("data", data);
			} else if (jsonRequest.getString("type").equals("getPrice")) {
				jsonResponse.put("success code", 1);
				jsonResponse.put("price", 15.20);
			}
			if (jsonRequest.getString("type").equals("exit")) {
				jsonResponse.put("success code", -1);
				jsonResponse.put("type", "exit");
				this.socket.send(jsonResponse.toString().getBytes(ZMQ.CHARSET), 0);
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
