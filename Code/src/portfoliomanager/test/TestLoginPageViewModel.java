package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleObjectProperty;
import portfoliomanager.client.Client;
import portfoliomanager.client.Requests;
import portfoliomanager.model.Account;
import portfoliomanager.viewmodel.LoginPageViewModel;
import portfoliomanager.viewmodel.SignUpPageViewModel;


public class TestLoginPageViewModel {
	private static final String PROTOCOL_IP = "tcp://127.0.0.1:";
	private LoginPageViewModel page;
	private static Thread serverThread;
	private static MockServer mockServer;
	private static String port;
	private static Client client;
	
	@BeforeAll
	static void startServer() {
		try {
			mockServer = new MockServer();
			port = "5560";
			serverThread = new Thread(() -> mockServer.mockServer(PROTOCOL_IP + port));
			serverThread.start();
		} catch (Exception e){
			System.out.println("Address is in use, but test cases continue");
		}
		
	}
	
	@BeforeEach
	public void setUp() {
		this.page = new LoginPageViewModel(new SimpleObjectProperty<Boolean>(false), new Account("Sam", "pw"), "test");
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
	public void testValidLoginPageViewModelConstructor() {
		assertEquals("user", this.page.getAccounts().get(0).getUserName());
		assertEquals("pass123", this.page.getAccounts().get(0).getPassword());
		assertEquals("Sam", this.page.getUser().getUserName());
		assertEquals("pw", this.page.getUser().getPassword());
	}
	
	@Test
	public void testValidLoginPageViewModelConstructorEmptyAccounts() {
		SignUpPageViewModel.getAccounts().clear();
		
		assertTrue(SignUpPageViewModel.getAccounts().isEmpty());
		
		new LoginPageViewModel(new SimpleObjectProperty<Boolean>(false), new Account("Sam", "pw"));
		
		assertFalse(SignUpPageViewModel.getAccounts().isEmpty());
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
}