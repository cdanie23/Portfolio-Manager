package portfoliomanager.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
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
	@Test
	void testGetOneDayPriceChange() {
		Crypto btc = new Crypto("bitcoin", Double.valueOf(100));
		HashMap<String, Double> historicalData = new HashMap<String, Double>();
		historicalData.put("2025-02-23", Double.valueOf(100));
		historicalData.put("2025-02-22", Double.valueOf(80));
		btc.setHistoricalPrices(historicalData);
		assertEquals(25, btc.getOneDayPriceChange(), .001);
	}
	@Test
	void testPriceOneDayIncrease() {
		Crypto btc = new Crypto("bitcoin", Double.valueOf(100));
		HashMap<String, Double> historicalData = new HashMap<String, Double>();
		historicalData.put("2025-02-23", Double.valueOf(100));
		historicalData.put("2025-02-22", Double.valueOf(80));
		btc.setHistoricalPrices(historicalData);
		
		assertFalse(btc.didOneDayPriceDecrease());
	}
	@Test
	void testPriceOneDayDecrease() {
		Crypto btc = new Crypto("bitcoin", Double.valueOf(80));
		HashMap<String, Double> historicalData = new HashMap<String, Double>();
		historicalData.put("2025-02-23", Double.valueOf(80));
		historicalData.put("2025-02-22", Double.valueOf(100));
		btc.setHistoricalPrices(historicalData);
		assertTrue(btc.didOneDayPriceDecrease());
	}
	@Test
	void testToString() {
		Crypto btc = new Crypto("bitcoin", Double.valueOf(80));
		HashMap<String, Double> historicalData = new HashMap<String, Double>();
		historicalData.put("2025-02-23", Double.valueOf(80));
		historicalData.put("2025-02-22", Double.valueOf(100));
		btc.setHistoricalPrices(historicalData);
		
		assertTrue((String.format("%25s%68.2f%66.2f", btc.getName(), btc.getCurrentPrice(), btc.getOneDayPriceChange()) + "%").equals(btc.toString()));
	}
}
