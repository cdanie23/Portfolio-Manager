package portfoliomanager.test;

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
		assertEquals("user@email.com", this.page.getAccounts().get(0).getEmail());
		assertEquals("pass123", this.page.getAccounts().get(0).getPassword());
	}
	
	@Test
	public void testCreateAccount()
	{
		this.page.getEmailProperty().set("testuser@email.com");
		this.page.getPasswordProperty().set("testPassword123");
		
		this.page.createAccount();
		
		assertAll(()-> assertEquals(2, this.page.getAccounts().size()),
				()-> assertEquals("testuser@email.com", this.page.getAccounts().get(1).getEmail()),
				()-> assertEquals("testPassword123", this.page.getAccounts().get(1).getPassword()));
	}
	
	@Test
	public void testCreateAccountDuplication()
	{
		this.page.getEmailProperty().set("testuser@email.com");
		this.page.getPasswordProperty().set("testPassword123");
		
		assertThrows(IllegalArgumentException.class,()->{
			this.page.createAccount();
		});
	}
 }
