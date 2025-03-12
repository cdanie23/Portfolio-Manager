package portfoliomanager.viewmodel;

import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
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
	private Account user;
	private List<Holding> holdings;
	private ListProperty<Holding> holdingsProperty;
	private ListProperty<Crypto> cryptoListProperty;
	private ObjectProperty<Boolean> isLoggedIn;
	private StringProperty welcomeLabelProperty;
	private StringProperty portfolioNameProperty;
	private StringProperty fundsAvailable;
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
		this.dataReader = new DataReader(DataReader.FILEPATH);
		this.dataReader.readCryptoData();
		this.cryptoListProperty = new SimpleListProperty<Crypto>(FXCollections.observableArrayList(this.dataReader.getCryptoCollection()));
		this.fundsAvailable = new SimpleStringProperty();
		// Prepopulated for now since we don't have server
		this.user = new Account("user", "pass123");
		Holding userHolding = new Holding("Bitcoin", Double.valueOf(1000), 2);
		this.user.addHolding(userHolding);
		this.holdings = this.user.getHoldings();
		this.fundsAvailable.setValue("$" + this.user.getFundsAvailable());
		this.holdingsProperty = new SimpleListProperty<Holding>(FXCollections.observableArrayList(this.user.getHoldings()));
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
	public void updateForAuthenticatedUser() {
		System.out.println("authenticated called");
		if (this.isLoggedIn.getValue()) {
			this.updateWelcomeLabels();
			System.out.println("reached");
		}
	}
	
	/**
	 * Updates labels after logging in
	 */
	private void updateWelcomeLabels() {
		this.welcomeLabelProperty.setValue("Welcome back," + this.user.getUserName());
		this.portfolioNameProperty.setValue(this.user.getUserName() + "'s Portfolio");
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
	public Account getUser() {
		return this.user;
	}
	
	/**
	 * Gets the list property for cryptos
	 * @return the list property for cryptos
	 */
	public ListProperty<Crypto> getCryptoListProperty() {
		return this.cryptoListProperty;
	}
	/**
	 * Gets if the user is logged in 
	 * @return if user is logged in 
	 */
	
	public ObjectProperty<Boolean> getIsLoggedIn() {
		return this.isLoggedIn;
	}
}
