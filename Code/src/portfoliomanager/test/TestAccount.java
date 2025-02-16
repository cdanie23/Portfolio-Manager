package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import portfoliomanager.model.Account;

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
}