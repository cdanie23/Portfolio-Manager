package portfoliomanager.datareader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import portfoliomanager.client.Client;
import portfoliomanager.client.CryptoCurrencies;
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
	private Client client;
	private CryptoCollection cryptos;
	
	/**
	 * Instantiates a Data reader object
	 * @throws FileNotFoundException 
	 * @param client the client that requests the data from the server
	 */
	public DataReader(Client client) {
		this.client = client;
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
//		HashMap<String, BigDecimal> prices = this.readHistoricalPrices();
//		this.client.makeRequest(Requests.btcPrice);
//		Object price = this.client.getResponse().get("Price");
//		Crypto bitcoin = new Crypto(CryptoCurrencies.Bitcoin, Double.parseDouble(price.toString()));
//		this.cryptos.addCrypto(bitcoin);
//		bitcoin.setHistoricalPrices(prices);
		//this.client.makeRequest(Requests.exit); //Shutting server only when required to shut down server.
	
		this.client.makeRequest(Requests.getData);
		HashMap<String, HashMap<String, BigDecimal>> data = (HashMap<String, HashMap<String, BigDecimal>>) this.client.getResponse().get("data");
		String cryptoName = "";
		HashMap<String, BigDecimal> cryptoData = new HashMap<String, BigDecimal>();
		for (HashMap.Entry<String, HashMap<String, BigDecimal>> entry : data.entrySet()) {
			 cryptoName = entry.getKey();
			 cryptoData = entry.getValue();
			 this.client.makeCryptoPriceRequest(Requests.getPrice, cryptoName);
			 BigDecimal currPrice = (BigDecimal) this.client.getResponse().get("price");
			 Crypto crypto = new Crypto(CryptoCurrencies.valueOf(cryptoName), currPrice.doubleValue());
			 this.cryptos.add(crypto);
			 crypto.setHistoricalPrices(cryptoData);
		}
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
