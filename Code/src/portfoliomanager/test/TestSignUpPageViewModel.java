package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import portfoliomanager.model.Account;
import portfoliomanager.viewmodel.SignUpPageViewModel;

public class TestSignUpPageViewModel {
	private SignUpPageViewModel page;
	
	@BeforeEach
	public void setUp() {
		this.page = new SignUpPageViewModel();
	}
	
	@Test
	public void testValidSignUpPageViewModelConstructor() {
		Account account = page.getAccounts().get(0);
		
		assertEquals("user@email.com", account.getEmail());
		assertEquals("pass123", account.getPassword());
	}
	
	@Test
	public void testValidCreateAccount() {
		Account account = new Account("user1@email.com", "pass123");
		
		assertTrue(this.page.createAccount(account));
		assertEquals(account, this.page.getAccounts().get(1));
	}
	
	@Test
	public void testInvalidCreateAccountDuplicateEmail() {
		Account account = new Account("user@email.com", "pass123");
		
		assertFalse(this.page.createAccount(account));
	}
}
