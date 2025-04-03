package portfoliomanager.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.zeromq.ZMQException;

import portfoliomanager.viewmodel.SignUpPageViewModel;

@TestInstance(Lifecycle.PER_CLASS)
public class TestSignUpPageViewModel {
	private static final String PROTOCOL_IP = "tcp://127.0.0.1:";
	private SignUpPageViewModel page;
	private Thread serverThread;
	private MockServer mockServer;
	private String port;
	
	@BeforeAll
	void startServer() {
		try {
			this.mockServer = new MockServer();
			this.port = "5560";
			serverThread = new Thread(() -> this.mockServer.mockServer(PROTOCOL_IP + this.port));
			serverThread.start();
		} catch (ZMQException e){
			throw new IllegalArgumentException("Address in use but test cases continue");
		}
		
	}
	
	@BeforeEach
	public void setUp() {
		this.page = new SignUpPageViewModel();
		this.page.setClient(this.port);
	}
	
	@AfterAll
	void interruptServer() {
		this.serverThread.interrupt();
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
		assertFalse(this.page.getSignedUpStatus());
	}
 }
