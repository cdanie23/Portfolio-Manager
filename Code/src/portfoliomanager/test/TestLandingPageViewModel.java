package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import portfoliomanager.client.Client;
import portfoliomanager.client.Requests;
import portfoliomanager.model.Account;
import portfoliomanager.model.Crypto;
import portfoliomanager.viewmodel.LandingPageViewModel;

@TestInstance(Lifecycle.PER_CLASS)
class TestLandingPageViewModel {
	private static final String PROTOCOL_IP = "tcp://127.0.0.1:";
	LandingPageViewModel viewModel;
	private static Thread serverThread;
	private static MockServer mockServer;
	private static String port;
	private static Client client;
	
	@BeforeAll 
	static void startServer() {
		try {
			mockServer = new MockServer();
			port = "5565";
			serverThread = new Thread(() -> mockServer.mockServer(PROTOCOL_IP + port));
			serverThread.start();
		} catch (Exception e){
			System.out.println("Address is in use, but test cases continue");
		}
	}
	
	@BeforeEach
	public void setUp() {
		this.viewModel = new LandingPageViewModel("test");
		this.viewModel.setClient(port);
		client = this.viewModel.getClient();
	}
	
	@AfterAll
	static void interruptServer() {
		client.makeRequest(Requests.exit);
		client.resetClient();
	}
	
	@Test
	void testValidConstructor() {
		LandingPageViewModel viewModel = new LandingPageViewModel();
		assertEquals(viewModel.getUser().getValue(), null);
		assertFalse(viewModel.getIsLoggedIn().getValue());
	}
	
	@Test
	void testGetCryptoCollection() {
		assertTrue(!this.viewModel.getCryptoListProperty().getValue().isEmpty());
	}
	
	@Test
	void testGetFundsAvailabe() {
		String expected = "$0.0";
		String actual = this.viewModel.getFundsAvailabe().getValue();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetCryptoHoldings() {
		assertTrue(this.viewModel.getCryptoHoldings().isEmpty());
	}
	
	@Test
	void testGetHoldingsProperty() {
		assertTrue(this.viewModel.getHoldingsProperty().getValue().isEmpty());
	}
	@Test
	void testGetUser() {
		Account expectedUser = this.viewModel.getUser().getValue();
		
		assertEquals(expectedUser, this.viewModel.getUser().getValue());
	}
	@Test
	void testUpdateForAuthenticatedUser() {
		this.viewModel.getIsLoggedIn().setValue(true);
		this.viewModel.updateLabels();
		
		String welcomeText = this.viewModel.getWelcomeLabelProperty().getValue();
		assertEquals(welcomeText, "Welcome back, "+ this.viewModel.getUser().getValue().getUserName());
	}
	
	@Test
	void testUpdateForNonAuthenticatedUser() {
		this.viewModel.getIsLoggedIn().setValue(false);
		this.viewModel.updateLabels();
		
		assertEquals(this.viewModel.getWelcomeLabelProperty().get(), "Welcome to Crypto Vault");
	}
	
	@Test
	void testPortfolioLabelProperties() {
		this.viewModel.getIsLoggedIn().setValue(true);
		this.viewModel.updateLabels();
		String expectedPortfolioLabel = "testUser's Portfolio";
		String actual = this.viewModel.getPortfolioNameProperty().getValue();
		
		assertEquals(expectedPortfolioLabel, actual);
	}
	
	@SuppressWarnings("serial")
	@Test
	void testSortingWithClient() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
	    String yesterday = LocalDate.now().minusDays(1).format(formatter);
		Crypto btc = new Crypto("Bitcoin", 70000.00);
		btc.setHistoricalPrices(new HashMap<>() {{
			put(yesterday, BigDecimal.valueOf(69000.00));
		}});
		Crypto sol = new Crypto("Solana", 130.00);
		sol.setHistoricalPrices(new HashMap<>() {{
			put(yesterday, BigDecimal.valueOf(131.00));
		}});
		Crypto xrp = new Crypto("XRP", 2.00);
		xrp.setHistoricalPrices(new HashMap<>() {{
			put(yesterday, BigDecimal.valueOf(2.00));
		}});
		
		ObservableList<Crypto> testList = FXCollections.observableArrayList(btc, sol, xrp);	
		this.viewModel.getCryptoListProperty().setAll(testList);
		
		assertEquals("Name", this.viewModel.getNameLabel().getValue());
		this.viewModel.sortByName();
		assertEquals("Name ↧", this.viewModel.getNameLabel().getValue());
		assertEquals("Bitcoin", this.viewModel.getCryptoList().get(0).getName().toString());
		this.viewModel.sortByName();
		assertEquals("Name ↥", this.viewModel.getNameLabel().getValue());
		assertEquals("XRP", this.viewModel.getCryptoList().get(0).getName().toString());
		
		assertEquals("Price", this.viewModel.getPriceLabel().getValue());
		this.viewModel.sortByPrice();
		assertEquals("Price ↧", this.viewModel.getPriceLabel().getValue());
		assertEquals("XRP", this.viewModel.getCryptoList().get(0).getName().toString());
		this.viewModel.sortByPrice();
		assertEquals("Price ↥", this.viewModel.getPriceLabel().getValue());
		assertEquals("Bitcoin", this.viewModel.getCryptoList().get(0).getName().toString());
		
		assertEquals("24hr Price Trend", this.viewModel.getTrendLabel().getValue());
		this.viewModel.sortByTrend();
		assertEquals("24hr Price Trend ↧", this.viewModel.getTrendLabel().getValue());
		this.viewModel.sortByTrend();
		assertEquals("24hr Price Trend ↥", this.viewModel.getTrendLabel().getValue());
	}
	
	@Test
	void testHandleUserLogout() {
		Account user = this.viewModel.getUser().get();
		user.setAuth("token");
		this.viewModel.getIsLoggedIn().setValue(true);
		this.viewModel.updateLabels();
		
		String welcomeText = this.viewModel.getWelcomeLabelProperty().getValue();
		assertEquals(welcomeText, "Welcome back, "+ this.viewModel.getUser().getValue().getUserName());
		
		boolean result = this.viewModel.handleLogout();

		assertTrue(result);
	}
	
	@Test
	void testUpdateCryptoPrice() {
		ObservableList<Crypto> cryptoList = this.setUpList();
		Map<String, BigDecimal> trendUpdate = new HashMap<String, BigDecimal>();
		trendUpdate.put("Bitcoin", new BigDecimal(5000.00));
		trendUpdate.put("Ethereum", new BigDecimal(55.230));
		this.viewModel.updateCryptoPrice(cryptoList, trendUpdate);

		assertAll(()-> assertEquals(3, cryptoList.size()),
				()-> assertEquals(5000.00, cryptoList.get(0).getCurrentPrice()),
				()-> assertEquals(55.230, cryptoList.get(1).getCurrentPrice()),
				()-> assertEquals(15.56, cryptoList.get(2).getCurrentPrice()));
	}
	
	private ObservableList<Crypto> setUpList() {
		ObservableList<Crypto> cryptoList= FXCollections.observableArrayList();
		Crypto btc = new Crypto("Bitcoin", 7555.00);
		LocalDate today= LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
		String todaysDate = today.format(formatter);
		String yesterdayDate = today.minusDays(1).format(formatter);
		HashMap<String, BigDecimal> btcHist = new HashMap<String, BigDecimal>();
		btcHist.put(todaysDate, new BigDecimal(7555.00));
		btcHist.put(yesterdayDate, new BigDecimal(8890));
		btc.setHistoricalPrices(btcHist);
		Crypto eth = new Crypto("Ethereum", 15.561);
		HashMap<String, BigDecimal> ethHist = new HashMap<String, BigDecimal>();
		ethHist.put(todaysDate, new BigDecimal(55.300));
		ethHist.put(yesterdayDate, new BigDecimal(89.550));
		eth.setHistoricalPrices(ethHist);
		cryptoList.add(btc);
		cryptoList.add(eth);
		Crypto th = new Crypto("Tether", 15.561);
		HashMap<String, BigDecimal> thHist = new HashMap<String, BigDecimal>();
		thHist.put(todaysDate, new BigDecimal(55.300));
		thHist.put(yesterdayDate, new BigDecimal(89.550));
		th.setHistoricalPrices(ethHist);
		cryptoList.add(th);
		return cryptoList;
	}
}
