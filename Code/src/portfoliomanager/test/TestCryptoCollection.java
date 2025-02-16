package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import portfoliomanager.model.Crypto;
import portfoliomanager.model.CryptoCollection;

class TestCryptoCollection {

	@Test
	void TestConstructor() {
		//ARRANGE
		Crypto crypto = new Crypto("BTC", 93.05);
		//ACT
		CryptoCollection cryptos = new CryptoCollection();
		cryptos.addCrypto(crypto);
		//ASSERT
		assertAll(
				()-> assertEquals(1, cryptos.getCryptos().size()),
				()-> assertEquals("BTC", cryptos.getCryptos().get(0).getName()),
				()-> assertEquals(93.05, cryptos.getCryptos().get(0).getCurrentPrice()));
	}
	
	@Test
	void TestAddCrypto() {
		//ARRANGE
		Crypto crypto = new Crypto("BTC", 93.05);
		//ACT
		CryptoCollection cryptos = new CryptoCollection();
		Crypto newCrypto = new Crypto("ETH", 45.12);
		cryptos.addCrypto(crypto);
		cryptos.addCrypto(newCrypto);
		//ASSERT
		assertAll(
				()-> assertEquals(2, cryptos.getCryptos().size()),
				()-> assertEquals("BTC", cryptos.getCryptos().get(0).getName()),
				()-> assertEquals(93.05, cryptos.getCryptos().get(0).getCurrentPrice()),
				()-> assertEquals("ETH", cryptos.getCryptos().get(1).getName()),
				()-> assertEquals(45.12, cryptos.getCryptos().get(1).getCurrentPrice()));
	}
	
	@Test
	void TestAddNullCrypto() {
		CryptoCollection cryptos = new CryptoCollection();
		assertThrows(IllegalArgumentException.class,()->{cryptos.addCrypto(null);});
	}

}
