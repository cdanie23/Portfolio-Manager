package portfoliomanager.model;

import java.util.ArrayList;

/**
 * Creates a collection class of cryptos
 * 
 * @author Group 2
 * @version Spring 2025
 */
public class CryptoCollection {
	private ArrayList<Crypto> cryptos;
	
	/**
	 * Instantiates a new collection of cryptos
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 */
	public CryptoCollection() {
		this.cryptos = new ArrayList<Crypto>();
	}
	
	/** Gets the list of cryptos
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the list of cryptos
	 */
	public ArrayList<Crypto> getCryptos() {
		return this.cryptos;
	}
	
	/** Adds the kind of crypto to the list of cryptos
	 * 
	 * @precondition crypto != null
	 * @postcondition this.cryptos.size() @post = this.cryptos.size() @pre + 1
	 * 
	 * @param crypto the crypto to be added to the collection
	 */
	public void addCrypto(Crypto crypto) {
		if (crypto == null) {
			throw new IllegalArgumentException("Crypto data to be added in the collection must not be null.");
		}
		this.cryptos.add(crypto);
	}
}
