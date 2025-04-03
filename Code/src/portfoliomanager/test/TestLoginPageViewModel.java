package portfoliomanager.test;

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

import javafx.beans.property.SimpleObjectProperty;
import portfoliomanager.model.Account;
import portfoliomanager.viewmodel.LoginPageViewModel;
import portfoliomanager.viewmodel.SignUpPageViewModel;


@TestInstance(Lifecycle.PER_CLASS)
public class TestLoginPageViewModel {
	private static final String PROTOCOL_IP = "tcp://127.0.0.1:";
	private LoginPageViewModel page;
	private Thread serverThread;
	private MockServer mockServer;
	private String port;
	
	@BeforeAll
	void startServer() {
		try {
			this.mockServer = new MockServer();
			this.port = "5559";
			serverThread = new Thread(() -> this.mockServer.mockServer(PROTOCOL_IP + this.port));
			serverThread.start();
		} catch (Exception e){
			System.out.println("Address is in use, but test cases continue");
		}
		
	}
	
	@BeforeEach
	public void setUp() {
		this.page = new LoginPageViewModel(new SimpleObjectProperty<Boolean>(false), new Account("Sam", "pw"));
		this.page.setClient(this.port);
		}
	
	@AfterAll
	void interruptServer() {
		this.serverThread.interrupt();
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