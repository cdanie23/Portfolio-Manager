package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import portfoliomanager.model.Holding;

class TestHolding {
	Holding holding;
	@BeforeEach
	void setUp() {
		 this.holding = new Holding("Bitcoin", Double.valueOf(1000), 10);
		 
	}
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetTotalPrice() {
		double totalPrice = this.holding.getTotalPrice();
		
		double actualTotalPrice = 10000;
		assertEquals(totalPrice, actualTotalPrice);
	}
	
	@Test
	void testToString() {
		String expected = String.format("%-10s %12.2f %8.2f", this.holding.getName(), this.holding.getTotalPrice(), this.holding.getAmountHeld());
		String actual = this.holding.toString();
		
		assertTrue(expected.equals(actual));
		
	}
	@Test
	void testGetAmountHeld() {
		double actualAmountHeld = 10;
		
		assertEquals(actualAmountHeld, this.holding.getAmountHeld());
	}
	@Test
	void testSetAmountHeld() {
		double amountToSet = 10;
		this.holding.setAmountHeld(amountToSet);
		assertEquals(amountToSet, this.holding.getAmountHeld());
	}
	@Test
	void testGetProfit() {
		double actualProfit = 10 * 1000;
		assertEquals(this.holding.getProfit(10), actualProfit);
	}
}
