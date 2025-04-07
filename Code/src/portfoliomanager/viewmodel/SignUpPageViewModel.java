package portfoliomanager.viewmodel;

import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import portfoliomanager.client.Client;
import portfoliomanager.client.Requests;
import portfoliomanager.model.Account;
import portfoliomanager.test.MockServer;

/**
 * The Sign Up Page View Model
 * 
 * @author Sam
 * @version Spring 2025
 */
public class SignUpPageViewModel {
	
	private StringProperty usernameProperty;
	private StringProperty passwordProperty;
	private StringProperty passwordConfirmProperty;
	private ObjectProperty<Boolean> isSignedUp;
	private Client client;
	private ObjectProperty<Account> user;
	/**
	 * Instantiates a new sign up page view model
	 * @post this.user == user, username | password | passwordConfirm | properties are initialized
	 * 		 this.isSignedUp == false, this.Client != null 
	 */
	
	public SignUpPageViewModel() {
		this.isSignedUp = new SimpleObjectProperty<Boolean>(false);
		this.usernameProperty = new SimpleStringProperty();
		this.passwordProperty = new SimpleStringProperty();
		this.passwordConfirmProperty = new SimpleStringProperty();
		this.user = new SimpleObjectProperty<Account>();
		this.client = Client.getInstance();
	}
	/**
	 * A constructor used for testing purposes as to not create a new client 
	 * before setting the appropriate port
	 * @param test used to signify a test constructor
	 */
	
	public SignUpPageViewModel(String test) {
		this.usernameProperty = new SimpleStringProperty();
		this.passwordProperty = new SimpleStringProperty();
		this.passwordConfirmProperty = new SimpleStringProperty();
		this.isSignedUp = new SimpleObjectProperty<Boolean>(false);
		this.user = new SimpleObjectProperty<Account>();
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
	 * Returns the signed up status of the account
	 * @return this.isSignedUp
	 */
	public ObjectProperty<Boolean> getSignedUpStatus() {
		return this.isSignedUp;
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
	 * Adds the created account to the list of accounts.
	 * @post if user is validated then this.user == user
	 */
	public void createAccount() {
		String username = this.usernameProperty.get();
		String password = this.passwordProperty.get();
		String passwordConfirm = this.passwordConfirmProperty.get();
		
		if (!password.trim().equals(passwordConfirm.trim())) {
			throw new IllegalArgumentException("Passwords do not match.");
		}
		this.client.makeAuthRequest(Requests.signUp, username, password, passwordConfirm);
		Map<String, Object> response = this.client.getResponse();
		int successCode = (int) response.get("success code");
		if (successCode == -1) {
			String statusMsg = (String) response.get("error description");
			throw new IllegalArgumentException(statusMsg);
		}
		
		this.isSignedUp.setValue(true);
		String auth = (String) response.get("token");
		Account account = new Account(username, password, auth);
		if (MockServer.ACCOUNTS.contains(account)) {
			throw new IllegalArgumentException("Cannot create a duplicate account");
		}
		MockServer.ACCOUNTS.add(account);
		this.user.setValue(account);
		
	}
	
	/**
	 * Gets the user 
	 * @return the user
	 */
	public ObjectProperty<Account> getUser() {
		return this.user;
	}
}
