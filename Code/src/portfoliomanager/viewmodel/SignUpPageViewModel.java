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
	private static List<Account> accounts = new ArrayList<>();
	
	/**
	 * Instantiates a new sign up page view model.
	 */
	public SignUpPageViewModel() {
		if (SignUpPageViewModel.accounts.isEmpty()) {
			SignUpPageViewModel.accounts.add(new Account("user@email.com", "pass123"));
		}
	}
	
	/**
	 * Gets the list of accounts.
	 *
	 * @return the list of accounts
	 */
	public List<Account> getAccounts() {
		return SignUpPageViewModel.accounts;
	}
	
	/**
	 * Adds the created account to the list of accounts.
	 *
	 * @param email the new account's email
	 * @param password the new account's password
	 * @return true, if account is added to the list of accounts
	 */
	public boolean createAccount(String email, String password) {
		for (Account account : SignUpPageViewModel.accounts) {
	        if (account.getEmail().trim().equalsIgnoreCase(email.trim())) {
	            return false;
	        }
		}
		
		Account newAccount = new Account(email, password);
		SignUpPageViewModel.accounts.add(newAccount);
		
		System.out.println("Email " + email);
		for (Account account : SignUpPageViewModel.accounts) {
			System.out.println(account.getEmail());
		}
		
		return true;
	}
}
