package portfoliomanager.client;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * The request creator class
 * @author Colby
 * @version Spring 2025
 */
public class RequestCreator {
	private static final String TYPE = "type";
	
	/**
	 * Make the request for getting cryptos
	 * @return request
	 */
	public Map<String, String> cryptos() {
		Map<String, String> request = new HashMap<String, String>();
		request.put(TYPE, Requests.cryptos.toString());
		return request;
	}
	
	public Map<String, String> exit() {
		Map<String, String> request = new HashMap<String, String>();
		request.put(TYPE, Requests.exit.toString());
		return request;
	}
	/**
	 * Make the request for getting the current btc price
	 * @return request
	 */
	
	public Map<String, String> btcPrice() {
		Map<String, String> request = new HashMap<String, String>();
		request.put(TYPE, Requests.btcPrice.toString());
		return request;
	}
	/**
	 * Make the request for getting the price history of bitcoin
	 * @return request
	 */
	
	public Map<String, String> btcHistory() {
		Map<String, String> request = new HashMap<String, String>();
		request.put(TYPE, Requests.btcHistory.toString());
		return request;
	}
}
