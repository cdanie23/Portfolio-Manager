package portfoliomanager.viewmodel;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import portfoliomanager.model.Account;
import portfoliomanager.model.Crypto;
import portfoliomanager.model.Holding;

/**
 * ViewModel class for buy crypto code behind
 * 
 * @author Group 2
 * @version Spring 2025
 */
public class BuyCryptoViewModel {
	private Account user;
	private StringProperty amountProperty;
	private ObjectProperty<Crypto> selectedCrypto;
	private StringProperty cryptoDetailsProperty;
	private ListProperty<Holding> holdingsProperty;
	private StringProperty fundsAvailableProperty;
	
	/**
	 * Instantiates a new buy crypto view model class
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @param user the user who wants to buy
	 * @param selectedCrypto crypto to be bought
	 * @param holdingsProperty observable list of holdings
	 * @param fundsAvailable string property for funds
	 */
	public BuyCryptoViewModel(Account user, ObjectProperty<Crypto> selectedCrypto, ListProperty<Holding> holdingsProperty, StringProperty fundsAvailable) {
		this.user = user;
		this.amountProperty = new SimpleStringProperty();
		this.selectedCrypto = selectedCrypto;
		this.cryptoDetailsProperty = new SimpleStringProperty();
		this.holdingsProperty = holdingsProperty;
		this.fundsAvailableProperty = fundsAvailable;
	}
	
	/**
	 * Gets the amountProperty
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the amount Property
	 */
	public StringProperty getAmountProperty() {
		return this.amountProperty;
	}
	
	/**
	 * Gets the user
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the user
	 */
	public Account getUser() {
		return this.user;
	}
	
	/**
	 * Gets the selected crypto
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the selected crypto
	 */
	public ObjectProperty<Crypto> getSelectedCryto() {
		return this.selectedCrypto;
	}
	
	/**
	 * Gets the crypto details Property
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the crypto details Property
	 */
	public StringProperty getCryptoDetailsProperty() {
		String details = this.selectedCrypto.getValue().getName() + ": $" + this.selectedCrypto.getValue().getCurrentPrice();
		this.cryptoDetailsProperty.set(details);
		return this.cryptoDetailsProperty;
	}
	
	/**
	 * Gets the list propety for holdings
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the observablelist of the holdings
	 */
	public ListProperty<Holding> getHoldingsProperty() {
		return this.holdingsProperty;
	}
	
	/**
	 * Gets the fundAvailableProperty
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the fundsAvailableProperty
	 */
	public StringProperty getFundsAvailableProperty() {
		return this.fundsAvailableProperty;
	}
	
	/**
	 * Buy Crypto
	 * 
	 * @precondition amountToBuy != null or amountToBuy != ""
	 * @postcondition holding @post = holding @pre + 1
	 * 
	 */
	public void buyCrypto() {
		if (this.amountProperty.get() == null || this.amountProperty.get().isEmpty()) {
			throw new IllegalArgumentException("Please enter a valid amount of crypto to buy.");
		}
		int amountToBuy = Integer.parseInt(this.amountProperty.get());
		Crypto crypto = this.selectedCrypto.getValue();
		double totalCost = amountToBuy * crypto.getCurrentPrice();
		if (totalCost >= this.user.getFundsAvailable()) {
			throw new IllegalArgumentException("You do not have enough funds in your account.");
		}
		Holding holding = new Holding(crypto.getName(), crypto.getCurrentPrice(), amountToBuy);
		this.user.getHoldings().add(holding);
		this.holdingsProperty.set(FXCollections.observableArrayList(this.user.getHoldings()));
		this.user.setFundsAvailable(this.user.getFundsAvailable() - totalCost);
		this.fundsAvailableProperty.setValue("Funds Available $: " + this.user.getFundsAvailable());
	}
}
