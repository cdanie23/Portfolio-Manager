package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import portfoliomanager.model.Client;

class TestClient {
	
	@Test
	void testConstructor() {
		Client client = new Client("abc");
		assertAll(()-> assertEquals("abc", client.getRequest()),
				()-> assertEquals(null, client.getResponse()));
	}
	
	@Test
	void testNullRequest() {
		assertThrows(IllegalArgumentException.class,()->{
			new Client(null);
		});
	}
	
	@Test
	void testEmptyRequest() {
		assertThrows(IllegalArgumentException.class,()->{
			new Client("");
		});
	}
	
	@Test
	void testBlankRequest() {
		assertThrows(IllegalArgumentException.class,()->{
			new Client("   ");
		});
	}

}
