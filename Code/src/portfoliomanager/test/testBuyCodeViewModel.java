package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import portfoliomanager.model.Account;
import portfoliomanager.model.Crypto;
import portfoliomanager.viewmodel.BuyCryptoViewModel;

class testBuyCodeViewModel {

	private Account user;
	private ObjectProperty<Crypto> selectedCrypto;
	private BuyCryptoViewModel vm;
	
	@BeforeEach
	void setup() {
		this.user = new Account("test@user.com", "pass@word");
		Crypto crypto = new Crypto("a", 9.1);
		this.selectedCrypto = new SimpleObjectProperty<Crypto> (crypto);
		this.vm = new BuyCryptoViewModel(this.user, this.selectedCrypto);
	}
	
	@Test
	void testConstructor() {
		assertAll(()-> assertEquals("test@user.com", this.vm.getUser().getEmail()),
				()-> assertEquals("pass@word", this.vm.getUser().getPassword()),
				()-> assertEquals("a", this.vm.getSelectedCryto().get().getName()),
				()-> assertEquals(9.1, this.vm.getSelectedCryto().get().getCurrentPrice()),
				()-> assertNull(this.vm.getAmountProperty().get()),
				()-> assertEquals("a: $9.1", this.vm.getCryptoDetailsProperty().get()));
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
		()-> assertEquals(954.5, this.vm.getUser().getFundsAvailable()));
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
