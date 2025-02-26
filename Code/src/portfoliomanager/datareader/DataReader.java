package portfoliomanager.datareader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import portfoliomanager.model.Crypto;
import portfoliomanager.model.CryptoCollection;

/**
 * Reads crypto data from the file
 *
 * @author Group 2
 * @version Spring 2025
 */
public class DataReader {
	private String filePath;
	private CryptoCollection cryptos;

	/**
	 * Instantiates a Data reader object
	 * @param filePath the path of the file to read
	 * @throws FileNotFoundException 
	 */
	public DataReader(String filePath) {
		if (filePath == null || filePath.isBlank()) {
			throw new IllegalArgumentException("File path must not be null or blank");
		}
		File file = new File(filePath);
		if (!file.exists()) {
			throw new IllegalArgumentException("File doesn't exist");
		}
		this.filePath = filePath;
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
		try {
			HashMap<String, Double> prices = this.readHistoricalPrices();
			var firstEntry = prices.entrySet().iterator().next();
			Crypto crypto = new Crypto("BTC-USD", firstEntry.getValue());
			this.cryptos.addCrypto(crypto);
			crypto.setHistoricalPrices(prices);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	private HashMap<String, Double> readHistoricalPrices() throws IOException {
		LinkedHashMap<String, Double> historicalData = new LinkedHashMap<String, Double>();
		for (String[] row : this.readFile(this.filePath)) {
			if (row.length == 2) {
				String date = row[0].trim();
				double price = Double.parseDouble(row[1].trim());
				historicalData.put(date, price);
			} else {
				throw new IllegalArgumentException("The file doesn't contain enough data.");
			}
		} 
		return historicalData;
	}
	
	private List<String[]> readFile(String filePath) throws IOException {
		List<String[]> rows = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			reader.readLine();
			String line;
			while ((line = reader.readLine()) != null) {
				String[] row = line.split(",", -1);
				rows.add(row);
			}
		}
		return rows;
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
