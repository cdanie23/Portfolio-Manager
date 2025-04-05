package portfoliomanager.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zeromq.ZMQException;

import portfoliomanager.client.Client;
import portfoliomanager.client.Requests;
import portfoliomanager.viewmodel.SignUpPageViewModel;

public class TestSignUpPageViewModel {
	private static final String PROTOCOL_IP = "tcp://127.0.0.1:";
	private SignUpPageViewModel page;
	private static Thread serverThread;
	private static MockServer mockServer;
	private static String port;
	private static Client client;
	@BeforeAll
	static void startServer() {
		try {
			port = "5557";
			mockServer = new MockServer();
			serverThread = new Thread(() -> mockServer.mockServer(PROTOCOL_IP + port));
			serverThread.start();
		} catch (ZMQException e){
			throw new IllegalArgumentException("Address in use but test cases continue");
		}
	}
	@BeforeEach
	void setup() {
		this.page = new SignUpPageViewModel("test");
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
	public void testValidSignUpPageViewModelConstructor() {
		assertEquals("user", SignUpPageViewModel.getAccounts().get(0).getUserName());
		assertEquals("pass123", SignUpPageViewModel.getAccounts().get(0).getPassword());
	}
	
	@Test
	public void testCreateAccount()
	{
		this.page.getUserNameProperty().set("testuser");
		this.page.getPasswordProperty().set("testPassword123");
		this.page.getPasswordConfirmProperty().set("testPassword123");
		this.page.createAccount();
		
		assertAll(()-> assertEquals(2, SignUpPageViewModel.getAccounts().size()),
				()-> assertEquals("testuser", SignUpPageViewModel.getAccounts().get(1).getUserName()),
				()-> assertEquals("testPassword123", SignUpPageViewModel.getAccounts().get(1).getPassword()));
	}
	
	@Test
	public void testCreateAccountDuplication()
	{
		this.page.getUserNameProperty().set("testuser");
		this.page.getPasswordProperty().set("testPassword123");
		this.page.getPasswordConfirmProperty().set("testPassword123");
		assertThrows(IllegalArgumentException.class,()->{
			this.page.createAccount();
		});
	}
	
	@Test
	public void testWrongPassword() {
		this.page.getUserNameProperty().set("testuser");
		this.page.getPasswordProperty().set("testPassword123");
		this.page.getPasswordConfirmProperty().set("testPassword");
		assertThrows(IllegalArgumentException.class,()->{
			this.page.createAccount();
		});
	}
	@Test
	public void testNotSignedIn() {
		assertFalse(page.getSignedUpStatus());
	}
 }
