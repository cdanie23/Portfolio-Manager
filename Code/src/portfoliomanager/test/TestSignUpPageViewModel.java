package portfoliomanager.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQException;

import portfoliomanager.viewmodel.SignUpPageViewModel;

public class TestSignUpPageViewModel {
	private SignUpPageViewModel page;
	private static Thread serverThread;
	private static volatile boolean running = true;
	private static Context context;
	private static Socket socket;
	
	@BeforeAll 
	static void startServer() {
		try {
			serverThread = new Thread(() -> MockServer.mockServer(context, socket, running));
			serverThread.start();
		} catch (ZMQException e){
			throw new IllegalArgumentException("Address in use but test cases continue");
		}
		
	}
	
	@AfterAll
	static void interruptServer() throws InterruptedException {
		running = false;
		serverThread.join(1);
	}
	
	@BeforeEach
	public void setUp() {
		this.page = new SignUpPageViewModel();
		this.page.setClient("6586");
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
