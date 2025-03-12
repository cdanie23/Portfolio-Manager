package portfoliomanager.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.SimpleObjectProperty;
import portfoliomanager.model.Account;
import portfoliomanager.viewmodel.LoginPageViewModel;
import portfoliomanager.viewmodel.SignUpPageViewModel;

public class TestLoginPageViewModel {
	private LoginPageViewModel page;
	
	@BeforeEach
	public void setUp() {
		this.page = new LoginPageViewModel(new SimpleObjectProperty<Boolean>(false), new Account("Sam", "pw"));
	}
	
	@Test
	public void testValidLoginPageViewModelConstructor() {
		assertEquals("user", this.page.getAccounts().get(0).getUserName());
		assertEquals("pass123", this.page.getAccounts().get(0).getPassword());
		assertEquals("Sam", this.page.getUser().getUserName());
		assertEquals("pw", this.page.getUser().getPassword());
	}
	
	@Test
	public void testValidLoginPageViewModelConstructorEmptyAccounts() {
		SignUpPageViewModel.getAccounts().clear();
		
		assertTrue(SignUpPageViewModel.getAccounts().isEmpty());
		
		new LoginPageViewModel(new SimpleObjectProperty<Boolean>(false), new Account("Sam", "pw"));
		
		assertFalse(SignUpPageViewModel.getAccounts().isEmpty());
	}
	
	@Test
	public void testValidVerifyAccount() {
		this.page.getUserNameProperty().set("user");
		this.page.getPasswordProperty().set("pass123");
		
		this.page.verifyLogin();
		
		assertTrue(this.page.getLoginStatus().getValue());
	}
	
	@Test
	public void testInvalidVerifyAccountWrongEmail() {
		this.page.getUserNameProperty().set("user1");
		this.page.getPasswordProperty().set("pass123");
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.page.verifyLogin();
		});
	}
	
	@Test
	public void testInvalidVerifyAccountWrongPassword() {
		this.page.getUserNameProperty().set("user");
		this.page.getPasswordProperty().set("Pass123");
		
		assertThrows(IllegalArgumentException.class, () -> {
			this.page.verifyLogin();
		});
	}
}