package portfoliomanager.client;

import org.zeromq.ZMQ;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Handles subscription to real-time trend updates from the server
 * 
 * @author Group 2
 * @version Spring 2025
 */
public class TrendClient {
	private static final String SUBSCRIBE_ENDPOINT = "tcp://127.0.0.1:";
    private ZMQ.Context context;
    private ZMQ.Socket subscriberSocket;
    private Thread subscriberThread;
    private volatile boolean keepListening;
    private String serverPort = "5554";
    
    /**
     * Sets a different serverPort to listen to
     * 
     * @param port to listen/subscribe to
     */
    public void setServerPort(String port) {
    	if (port != null) {
    		this.serverPort = port;
    	}
    }
    
    /**
     *  Starts the trend subscriber thread
     * @param callback a function to handle incoming trend updates
     */
    public void start(Consumer<Map<String, BigDecimal>> callback) {
    	this.context = ZMQ.context(1);
        this.subscriberSocket = this.context.socket(ZMQ.SUB);
        this.subscriberSocket.connect(SUBSCRIBE_ENDPOINT + this.serverPort);
        this.subscriberSocket.subscribe("".getBytes(ZMQ.CHARSET));

        this.keepListening = true;

        this.subscriberThread = new Thread(() -> {
            System.out.println("Trend subscriber started and listening...");
            while (this.keepListening && !Thread.currentThread().isInterrupted()) {
                try {
                    String message = this.subscriberSocket.recvStr(0);
                	JSONObject jsonResponse = new JSONObject(message);
                    JSONArray data = (JSONArray) jsonResponse.get("getData");
                    callback.accept(this.extractCryptoPriceMap(data));
                } catch (Exception caughtException) {
                	System.out.println("The connection between publisher and subscriber has been terminated.");
                }
            }
        });
        this.subscriberThread.setDaemon(true);
        this.subscriberThread.start();
    }
    
    /**
     * Interrupts the thread
     */
    public void stop() {
    	this.keepListening = false;
    	if (this.subscriberThread != null && this.subscriberThread.isAlive()) {
            this.subscriberThread.interrupt();
        }
    	this.close();
    }
    
    private void close() {
        if (this.subscriberSocket != null) {
            this.subscriberSocket.close();
        }
        if (this.context != null) {
            this.context.term();
        }
        System.out.println("TrendSubscriber - Subscription stopped.");
    }
    
    private HashMap<String, BigDecimal> extractCryptoPriceMap(JSONArray data) {
    	HashMap<String, BigDecimal> priceMap = new HashMap<>();

        for (int idx = 0; idx < data.length(); idx++) {
            JSONObject obj = data.getJSONObject(idx);
            String name = obj.getString("name");
            BigDecimal price = new BigDecimal(String.valueOf(obj.get("current_price")));
            priceMap.put(name, price);
        }

        return priceMap;
    }
}
