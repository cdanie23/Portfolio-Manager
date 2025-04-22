package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import portfoliomanager.client.Client;
import portfoliomanager.model.Account;
import portfoliomanager.viewmodel.AddFundsViewModel;

@TestInstance(Lifecycle.PER_CLASS)
class TestAddFundsViewModel {
	private Account user;
	private AddFundsViewModel vm;
	private StringProperty fA;
	
	private static final String PROTOCOL_IP = "tcp://127.0.0.1:";
	private static Thread serverThread;
	private static MockServer mockServer;
	private static String port;
	private static Client client;
	
	@BeforeAll
	static void startServer() {
		try {
			mockServer = new MockServer();
			port = "5561";
			serverThread = new Thread(() -> mockServer.mockServer(PROTOCOL_IP + port));
			serverThread.start();
		} catch (Exception e){
			System.out.println("Address is in use, but test cases continue");
		}
		
	}
	@BeforeEach
	void setup() {
		this.user = new Account("acc", "pass.145", "$123");
		this.fA = new SimpleStringProperty();
		this.vm = new AddFundsViewModel(this.user, this.fA, "test");
		this.vm.setClient(port);
		client = this.vm.getClient();
	}
	@AfterAll
	static void interruptServer() {
		client.resetClient();
		serverThread.interrupt();
	}
	@Test
	void testConstructor() {
		AddFundsViewModel viewModel = new AddFundsViewModel(this.user, this.fA);
		assertAll(
				()-> assertEquals("acc", viewModel.getUser().getUserName()),
				()-> assertEquals("pass.145", viewModel.getUser().getPassword()),
				()-> assertNull(viewModel.getAmountProperty().get()));		
	}
	
	@Test
	void testAddFunds() {
		this.vm.getAmountProperty().setValue(String.valueOf(1000));
		this.vm.addFunds();
		
		assertEquals("$1000.00", this.vm.getFundsAvailableProperty().get());
	}
	
	@Test
	void testAddNullFunds() {
		client.makeAddFundsRequest(null, 1.0);
		assertThrows(IllegalArgumentException.class,()->{
			this.vm.addFunds();
		});
	}
	
	@Test
	void testAddFundNoAmountProvided() {
		this.vm.getAmountProperty().setValue(null);
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.vm.addFunds();
		});
	}
	
	@Test
	void testAddFundEmptyAmountProvided() {
		this.vm.getAmountProperty().setValue("");
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.vm.addFunds();
		});
	}
	@Test
	void testNegativeFundsAmount() {
		this.vm.getAmountProperty().setValue("-5.0");
		assertThrows(UnsupportedOperationException.class, () -> {
			this.vm.addFunds();
		});
	}
 }
