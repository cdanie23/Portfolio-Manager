package portfoliomanager.test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import portfoliomanager.model.Account;
import portfoliomanager.model.Holding;

public class TestAccount {
	private String username;
	private String password;
	private Account account;
	private String auth = "$123";
	@BeforeEach
	public void setUp() {
		this.username = "user";
		this.password = "pass123";
		
		this.account = new Account(this.username, this.password, auth);
	}
	
	@Test
	public void testValidAccountConstructor() {
		assertEquals("user", this.account.getUserName());
		assertEquals("pass123", this.account.getPassword());
		assertEquals("$123", this.account.getAuth());
	}
	
	@Test
	public void testValidSetEmail() {
		account.setUserName("user1");
		
		assertEquals("user1", account.getUserName());
	}
	
	@Test
	public void testValidSetPassword() {
		account.setPassword(this.password, "newPassword");
		
		assertEquals("newPassword", account.getPassword());
	}
	
	@Test
	public void testInvalidAccountConstructorEmptyUsername() {
		assertThrows(IllegalArgumentException.class, () -> new Account("", "pass123", "$123"));
	}
	
	@Test
	public void testInvalidAccountConstructorEmptyPassword() {
		assertThrows(IllegalArgumentException.class, () -> new Account("user", "", "$123"));
	}
	@Test
	public void testInvalidAccountConstructorEmptyAuth() {
		assertThrows(IllegalArgumentException.class, () -> new Account("user", "pass123", ""));
	}
	@Test
	public void testInvalidSetEmail() {
		assertThrows(IllegalArgumentException.class, () -> account.setUserName(""));
	}
	
	@Test
	public void testInvalidSetPasswordIncorrectPassword() {
		assertThrows(IllegalArgumentException.class, () -> account.setPassword("pass1234", "newPassword"));
	}
	
	@Test
	public void testInvalidSetPasswordBlankNewPassword() {
		assertThrows(IllegalArgumentException.class, () -> account.setPassword(password, ""));
	}
	@Test
	public void testGetsFundsAvailable() {
		this.username = "user";
		this.password = "pass123";
		this.account = new Account(this.username, this.password, this.auth);
		this.account.setFundsAvailable(1000);
		
		assertEquals(1000, this.account.getFundsAvailable());
	}
	@Test
	public void testAddHolding() {
		Holding holding = new Holding("btc", Double.valueOf(1000), 1);
		this.account.addHolding(holding);
		
		assertTrue(this.account.getHoldings().contains(holding));
	}
	
	@Test
	public void testAddHoldingSameName() {
		Holding holding = new Holding("btc", Double.valueOf(1000), 1);
		this.account.addHolding(holding);
		
		assertTrue(this.account.getHoldings().contains(holding));
		
		this.account.addHolding(holding);
		
		assertAll(()-> assertEquals(1, this.account.getHoldings().size()),
				()-> assertEquals(2, this.account.getHoldings().get(0).getAmountHeld()));
	}
}