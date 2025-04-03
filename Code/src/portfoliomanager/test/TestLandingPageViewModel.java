package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import portfoliomanager.model.Account;
import portfoliomanager.viewmodel.LandingPageViewModel;


@TestInstance(Lifecycle.PER_CLASS)
class TestLandingPageViewModel {
	private static final String PROTOCOL_IP = "tcp://127.0.0.1:";
	LandingPageViewModel viewModel;
	private Thread serverThread;
	private MockServer mockServer;
	private String port;
	
	@BeforeAll 
	void startServer() {
		try {
			this.mockServer = new MockServer();
			this.port = "5558";
			serverThread = new Thread(() -> this.mockServer.mockServer(PROTOCOL_IP + this.port));
			serverThread.start();
		} catch (Exception e){
			System.out.println("Address is in use, but test cases continue");
		}
		
	}
	
	@BeforeEach
	public void setUp() {
		this.viewModel = new LandingPageViewModel();
		this.viewModel.setClient(this.port);
	}
	
	@AfterAll
	void interruptServer() {
		this.serverThread.interrupt();
	}
	
	@AfterEach
	void tearDown() throws Exception {
	}
	
	@Test
	void testGetCryptoCollection() {
		assertTrue(!this.viewModel.getCryptoListProperty().get().isEmpty());
	}
	@Test
	void testGetFundsAvailabe() {
		String expected = "$0.0";
		String actual = this.viewModel.getFundsAvailabe().getValue();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetCryptoHoldings() {
		assertTrue(!this.viewModel.getCryptoHoldings().isEmpty());
	}
	
	@Test
	void testGetHoldingsProperty() {
		assertTrue(!this.viewModel.getHoldingsProperty().get().isEmpty());
	}
	@Test
	void testGetUser() {
		Account expectedUser = this.viewModel.getUser();
		
		assertEquals(expectedUser, this.viewModel.getUser());
	}
	@Test
	void testUpdateForAuthenticatedUser() {
		this.viewModel.getIsLoggedIn().setValue(true);
		this.viewModel.updateForAuthenticatedUser();
		
		String welcomeText = this.viewModel.getWelcomeLabelProperty().getValue();
		assertEquals(welcomeText, "Welcome back,"+ this.viewModel.getUser().getUserName());
	}
	
	@Test
	void testUpdateForNonAuthenticatedUser() {
		this.viewModel.getIsLoggedIn().setValue(false);
		this.viewModel.updateForAuthenticatedUser();
		
		assertEquals(this.viewModel.getWelcomeLabelProperty().get(), "Welcome to Crypto Vault");
	}
	
	@Test
	void testPortfolioLabelProperties() {
		this.viewModel.getIsLoggedIn().setValue(true);
		this.viewModel.updateForAuthenticatedUser();
		String expectedPortfolioLabel = "user's Portfolio";
		String actual = this.viewModel.getPortfolioNameProperty().getValue();
		
		assertEquals(expectedPortfolioLabel, actual);
	}
}
