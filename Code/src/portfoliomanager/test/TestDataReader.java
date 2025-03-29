package portfoliomanager.test;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import portfoliomanager.client.Client;
import portfoliomanager.datareader.DataReader;
import portfoliomanager.model.Crypto;


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
