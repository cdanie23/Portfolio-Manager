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
public class LoginPageViewModel {
	private static List<Account> accounts = new ArrayList<>();
	private boolean loginStatus;
	private StringProperty emailProperty;
	private StringProperty passwordProperty;
	
	/**
	 * Instantiates a new login page view model.
	 */
	public LoginPageViewModel() {
		if (LoginPageViewModel.accounts.isEmpty()) {
			LoginPageViewModel.accounts.add(new Account("user@email.com", "pass123"));
		}
		
		this.loginStatus = false;
		this.emailProperty = new SimpleStringProperty();
		this.passwordProperty = new SimpleStringProperty();
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
	public List<Account> getAccounts() {
		return LoginPageViewModel.accounts;
	}
	
	/**
	 * Gets the status of the login state.
	 *
	 * @return the login status
	 */
	public boolean getLoginStatus() {
		return this.loginStatus;
	}
	
	/**
	 * Verifies the login information for the account.
	 */
	public void verifyLogin() {
		String email = this.emailProperty.get();
		String password = this.passwordProperty.get();
		
		for (Account account : LoginPageViewModel.accounts) {
	        if (!account.getEmail().trim().equalsIgnoreCase(email.trim()) || !account.getPassword().trim().equals(password.trim())) {
	        	throw new IllegalArgumentException("Email or password are incorrect.");
	        }
		}
		
		this.loginStatus = true;
	}
}
