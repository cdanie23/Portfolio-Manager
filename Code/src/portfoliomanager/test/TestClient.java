package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.zeromq.ZMQException;

import portfoliomanager.client.Client;
import portfoliomanager.client.Requests;

@TestInstance(Lifecycle.PER_CLASS)
class TestClient {
	private static final String PROTOCOL_IP = "tcp://127.0.0.1:";
	private String port;
	private static Client client;
	private Thread serverThread;
	private MockServer mockServer;

	
	@BeforeAll
	void startServer() {
		try {
			this.mockServer = new MockServer();
			this.port = "5556";
			this.serverThread = new Thread(() -> this.mockServer.mockServer(PROTOCOL_IP + this.port));
			this.serverThread.start();
		} catch (ZMQException e) {
			System.out.println("Address is in use, but test cases continue");
		}
	}

	@AfterAll
	void interruptServer() {
		this.serverThread.interrupt();
	}

	@BeforeEach
	void setup() {
		client = Client.getInstance(this.port);
	}

	@Test
	void testNullRequest() {
		assertThrows(IllegalArgumentException.class, () -> {
			client.makeRequest(null);
		});
	}

	@Test
	void testInvalidRequest() {
		assertThrows(IllegalArgumentException.class, () -> {
			client.makeRequest(Requests.valueOf("abc"));
		});
	}

	@Test
	void testNullAuthRequest() {
		assertThrows(IllegalArgumentException.class, () -> {
			client.makeAuthRequest(null, "user", "pass123", null);
		});
	}

	@Test
	void testNullAuthRequestNullUserName() {
		assertThrows(IllegalArgumentException.class, () -> {
			client.makeAuthRequest(Requests.login, null, "pass123", null);
		});
	}

	@Test
	void testNullAuthRequestNullPassword() {
		assertThrows(IllegalArgumentException.class, () -> {
			client.makeAuthRequest(Requests.login, "user", null, null);
		});
	}

	@Test
	void testGetBtcPriceRequest() {
		client.makeRequest(Requests.btcPrice);
		assertAll(() -> assertEquals("btcPrice", client.getRequest().get("type")),
				() -> assertEquals(new BigDecimal("91.26"), client.getResponse().get("Price")));
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
		assertAll(() -> assertEquals(1, response.get("success code")), () -> assertTrue(response.containsKey("token")));
	}

	@Test
	void testHandleLogin() {
		client.makeAuthRequest(Requests.login, "user", "pass123", null);
		Map<String, Object> response = client.getResponse();

		assertAll(() -> assertEquals(1, response.get("success code")), () -> assertTrue(response.containsKey("token")));
	}
	
	@Test
	void testExitRequest() {
		client.makeRequest(Requests.exit);
		Map<String, Object> response = client.getResponse();

		assertAll(
		()->assertEquals(-1, response.get("success code")), 
		()->assertEquals("exit", response.get("type")));
	}
	
	@Test
	void testNullCustomPort() {
		Client newClient = Client.getInstance();
		assertEquals("5555", newClient.getPort());
	}
	
	@Test
	void testEmptyCustomPort() {
		Client newClient = Client.getInstance("");
		assertEquals("5555", newClient.getPort());
	}
	
}
