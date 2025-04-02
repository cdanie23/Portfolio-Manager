package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;
import javafx.beans.property.SimpleObjectProperty;
import portfoliomanager.model.Account;
import portfoliomanager.viewmodel.LoginPageViewModel;
import portfoliomanager.viewmodel.SignUpPageViewModel;

public class TestLoginPageViewModel {
	private LoginPageViewModel page;
	private static Thread serverThread;
	private static volatile boolean running = true;
	private static Context context;
	private static Socket socket;
	
	@BeforeAll 
	static void startServer() {
		try {
			serverThread = new Thread(() -> MockServer.mockServer(context, socket, running));
			serverThread.start();
		} catch (Exception e){
			System.out.println("Address is in use, but test cases continue");
		}
		
	}
	
	@AfterAll
	static void interruptServer() throws InterruptedException {
		running = false;
		serverThread.join(1);
	}
	
	@BeforeEach
	public void setUp() {
		this.page = new LoginPageViewModel(new SimpleObjectProperty<Boolean>(false), new Account("Sam", "pw"));
		this.page.setClient("6586");
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