package portfoliomanager.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import portfoliomanager.viewmodel.SignUpPageViewModel;

public class TestSignUpPageViewModel {
	private SignUpPageViewModel page;
	
	@BeforeEach
	public void setUp() {
		this.page = new SignUpPageViewModel();
	}
	@Test
	public void testValidSignUpPageViewModelConstructor() {
		assertEquals("user", SignUpPageViewModel.getAccounts().get(0).getUserName());
		assertEquals("pass123", SignUpPageViewModel.getAccounts().get(0).getPassword());
	}
	
	@Test
	public void testCreateAccount()
	{
		this.page.getUserNameProperty().set("testuser");
		this.page.getPasswordProperty().set("testPassword123");
		this.page.getPasswordConfirmProperty().set("testPassword123");
		this.page.createAccount();
		
		assertAll(()-> assertEquals(2, SignUpPageViewModel.getAccounts().size()),
				()-> assertEquals("testuser", SignUpPageViewModel.getAccounts().get(1).getUserName()),
				()-> assertEquals("testPassword123", SignUpPageViewModel.getAccounts().get(1).getPassword()));
	}
	
	@Test
	public void testCreateAccountDuplication()
	{
		this.page.getUserNameProperty().set("testuser");
		this.page.getPasswordProperty().set("testPassword123");
		this.page.getPasswordConfirmProperty().set("testPassword123");
		assertThrows(IllegalArgumentException.class,()->{
			this.page.createAccount();
		});
	}
	@Test
	public void testWrongPassword() {
		this.page.getUserNameProperty().set("testuser");
		this.page.getPasswordProperty().set("testPassword123");
		this.page.getPasswordConfirmProperty().set("testPassword");
		assertThrows(IllegalArgumentException.class,()->{
			this.page.createAccount();
		});
	}
	@Test
	public void testNotSignedIn() {
		assertFalse(this.page.getSignedUpStatus());
	}
 }
