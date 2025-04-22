package portfoliomanager.test;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import portfoliomanager.client.Client;
import portfoliomanager.client.Requests;
import portfoliomanager.model.Account;
import portfoliomanager.viewmodel.LoginPageViewModel;

@TestInstance(Lifecycle.PER_CLASS)
public class TestLoginPageViewModel {
	private static final String PROTOCOL_IP = "tcp://127.0.0.1:";
	private LoginPageViewModel page;
	private static Thread serverThread;
	private static MockServer mockServer;
	private static String port;
	private static Client client;
	ObjectProperty<Account> user = new SimpleObjectProperty<Account>();
	ObjectProperty<Boolean> isLoggedIn = new SimpleObjectProperty<Boolean>();

	@BeforeAll
	static void startServer() {
		try {
			mockServer = new MockServer();
			port = "5566";
			serverThread = new Thread(() -> mockServer.mockServer(PROTOCOL_IP + port));
			serverThread.start();
		} catch (Exception e){
			System.out.println("Address is in use, but test cases continue");
		}
		
	}
	
	@BeforeEach
	public void setUp() {
		isLoggedIn.setValue(false);
		this.page = new LoginPageViewModel(isLoggedIn, user, "test");
		this.page.setClient(port);
		client = this.page.getClient();
		}
	
	@AfterAll
	static void interruptServer() {
		client.makeRequest(Requests.exit);
		client.resetClient();
		serverThread.interrupt();
	}
	@Test
	public void testValidNonTestConstructor() {
		LoginPageViewModel viewModel = new LoginPageViewModel(isLoggedIn, user);
		assertFalse(viewModel.getLoginStatus().getValue());
		assertNull(viewModel.getUser().getValue());
	}
	@Test
	public void testValidLoginPageViewModelConstructor() {
		assertEquals("user", MockServer.ACCOUNTS.get(0).getUserName());
		assertEquals("pass123", MockServer.ACCOUNTS.get(0).getPassword());
	}
	
	
	
	@Test
	public void testValidVerifyAccount() {
		this.page.getUserNameProperty().set("user");
		this.page.getPasswordProperty().set("pass123");
		
		this.page.verifyLogin();
		
		assertTrue(this.page.getLoginStatus().getValue());
	}
	
	@Test
	public void testInvalidVerifyAccountWrongEmail() {
		this.page.getUserNameProperty().set("user1");
		this.page.getPasswordProperty().set("pass123");
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.page.verifyLogin();
		});
	}
	
	@Test
	public void testInvalidVerifyAccountWrongPassword() {
		this.page.getUserNameProperty().set("user");
		this.page.getPasswordProperty().set("Pass123");
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.page.verifyLogin();
		});
	}
	@Test
	public void testValidUserWithFunds() {
		
	}
}