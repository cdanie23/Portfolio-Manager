package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import portfoliomanager.model.Account;
import portfoliomanager.viewmodel.AddFundsViewModel;

class testAddFundsViewModel {
	private Account user;
	private AddFundsViewModel vm;
	private StringProperty fA;
	
	@BeforeEach
	void setup() {
		this.user = new Account("acc", "pass.145");
		this.fA = new SimpleStringProperty();
		this.vm = new AddFundsViewModel(this.user, this.fA);
	}
	
	@Test
	void testConstructor() {
		assertAll(()-> assertNull(this.fA.get()),
				()-> assertEquals("acc", this.vm.getUser().getUserName()),
				()-> assertEquals("pass.145", this.vm.getUser().getPassword()),
				()-> assertNull(this.vm.getAmountProperty().get()));		
	}
	
	@Test
	void testAddFunds() {
		this.vm.getAmountProperty().setValue(String.valueOf(1000));
		this.vm.addFunds();
		
		assertEquals("Funds Available $: 1000.0", this.vm.getFundsAvailableProperty().get());
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
 }
