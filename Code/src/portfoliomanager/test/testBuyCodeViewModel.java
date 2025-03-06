package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import portfoliomanager.model.Account;
import portfoliomanager.model.Crypto;
import portfoliomanager.model.Holding;
import portfoliomanager.viewmodel.BuyCryptoViewModel;

class testBuyCodeViewModel {

	private Account user;
	private ObjectProperty<Crypto> selectedCrypto;
	private BuyCryptoViewModel vm;
	private ListProperty<Holding> holdingsProperty;
	private StringProperty fundsAvailableProperty;
	
	@BeforeEach
	void setup() {
		this.user = new Account("test@user.com", "pass@word");
		Crypto crypto = new Crypto("a", 9.1);
		this.selectedCrypto = new SimpleObjectProperty<Crypto> (crypto);
		this.holdingsProperty = new SimpleListProperty<Holding>(FXCollections.observableArrayList(this.user.getHoldings()));
		this.fundsAvailableProperty = new SimpleStringProperty();
		this.vm = new BuyCryptoViewModel(this.user, this.selectedCrypto, this.holdingsProperty, this.fundsAvailableProperty);
	}
	
	@Test
	void testConstructor() {
		assertAll(()-> assertEquals("test@user.com", this.vm.getUser().getEmail()),
				()-> assertEquals("pass@word", this.vm.getUser().getPassword()),
				()-> assertEquals("a", this.vm.getSelectedCryto().get().getName()),
				()-> assertEquals(9.1, this.vm.getSelectedCryto().get().getCurrentPrice()),
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
		()-> assertEquals("Funds Available $: 954.5", this.vm.getFundsAvailableProperty().get()));
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
		this.vm.getAmountProperty().setValue(String.valueOf(5));
		assertThrows(IllegalArgumentException.class,()->{
			this.vm.buyCrypto();
		});
	}

}
