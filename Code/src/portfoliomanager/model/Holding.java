package portfoliomanager.model;

/**
 * Crypto Holdings 
 * @author Colby
 * @version Spring 2025
 */
public class Holding extends Crypto {
	private double amountHeld;
	/**
	 * Creates an instance of a Holding
	 * @param name the name 
	 * @param currentPrice the current price
	 * @param amountHeld the amount held
	 */
	
	public Holding(String name, Double currentPrice, double amountHeld) {
		super(name, currentPrice);
		this.amountHeld = amountHeld;
	}
	
	/**
	 * Gets the total price 
	 * @return the price of the asset
	 */
	public double getTotalPrice() {
		return super.getCurrentPrice() * this.amountHeld;
	}
	
	@Override
	public String toString() {
		return String.format("%10s%25s%25s", super.getName(), this.getTotalPrice(), this.amountHeld);
	}
	/**
	 * Gets the amount held
	 * @return the amount held
	 */
	
	public double getAmountHeld() {
		return this.amountHeld;
	}
	/**
	 * Sets the ammount held
	 * @param amount the amount to set
	 */
	
	public void setAmountHeld(double amount) {
		this.amountHeld = amount;
	}
	/**
	 * Gets the profift by selling a portion of it 
	 * @param amountToSell the amount you want to sell
	 * @return the total gain from selling
	 */
	
	public double getProfit(double amountToSell) {
		return super.getCurrentPrice() * amountToSell;
	}
}
