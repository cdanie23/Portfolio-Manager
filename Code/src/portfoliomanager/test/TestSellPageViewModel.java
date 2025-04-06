package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleStringProperty;
import portfoliomanager.model.Account;
import portfoliomanager.model.Holding;
import portfoliomanager.viewmodel.SellPageViewModel;

class TestSellPageViewModel {
	SellPageViewModel viewModel;
	@BeforeEach 
	void setUp() {
		Account user = new Account("colby", "password", "$123");
		Holding holdingToSell = new Holding("btc", Double.valueOf(1000), 10);
		List<Holding> holdings = new ArrayList<Holding>();
		holdings.add(holdingToSell);
		
		SimpleStringProperty fundsAvailable = new SimpleStringProperty();
		fundsAvailable.setValue("$" + user.getFundsAvailable());
		
		this.viewModel = new SellPageViewModel(user, holdingToSell, holdings, fundsAvailable);
		double amountToSell = 2;
		this.viewModel.getAmountToSell().setValue(String.valueOf(amountToSell));
	}

	@Test
	void testGetHoldings() {
		assertTrue(!this.viewModel.getHoldings().isEmpty());
	}
	
	@Test
	void testGetAmountToSell() {
		double amountToSell = 2;
		
		assertEquals(amountToSell, Double.parseDouble(this.viewModel.getAmountToSell().getValue()));
	}
	@Test
	void testGetAmountLeft() {	
		double amountLeft = 8;
		assertEquals(amountLeft, this.viewModel.getAmountLeft());
	}
	@Test
	void testGetProfit() {
		double profit = 2 * 1000;
		
		assertEquals(profit, this.viewModel.getProfit());
	}
	@Test
	void testGetHoldingToSell() {
		assertEquals(10, this.viewModel.getHoldingToSell().getAmountHeld());
		assertEquals(1000, this.viewModel.getHoldingToSell().getCurrentPrice());
		
	}
	@Test
	void testSellPartialAmountOfCrypto() {
		this.viewModel.sellCrypto();
		
		assertEquals(8, this.viewModel.getHoldingToSell().getAmountHeld());
		assertEquals(2000, this.viewModel.getUser().getFundsAvailable());
		String expectedFundsAvailableDisplayed = "$" + 2000.0;
		
		assertTrue(expectedFundsAvailableDisplayed.equals(this.viewModel.getAvailableFundsProperty().getValue()));
	}
	@Test
	void testSellingFullAmountHeld()  {
		this.viewModel.getAmountToSell().setValue(String.valueOf(10));
		this.viewModel.sellCrypto();
		
		assertTrue(this.viewModel.getUser().getHoldings().isEmpty());
		assertEquals(10000, this.viewModel.getUser().getFundsAvailable());
		String expectedFundsAvailableDisplayed = "$" + 10000.0;
		assertTrue(expectedFundsAvailableDisplayed.equals(this.viewModel.getAvailableFundsProperty().getValue()));
	}
}
