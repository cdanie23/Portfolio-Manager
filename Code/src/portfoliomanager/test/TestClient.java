package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import portfoliomanager.client.Client;
import portfoliomanager.client.Requests;

class TestClient {
	Client client;
	@BeforeEach
	void setup() {
		this.client = Client.getInstance();
	}
	
	@Test
	void testNullRequest() {
		assertThrows(IllegalArgumentException.class,()->{
			this.client.makeRequest(null);
		});
	}
	
	@Test
	void testInvalidRequest() {
		assertThrows(IllegalArgumentException.class,()->{
			this.client.makeRequest(Requests.valueOf("abc"));
		});
	}
	
	@Test
	void testNullAuthRequest() {
		assertThrows(IllegalArgumentException.class,()->{
			this.client.makeAuthRequest(null, "user", "pass123", null);
		});
	}
	
	@Test
	void testNullAuthRequestNullUserName() {
		assertThrows(IllegalArgumentException.class,()->{
			this.client.makeAuthRequest(Requests.login, null, "pass123", null);
		});
	}
	
	@Test
	void testNullAuthRequestNullPassword() {
		assertThrows(IllegalArgumentException.class,()->{
			this.client.makeAuthRequest(Requests.login, "user", null, null);
		});
	}
	
//	/**
//	 * @pre must have server running at the time of the call
//	 */
	@Test
	void testGetBtcPriceRequest() {
		this.client.makeRequest(Requests.btcPrice);
		assertTrue(this.client.getRequest().containsKey("Price"));
	}
	
	@Test
	void testGetBtcPriceHistory() {
		this.client.makeRequest(Requests.btcHistory);
		assertTrue(!this.client.getResponse().isEmpty());		
	}
	
	@Test
	void testHandleSignUp() {
		this.client.makeAuthRequest(Requests.signUp, "user1", "pass123", "pass123");
		Map<String, Object> response = this.client.getResponse();

		assertAll(
            () -> assertEquals(1, response.get("success code")),
            () -> assertTrue(response.containsKey("token"))
        );
	}
	
	@Test
    void testHandleLogin() {
		this.client.makeAuthRequest(Requests.login, "user", "pass123", null);
		Map<String, Object> response = this.client.getResponse();

        assertAll(
            () -> assertEquals(1, response.get("success code")),
            () -> assertTrue(response.containsKey("token"))
        );
    }
}
