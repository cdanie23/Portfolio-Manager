package portfoliomanager.viewmodel;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
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
	private ListProperty<Holding> holdingsProperty;
	private StringProperty fundsAvailable;
	
	/**
	 * Instantiates a new sell page view model
	 * @param user the user 
	 * @param holdingToSell the selected holding to sell
	 * @param fundsAvailable the funds available to the user
	 * @post  this.amountToSell != null, this.fundsAvailable == fundsAvailable, this.user == user, this.holdingToSell == holdingToSell
	 *        this.holdings == holdings
	 */
	public SellPageViewModel(Account user, Holding holdingToSell, StringProperty fundsAvailable) {
		this.amountToSell = new SimpleStringProperty();
		this.fundsAvailable = fundsAvailable;
		this.user = user;
		this.holdingToSell = holdingToSell;
		this.holdingsProperty = new SimpleListProperty<Holding>(FXCollections.observableArrayList(this.user.getHoldings()));
	}
	/**
	 * Gets the holdings of the user
	 * @return the holdings
	 */
	
	public ListProperty<Holding> getHoldingsProperty() {
		return this.holdingsProperty;
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
		double amountToSell = Double.parseDouble(this.amountToSell.getValue());
		
		return this.holdingToSell.getAmountHeld() - amountToSell;
	}
	
	/**
	 * Gets the profit of selling the holding
	 * @return the profit
	 */
	public double getProfit() {
		double amountToSell = Double.parseDouble(this.amountToSell.getValue());
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
		Holding holding = this.getHoldingToSell();
		holding.setAmountHeld(this.getAmountLeft());
		if (holding.getAmountHeld() == 0.0) {
			this.user.getHoldings().remove(holding);
		}
		double totalFunds = this.user.getFundsAvailable() + this.getProfit();
		this.user.setFundsAvailable(totalFunds);
		this.fundsAvailable.setValue("$" + totalFunds);
	}
	/**
	 * Gets the user
	 * @return the user
	 */
	
	public Account getUser() {
		return this.user;
	}
	/**
	 * Gets the funds available property
	 * @return the funds available
	 */
	
	public StringProperty getAvailableFundsProperty() {
		return this.fundsAvailable;
	}
	
}
