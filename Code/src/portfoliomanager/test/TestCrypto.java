package portfoliomanager.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

import portfoliomanager.client.CryptoCurrencies;
import portfoliomanager.model.Crypto;

class TestCrypto {
	
	@Test
	void testConstructor() {
		//ARRANGE
		Double currentPrice = 54.36;
		
		//ACT
		Crypto crypto = new Crypto(CryptoCurrencies.Bitcoin, currentPrice);
		//ASSERT
		assertAll(
				()-> assertEquals("Bitcoin", crypto.getName().toString()),
				()-> assertEquals(54.36, crypto.getCurrentPrice().doubleValue(), 0.01),
				()-> assertEquals(0, crypto.getHistoricalPrice().size()));
	}
	
	@Test
	void TestSetters() {
		//ARRANGE
		
		Double currentPrice = 54.36;
		HashMap<String, BigDecimal> prices = new HashMap<String, BigDecimal>();
		prices.put("2024-01-01", new BigDecimal(30.50));
		prices.put("2024-01-02", new BigDecimal(31.52));
		prices.put("2024-01-03", new BigDecimal(40.07));
		Crypto crypto = new Crypto(CryptoCurrencies.Bitcoin, currentPrice);
		assertAll(
				()-> assertEquals("Bitcoin", crypto.getName().toString()),
				()-> assertEquals(54.36, crypto.getCurrentPrice().doubleValue(), 0.01),
				()-> assertEquals(0, crypto.getHistoricalPrice().size()));
		
		//ACT
		crypto.setName(CryptoCurrencies.Ethereum);
		crypto.setCurrentPrice(9.11);
		crypto.setHistoricalPrices(prices);
		//ASSERT
		assertAll(
				()-> assertEquals("Ethereum", crypto.getName().toString()),
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
	void testNullCurrentPrice() {
		assertThrows(IllegalArgumentException.class, ()-> {
			new Crypto(CryptoCurrencies.Bitcoin, null);
		});
	}
	
	@Test
	void testGetOneDayPriceChange() {
		Crypto btc = new Crypto(CryptoCurrencies.Bitcoin, Double.valueOf(100));
		DateTimeFormatter fomatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String today = btc.getTodaysDate().format(fomatter);
		String yesterday = btc.getTodaysDate().minusDays(1).format(fomatter);
		HashMap<String, BigDecimal> historicalData = new HashMap<String, BigDecimal>();
		historicalData.put(today, new BigDecimal(100));
		historicalData.put(yesterday, new BigDecimal(80));
		btc.setHistoricalPrices(historicalData);
		assertEquals(25, btc.getOneDayPriceChange(), .001);
	}
	@Test
	void testPriceOneDayIncrease() {
		Crypto btc = new Crypto(CryptoCurrencies.Bitcoin, Double.valueOf(100));
		DateTimeFormatter fomatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String today = btc.getTodaysDate().format(fomatter);
		String yesterday = btc.getTodaysDate().minusDays(1).format(fomatter);
		HashMap<String, BigDecimal> historicalData = new HashMap<String, BigDecimal>();
		historicalData.put(today, new BigDecimal(100));
		historicalData.put(yesterday, new BigDecimal(80));
		btc.setHistoricalPrices(historicalData);
		assertFalse(btc.didOneDayPriceDecrease());
	}
	@Test
	void testPriceOneDayDecrease() {
		Crypto btc = new Crypto(CryptoCurrencies.Bitcoin, Double.valueOf(80));
		DateTimeFormatter fomatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String today = btc.getTodaysDate().format(fomatter);
		String yesterday = btc.getTodaysDate().minusDays(1).format(fomatter);
		HashMap<String, BigDecimal> historicalData = new HashMap<String, BigDecimal>();
		historicalData.put(today, new BigDecimal(80));
		historicalData.put(yesterday, new BigDecimal(100));
		btc.setHistoricalPrices(historicalData);
		assertTrue(btc.didOneDayPriceDecrease());
	}
	@Test
	void testToString() {
		Crypto btc = new Crypto(CryptoCurrencies.Bitcoin, Double.valueOf(80));
		DateTimeFormatter fomatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String today = btc.getTodaysDate().format(fomatter);
		String yesterday = btc.getTodaysDate().minusDays(1).format(fomatter);
		String dayBefore = btc.getTodaysDate().minusDays(2).format(fomatter);
		HashMap<String, BigDecimal> historicalData = new HashMap<String, BigDecimal>();
		historicalData.put(today, new BigDecimal(100));
		historicalData.put(yesterday, null);
		historicalData.put(dayBefore, new BigDecimal(70));
		btc.setHistoricalPrices(historicalData);
		assertTrue((String.format("%25s%68.2f%66.2f", btc.getName(), btc.getCurrentPrice(), btc.getOneDayPriceChange()) + "%").equals(btc.toString()));
	}
	
	@Test
	void testGetPriceForRange() {
		
		Double currentPrice = 54.36;
		Crypto crypto = new Crypto(CryptoCurrencies.Bitcoin, currentPrice);
		crypto.getHistoricalPrice().put("2024-01-01", new BigDecimal(30.50));
		crypto.getHistoricalPrice().put("2024-01-02", new BigDecimal(31.52));
		crypto.getHistoricalPrice().put("2024-01-03", new BigDecimal(40.07));
		
		assertAll(()-> assertTrue(!crypto.getPriceForRange(2).isEmpty()),
				()-> assertEquals(2, crypto.getPriceForRange(2).size()));
	}
	
	@Test
	void testGetPriceForRangeNegativeValue() {
		Double currentPrice = 54.36;
		Crypto crypto = new Crypto(CryptoCurrencies.Bitcoin, currentPrice);
		crypto.getHistoricalPrice().put("2024-01-01", new BigDecimal(30.50));
		crypto.getHistoricalPrice().put("2024-01-02", new BigDecimal(31.52));
		crypto.getHistoricalPrice().put("2024-01-03", new BigDecimal(40.07));
		
		assertAll(()-> assertTrue(crypto.getPriceForRange(-1).isEmpty()),
				()-> assertEquals(0, crypto.getPriceForRange(-1).size()));
	}
}
