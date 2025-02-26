package portfoliomanager.model;

import java.util.HashMap;

//import java.util.HashMap;

/**
 * Class that creates a crypto object
 * 
 * @author Group 2
 * @version Spring 2025
 */
public class Crypto {

	private String name;
	private Double currentPrice;
	private HashMap<String,Double> historicalPrices;
	
	/**
	 * Instantiates a new Crypto object
	 * @param name the name of the crypto
	 * @param currentPrice the current price of the crypto
	 */
	public Crypto(String name, Double currentPrice) {
		if (name == null || name.isEmpty() || name.isBlank()) {
			throw new IllegalArgumentException("Name of the cryptocurrency must not be null or empty.");
		}
		if (currentPrice == null) {
			throw new IllegalArgumentException("Current price of the cryptocurrency must not be null or empty.");
		}
		this.name = name;
		this.currentPrice = currentPrice;
		this.historicalPrices = new HashMap<String,Double>();
	}
	
	/** Gets the name of the crypto
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the name of the crypto
	 */
	public String getName() {
		return this.name;
	}
	
	/** Sets the name of crypto
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @param name the name of the crypto to be set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/** Gets the currentPrice of the crypto (in this case today's price)
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return current price of the crypto
	 */
	public Double getCurrentPrice() {
		return this.currentPrice;
	}
	
	/** Sets the current price of the cryto (today's price)
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @param currentPrice the current Price of the crypto to be set
	 */
	public void setCurrentPrice(Double currentPrice) {
		this.currentPrice = currentPrice;
	}
	
	/** Sets the historical prices for the given crypto
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @param historicalPrices
	 */
	public void setHistoricalPrices(HashMap<String,Double> historicalPrices) {
		this.historicalPrices = historicalPrices;
	}
	
	/** Returns the historical Prices of the crypto
	 * 
	 * @precondition none
	 * @postcondition none
	 * @return the historical prices of the cryoto
	 */
	public HashMap<String,Double> getHistoricalPrice() {
		return this.historicalPrices;
	}
}
