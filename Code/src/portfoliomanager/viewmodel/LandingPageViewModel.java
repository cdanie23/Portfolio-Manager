package portfoliomanager.viewmodel;

import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import portfoliomanager.datareader.DataReader;
import portfoliomanager.model.Account;
import portfoliomanager.model.Crypto;
import portfoliomanager.model.CryptoCollection;
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
	private CryptoCollection cryptos;
	private List<Holding> holdings;
	private ListProperty<Holding> holdingsProperty;
	private StringProperty fundsAvailable;

	/**
	 * Instantiates an instance of the view-model
	 * @post this.dataReader != null, this.cryptoObservableList != null
	 */
	
	public LandingPageViewModel() {
		this.dataReader = new DataReader(DataReader.FILEPATH);
		this.dataReader.readCryptoData();
		this.cryptos = this.dataReader.getCryptoCollection(); 
		this.fundsAvailable = new SimpleStringProperty();
		// Prepopulated for now since we don't have server
		this.user = new Account("johndoe@gmail.com", "password");
		Holding userHolding = new Holding("Bitcoin", Double.valueOf(1000), 2);
		this.user.addHolding(userHolding);
		this.holdings = this.user.getHoldings();
		this.holdingsProperty = new SimpleListProperty<Holding>(FXCollections.observableArrayList(this.holdings));
		
		this.fundsAvailable.setValue("$" + this.user.getFundsAvailable());
		
	}
	/**
	 * Gets the observable list of cryptos
	 * @return observable list of cryptos
	 */
	
	public ObservableList<Crypto> getCryptoCollection() {
		return FXCollections.observableList(this.cryptos);
	}
	/**
	 * Gets the funds available 
	 * @return the string property of the funds available
	 */
	
	public StringProperty getFundsAvailabe() {
		return this.fundsAvailable;
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
	
}
