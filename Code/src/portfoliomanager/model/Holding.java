package portfoliomanager.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Crypto Holdings 
 * @author Colby
 * @version Spring 2025
 */
public class Holding extends Crypto {
	private double amount;
	/**
	 * Creates an instance of a Holding
	 * @param name the name 
	 * @param currentPrice the current price
	 * @param amount the amount held
	 */
	
	public Holding(String name, Double currentPrice, double amount) {
		super(name, currentPrice);
		this.amount = amount;
	}
	
	/**
	 * Gets the total price 
	 * @return the price of the asset
	 */
	public double getTotalPrice() {
		return super.getCurrentPrice() * this.amount;
	}
	
	@Override
	public String toString() {
		return String.format("%10s%25s%25s", super.getName(), this.getTotalPrice(), this.amount);
	}
	/**
	 * Gets the amount held
	 * @return the amount held
	 */
	
	public double getAmountHeld() {
		return this.amount;
	}
	/**
	 * Sets the ammount held
	 * @param amount the amount to set
	 */
	
	public void setAmountHeld(double amount) {
		this.amount = this.roundToTwoDecimalPlaces(amount);

	}
	
	private double roundToTwoDecimalPlaces(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	/**
	 * Gets the profit by selling a portion of it 
	 * @param amountToSell the amount you want to sell
	 * @return the total gain from selling
	 */
	
	public double getProfit(double amountToSell) {
		return super.getCurrentPrice() * amountToSell;
	}
}
