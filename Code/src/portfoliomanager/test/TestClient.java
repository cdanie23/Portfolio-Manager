package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.zeromq.ZMQException;

import portfoliomanager.client.Client;
import portfoliomanager.client.Requests;

class TestClient {
	private static final String PROTOCOL_IP = "tcp://127.0.0.1:";
	private static String port;
	private static Client client;
	private static Thread serverThread;
	private static MockServer mockServer;

	
	@BeforeAll
	static void startServer() {
		try {
			mockServer = new MockServer();
			port = "5556";
			serverThread = new Thread(() -> mockServer.mockServer(PROTOCOL_IP + port));
			serverThread.start();
		} catch (ZMQException e) {
			System.out.println("Address is in use, but test cases continue");
		}
	}

	@AfterAll
	static void interruptServer() {
		client.makeRequest(Requests.exit);
		client.resetClient();
		serverThread.interrupt();
	}

	@BeforeEach
	void setup() {
		client = Client.getInstance(port);
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
	void testNullLogoutRequest() {
		assertThrows(IllegalArgumentException.class, () -> {
			client.makeLogoutRequest(null, "");
		});
	}
	
	@Test
	void testNullLogoutRequestNullToken() {
		assertThrows(IllegalArgumentException.class, () -> {
			client.makeLogoutRequest(Requests.logout, null);
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
	void testHandleLogout() {
		client.makeAuthRequest(Requests.signUp, "user1", "pass123", "pass123");
		Map<String, Object> response = client.getResponse();
		
		client.makeLogoutRequest(Requests.logout, (String) response.get("token"));
		Map<String, Object> response2 = client.getResponse();
		
		assertEquals(1, response2.get("success code"));
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
