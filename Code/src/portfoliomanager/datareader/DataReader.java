package portfoliomanager.datareader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import portfoliomanager.client.Client;
import portfoliomanager.client.Requests;
import portfoliomanager.model.Crypto;
import portfoliomanager.model.CryptoCollection;

/**
 * Reads crypto data from the file
 *
 * @author Group 2
 * @version Spring 2025
 */
public class DataReader {
	public static final String FILEPATH = "resources/BTC-USD_data.txt";
	
	private Client client;
	private CryptoCollection cryptos;
	
	/**
	 * Instantiates a Data reader object
	 * @throws FileNotFoundException 
	 */
	public DataReader() {
		this.client = Client.getInstance();
		this.cryptos = new CryptoCollection();
	}

	/**
	 * Reads the data from files
	 * @throws IOException 
	 * 
	 * @precondition File exits
	 * @postcondition none
	 */
	public void readCryptoData() {
			HashMap<String, BigDecimal> prices = this.readHistoricalPrices();
			LocalDate today = LocalDate.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			String formattedDat = today.format(formatter);
			Crypto crypto = new Crypto("BTC-USD", prices.get(formattedDat).doubleValue());
			this.cryptos.addCrypto(crypto);
			crypto.setHistoricalPrices(prices);
		
	}
	
	@SuppressWarnings("unchecked")
	private HashMap<String, BigDecimal> readHistoricalPrices() {
		this.client.makeRequest(Requests.btcHistory);
		//TODO convert this into a hashmap since that is what is returned
		//if (this.client.getResponse().get("History") instanceof HashMap<String, BigDecimal>) {
		//	return (HashMap<String, BigDecimal>) this.client.getResponse().get("History");
		//}
		Object historyData = this.client.getResponse().get("History");

	    if (historyData instanceof Map) {
	        return (HashMap<String, BigDecimal>) historyData;
	    }
	    
	    throw new ClassCastException("History is not a valid HashMap<String, BigDecimal>");
	}
	
	/** Returns the collection of crypto
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return returns the collection of crypto
	 */
	public CryptoCollection getCryptoCollection() {
		return this.cryptos;
	}

}
