package portfoliomanager.datareader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
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
		this.client.makeRequest(Requests.getData);
		@SuppressWarnings("unchecked")
		HashMap<String, HashMap<String, BigDecimal>> data = (HashMap<String, HashMap<String, BigDecimal>>) this.client.getResponse().get("data");
		String cryptoName = "";
		HashMap<String, BigDecimal> cryptoData = new HashMap<String, BigDecimal>();
		for (HashMap.Entry<String, HashMap<String, BigDecimal>> entry : data.entrySet()) {
			 cryptoName = entry.getKey();
			 cryptoData = entry.getValue();
			 this.client.makeCryptoPriceRequest(Requests.getPrice, cryptoName);
			 BigDecimal currPrice = (BigDecimal) this.client.getResponse().get("price");
			 Crypto crypto = new Crypto(cryptoName, currPrice.doubleValue());
			 this.cryptos.add(crypto);
			 crypto.setHistoricalPrices(cryptoData);
		}
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
