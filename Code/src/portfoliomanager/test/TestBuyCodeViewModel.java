package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import portfoliomanager.client.Client;
import portfoliomanager.client.CryptoCurrencies;
import portfoliomanager.model.Account;
import portfoliomanager.model.Crypto;
import portfoliomanager.model.Holding;
import portfoliomanager.viewmodel.BuyCryptoViewModel;
@TestInstance(Lifecycle.PER_CLASS)
class TestBuyCodeViewModel {

	private Account user;
	private BuyCryptoViewModel vm;
	private ListProperty<Holding> holdingsProperty;
	private StringProperty fundsAvailableProperty;
	private ObservableList<Crypto> cryptoList;
	private HashMap<String, BigDecimal> historicalPrices;
	
	private static final String PROTOCOL_IP = "tcp://127.0.0.1:";
	private static Thread serverThread;
	private static MockServer mockServer;
	private static String port;
	private static Client client;
	
	@BeforeAll
	static void startServer() {
		try {
			mockServer = new MockServer();
			port = "5562";
			serverThread = new Thread(() -> mockServer.mockServer(PROTOCOL_IP + port));
			serverThread.start();
		} catch (Exception e){
			System.out.println("Address is in use, but test cases continue");
		}
	}
	@AfterAll
	static void interruptServer() {
		
		client.resetClient();
		
	}
	@BeforeEach
	void setup() {
		this.user = new Account("testuser", "pass@word", "$123");
		Crypto crypto = new Crypto(CryptoCurrencies.Bitcoin, 9.1);
		this.holdingsProperty = new SimpleListProperty<Holding>(FXCollections.observableArrayList(this.user.getHoldings()));
		this.fundsAvailableProperty = new SimpleStringProperty();
		this.cryptoList = FXCollections.observableArrayList();
		this.vm = new BuyCryptoViewModel(this.user, cryptoList, this.holdingsProperty, this.fundsAvailableProperty, "test");
		this.vm.getSelectedCrypto().set(crypto);
		this.historicalPrices = new HashMap<String, BigDecimal>();
		this.historicalPrices.put("2025-01-30", new BigDecimal(10.01));
		this.historicalPrices.put("2025-01-31", new BigDecimal(9.56));
		this.historicalPrices.put("2025-02-01", new BigDecimal(56.4));
		crypto.setHistoricalPrices(historicalPrices);
		this.cryptoList.add(crypto);
		this.vm.setClient(port);
		client = this.vm.getClient();
	}
	
	@Test
	void testConstructor() {
		BuyCryptoViewModel viewModel = new BuyCryptoViewModel(this.user, cryptoList, this.holdingsProperty, this.fundsAvailableProperty);
		Crypto crypto = new Crypto(CryptoCurrencies.Bitcoin, 9.1);
		viewModel.getSelectedCrypto().set(crypto);
		assertAll(()-> assertEquals("testuser", viewModel.getUser().getUserName()),
				()-> assertEquals("pass@word", viewModel.getUser().getPassword()),
				()-> assertEquals("Bitcoin", viewModel.getSelectedCrypto().get().getName().toString()),
				()-> assertEquals(9.1, viewModel.getSelectedCrypto().get().getCurrentPrice()),
				()-> assertNull(viewModel.getAmountProperty().get()),
				()-> assertEquals("Bitcoin: $9.1", viewModel.getCryptoDetailsProperty().get()),
				()-> assertTrue(viewModel.getHoldingsProperty().get().isEmpty()),
				()-> assertNull(viewModel.getFundsAvailableProperty().get()));
	}
	
	@Test
	void testBuyCrypto() {
		this.vm.getUser().setFundsAvailable(1000);
		this.vm.getAmountProperty().setValue(String.valueOf(5));
		this.vm.buyCrypto();
		Map<String, Object> response = client.getResponse();
		
		assertAll(()-> assertEquals(1, this.vm.getUser().getHoldings().size()),
		()-> assertEquals("Bitcoin", this.vm.getUser().getHoldings().get(0).getName().toString()),
		()-> assertEquals(9.1, this.vm.getUser().getHoldings().get(0).getCurrentPrice()),
		()-> assertEquals(5, this.vm.getUser().getHoldings().get(0).getAmountHeld()),
		()-> assertEquals(954.5, this.vm.getUser().getFundsAvailable()),
		()-> assertTrue(!this.vm.getHoldingsProperty().get().isEmpty()),
		()-> assertFalse(this.vm.getHoldingsProperty().get().isEmpty()),
		()-> assertEquals("$954.50", this.vm.getFundsAvailableProperty().get()),
		() -> assertEquals(response.get("auth"), "$123"),
		() -> assertEquals(response.get("success code"), 1)
		);
	}
	
	@Test
	void testUpdateLineChart() {
		this.vm.updateLineChart(String.valueOf(320));
		assertAll(()-> assertTrue(!this.vm.getCryptoList().get().isEmpty()),
				()-> assertTrue(!this.vm.getLineChartSeriesProperty().getData().isEmpty()));
				}
	
	@Test
	void testNullAmountToBuy() {
		this.vm.getAmountProperty().setValue(null);
		assertThrows(IllegalArgumentException.class,()->{
			this.vm.buyCrypto();
		});
	}
	
	@Test
	void testEmptyAmountToBuy() {
		this.vm.getAmountProperty().setValue(String.valueOf(""));
		assertThrows(IllegalArgumentException.class,()->{
			this.vm.buyCrypto();
		});
	}
	
	@Test
	void testFundsLowerThanTotalCost() {
		this.vm.getFundsAvailableProperty().setValue(String.valueOf(10));
		this.vm.getAmountProperty().setValue(String.valueOf(5));
		assertThrows(IllegalArgumentException.class,()->{
			this.vm.buyCrypto();
		});
	}
	
	@Test
	void testNullRange() {
		assertThrows(IllegalArgumentException.class, ()->{
			this.vm.updateLineChart(null);
		});
	}

}
