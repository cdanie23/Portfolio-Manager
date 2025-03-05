package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import portfoliomanager.viewmodel.LoginPageViewModel;
import portfoliomanager.viewmodel.SignUpPageViewModel;

public class TestLoginPageViewModel {
	private LoginPageViewModel page;
	
	@BeforeEach
	public void setUp() {
		this.page = new LoginPageViewModel();
	}
	
	@Test
	public void testValidLoginPageViewModelConstructor() {
		assertEquals("user@email.com", this.page.getAccounts().get(0).getEmail());
		assertEquals("pass123", this.page.getAccounts().get(0).getPassword());
	}
	
	@Test
	public void testValidLoginPageViewModelConstructorEmptyAccounts() {
		SignUpPageViewModel.getAccounts().clear();
		
		assertTrue(SignUpPageViewModel.getAccounts().isEmpty());
		
		new LoginPageViewModel();
		
		assertFalse(SignUpPageViewModel.getAccounts().isEmpty());
	}
	
	@Test
	public void testValidVerifyAccount() {
		this.page.getEmailProperty().set("user@email.com");
		this.page.getPasswordProperty().set("pass123");
		
		this.page.verifyLogin();
		
		assertTrue(this.page.getLoginStatus());
	}
	
	@Test
	public void testInvalidVerifyAccountWrongEmail() {
		this.page.getEmailProperty().set("user1@email.com");
		this.page.getPasswordProperty().set("pass123");
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.page.verifyLogin();
		});
	}
	
	@Test
	public void testInvalidVerifyAccountWrongPassword() {
		this.page.getEmailProperty().set("user@email.com");
		this.page.getPasswordProperty().set("Pass123");
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.page.verifyLogin();
		});
	}
}