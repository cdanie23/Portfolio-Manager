package portfoliomanager.model;

/**
 * Crypto Holdings 
 * @author Colby
 * @version Spring 2025
 */
public class Holding extends Crypto{
	private double amountHeld;
	public Holding(String name, Double currentPrice, double amountHeld) {
		super(name, currentPrice);
		this.amountHeld = amountHeld;
	}
	
	
	public double getTotalPrice() {
		return super.getCurrentPrice() * amountHeld;
	}
	@Override
	public String toString() {
		return String.format("%10s%25s%25s", super.getName(), this.getTotalPrice(), this.amountHeld);
	}
	
	public double getAmountHeld() {
		return this.amountHeld;
	}
	public void setAmountHeld(double amount) {
		this.amountHeld = amount;
	}
	public double getProfit(double amountToSell) {
		return super.getCurrentPrice() * amountToSell;
	}
}
