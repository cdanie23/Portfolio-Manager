package portfoliomanager.test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import portfoliomanager.model.Account;
import portfoliomanager.model.Holding;

public class TestAccount {
	private String email;
	private String password;
	private Account account;
	
	@BeforeEach
	public void setUp() {
		this.email = "user@email.com";
		this.password = "pass123";
		this.account = new Account(this.email, this.password);
	}
	
	@Test
	public void testValidAccountConstructor() {
		assertEquals("user@email.com", this.account.getEmail());
		assertEquals("pass123", this.account.getPassword());
	}
	
	@Test
	public void testValidSetEmail() {
		account.setEmail("user1@email.com");
		
		assertEquals("user1@email.com", account.getEmail());
	}
	
	@Test
	public void testValidSetPassword() {
		account.setPassword(this.password, "newPassword");
		
		assertEquals("newPassword", account.getPassword());
	}
	
	@Test
	public void testInvalidAccountConstructorEmptyEmail() {
		assertThrows(IllegalArgumentException.class, () -> new Account("", "pass123"));
	}
	
	@Test
	public void testInvalidAccountConstructorEmptyPassword() {
		assertThrows(IllegalArgumentException.class, () -> new Account("user@email.com", ""));
	}
	
	@Test
	public void testInvalidSetEmail() {
		assertThrows(IllegalArgumentException.class, () -> account.setEmail(""));
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
		this.email = "user@email.com";
		this.password = "pass123";
		this.account = new Account(this.email, this.password);
		this.account.setFundsAvailable(1000);
		
		assertEquals(1000, this.account.getFundsAvailable());
	}
	@Test
	public void testAddHolding() {
		Holding holding = new Holding("btc", Double.valueOf(1000), 1);
		this.account.addHolding(holding);
		
		assertTrue(this.account.getHoldings().contains(holding));
	}
}