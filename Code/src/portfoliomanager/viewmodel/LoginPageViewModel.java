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
	private StringProperty emailProperty;
	private StringProperty passwordProperty;
	
	/**
	 * Instantiates a new login page view model.
	 */
	public LoginPageViewModel() {
		if (SignUpPageViewModel.getAccounts().isEmpty()) {
            new SignUpPageViewModel();
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
		String email = this.emailProperty.get();
		String password = this.passwordProperty.get();
		
		List<Account> accounts = SignUpPageViewModel.getAccounts();
		
		System.out.println("Stored accounts at login:");
        if (accounts.isEmpty()) {
            System.out.println("No accounts found!");
        } else {
            for (Account acc : accounts) {
                System.out.println(acc.getEmail() + " | " + acc.getPassword());
            }
        }
		
		for (Account account : accounts) {
			System.out.println("Checking against account: " + account.getEmail() + " | " + account.getPassword());
	        if (account.getEmail().trim().equalsIgnoreCase(email.trim()) && account.getPassword().trim().equals(password.trim())) {
	        	this.loginStatus = true;
	        	
	        	return;
	        }
		}
		
		throw new IllegalArgumentException("Email or password are incorrect.");
	}
}
