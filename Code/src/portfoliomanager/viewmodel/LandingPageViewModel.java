package portfoliomanager.viewmodel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import portfoliomanager.client.Client;
import portfoliomanager.client.CryptoCurrencies;
import portfoliomanager.client.Requests;
import portfoliomanager.datareader.DataReader;
import portfoliomanager.model.Account;
import portfoliomanager.model.Crypto;
import portfoliomanager.model.Holding;

/**
 * The view model for the landing page
 * 
 * @author Colby
 * @version Spring 2025
 */
public class LandingPageViewModel {
	private DataReader dataReader;
	private ObjectProperty<Account> user;
	private List<Holding> holdings;
	private ListProperty<Holding> holdingsProperty;
	private ListProperty<Crypto> cryptoListProperty;
	private ObjectProperty<Boolean> isLoggedIn;
	private StringProperty welcomeLabelProperty;
	private StringProperty portfolioNameProperty;
	private StringProperty fundsAvailable;
	private Client client;
	
	/**
	 * Instantiates an instance of the view-model
	 * @post this.dataReader != null, this.cryptoObservableList != null
	 */
	public LandingPageViewModel() {
		this.welcomeLabelProperty = new SimpleStringProperty();
		this.welcomeLabelProperty.setValue("Welcome to Crypto Vault");
		this.portfolioNameProperty = new SimpleStringProperty();
		this.isLoggedIn = new SimpleObjectProperty<Boolean>();
		this.isLoggedIn.setValue(false);
		
		this.client = Client.getInstance();
		this.dataReader = null;
		this.cryptoListProperty = null;
		
		this.fundsAvailable = new SimpleStringProperty();
	
		this.holdings = new ArrayList<Holding>(); 

		this.holdingsProperty = new SimpleListProperty<Holding>();
		this.user = new SimpleObjectProperty<Account>();
	}

	/**
	 * Used for testing purposes as to not create a new instance of the client
	 * without setting the appropriate testing port
	 * @param test used to express it is a testing constructor
	 */
	public LandingPageViewModel(String test) {
		this.welcomeLabelProperty = new SimpleStringProperty();
		this.welcomeLabelProperty.setValue("Welcome to Crypto Vault");
		this.portfolioNameProperty = new SimpleStringProperty();
		this.isLoggedIn = new SimpleObjectProperty<Boolean>();
		this.isLoggedIn.setValue(false);
		
		this.dataReader = null;
		this.cryptoListProperty = null;
		this.fundsAvailable = new SimpleStringProperty();
		this.holdings = new ArrayList<Holding>(); 		
		this.holdingsProperty = new SimpleListProperty<Holding>();
		this.holdingsProperty.setValue(FXCollections.observableList(this.holdings));
		this.user = new SimpleObjectProperty<Account>(new Account("testUser", "testPass", "$123"));
		this.fundsAvailable.setValue("$0.0");
	}
	
	/**
	 * Gets the portfolio name property
	 * @return the portfolio name property
	 */
	public StringProperty getPortfolioNameProperty() {
		return this.portfolioNameProperty;
	}
	
	/**
	 * Gets the welcome label property
	 * @return the welcome label property
	 */
	public StringProperty getWelcomeLabelProperty() {
		return this.welcomeLabelProperty;
	}
	
	/**
	 * Gets the funds available 
	 * @return the string property of the funds available
	 */
	public StringProperty getFundsAvailabe() {
		return this.fundsAvailable;
	}
	
	/**
	 * Notifies authentication
	 */
	public void updateLabels() {
		if (this.isLoggedIn.getValue()) {
			this.updateWelcomeLabels();
			this.updateUserProperties();
		}
	}
	
	private void updateUserProperties() {
		
		this.fundsAvailable.setValue(String.format("$%.2f", this.user.getValue().getFundsAvailable()));
		
		this.client.makeGetHoldingRequest(this.user.getValue().getAuth());
		Map<String, Object> response = this.client.getResponse();
		
		List<Map<String, Object>> holdingsList = (List<Map<String, Object>>) response.get("holdings");
		
		List<Holding> holdings = new ArrayList<>();

		for (Map<String, Object> item : holdingsList) {
			String currencyStr = (String) item.get("name");
			CryptoCurrencies name = CryptoCurrencies.valueOf(currencyStr);
		    BigDecimal amountDecimal = (BigDecimal) item.get("amount"); 
		    double amount = amountDecimal.doubleValue();
		    this.client.makeRequest(Requests.btcPrice);
		    Map<String, Object> priceResponse = this.client.getResponse();
		    BigDecimal currPrice = (BigDecimal) priceResponse.get("Price");
		    holdings.add(new Holding(name, currPrice.doubleValue(), amount));
		}
		this.user.getValue().setHoldings(holdings);
		this.holdingsProperty.setValue(FXCollections.observableList(this.user.getValue().getHoldings()));
	}
	
	/**
	 * Updates labels after logging in
	 */
	private void updateWelcomeLabels() {
		if (this.user.getValue() != null) { 
			this.welcomeLabelProperty.setValue("Welcome back, " + this.user.getValue().getUserName());
			this.portfolioNameProperty.setValue(this.user.getValue().getUserName() + "'s Portfolio");
		} else {
			this.welcomeLabelProperty.setValue("Welcome to Crypto Vault "); 
			this.portfolioNameProperty.setValue("'s Portfolio");
		}
	}
	
	/**
	 * Gets the holdings property
	 * @return the holdings property
	 */
	public ListProperty<Holding> getHoldingsProperty() {
		return this.holdingsProperty;
	}
	
	/**
	 * Gets the crypto holding property
	 * @return observable list of crypto holdings
	 */
	
	public List<Holding> getCryptoHoldings() {
		return this.holdings;
	}
	
	/**
	 * Gets the user 
	 * @return the user
	 */
	public ObjectProperty<Account> getUser() {
		return this.user;
	}
	
	/**
	 * Gets the list property for cryptos
	 * @return the list property for cryptos
	 */
	public ListProperty<Crypto> getCryptoListProperty() {
		this.readCryptoList();
		
		return this.cryptoListProperty;
	}
	
	/**
	 * Reads the crypto list.
	 */
	private void readCryptoList() {
		this.dataReader = new DataReader(this.client);
		this.dataReader.readCryptoData();
		this.cryptoListProperty = new SimpleListProperty<Crypto>(FXCollections.observableArrayList(this.dataReader.getCryptoCollection()));
	}
	
	/**
	 * Gets if the user is logged in 
	 * @return if user is logged in 
	 */
	public ObjectProperty<Boolean> getIsLoggedIn() {
		return this.isLoggedIn;
	}
	
	/**
	 * Handles the user logout.
	 *
	 * @return true, if successful
	 */
	public boolean handleLogout() {
		String token = this.user.get().getAuth();
		
		if (token != null && !token.isBlank()) {
			this.client.makeLogoutRequest(Requests.logout, token);
			System.out.println(token);
			this.user.get().setAuth("");
			
			return true;
		}

		return false;
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
	 * Handles whenever a user logs out
	 * @pre must be logged in 
	 * @post this.isLoggedIn.getValue() == false,
	 * 		 this.user.getValue() == null,
	 * 		 the welcome labels are set back to their default value
	 */
	
	public void handleLogOut() {
		this.isLoggedIn.setValue(false);
		this.user.setValue(null);
		this.updateWelcomeLabels();
	}
}
