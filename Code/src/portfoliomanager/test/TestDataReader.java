package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import portfoliomanager.client.Client;
import portfoliomanager.datareader.DataReader;


@TestInstance(Lifecycle.PER_CLASS)
class TestDataReader {
	private static final String PROTOCOL_IP = "tcp://127.0.0.1:";
	private DataReader dataReader;
	private Client client;
	private Thread serverThread;
	private MockServer mockServer;
	private static String port;
	
	@BeforeAll 
	void startServer() {
		try {
			this.mockServer = new MockServer();
			port = "5557";
			serverThread = new Thread(() -> this.mockServer.mockServer(PROTOCOL_IP + port));
			serverThread.start();
		} catch (Exception e){
			System.out.println("Address is in use, but test cases continue");
		}
	}

	@BeforeEach
	void setup() {
		client = Client.getInstance(port);
		this.dataReader = new DataReader(client);
	}
	
	@AfterAll
	void interruptServer() {
		this.serverThread.interrupt();
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
