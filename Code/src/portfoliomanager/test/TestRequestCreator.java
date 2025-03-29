package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import portfoliomanager.client.Client;
import portfoliomanager.client.Requests;

class TestRequestCreator {
	Client client;
	
	@Test
	void testCreateRequest() {
		client = Client.getInstance();
		client.makeRequest(Requests.btcPrice);
		
		assertAll(()-> assertEquals("cryptos", client.getRequest()));
	}

}
