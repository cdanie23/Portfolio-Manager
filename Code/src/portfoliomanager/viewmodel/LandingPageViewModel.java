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
import javafx.collections.ObservableList;
import portfoliomanager.client.Client;
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
	private boolean ascendingName = true;
	private boolean ascendingPrice = true;
	private boolean ascendingTrend = true;
	private StringProperty listNameLabel;
    private StringProperty listPriceLabel;
    private StringProperty listTrendLabel;
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

		this.holdingsProperty = new SimpleListProperty<Holding>(FXCollections.observableArrayList());
		
		this.user = new SimpleObjectProperty<Account>();
		
		this.listNameLabel = new SimpleStringProperty("Name");
	    this.listPriceLabel = new SimpleStringProperty("Price");
	    this.listTrendLabel = new SimpleStringProperty("24hr Price Trend");
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
		
		this.listNameLabel = new SimpleStringProperty("Name");
	    this.listPriceLabel = new SimpleStringProperty("Price");
	    this.listTrendLabel = new SimpleStringProperty("24hr Price Trend");
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
	
	/**
	 * Updates user properties after logging in
	 */
	private void updateUserProperties() {
		
		this.fundsAvailable.setValue(String.format("$%.2f", this.user.getValue().getFundsAvailable()));
		
		this.client.makeGetHoldingRequest(this.user.getValue().getAuth());
		Map<String, Object> response = this.client.getResponse();
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> holdingsList = (List<Map<String, Object>>) response.get("holdings");
		
		List<Holding> holdings = new ArrayList<>();

		for (Map<String, Object> item : holdingsList) {
			String currencyStr = (String) item.get("name");
		    BigDecimal amountDecimal = (BigDecimal) item.get("amount"); 
		    double amount = amountDecimal.doubleValue();
		    this.client.makeCryptoPriceRequest(Requests.getPrice, currencyStr);
		    Map<String, Object> priceResponse = this.client.getResponse();
		    BigDecimal currPrice = (BigDecimal) priceResponse.get("price");
		    if (amount > 0.00) {
		    	holdings.add(new Holding(currencyStr, currPrice.doubleValue(), amount));
		    } else {
		    	continue;
		    }
		}
		this.user.getValue().setHoldings(holdings);
		this.holdingsProperty.setAll(FXCollections.observableList(this.user.getValue().getHoldings()));
		
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
	 * Gets the list property for cryptos for testing
	 * 
	 * @return the list property for cryptos
	 */
	public ListProperty<Crypto> getCryptoList() {
		return this.cryptoListProperty;
	}
	
	/**
	 * Reads the crypto list.
	 */
	private void readCryptoList() {
		this.dataReader = new DataReader(this.client);
		this.dataReader.readCryptoData();
		if (this.cryptoListProperty == null) {
			this.cryptoListProperty = new SimpleListProperty<Crypto>(FXCollections.observableArrayList(this.dataReader.getCryptoCollection()));
		}
		this.cryptoListProperty.setAll(this.dataReader.getCryptoCollection());
	}
	
	/**
	 * Starts current crypto price updater
	 * 
	 * @param cryptoListView the list in the UI
	 * @param trendUpdate map with new updates
	 */
	public void updateCryptoPrice(ObservableList<Crypto> cryptoListView, Map<String, BigDecimal> trendUpdate) {
		for (Crypto currCrypto : cryptoListView) {
			if (trendUpdate.containsKey(currCrypto.getName())) {
				currCrypto.setCurrentPrice(trendUpdate.get(currCrypto.getName()).doubleValue());
			}
		}
	}
	
	/**
	 * Gets the name label.
	 *
	 * @return the name label
	 */
	public StringProperty getNameLabel() {
        return this.listNameLabel;
    }

    /**
     * Gets the price label.
     *
     * @return the price label
     */
    public StringProperty getPriceLabel() {
        return this.listPriceLabel;
    }

    /**
     * Gets the trend label.
     *
     * @return the trend label
     */
    public StringProperty getTrendLabel() {
        return this.listTrendLabel;
    }
	
	/**
	 * Sorts the crypto list by name.
	 */
	public void sortByName() {
		if (this.cryptoListProperty != null) {
		    FXCollections.sort(this.cryptoListProperty, (c1, c2) -> {
		        int comparator = c1.getName().compareToIgnoreCase(c2.getName());
		        
		        if (this.ascendingName) {
		            return comparator;
		        } else {
		            return -comparator;
		        }
		    });
		}
	    
	    this.ascendingName = !this.ascendingName;
	    this.updateSortIndicators("name", this.ascendingName);
	}

	/**
	 * Sorts the crypto list by price.
	 */
	public void sortByPrice() {
	    if (this.cryptoListProperty != null) {
	    	FXCollections.sort(this.cryptoListProperty, (c1, c2) -> {
		        int comparator = Double.compare(c1.getCurrentPrice(), c2.getCurrentPrice());
		        
		        if (this.ascendingPrice) {
		            return comparator;
		        } else {
		            return -comparator;
		        }
		    });
	    }
	    
	    this.ascendingPrice = !this.ascendingPrice;
	    this.updateSortIndicators("price", this.ascendingPrice);
	}

	/**
	 * Sorts the crypto list by trend.
	 */
	public void sortByTrend() {
	    if (this.cryptoListProperty != null) {
	    	FXCollections.sort(this.cryptoListProperty, (c1, c2) -> {
		        int comparator = Double.compare(c1.getOneDayPriceChange(), c2.getOneDayPriceChange());
		        
		        if (this.ascendingTrend) {
		            return comparator;
		        } else {
		            return -comparator;
		        }
		    });
	    }
	    
	    this.ascendingTrend = !this.ascendingTrend;
	    this.updateSortIndicators("trend", this.ascendingTrend);
	}
	
	/**
	 * Adds arrow indicator next to the currently sorted label to indicate ascending/descending sort
	 * 
	 * @param label which label to update
	 * @param ascending which sort direction
	 */
	private void updateSortIndicators(String label, boolean ascending) {
	    String upArrow = "\u21A5";
	    // \u21A5 \u21E7
	    String downArrow = "\u21A7";
	    // \u21A7 \u21E9
	    
	    String arrow;
	    if (ascending) {
	        arrow = upArrow;
	    } else {
	        arrow = downArrow;
	    }

	    switch (label) {
	        case "name":
	        	this.listNameLabel.setValue("Name " + arrow);
	        	this.listPriceLabel.setValue("Price");
	    	    this.listTrendLabel.setValue("24hr Price Trend");
	        	break;
	        case "price":
	        	this.listNameLabel.setValue("Name");
	        	this.listPriceLabel.setValue("Price " + arrow);
	        	this.listTrendLabel.setValue("24hr Price Trend");
	        	break;
	        case "trend":
	        	this.listNameLabel.setValue("Name");
	    	    this.listPriceLabel.setValue("Price");
	        	this.listTrendLabel.setValue("24hr Price Trend " + arrow);
	        	break;
	        default:
	        	break;
	    }
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
			this.user.get().setAuth((String) this.client.getResponse().get("token"));
			this.handleLogOut();
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
