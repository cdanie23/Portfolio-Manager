package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

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
	
}
