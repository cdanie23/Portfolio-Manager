package portfoliomanager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The Account Class.
 * @author Sam
 * @version Spring 2025
 */
public class Account {
	private String email;
	private String password;

	private List<Holding> holdings;
	private double fundsAvailable;
	/**
	 * Instantiates a new account with given email and password.
	 *
	 * @param email the email
	 * @param password the password
	 */
	public Account(String email, String password) {
		if (email.isBlank() || password.isBlank()) {
			throw new IllegalArgumentException("Email or password don't meet specified requirements.");
		}
		
		this.email = email;
		this.password = password;
		this.holdings = new ArrayList<Holding>();
	}
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}
	
	/**
	 * Sets the email.
	 *
	 * @param newEmail the new email
	 */
	public void setEmail(String newEmail) {
		if (newEmail.isBlank()) {
			throw new IllegalArgumentException("Email doesn't meet specified requirements.");
		}
		
		this.email = newEmail;
	}
	
	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}
	
	/**
	 * Sets the password given the old, current password and the new password to be changed to.
	 *
	 * @param oldP the old P
	 * @param newP the new P
	 */
	public void setPassword(String oldP, String newP) {
		if (oldP != this.password) {
			throw new IllegalArgumentException("The old password is incorrect.");
		}
		
		if (newP.isBlank()) {
			throw new IllegalArgumentException("The new password doesn't meet requirements.");
		}
		
		this.password = newP;
	}
	
	/**
	 * Gets the holdings of the account
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the list of holdings of the account (owned cryptos)
	 */
	public List<Holding> getHoldings() {
		return this.holdings;
	}
	
	/**
	 * Adds holding to the list of holdings
	 * 
	 * @precondition holding != null
	 * @postcondition this.holdings.size @post = this.holdings.size @pre + 1    
	 * 
	 * @param holding the holding to be added to the list
	 * @return true if added to the list
	 * 			false if not.
	 */
	public boolean addHolding(Holding holding) {
		return this.holdings.add(holding);
	}
	
	/**
	 * Gets the funds available for the account
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @return the funds available for the account
	 */
	public double getFundsAvailable() {
		return this.fundsAvailable;
	}
	
	/**
	 * Sets the fund available for the account
	 * 
	 * @precondition amount == null
	 * @postcondiiton this.fundsAvailable = amount
	 * 
	 * @param amount the funds available to set
	 */
	public void setFundsAvailable(double amount) {
		this.fundsAvailable = amount;
	}

}