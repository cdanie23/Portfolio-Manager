package portfoliomanager.model;

/**
 * Crypto Holdings 
 * @author Colby
 * @version Spring 2025
 */
public class Holding extends Crypto {
	private double amountHeld;
	
	/**
	 * Insatantiates a holding object 
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @param name name of the crypto being held
	 * @param currentPrice current Price of the crypto held
	 * @param amountHeld total amount of the crypto held
	 */
	public Holding(String name, Double currentPrice, double amountHeld) {
		super(name, currentPrice);
		this.amountHeld = amountHeld;
	}
	
	/**
	 * Gets the total price of the crypto held
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the total amount of crypto held multiplied with the current price of the crypto
	 */
	public double getTotalPrice() {
		return super.getCurrentPrice() * this.amountHeld;
	}
	
	@Override
	public String toString() {
		return String.format("%10s%25s%25s", super.getName(), this.getTotalPrice(), this.amountHeld);
	}
	
	/**
	 * Gets the amount of the crypto held
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the amount of the crypto held
	 */
	public double getAmountHeld() {
		return this.amountHeld;
	}
	
	/** Sets the amount of the crypto being held
	 * 
	 * @precondition amount != null || amount >= 0
	 * @postcondition this.amount = amount from param
	 * 
	 * @param amount the amount of crypto held to be set for the holding
	 */
	public void setAmountHeld(double amount) {
		this.amountHeld = amount;
	}
	
	/**
	 * Gets the total profit after sell (needs a bought price later).
	 * 
	 * @precondition amountToSell != null or amountToSell <= amountHeld
	 * @postcondition none
	 * 
	 * @param amountToSell amount of crypto to sell
	 * @return the total profit made after the sell.
	 */
	public double getProfit(double amountToSell) {
		return super.getCurrentPrice() * amountToSell;
	}
}
