package portfoliomanager.viewmodel;

import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import portfoliomanager.model.Account;
import portfoliomanager.model.Holding;

/**
 * The view model for the sell page
 * @author Colby
 * @version Spring 2025
 */
public class SellPageViewModel {
	private StringProperty amountToSell;
	private Account user;
	private Holding holdingToSell;
	private List<Holding> holdings;
	private StringProperty fundsAvailable;
	
	/**
	 * Instantiates a new sell page view model
	 * @param user the user 
	 * @param holdingToSell the selected holding to sell
	 * @param holdings the list of holdings of the user
	 * @param fundsAvailable the funds available to the user
	 * @post  this.amountToSell != null, this.fundsAvailable == fundsAvailable, this.user == user, this.holdingToSell == holdingToSell
	 *        this.holdings == holdings
	 */
	public SellPageViewModel(Account user, Holding holdingToSell, List<Holding> holdings, StringProperty fundsAvailable) {
		this.amountToSell = new SimpleStringProperty();
		this.fundsAvailable = fundsAvailable;
		this.user = user;
		this.holdingToSell = holdingToSell;
		this.holdings = holdings;
		
	}
	/**
	 * Gets the holdings of the user
	 * @return the holdings
	 */
	
	public List<Holding> getHoldings() {
		return this.holdings;
	}
	
	/**
	 * Gets the amount the user wants to sell
	 * @return the amount to sell
	 */
	public StringProperty getAmountToSell() {
		return this.amountToSell;
	}
	/**
	 * Gets the amount left after selling of the holding
	 * @return the amount left
	 */
	
	public double getAmountLeft() {
		double amountToSell = Integer.parseInt(this.amountToSell.getValue());
		
		return this.holdingToSell.getAmountHeld() - amountToSell;
	}
	
	/**
	 * Gets the profit of selling the holding
	 * @return the profit
	 */
	public double getProfit() {
		double amountToSell = Integer.parseInt(this.amountToSell.getValue());
		return this.holdingToSell.getProfit(amountToSell);
	}
	/**
	 * Gets the holding the user wants to sell
	 * @return the holding
	 */
	
	public Holding getHoldingToSell() {
		return this.holdingToSell;
	}
	
	/**
	 * Handles the action of user selling crypto
	 */
	public void sellCrypto() {
		int indexOfHolding = this.holdings.indexOf(this.holdingToSell);
		Holding holding = this.holdings.get(indexOfHolding);
		holding.setAmountHeld(this.getAmountLeft());
		if (holding.getAmountHeld() == 0) {
			this.holdings.remove(holding);
		}
		double totalFunds = this.user.getFundsAvailable() + this.getProfit();
		this.user.setFundsAvailable(totalFunds);
		this.fundsAvailable.setValue("Funds available: $" + totalFunds);
	}
	
}
