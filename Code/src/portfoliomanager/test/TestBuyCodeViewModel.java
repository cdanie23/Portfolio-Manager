package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import portfoliomanager.model.Account;
import portfoliomanager.model.Crypto;
import portfoliomanager.model.Holding;
import portfoliomanager.viewmodel.BuyCryptoViewModel;

class TestBuyCodeViewModel {

	private Account user;
	private BuyCryptoViewModel vm;
	private ListProperty<Holding> holdingsProperty;
	private StringProperty fundsAvailableProperty;
	private ObservableList<Crypto> cryptoList;
	private HashMap<String, Double> historicalPrices;
	
	@BeforeEach
	void setup() {
		this.user = new Account("testuser", "pass@word");
		Crypto crypto = new Crypto("a", 9.1);
		this.holdingsProperty = new SimpleListProperty<Holding>(FXCollections.observableArrayList(this.user.getHoldings()));
		this.fundsAvailableProperty = new SimpleStringProperty();
		this.cryptoList = FXCollections.observableArrayList();
		this.vm = new BuyCryptoViewModel(this.user, cryptoList, this.holdingsProperty, this.fundsAvailableProperty);
		this.vm.getSelectedCrypto().set(crypto);
		this.historicalPrices = new HashMap<String, Double>();
		this.historicalPrices.put("2025-01-30", 10.01);
		this.historicalPrices.put("2025-01-31", 9.56);
		this.historicalPrices.put("2025-02-01", 56.4);
		crypto.setHistoricalPrices(historicalPrices);
		this.cryptoList.add(crypto);
	}
	
	@Test
	void testConstructor() {
		assertAll(()-> assertEquals("testuser", this.vm.getUser().getUserName()),
				()-> assertEquals("pass@word", this.vm.getUser().getPassword()),
				()-> assertEquals("a", this.vm.getSelectedCrypto().get().getName()),
				()-> assertEquals(9.1, this.vm.getSelectedCrypto().get().getCurrentPrice()),
				()-> assertNull(this.vm.getAmountProperty().get()),
				()-> assertEquals("a: $9.1", this.vm.getCryptoDetailsProperty().get()),
				()-> assertTrue(this.vm.getHoldingsProperty().get().isEmpty()),
				()-> assertNull(this.vm.getFundsAvailableProperty().get()));
	}
	
	@Test
	void testBuyCrypto() {
		this.vm.getUser().setFundsAvailable(1000);
		this.vm.getAmountProperty().setValue(String.valueOf(5));
		this.vm.buyCrypto();
		
		assertAll(()-> assertEquals(1, this.vm.getUser().getHoldings().size()),
		()-> assertEquals("a", this.vm.getUser().getHoldings().get(0).getName()),
		()-> assertEquals(9.1, this.vm.getUser().getHoldings().get(0).getCurrentPrice()),
		()-> assertEquals(5, this.vm.getUser().getHoldings().get(0).getAmountHeld()),
		()-> assertEquals(954.5, this.vm.getUser().getFundsAvailable()),
		()-> assertTrue(!this.vm.getHoldingsProperty().get().isEmpty()),
		()-> assertFalse(this.vm.getHoldingsProperty().get().isEmpty()),
		()-> assertEquals("Funds Available $: 954.5", this.vm.getFundsAvailableProperty().get()));
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
