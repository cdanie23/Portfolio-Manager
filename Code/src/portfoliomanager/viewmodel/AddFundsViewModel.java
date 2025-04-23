package portfoliomanager.viewmodel;

import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import portfoliomanager.client.Client;
import portfoliomanager.model.Account;

/**
 * Class to Add fund and control the add fund code behind
 * 
 * @author Group 2
 * @version Spring 2025
 */
public class AddFundsViewModel {
	private StringProperty amountProperty;
	private Account user;
	private StringProperty fundsAvailable;
	private Client client;
	
	/**
	 * Instantiates viewmodel object
	 * 
	 * @param user the user of the account
	 * @param fundsAvailable the string property of the funds
	 */
	public AddFundsViewModel(Account user, StringProperty fundsAvailable) {
		this.amountProperty = new SimpleStringProperty();
		this.user = user;
		this.fundsAvailable = fundsAvailable;
		this.client = Client.getInstance();
	}
	/**
	 * Instantiates viewmodel object for testing purposes as to not set the client
	 * 
	 * @param user the user of the account
	 * @param fundsAvailable the string property of the funds
	 * @param test so you know its a test
	 */
	
	public AddFundsViewModel(Account user, StringProperty fundsAvailable, String test) {
		this.amountProperty = new SimpleStringProperty();
		this.user = user;
		this.fundsAvailable = fundsAvailable;
	}
	
	/**
	 * Gets the string Property for the funds to be added
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the string property for the funds to be added
	 */
	public StringProperty getAmountProperty() {
		return this.amountProperty;
	}

	/**
	 * Adds the funds to the user's already available fund
	 * 
	 */
	public void addFunds() {
		if (this.amountProperty.get() == null || this.amountProperty.get().isEmpty()) {
			throw new IllegalArgumentException("Please enter a valid number.");
		}
		double newFunds = Double.valueOf(this.amountProperty.get());
		this.client.makeAddFundsRequest(this.user.getAuth(), newFunds);
		Map<String, Object> response = this.client.getResponse();
		int successCode = (int) response.get("success code");
		
		if (successCode == -1) {
			String errorMsg = (String) response.get("error description");
			throw new UnsupportedOperationException(errorMsg);
		}
		
		newFunds += this.user.getFundsAvailable();
		this.user.setFundsAvailable(newFunds);
		this.fundsAvailable.setValue(String.format("$%.2f", this.user.getFundsAvailable()));
	}
	
	/**
	 * Gets the string property for the funds
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the string property for the funds
	 */
	public StringProperty getFundsAvailableProperty() {
		return this.fundsAvailable;
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
}
