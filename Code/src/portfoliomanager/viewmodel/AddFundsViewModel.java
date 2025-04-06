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
		double newFunds = Integer.parseInt(this.amountProperty.get());
		this.client.makeAddFundsRequest(this.user.getAuth(), newFunds);
		Map<String, Object> response = this.client.getResponse();
		int successCode = (int) response.get("success code");
		
		if (successCode == -1) {
			String errorMsg = (String) response.get("error description");
			throw new UnsupportedOperationException(errorMsg);
		}
		//TODO logout button does not update user labels
		//TODO buy button does not get enabled whenever you log in 
		newFunds += this.user.getFundsAvailable();
		this.user.setFundsAvailable(newFunds);
		this.fundsAvailable.setValue("$: " + this.user.getFundsAvailable());
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
	
}
