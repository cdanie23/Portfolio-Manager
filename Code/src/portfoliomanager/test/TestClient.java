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
//	@Test
//	void testConstructor() {
//		assertAll(()-> assertEquals(client.getRequest(), null),
//				()-> assertEquals(null, client.getResponse()));
//	}
//	
//	@Test
//	void testNullRequest() {
//		assertThrows(IllegalArgumentException.class,()->{
//			this.client.makeRequest(null);
//		});
//	}
//	/**
//	 * @pre must have server running at the time of the call
//	 */
	@Test
	void testGetBtcPriceRequest() {
		this.client.makeRequest(Requests.btcPrice);
		System.out.println(this.client.getResponse().get("Price"));
	}
	
	@Test
	void testGetBtcPriceHistory() {
		this.client.makeRequest(Requests.btcHistory);
		System.out.println(this.client.getResponse().get("History"));
		
	}
}
