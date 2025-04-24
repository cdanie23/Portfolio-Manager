package portfoliomanager.viewmodel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import portfoliomanager.client.Client;
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
	private Client client;
	
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
		this.client = Client.getInstance();
	}
	
	/**
	 * Instantiates a new sell page view model
	 * @param user the user 
	 * @param holdingToSell the selected holding to sell
	 * @param fundsAvailable the funds available to the user
	 * @param test indicates the instance of object for testing
	 * @post  this.amountToSell != null, this.fundsAvailable == fundsAvailable, this.user == user, this.holdingToSell == holdingToSell
	 *        this.holdings == holdings
	 */
	public SellPageViewModel(Account user, Holding holdingToSell, StringProperty fundsAvailable, String test) {
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
		double profit = this.holdingToSell.getProfit(amountToSell);
	    BigDecimal roundedProfit = new BigDecimal(profit).setScale(2, RoundingMode.HALF_UP);
	    
	    return roundedProfit.doubleValue();
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
		Double totalAmount = Double.parseDouble(this.amountToSell.getValue());
		this.client.makeModifyTradeRequest(holding.getName(), totalAmount, this.user.getAuth(), this.getProfit(), false);
		Map<String, Object> response = this.client.getResponse();
		int successCode = (int) response.get("success code");
		BigDecimal updatedHoldingAmount = new BigDecimal(0);
		BigDecimal updatedUserFunds = new BigDecimal(0);
		if (successCode == -1) {
			String errorMsg = (String) response.get("error description");
			throw new UnsupportedOperationException(errorMsg);
		} else {
			if (response.get("amount") instanceof Integer) {
				updatedHoldingAmount = BigDecimal.valueOf((Integer) response.get("amount"));
			} else {
			updatedHoldingAmount = (BigDecimal) response.get("amount");
			}
			updatedUserFunds = (BigDecimal) response.get("funds");
		}
		holding.setAmountHeld(updatedHoldingAmount.doubleValue());
		if (holding.getAmountHeld() == 0.00) {
			this.user.getHoldings().remove(holding);
			this.holdingsProperty.setValue(FXCollections.observableArrayList(this.user.getHoldings()));
		}
		this.user.setFundsAvailable(updatedUserFunds.doubleValue());
		this.fundsAvailable.setValue("$" + this.user.getFundsAvailable());
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
	/**
	 * Sets the client for a specific port
	 * 
	 * @param serverPort port to be changed to 
	 * Primarily used for testing
	 */
	
	public void setClient(String serverPort) {
		if (serverPort != null) {
			this.client = Client.getInstance(serverPort);
		}
	}
	/**
	 * Gets the client
	 * @return the client
	 */
	
	public Client getClient() {
		return this.client;
	}
	/**
	 * Sets the user
	 * @param user the user to set
	 * @post this.user == user
	 */
	
	public void setUser(Account user) {
		this.user = user;
	}
	
}
