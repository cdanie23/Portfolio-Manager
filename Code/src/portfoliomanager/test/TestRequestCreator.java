package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

import portfoliomanager.client.RequestCreator;
import portfoliomanager.client.Requests;

class TestRequestCreator {
	
	@Test
	void testCreateLoginRequest() {
		RequestCreator requestCreator = new RequestCreator();
		Map<String,String> result = requestCreator.createAuthRequest(Requests.login, "abc", "pass", null);
		assertAll(()-> assertEquals("login", result.get("type")),
				()-> assertEquals("abc", result.get("username")),
				()-> assertEquals("pass", result.get("password")));
	}
	
	@Test
	void testCreateSignUpRequest() {
		RequestCreator requestCreator = new RequestCreator();
		Map<String,String> result = requestCreator.createAuthRequest(Requests.login, "abc", "pass", "pass");
		assertAll(()-> assertEquals("login", result.get("type")),
				()-> assertEquals("abc", result.get("username")),
				()-> assertEquals("pass", result.get("password")),
				()-> assertEquals("pass", result.get("confirmPassword")));
	}
	
	@Test
	void testCreateBtcPriceRequest() {
		RequestCreator requestCreator = new RequestCreator();
		Map<String,String> result = requestCreator.createRequest(Requests.btcPrice);
		assertAll(()-> assertEquals("btcPrice", result.get("type")));
	}
	
	@Test
	void testCreateBtcHistoryRequest() {
		RequestCreator requestCreator = new RequestCreator();
		Map<String,String> result = requestCreator.createRequest(Requests.btcHistory);
		assertAll(()-> assertEquals("btcHistory", result.get("type")));
	}
	
	@Test
	void testCreateExitRequest() {
		RequestCreator requestCreator = new RequestCreator();
		Map<String,String> result = requestCreator.createRequest(Requests.exit);
		assertAll(()-> assertEquals("exit", result.get("type")));
	}
}
