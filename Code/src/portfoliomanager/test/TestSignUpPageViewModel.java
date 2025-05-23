package portfoliomanager.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import portfoliomanager.client.Client;
import portfoliomanager.client.Requests;
import portfoliomanager.model.Account;
import portfoliomanager.viewmodel.SignUpPageViewModel;
@TestInstance(Lifecycle.PER_CLASS)
public class TestSignUpPageViewModel {
	private static final String PROTOCOL_IP = "tcp://127.0.0.1:";
	private SignUpPageViewModel page;
	private static Thread serverThread;
	private static MockServer mockServer;
	private static String port;
	private static Client client;
	ObjectProperty<Account> user = new SimpleObjectProperty<Account>();
	ObjectProperty<Boolean> isLoggedIn = new SimpleObjectProperty<Boolean>();
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
		this.isLoggedIn.setValue(false);
	}
	
	@AfterAll
	static void interruptServer() {
		client.makeRequest(Requests.exit);
		client.resetClient();
		
	}
	
	@Test
	public void testValidConstructor() {
		SignUpPageViewModel viewModel = new SignUpPageViewModel(user, isLoggedIn);
		assertFalse(viewModel.getSignedUpStatus().get());
		assertNull(viewModel.getUser().getValue());
	}
	@Test
	public void testCreateAccount()
	{
		this.page.getUserNameProperty().set("testuser");
		this.page.getPasswordProperty().set("testPassword123");
		this.page.getPasswordConfirmProperty().set("testPassword123");
		this.page.createAccount();
		
		assertAll(()-> assertEquals("testuser", this.page.getUser().get().getUserName()),
				()-> assertEquals("testPassword123", this.page.getUser().get().getPassword()));
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
	public void testBadUsername() {
		this.page.getUserNameProperty().set("uglyName");
		this.page.getPasswordProperty().set("testPassword123");
		this.page.getPasswordConfirmProperty().set("testPassword123");
		
		assertThrows(IllegalArgumentException.class, () -> this.page.createAccount());
	}
	@Test
	public void testNotSignedIn() {
		assertFalse(page.getSignedUpStatus().get());
	}
 }
