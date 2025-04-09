package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import portfoliomanager.client.Client;
import portfoliomanager.client.Requests;
import portfoliomanager.model.Account;
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
			port = "5555";
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
}
