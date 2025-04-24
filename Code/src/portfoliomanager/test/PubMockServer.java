package portfoliomanager.test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.zeromq.ZMQ;


import java.math.BigDecimal;
import java.util.HashMap;

public class PubMockServer {
	private ZMQ.Context context;
	private ZMQ.Socket socket;
	private Thread serverThread;
	
	public void start(String bindingPort) {
		this.context = ZMQ.context(1);
		this.socket = this.context.socket(ZMQ.PUB);
		this.socket.bind(bindingPort);
		this.serverThread = new Thread(() -> {
			while(true) {
				 try {
					Thread.sleep(1000);
					JSONObject jsonResponse = new JSONObject();
					HashMap<String, Object> bitcoin = new HashMap<String, Object>();
					HashMap<String, Object> ethereum = new HashMap<String, Object>();
					JSONArray jsonResponseArray = new JSONArray();
					bitcoin.put("name", "Bitcoin");
					bitcoin.put("current_price", new BigDecimal(5000.00));
					ethereum.put("name", "Ethereum");
					ethereum.put("current_price", new BigDecimal(55.230));
					jsonResponseArray.put(bitcoin);
					jsonResponseArray.put(ethereum);
					jsonResponse.put("type","trend_update");
					jsonResponse.put("getData", jsonResponseArray);
					String message = jsonResponse.toString();
					socket.send(message.getBytes(ZMQ.CHARSET), 0);
				} catch (InterruptedException e) {
					System.out.println(e.getLocalizedMessage());
					break;
				}
			}
		});
		this.serverThread.setDaemon(true);
		this.serverThread.start();
	}
	
	public void stop() {
        if (this.socket != null) {
            this.socket.close();
        }
        if (this.context != null) {
            this.context.term();
        }
    }


}
