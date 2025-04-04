package portfoliomanager.viewmodel;

import java.util.List;
import java.util.Map;
import javafx.beans.property.ObjectProperty;
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
public class LoginPageViewModel {
	private StringProperty usernameProperty;
	private StringProperty passwordProperty;
	private ObjectProperty<Boolean> isLoggedIn;
	private Account user;
	private Client client;
	
	/**
	 * Instantiates a new login page view model.
	 * @param isLoggedIn the login status of the user
	 * @param user that logs in to the system
	 */
	
	public LoginPageViewModel(ObjectProperty<Boolean> isLoggedIn, Account user) {
		if (SignUpPageViewModel.getAccounts().isEmpty()) {
            new SignUpPageViewModel();
        }
		this.user = user;
		this.isLoggedIn = isLoggedIn;
		this.usernameProperty = new SimpleStringProperty();
		this.passwordProperty = new SimpleStringProperty();
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
	public ObjectProperty<Boolean> getLoginStatus() {
		return this.isLoggedIn;
	}

	/**
	 * Gets the user
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the user that logs in to the system
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
	 * Verifies the login information for the account.
	 */
	public void verifyLogin() {
		String username = this.usernameProperty.get();
		String password = this.passwordProperty.get();
		
		List<Account> accounts = SignUpPageViewModel.getAccounts();
		
		for (Account account : accounts) {
	        if (account.getUserName().trim().equalsIgnoreCase(username.trim()) && account.getPassword().trim().equals(password.trim())) {
	        	this.isLoggedIn.setValue(true);
	        	this.user = new Account(username, password);
	        	
	        	this.client.makeAuthRequest(Requests.login, username, password, null);
	        	Map<String, Object> response = this.client.getResponse();
	        	
	        	if (response.get("success code") instanceof Integer && (Integer) response.get("success code") == 1) {
	        		this.client.setToken((String) response.get("token"));
	        	}
	        	
	        	return;
	        }
		}
		
		throw new IllegalArgumentException("Username or password are incorrect.");
	}
}
