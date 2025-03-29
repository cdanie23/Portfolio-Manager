package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import portfoliomanager.client.Client;
import portfoliomanager.datareader.DataReader;

class TestDataReader {
	private DataReader dataReader;
	private Client client;
	
	@BeforeEach
	void setUp() {
		this.client = Client.getInstance();
		this.dataReader = new DataReader(this.client);
	}
	
	@Test
	void testGetCryptoCollection() {
		assertTrue(this.dataReader.getCryptoCollection().isEmpty());
	}
	
	@Test
	void testReadCryptoData() {
		this.dataReader.readCryptoData();
		assertTrue(!this.dataReader.getCryptoCollection().isEmpty());
		assertTrue(!this.dataReader.getCryptoCollection().getFirst().getHistoricalPrice().isEmpty());
	}
 }
