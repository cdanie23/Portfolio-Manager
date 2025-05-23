package portfoliomanager.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * The Account Class.
 * @author Sam
 * @version Spring 2025
 */
public class Account {
	private String username;
	private String password;
	private String auth;
	private List<Holding> holdings;
	private double fundsAvailable;
	
	/**
	 * Instantiates a new account.
	 *
	 * @param username the username
	 * @param password the password
	 * @param auth the token for the account
	 */
	public Account(String username, String password, String auth) {
		if (username.isBlank() || password.isBlank() || auth.isBlank()) {
			throw new IllegalArgumentException("Username or password don't meet specified requirements.");
		}
		this.auth = auth;
		this.username = username;
		this.password = password;
		this.holdings = new ArrayList<Holding>();
	}
	
	/**
	 * Instantiates a new account
	 * @param username the username
	 * @param password the password
	 * @param auth the auth token
	 * @param fundsAvailable the funds available to the account
	 * @post this.fundsAvaiable == fundsAvailable
	 */
	public Account(String username, String password, String auth, double fundsAvailable) {
		this(username, password, auth);
		this.fundsAvailable = fundsAvailable;
	}
	
	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return this.username;
	}

	/**
	 * Sets the user name.
	 *
	 * @param newName the new user name
	 */
	public void setUserName(String newName) {
		if (newName.isBlank()) {
			throw new IllegalArgumentException("Username doesn't meet specified requirements.");
		}
		
		this.username = newName;
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
	 * Gets the holding of the user
	 * @return the holding
	 */
	public List<Holding> getHoldings() {
		return this.holdings;
	}
	
	/**
	 * Adds the holding of the user 
	 * @param holding the holding to add
	 * @return true or false based on if added properly
	 */
	public boolean addHolding(Holding holding) {
		for (Holding currHolding : this.holdings) {
			if (currHolding != null && currHolding.getName().equals(holding.getName())) {
				currHolding.setAmountHeld(holding.getAmountHeld());
				return true;
			}
		}
		this.holdings.add(holding);
		
		return true;
	}
	
	/**
	 * Gets the funds available 
	 * @return the funds
	 */
	public double getFundsAvailable() {
		return this.fundsAvailable;
	}
	
	/**
	 * Sets the funds available
	 * @param amount the amount to set
	 */
	public void setFundsAvailable(double amount) {
		this.fundsAvailable = this.roundToTwoDecimalPlaces(amount);
	}
	
	private double roundToTwoDecimalPlaces(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	/**
	 * Gets the accounts auth token
	 * @return the auth token
	 */
	public String getAuth() {
		return this.auth;
	}
	
	/**
	 * Sets the accounts auth token.
	 *
	 * @param authtoken the new authtoken
	 */
	public void setAuth(String authtoken) {
		this.auth = authtoken;
	}
	/**
	 * Sets the users holdings
	 * @param holdings the holdings to set
	 * @post this.holdings == holdings
	 */
	
	public void setHoldings(List<Holding> holdings) {
		this.holdings = holdings;
	}
	
	@Override
	public boolean equals(Object object) {
		Account other = (Account) object;
		boolean equal = false;
		if (this.username.equals(other.getUserName())) {
			if (this.auth.equalsIgnoreCase(other.getAuth())) {
				equal = true;
			}
		}
		return equal;
	}

}