package portfoliomanager.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

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
	private HashMap<String, BigDecimal> historicalPrices;
	
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
		this.currentPrice = this.roundToTwoDecimalPlaces(currentPrice);
		this.historicalPrices = new HashMap<String, BigDecimal>();
		
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
		this.currentPrice = this.roundToTwoDecimalPlaces(currentPrice);
	}
	
	private double roundToTwoDecimalPlaces(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	/** Sets the historical prices for the given crypto
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @param historicalPrices the prices of the past
	 */
	public void setHistoricalPrices(HashMap<String, BigDecimal> historicalPrices) {
		this.historicalPrices = historicalPrices;
	}
	
	/** Returns the historical Prices of the crypto
	 * 
	 * @precondition none
	 * @postcondition none
	 * @return the historical prices of the cryoto
	 */
	public HashMap<String, BigDecimal> getHistoricalPrice() {
		return this.historicalPrices;
	}
	
	/**
	 * Returns the historical prices of the crypto for specified days
	 * 
	 * @precondition number of days != null
	 * @postcondition none
	 * 
	 * @param days the days to get the historical prices of the crypto for
	 * @return the crypto Historical price fot the specified days
	 */
	public HashMap<String, Double> getPriceForRange(int days) {
		HashMap<String, Double> rangeHistorics = new LinkedHashMap<String, Double>();
		for (var currdate : this.getDates(days)) {
			if (this.getHistoricalPrice().get(currdate) != null) {
				rangeHistorics.put(currdate, this.getHistoricalPrice().get(currdate).doubleValue());
			}
		}
		return rangeHistorics;
	}
	
	private List<String> getDates(int days) {
		List<String> dateList = new ArrayList<String>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		for (int idx = days; idx > 0; idx--) {
			String date = this.getTodaysDate().minusDays(idx).format(formatter);
			dateList.add(date);
		}
		return dateList;
	}
	
	/**
	 * Checks if the price decreased over one day
	 * 
	 * @return true or false based on the change in value over a day
	 */
	public boolean didOneDayPriceDecrease() {
		return this.getOneDayPriceChange() < 0;
	}
	
	/**
	 * Gets the one day change of price
	 * @return the price change
	 */
	
	public double getOneDayPriceChange() {
		LocalDate today = this.getTodaysDate();
		LocalDate yesterday = today.minusDays(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    String formattedDate = yesterday.format(formatter);
	    BigDecimal yesterdaysPrice = this.historicalPrices.get(formattedDate);
	    if (yesterdaysPrice == null) {
	    	String dayBeforeDate = today.minusDays(2).format(formatter);
	    	yesterdaysPrice = this.historicalPrices.get(dayBeforeDate);
	    }
		return ((this.currentPrice.doubleValue() - yesterdaysPrice.doubleValue()) / yesterdaysPrice.doubleValue()) * 100;
	}
	
	/**
	 * Gets todays date
	 * @return todays date
	 */
	public LocalDate getTodaysDate() {
		return LocalDate.now();
	}
	
	@Override
	public String toString() {
		return String.format("%25s%68.2f%66.2f", this.name, this.currentPrice, this.getOneDayPriceChange()) + "%";
	
	}
}
