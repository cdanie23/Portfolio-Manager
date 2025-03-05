package portfoliomanager.viewmodel;

import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import portfoliomanager.model.Account;
import portfoliomanager.model.Holding;

public class SellPageViewModel {
	private StringProperty amountToSell;
	private Account user;
	private Holding holdingToSell;
	private List<Holding> holdings;
	private StringProperty fundsAvailable;
	
	
	public SellPageViewModel(Account user, Holding holdingToSell, List<Holding> holdings, StringProperty fundsAvailable) {
		this.amountToSell = new SimpleStringProperty();
		this.fundsAvailable = fundsAvailable;
		this.user = user;
		this.holdingToSell = holdingToSell;
		this.holdings = holdings;
		
	}
	
	public StringProperty getAmountToSell() {
		return this.amountToSell;
	}
	public double getAmountLeft() {
		double amountToSell = Integer.parseInt(this.amountToSell.getValue());
		
		return this.holdingToSell.getAmountHeld() - amountToSell;
	}
	
	public double getProfit() {
		double amountToSell = Integer.parseInt(this.amountToSell.getValue());
		return this.holdingToSell.getProfit(amountToSell);
	}
	
	public Holding getHoldingToSell() {
		return this.holdingToSell;
	}
	
	public void sellCrypto() {
		int indexOfHolding = this.holdings.indexOf(this.holdingToSell);
		Holding holding = this.holdings.get(indexOfHolding);
		holding.setAmountHeld(this.getAmountLeft());
		if (holding.getAmountHeld() == 0) {
			this.holdings.remove(holding);
		}
		double totalFunds = this.user.getFundsAvailable() + this.getProfit();
		this.user.setFundsAvailable(totalFunds);
		this.fundsAvailable.setValue("Funds available: $" + totalFunds);
	}
	
}
