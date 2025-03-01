package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.jupiter.api.Test;

import portfoliomanager.model.Crypto;
import portfoliomanager.model.CryptoCollection;

class TestCryptoCollection {

	@Test
	void TestConstructor() {
		// ARRANGE
		Crypto crypto = new Crypto("BTC", 93.05);
		// ACT
		CryptoCollection cryptos = new CryptoCollection();
		cryptos.addCrypto(crypto);
		// ASSERT
		assertAll(() -> assertEquals(1, cryptos.getCryptos().size()),
				() -> assertEquals("BTC", cryptos.getCryptos().get(0).getName()),
				() -> assertEquals(93.05, cryptos.getCryptos().get(0).getCurrentPrice()));
	}

	@Test
	void TestAddCrypto() {
		// ARRANGE
		Crypto crypto = new Crypto("BTC", 93.05);
		// ACT
		CryptoCollection cryptos = new CryptoCollection();
		Crypto newCrypto = new Crypto("ETH", 45.12);
		cryptos.addCrypto(crypto);
		cryptos.addCrypto(newCrypto);
		// ASSERT
		assertAll(() -> assertEquals(2, cryptos.getCryptos().size()),
				() -> assertEquals("BTC", cryptos.getCryptos().get(0).getName()),
				() -> assertEquals(93.05, cryptos.getCryptos().get(0).getCurrentPrice()),
				() -> assertEquals("ETH", cryptos.getCryptos().get(1).getName()),
				() -> assertEquals(45.12, cryptos.getCryptos().get(1).getCurrentPrice()));
	}

	@Test
	void TestAddNullCrypto() {
		CryptoCollection cryptos = new CryptoCollection();
		assertThrows(IllegalArgumentException.class, () -> {
			cryptos.addCrypto(null);
		});
	}

	@Test
	void testIsEmpty() {
		CryptoCollection cryptos = new CryptoCollection();

		assertTrue(cryptos.isEmpty());
	}

	@Test
	void testSize() {
		CryptoCollection cryptos = new CryptoCollection();

		assertEquals(cryptos.size(), 0);
	}

	@Test
	void testContains() {
		CryptoCollection cryptos = new CryptoCollection();

		Crypto btc = new Crypto("btc", Double.valueOf(1));

		cryptos.add(btc);

		assertTrue(cryptos.contains(btc));
	}

	@Test
	void testIterator() {
		CryptoCollection cryptos = new CryptoCollection();

		Crypto btc = new Crypto("btc", Double.valueOf(1));

		cryptos.add(btc);

		Iterator<Crypto> iterator = cryptos.iterator();

		int i = 0;
		while (iterator.hasNext()) {
			assertEquals(cryptos.get(i), iterator.next());

		}
	}

	@Test
	void testToArray() {
		CryptoCollection cryptos = new CryptoCollection();

		Crypto btc = new Crypto("btc", Double.valueOf(1));

		cryptos.add(btc);

		Object[] cryptoArray = cryptos.toArray();

		for (int i = 0; i < cryptos.size(); i++) {
			assertEquals(cryptos.get(i), cryptoArray[i]);
		}
	}

	@Test
	void testToArrayT() {
		CryptoCollection cryptos = new CryptoCollection();

		Crypto btc = new Crypto("btc", Double.valueOf(1));

		cryptos.add(btc);

		Crypto[] crypto = cryptos.toArray(new Crypto[1]);
		
		assertTrue(crypto[0] instanceof Crypto);
	}
	
	@Test
	void testRemove() {
		CryptoCollection cryptos = new CryptoCollection();
		
		Crypto btc = new Crypto("btc", Double.valueOf(1));
		
		cryptos.add(btc);
		
		cryptos.remove(btc);
		
		assertTrue(!cryptos.contains(btc));
	}
	
	@Test
	void testContainsAll() {
		CryptoCollection cryptos = new CryptoCollection();
		
		Crypto btc = new Crypto("btc", Double.valueOf(1));
		
		cryptos.add(btc);
		
		CryptoCollection cryptos2 = new CryptoCollection();
		
		Crypto ethereum = new Crypto("ethereum", Double.valueOf(2));
		
		cryptos2.add(ethereum);
		
		cryptos.addAll(cryptos2);
		
		assertTrue(cryptos.containsAll(cryptos2));
	}
	
	@Test
	void testAddAllFromIndex() {
		CryptoCollection cryptos = new CryptoCollection();
		
		Crypto btc = new Crypto("btc", Double.valueOf(1));
		
		cryptos.add(btc);
		
		CryptoCollection cryptos2 = new CryptoCollection();
		
		Crypto ethereum = new Crypto("ethereum", Double.valueOf(2));
		cryptos2.add(ethereum);
		Crypto doge = new Crypto("doge", Double.valueOf(10000));
		cryptos.add(doge);
		
		cryptos.addAll(1, cryptos2);
		
		assertTrue(cryptos.contains(doge));
	}	
	
	@Test
	void testRemoveAll() {
		CryptoCollection cryptos = new CryptoCollection();
		
		Crypto btc = new Crypto("btc", Double.valueOf(1));
		
		cryptos.add(btc);
		
		CryptoCollection cryptos2 = new CryptoCollection();
		
		Crypto ethereum = new Crypto("ethereum", Double.valueOf(2));
		cryptos2.add(ethereum);
		Crypto doge = new Crypto("doge", Double.valueOf(10000));
		cryptos.add(doge);
		cryptos2.add(btc);
		
		cryptos2.removeAll(cryptos);
		
		assertTrue(!cryptos2.contains(btc));
		
	}
	
	@Test
	void testRetainAll() {
		CryptoCollection cryptos = new CryptoCollection();
		
		Crypto btc = new Crypto("btc", Double.valueOf(1));
		
		cryptos.add(btc);
		
		CryptoCollection cryptos2 = new CryptoCollection();
		
		Crypto ethereum = new Crypto("ethereum", Double.valueOf(2));
		cryptos2.add(ethereum);
		Crypto doge = new Crypto("doge", Double.valueOf(10000));
		cryptos2.add(doge);
		
		cryptos2.retainAll(cryptos);
		
		assertAll(
				() -> assertTrue(!cryptos2.contains(ethereum)),
				() -> assertTrue(!cryptos2.contains(doge))
				);
		}
	
	@Test
	void testClear() {
		CryptoCollection cryptos = new CryptoCollection();
		
		Crypto ethereum = new Crypto("ethereum", Double.valueOf(2));
		cryptos.add(ethereum);
		Crypto doge = new Crypto("doge", Double.valueOf(10000));
		cryptos.add(doge);
		
		cryptos.clear();
		assertTrue(cryptos.isEmpty());
	}
	@Test
	void testSet() {
		CryptoCollection cryptos = new CryptoCollection();
		
		Crypto ethereum = new Crypto("ethereum", Double.valueOf(2));
		cryptos.add(0, ethereum);
		Crypto doge = new Crypto("doge", Double.valueOf(10000));
		cryptos.add(doge);
		Crypto btc = new Crypto("btc", Double.valueOf(1));
		
		cryptos.set(1, btc);
		
		assertAll(
				() -> assertTrue(!cryptos.contains(doge)),
				() -> assertTrue(cryptos.contains(btc))
				);
		
	}
	
	@Test
	void testRemoveIndex() {
		CryptoCollection cryptos = new CryptoCollection();
		
		Crypto ethereum = new Crypto("ethereum", Double.valueOf(2));
		cryptos.add(0, ethereum);
		Crypto doge = new Crypto("doge", Double.valueOf(10000));
		cryptos.add(doge);
		
		cryptos.remove(cryptos.indexOf(doge));
		
		assertTrue(!cryptos.contains(doge));
	}
	@Test
	void testlastIndexOf() {
		CryptoCollection cryptos = new CryptoCollection();
		
		Crypto ethereum = new Crypto("ethereum", Double.valueOf(2));
		cryptos.add(0, ethereum);
		Crypto doge = new Crypto("doge", Double.valueOf(10000));
		cryptos.add(doge);
		
		int lastIndexOf = cryptos.lastIndexOf(doge);
		
		assertEquals(lastIndexOf, 1);
	}
	
	@Test
	void testListIterator() {
		CryptoCollection cryptos = new CryptoCollection();
		
		Crypto ethereum = new Crypto("ethereum", Double.valueOf(2));
		cryptos.add(0, ethereum);
		Crypto doge = new Crypto("doge", Double.valueOf(10000));
		cryptos.add(doge);
		
		ListIterator<Crypto> iterator = cryptos.listIterator();
		int i = 0;
		while (iterator.hasNext()) {
			assertEquals(iterator.next(), cryptos.get(i));
			i++;
		}
		
	}
	@Test
	void testListIteratorWithIndex() {
		CryptoCollection cryptos = new CryptoCollection();
		
		Crypto ethereum = new Crypto("ethereum", Double.valueOf(2));
		cryptos.add(0, ethereum);
		Crypto doge = new Crypto("doge", Double.valueOf(10000));
		cryptos.add(doge);
		
		ListIterator<Crypto> iterator = cryptos.listIterator(1);
		int i = 1;
		while (iterator.hasNext()) {
			assertEquals(iterator.next(), cryptos.get(i));
			i++;
		}
	}
	@Test
	void testSubList() {
		CryptoCollection cryptos = new CryptoCollection();
		
		Crypto ethereum = new Crypto("ethereum", Double.valueOf(2));
		cryptos.add(0, ethereum);
		Crypto doge = new Crypto("doge", Double.valueOf(10000));
		cryptos.add(doge);
		Crypto btc = new Crypto("btc", Double.valueOf(1));
		cryptos.add(btc);
		
		List<Crypto> subList = cryptos.subList(1, 2);
		
		for (int i = 0; i < subList.size(); i++) {
			assertEquals(cryptos.get(i + 1), subList.get(i));
		}
	}
	
}
