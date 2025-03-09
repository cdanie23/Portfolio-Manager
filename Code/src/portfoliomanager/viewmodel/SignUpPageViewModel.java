package portfoliomanager.viewmodel;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import portfoliomanager.model.Account;

/**
 * The Sign Up Page View Model
 * 
 * @author Sam
 * @version Spring 2025
 */
public class SignUpPageViewModel {
	private static final List<Account> ACCOUNTS = new ArrayList<>();
	private StringProperty emailProperty;
	private StringProperty passwordProperty;
	private boolean isSignedUp;
	
	/**
	 * Instantiates a new sign up page view model.
	 */
	public SignUpPageViewModel() {
		if (ACCOUNTS.isEmpty()) {
			ACCOUNTS.add(new Account("user@email.com", "pass123"));
		}
		this.emailProperty = new SimpleStringProperty();
		this.passwordProperty = new SimpleStringProperty();
		this.isSignedUp = false;
	}
	
	/**
	 * Gets the emailProperty 
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the email property
	 */
	public StringProperty getEmailProperty() {
		return this.emailProperty;
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
		String email = this.emailProperty.get();
		String password = this.passwordProperty.get();
		for (Account account : SignUpPageViewModel.ACCOUNTS) {
	        if (account.getEmail().trim().equalsIgnoreCase(email.trim())) {
	            throw new IllegalArgumentException("Account with the given email already exists, try logging in.");
	        }
		}
		Account newAccount = new Account(email, password);
		this.isSignedUp = true;
		ACCOUNTS.add(newAccount);
	}
}
