package portfoliomanager.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import portfoliomanager.model.Crypto;

class TestCrypto {

	@Test
	void testConstructor() {
		//ARRANGE
		String name = "BTC-USD";
		Double currentPrice = 54.36;
		
		//ACT
		Crypto crypto = new Crypto(name, currentPrice);
		//ASSERT
		assertAll(
				()-> assertEquals("BTC-USD", crypto.getName()),
				()-> assertEquals(54.36, crypto.getCurrentPrice().doubleValue(), 0.01),
				()-> assertEquals(0, crypto.getHistoricalPrice().size()));
	}
	
	@Test
	void TestSetters() {
		//ARRANGE
		String name = "BTC-USD";
		Double currentPrice = 54.36;
		HashMap<String, Double> prices = new HashMap<String, Double>();
		prices.put("2024-01-01", 30.50);
		prices.put("2024-01-02", 31.52);
		prices.put("2024-01-03", 40.07);
		Crypto crypto = new Crypto(name, currentPrice);
		assertAll(
				()-> assertEquals("BTC-USD", crypto.getName()),
				()-> assertEquals(54.36, crypto.getCurrentPrice().doubleValue(), 0.01),
				()-> assertEquals(0, crypto.getHistoricalPrice().size()));
		
		//ACT
		crypto.setName("ETH");
		crypto.setCurrentPrice(9.11);
		crypto.setHistoricalPrices(prices);
		//ASSERT
		assertAll(
				()-> assertEquals("ETH", crypto.getName()),
				()-> assertEquals(9.11, crypto.getCurrentPrice().doubleValue(), 0.01),
				()-> assertEquals(3, crypto.getHistoricalPrice().size()),
				()-> assertEquals(30.50, crypto.getHistoricalPrice().get("2024-01-01").doubleValue(), 0.01),
				()-> assertEquals(31.52, crypto.getHistoricalPrice().get("2024-01-02").doubleValue(), 0.01),
				()-> assertEquals(40.07, crypto.getHistoricalPrice().get("2024-01-03").doubleValue(), 0.01));
	}
	
	
	@Test
	void testNullName() {
		assertThrows(IllegalArgumentException.class, ()-> {
			new Crypto(null, 10.16);
		});
	}
	
	@Test
	void testEmptyName() {
		assertThrows(IllegalArgumentException.class,()-> {
			new Crypto("", 9.11);
		});
	}
	
	@Test
	void testBlankName() {
		assertThrows(IllegalArgumentException.class,()-> {
			new Crypto("    ", 9.11);
		});
	}
	@Test
	void testNullCurrentPrice() {
		assertThrows(IllegalArgumentException.class, ()-> {
			new Crypto("BTC", null);
		});
	}

}
