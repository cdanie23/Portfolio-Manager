package portfoliomanager.viewmodel;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import portfoliomanager.client.Client;
import portfoliomanager.client.Requests;
import portfoliomanager.model.Account;

/**
 * The Sign Up Page View Model
 * 
 * @author Sam
 * @version Spring 2025
 */
public class SignUpPageViewModel {
	private static final List<Account> ACCOUNTS = new ArrayList<>();
	private StringProperty usernameProperty;
	private StringProperty passwordProperty;
	private StringProperty passwordConfirmProperty;
	private boolean isSignedUp;
	private Client client;
	
	/**
	 * Instantiates a new sign up page view model.
	 */
	public SignUpPageViewModel() {
		if (ACCOUNTS.isEmpty()) {
			ACCOUNTS.add(new Account("user", "pass123"));
		}
		
		this.usernameProperty = new SimpleStringProperty();
		this.passwordProperty = new SimpleStringProperty();
		this.passwordConfirmProperty = new SimpleStringProperty();
		this.isSignedUp = false;
		this.client = Client.getInstance();
	}
	
	/**
	 * Gets the user name property.
	 *
	 * @return the user name property
	 */
	public StringProperty getUserNameProperty() {
		return this.usernameProperty;
	}
	
	/**
	 * Gets the passwordProperty 
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the password property
	 */
	public StringProperty getPasswordProperty() {
		return this.passwordProperty;
	}
	
	/**
	 * Gets the passwordConfirmProperty 
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the passwordConfirm property
	 */
	public StringProperty getPasswordConfirmProperty() {
		return this.passwordConfirmProperty;
	}
	
	/**
	 * Gets the list of accounts.
	 *
	 * @return the list of accounts
	 */
	public static List<Account> getAccounts() {
		return ACCOUNTS;
	}
	
	/**
	 * Returns the signed up status of the account
	 * @return this.isSignedUp
	 */
	public boolean getSignedUpStatus() {
		return this.isSignedUp;
	}
	
	/**
	 * Adds the created account to the list of accounts.
	 */
	public void createAccount() {
		String username = this.usernameProperty.get();
		String password = this.passwordProperty.get();
		String passwordConfirm = this.passwordConfirmProperty.get();
		
		if (!password.trim().equals(passwordConfirm.trim())) {
			throw new IllegalArgumentException("Passwords do not match.");
		}
		
		for (Account account : SignUpPageViewModel.ACCOUNTS) {
	        if (account.getUserName().trim().equalsIgnoreCase(username.trim())) {
	            throw new IllegalArgumentException("Account with the given username already exists, try logging in.");
	        }
		}
		
		Account newAccount = new Account(username, password);
		this.isSignedUp = true;
		ACCOUNTS.add(newAccount);
		this.client.makeAuthRequest(Requests.signUp, username, password, passwordConfirm);
		// Temporary exit until further implementation
		this.client.makeRequest(Requests.exit);
	}
}
