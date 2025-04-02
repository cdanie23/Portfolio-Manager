package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import portfoliomanager.client.Client;
import portfoliomanager.client.Requests;

class TestClient {
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
	}
	
	@AfterAll
	static void interruptServer() throws InterruptedException {
		running = false;
		serverThread.join(1);
	}
	
	@Test
	void testNullRequest() {
		assertThrows(IllegalArgumentException.class,()->{
			client.makeRequest(null);
		});
	}
	
	@Test
	void testInvalidRequest() {
		assertThrows(IllegalArgumentException.class,()->{
			client.makeRequest(Requests.valueOf("abc"));
		});
	}
	
	@Test
	void testNullAuthRequest() {
		assertThrows(IllegalArgumentException.class,()->{
			client.makeAuthRequest(null, "user", "pass123", null);
		});
	}
	
	@Test
	void testNullAuthRequestNullUserName() {
		assertThrows(IllegalArgumentException.class,()->{
			client.makeAuthRequest(Requests.login, null, "pass123", null);
		});
	}
	
	@Test
	void testNullAuthRequestNullPassword() {
		assertThrows(IllegalArgumentException.class,()->{
			client.makeAuthRequest(Requests.login, "user", null, null);
		});
	}
	
	@Test
	void testGetBtcPriceRequest() {
		client.makeRequest(Requests.btcPrice);
		assertAll(()->assertEquals("btcPrice", client.getRequest().get("type")),
				()-> assertEquals(new BigDecimal("91.26"), client.getResponse().get("Price")));
	}
	
	@Test
	void testGetBtcPriceHistory() {
		client.makeRequest(Requests.btcHistory);
		assertTrue(!client.getResponse().isEmpty());		
	}
	
	@Test
	void testHandleSignUp() {
		client.makeAuthRequest(Requests.signUp, "user1", "pass123", "pass123");
		Map<String, Object> response = client.getResponse();
		assertAll(
            () -> assertEquals(1, response.get("success code")),
            () -> assertTrue(response.containsKey("token"))
        );
	}
	
	@Test
    void testHandleLogin() {
		client.makeAuthRequest(Requests.login, "user", "pass123", null);
		Map<String, Object> response = client.getResponse();

        assertAll(
            () -> assertEquals(1, response.get("success code")),
            () -> assertTrue(response.containsKey("token"))
        );
    }
	
	/*
	 * @Test void testExit() { client.makeRequest(Requests.exit);
	 * assertEquals("exit", client.getRequest().get("type"));
	 * 
	 * }
	 */
	
	
}
