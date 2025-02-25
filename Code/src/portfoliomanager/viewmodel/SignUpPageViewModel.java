package portfoliomanager.viewmodel;

import java.util.ArrayList;
import java.util.List;

import portfoliomanager.model.Account;

/**
 * The Sign Up Page View Model
 * 
 * @author Sam
 * @version Spring 2025
 */
public class SignUpPageViewModel {
	private List<Account> accounts = new ArrayList<>();
	
	/**
	 * Instantiates a new sign up page view model.
	 */
	public SignUpPageViewModel() {
		this.accounts.add(new Account("user@email.com", "pass123"));
	}
	
	/**
	 * Gets the list of accounts.
	 *
	 * @return the list of accounts
	 */
	public List<Account> getAccounts() {
		return this.accounts;
	}
	
	/**
	 * Adds created accounts to the list of accounts.
	 *
	 * @param newAccount the new account
	 * @return true, if successful
	 */
	public boolean createAccount(Account newAccount) {
		for (Account account : this.accounts) {
			if (account.getEmail().equalsIgnoreCase(newAccount.getEmail())) {
				return false;
			}
		}
		
		this.accounts.add(newAccount);
		
		return true;
	}
}
