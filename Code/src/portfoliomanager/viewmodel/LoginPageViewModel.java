package portfoliomanager.viewmodel;

<<<<<<< HEAD
import java.math.BigDecimal;
=======
>>>>>>> main
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
public class LoginPageViewModel {
	private StringProperty usernameProperty;
	private StringProperty passwordProperty;
	private ObjectProperty<Boolean> isLoggedIn;
	private ObjectProperty<Account> user;
	private Client client;
	
	/**
	 * Instantiates a new login page view model.
	 */

	public LoginPageViewModel(ObjectProperty<Boolean> isLoggedIn, ObjectProperty<Account> user) {
		this.user = user;
		this.isLoggedIn = isLoggedIn;
		this.usernameProperty = new SimpleStringProperty();
		this.passwordProperty = new SimpleStringProperty();
		this.client = Client.getInstance();
	}

	/**
	 * Used for testing purposes
	 * @param test used to express this is a testing constructor 
	 */
	public LoginPageViewModel(ObjectProperty<Boolean> isLoggedIn, ObjectProperty<Account> user, String test) {
		this.user = user;
		this.isLoggedIn = isLoggedIn;
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
	public ObjectProperty<Account> getUser() {
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

	/**
	 * Verifies the login information for the account.
	 */
	public void verifyLogin() {
		String username = this.usernameProperty.get().trim();
		String password = this.passwordProperty.get().trim();
		
		boolean userInSystem = false;
		for (Account user : MockServer.ACCOUNTS) {
			if (user.getUserName().equals(username) && user.getPassword().equals(password)) {
				userInSystem = true;
				break;
			}
		}
		if (!userInSystem) {
			throw new IllegalArgumentException("credentials do not match anything in our system");
		}
		
		this.client.makeAuthRequest(Requests.login, username, password, password);
		Map<String, Object> response = this.client.getResponse();
		int successCode = (int) response.get("success code");
		String authToken = (String) response.get("token");
		if (successCode == 1) {
			this.client.makeGetFundsRequest(authToken);
			response = this.client.getResponse();
			var fundsAvailable = 0.00;
			if (response.get("amount") instanceof BigDecimal) {
				 fundsAvailable = ((BigDecimal) response.get("amount")).doubleValue();
			} else {
				fundsAvailable = (Integer) response.get("amount");
			}
			
			this.user.setValue(new Account(username, password, authToken, fundsAvailable));
			this.isLoggedIn.setValue(true);
			return;
		} 
		throw new IllegalArgumentException("Username or password are incorrect.");
	}
}