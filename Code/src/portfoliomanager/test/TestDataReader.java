package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import portfoliomanager.client.Client;
import portfoliomanager.client.Requests;
import portfoliomanager.datareader.DataReader;



class TestDataReader {
	private static final String PROTOCOL_IP = "tcp://127.0.0.1:";
	private DataReader dataReader;
	private static Client client;
	private static Thread serverThread;
	private static MockServer mockServer;
	private static String port;
	
	@BeforeAll 
	static void startServer() {
		try {
			mockServer = new MockServer();
			port = "5558";
			serverThread = new Thread(() -> mockServer.mockServer(PROTOCOL_IP + port));
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
	static void interruptServer() {
		client.makeRequest(Requests.exit);
		client.resetClient();
		serverThread.interrupt();
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
