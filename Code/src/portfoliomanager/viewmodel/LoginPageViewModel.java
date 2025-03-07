package portfoliomanager.viewmodel;

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
	private boolean loginStatus;
	private StringProperty usernameProperty;
	private StringProperty passwordProperty;
	
	/**
	 * Instantiates a new login page view model.
	 */
	public LoginPageViewModel() {
		if (SignUpPageViewModel.getAccounts().isEmpty()) {
            new SignUpPageViewModel();
        }
		
		this.loginStatus = false;
		this.usernameProperty = new SimpleStringProperty();
		this.passwordProperty = new SimpleStringProperty();
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
	 * Gets the list of accounts.
	 *
	 * @return the list of accounts
	 */
	public List<Account> getAccounts() {
		return SignUpPageViewModel.getAccounts();
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
		String username = this.usernameProperty.get();
		String password = this.passwordProperty.get();
		
		List<Account> accounts = SignUpPageViewModel.getAccounts();
		
		for (Account account : accounts) {
	        if (account.getUserName().trim().equalsIgnoreCase(username.trim()) && account.getPassword().trim().equals(password.trim())) {
	        	this.loginStatus = true;
	        	
	        	return;
	        }
		}
		
		throw new IllegalArgumentException("Username or password are incorrect.");
	}
}
