package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;
import portfoliomanager.client.Client;
import portfoliomanager.datareader.DataReader;

class TestDataReader {
	private DataReader dataReader;
	private static Client client;
	private static Thread serverThread;
	private static volatile boolean running = true;
	private static Context context;
	private static Socket socket;
	
	@BeforeAll 
	static void startServer() {
		try {
			serverThread = new Thread(() -> MockServer.mockServer(context, socket, running));
			serverThread.start();
		} catch (Exception e){
			System.out.println("Address is in use, but test cases continue");
		}
	}

	@BeforeEach
	void setup() {
		client = Client.getInstance("6586");
		this.dataReader = new DataReader(client);
	}
	
	@AfterAll
	static void interruptServer() throws InterruptedException {
		running = false;
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
