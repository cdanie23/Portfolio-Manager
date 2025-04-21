package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleStringProperty;

import portfoliomanager.client.Client;

import portfoliomanager.model.Account;
import portfoliomanager.model.Holding;
import portfoliomanager.viewmodel.SellPageViewModel;

class TestSellPageViewModel {
	SellPageViewModel viewModel;
	private static final String PROTOCOL_IP = "tcp://127.0.0.1:";
	private static Thread serverThread;
	private static MockServer mockServer;
	private static String port;
	private static Client client;
	
	@BeforeAll
	static void startServer() {
		try {
			mockServer = new MockServer();
			port = "5563";
			serverThread = new Thread(() -> mockServer.mockServer(PROTOCOL_IP + port));
			serverThread.start();
		} catch (Exception e){
			System.out.println("Address is in use, but test cases continue");
		}
	}
	@AfterAll
	static void interruptServer() {
		client.resetClient();
		serverThread.interrupt();
	}
	
	@BeforeEach 
	void setUp() {
		Account user = new Account("colby", "password", "$123");
		Holding holdingToSell = new Holding("Bitcoin", Double.valueOf(1000), 10);
		List<Holding> holdings = new ArrayList<Holding>();
		holdings.add(holdingToSell);
		
		SimpleStringProperty fundsAvailable = new SimpleStringProperty();
		fundsAvailable.setValue("$" + user.getFundsAvailable());
		
		this.viewModel = new SellPageViewModel(user, holdingToSell, fundsAvailable, "test");
		BigDecimal amountToSell = new BigDecimal(2);
		this.viewModel.getAmountToSell().setValue(String.valueOf(amountToSell));
		this.viewModel.setClient(port);
		client = this.viewModel.getClient();
	}
	
	@Test
	void testConstructor() {
		Account user = new Account("colby2", "password", "$123");
	    Holding holdingToSell = new Holding("Bitcoin", Double.valueOf(1000), 10);
	    List<Holding> holdings = new ArrayList<Holding>();
		holdings.add(holdingToSell);
		
		SimpleStringProperty fundsAvailable = new SimpleStringProperty();
		fundsAvailable.setValue("$" + user.getFundsAvailable());

	    this.viewModel = new SellPageViewModel(user, holdingToSell, fundsAvailable);
	    BigDecimal amountToSell = new BigDecimal(2);
		this.viewModel.getAmountToSell().setValue(String.valueOf(amountToSell));
		this.viewModel.setClient(port);
		client = this.viewModel.getClient();
	    
	    assertNotNull(this.viewModel.getAmountToSell());
	    assertEquals(fundsAvailable, this.viewModel.getAvailableFundsProperty());
	    assertEquals(user, this.viewModel.getUser());
	    assertEquals(holdingToSell, this.viewModel.getHoldingToSell());
	    assertNotNull(this.viewModel.getClient());
	}

	@Test
	void testGetHoldings() {
		assertTrue(this.viewModel.getHoldingsProperty().get().isEmpty());
	}
	
	@Test
	void testGetAmountToSell() {
		double amountToSell = 2;
		
		assertEquals(amountToSell, Double.parseDouble(this.viewModel.getAmountToSell().getValue()));
	}
	@Test
	void testGetAmountLeft() {	
		double amountLeft = 8;
		assertEquals(amountLeft, this.viewModel.getAmountLeft());
	}
	@Test
	void testGetProfit() {
		double profit = 2 * 1000;
		
		assertEquals(profit, this.viewModel.getProfit());
	}
	@Test
	void testGetHoldingToSell() {
		assertEquals(10, this.viewModel.getHoldingToSell().getAmountHeld());
		assertEquals(1000, this.viewModel.getHoldingToSell().getCurrentPrice());
		
	}
	@Test
	void testSellPartialAmountOfCrypto() {
		this.viewModel.sellCrypto();
		
		assertEquals(8.0, this.viewModel.getHoldingToSell().getAmountHeld());
		assertEquals(2000, this.viewModel.getUser().getFundsAvailable());
		String expectedFundsAvailableDisplayed = "$" + 2000.0;
		
		assertTrue(expectedFundsAvailableDisplayed.equals(this.viewModel.getAvailableFundsProperty().getValue()));
	}
	@Test
	void testSellingFullAmountHeld()  {
		this.viewModel.getAmountToSell().setValue(String.valueOf(10));
		this.viewModel.sellCrypto();
		
		assertTrue(this.viewModel.getUser().getHoldings().isEmpty());
		assertEquals(10000, this.viewModel.getUser().getFundsAvailable());
		String expectedFundsAvailableDisplayed = "$" + 10000.0;
		assertTrue(expectedFundsAvailableDisplayed.equals(this.viewModel.getAvailableFundsProperty().getValue()));
	}
	@Test
	void testNonSuccessfulRequest() {
		Account badUser = new Account("colby", "pw", "badAuth");
		this.viewModel.setUser(badUser);
		this.viewModel.getAmountToSell().setValue(String.valueOf(10));
		
		
		assertThrows(UnsupportedOperationException.class, () -> this.viewModel.sellCrypto());
	}
}
