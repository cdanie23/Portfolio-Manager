package portfoliomanager.viewmodel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import portfoliomanager.client.Client;
import portfoliomanager.model.Account;
import portfoliomanager.model.Crypto;
import portfoliomanager.model.Holding;

/**
 * ViewModel class for buy crypto code behind
 * 
 * @author Group 2
 * @version Spring 2025
 */
public class BuyCryptoViewModel {
	private Account user;
	private StringProperty amountProperty;
	private ObjectProperty<Crypto> selectedCrypto;
	private StringProperty cryptoDetailsProperty;
	private ListProperty<Holding> holdingsProperty;
	private StringProperty fundsAvailableProperty;
	private Series<String, Double> lineChartSeriesProperty;
	private Client client;
	
	/**
	 * Instantiates a new buy crypto view model class
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @param user the user who wants to buy
	 * @param holdingsProperty observable list of holdings
	 * @param fundsAvailable string property for funds
	 */
	public BuyCryptoViewModel(Account user, ListProperty<Holding> holdingsProperty, StringProperty fundsAvailable) {
		this.user = user;
		this.amountProperty = new SimpleStringProperty();
		this.selectedCrypto = new SimpleObjectProperty<Crypto>();
		this.cryptoDetailsProperty = new SimpleStringProperty();
		this.holdingsProperty = holdingsProperty;
		this.fundsAvailableProperty = fundsAvailable;
		this.lineChartSeriesProperty = new Series<>();
		this.client = Client.getInstance();
	}
	
	/**
	 * Instantiates a new buy crypto view model class for testing that doesn't set the client
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @param user the user who wants to buy
	 * @param holdingsProperty observable list of holdings
	 * @param fundsAvailable string property for funds
	 * @param test so you know its a test
	 */
	
	public BuyCryptoViewModel(Account user, ListProperty<Holding> holdingsProperty, StringProperty fundsAvailable, String test) {
		this.user = user;
		this.amountProperty = new SimpleStringProperty();
		this.selectedCrypto = new SimpleObjectProperty<Crypto>();
		this.cryptoDetailsProperty = new SimpleStringProperty();
		this.holdingsProperty = holdingsProperty;
		this.fundsAvailableProperty = fundsAvailable;
		this.lineChartSeriesProperty = new Series<>();
	}
	
	/**
	 * Gets the amountProperty
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the amount Property
	 */
	
	public StringProperty getAmountProperty() {
		return this.amountProperty;
	}
	
	/**
	 * Gets the user
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the user
	 */
	public Account getUser() {
		return this.user;
	}
	
	/**
	 * Gets the selected crypto
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the selected crypto
	 */
	public ObjectProperty<Crypto> getSelectedCrypto() {
		return this.selectedCrypto;
	}
	
	/**
	 * Gets the crypto details Property
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the crypto details Property
	 */
	public StringProperty getCryptoDetailsProperty() {
		String details = this.selectedCrypto.getValue().getName() + ": $" + this.selectedCrypto.getValue().getCurrentPrice();
		this.cryptoDetailsProperty.set(details);
		return this.cryptoDetailsProperty;
	}
	
	/**
	 * Gets the list propety for holdings
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the observablelist of the holdings
	 */
	public ListProperty<Holding> getHoldingsProperty() {
		return this.holdingsProperty;
	}
	
	/**
	 * Gets the fundAvailableProperty
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the fundsAvailableProperty
	 */
	public StringProperty getFundsAvailableProperty() {
		return this.fundsAvailableProperty;
	}

	/**
	 * Gets the line chart series property
	 * 
	 * @return the line chart series property
	 */
	public Series<String, Double> getLineChartSeriesProperty() {
		return this.lineChartSeriesProperty;
	}
	
	/**
	 * Buy Crypto
	 * 
	 * @precondition amountToBuy != null or amountToBuy != ""
	 * @postcondition holding @post = holding @pre + 1
	 * 
	 */
	public void buyCrypto() {
		if (this.amountProperty.get() == null || this.amountProperty.get().isEmpty()) {
			throw new IllegalArgumentException("Please enter a valid amount of crypto to buy.");
		}
		double amountToBuy = Double.parseDouble(this.amountProperty.get());
		Crypto crypto = this.selectedCrypto.getValue();
		double totalCost = amountToBuy * crypto.getCurrentPrice();
		if (totalCost > this.user.getFundsAvailable()) {
			throw new IllegalArgumentException("You do not have enough funds in your account.");
		}
		Holding holding = new Holding(crypto.getName(), crypto.getCurrentPrice(), amountToBuy);
		this.client.makeModifyTradeRequest(crypto.getName(), amountToBuy, this.user.getAuth(), totalCost, true);
		Map<String, Object> response = this.client.getResponse();
		BigDecimal updatedHoldingAmount = new BigDecimal(0.0);
		BigDecimal updatedUserFunds = new BigDecimal(0.0);
		int successCode = (int) response.get("success code");
		if (successCode == -1) {
			String errorMsg = (String) response.get("error description");
			throw new UnsupportedOperationException(errorMsg);
		} else {
			updatedHoldingAmount = (BigDecimal) response.get("amount");
			updatedUserFunds = (BigDecimal) response.get("funds");
		}
		holding.setAmountHeld(updatedHoldingAmount.doubleValue());
		this.user.addHolding(holding);
		this.holdingsProperty.get().setAll(FXCollections.observableArrayList(this.user.getHoldings()));
		this.user.setFundsAvailable(updatedUserFunds.doubleValue());
		this.fundsAvailableProperty.setValue(String.format("$%.2f", this.user.getFundsAvailable()));
	}
	
	/**
	 * Updates the line Chart
	 * 
	 * @param response the range of days to get the data for
	 */
	public void updateLineChart(String response) {
		if (response == null) {
			throw new IllegalArgumentException("The range to get the price of crypto must not be null");
		}
		this.lineChartSeriesProperty.getData().clear();
		String[] parts = response.split(" ", -1);
		int days = Integer.parseInt(parts[0]);
		var sortedEntries = this.convertToAscendingOrder(days);
    	for (Map.Entry<String, Double> entry : sortedEntries) {
    		String date = entry.getKey();
    		Double price = entry.getValue();
    		var data = new XYChart.Data<>(date, price);
    		this.lineChartTypeConversion(data);
    		this.lineChartSeriesProperty.getData().add(data);
    	}
	}

	private List<Map.Entry<String, Double>> convertToAscendingOrder(int days) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
		if (this.selectedCrypto.get() == null) {
			throw new NullPointerException("Please select a crypto from the list of cryptos below to see it's price trends");
		}

		List<Map.Entry<String, Double>> sortedEntries = this.selectedCrypto.get().getPriceForRange(days)
		    .entrySet()
		    .stream()
		    .sorted(Comparator.comparing(entry -> LocalDate.parse(entry.getKey(), formatter)))
		    .collect(Collectors.toList());
		
		return sortedEntries;
	}

	private void lineChartTypeConversion(Data<String, Double> data) {
		data.setNode(null);
		Line line = new Line();
		line.setStrokeWidth(0.1);
		line.setStroke(Color.RED);
		data.setNode(line);
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
}



