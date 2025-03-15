package portfoliomanager.view;

import java.util.List;

import java.util.function.UnaryOperator;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import portfoliomanager.model.Account;
import portfoliomanager.model.Holding;
import portfoliomanager.viewmodel.SellPageViewModel;

/**
 * The sell page code behind
 * 
 * @author Colby
 * @version Spring 2024
 */
public class SellPageCodeBehind {
	@FXML
    private Label amountHeldLabel;

    @FXML
    private Label amountLeftLabel;
	@FXML
	private Label sellCryptoLabel;
	@FXML
	private TextField amountTextBox;

	@FXML
	private Button cancelButton;

	@FXML
	private Button confirmButton;
	
	@FXML
    private Label profitLabel;
	private SellPageViewModel viewModel;

	private StringProperty amountStringProperty;
	private Stage stage;
	private ListView<Holding> holdingsListView;
	
	/**
	 * Sets up the properties needed for the sell page code behind
	 * @post confirmButton disabled, !sellCryptoLabel.textProperty().getValue().IsEmpty(), this.amountStringProperty != null,
	 *       !this.amountHeldLabel.textProperty().isEmpty(), !this.amountLeftLabel.textProperty.isEmpty(), !this.profitLabel.textProperty().isEmpty()
	 */
	public void setUpCodeBehind() {
		this.confirmButton.setDisable(true);
		this.sellCryptoLabel.textProperty().setValue("Sell: " + this.viewModel.getHoldingToSell().getName());
		
		this.amountStringProperty = new SimpleStringProperty();
		
		this.amountHeldLabel.textProperty().setValue("Amount Held: " + this.viewModel.getHoldingToSell().getAmountHeld());
		this.amountLeftLabel.textProperty().setValue("Amount left: " + this.viewModel.getHoldingToSell().getAmountHeld());
		this.profitLabel.textProperty().setValue("Profit: " + 0);
		
		this.setUpDataBinding();
		this.setUpListeners();
		this.setUpTextFilter();
	}

	@FXML
	void cancelClicked(MouseEvent event) {
	    this.stage.close();
	}

	@FXML
	void confirmClicked(MouseEvent event) {
		this.viewModel.sellCrypto();
		this.update();
	    this.stage.close();
	}
	
	private void update() {
		this.holdingsListView.setItems(FXCollections.observableArrayList(this.viewModel.getHoldings()));
	}
	
	private void setUpDataBinding() {
		this.amountStringProperty.bindBidirectional(this.viewModel.getAmountToSell());
		
	}

	private void setUpListeners() {
		this.amountTextBox.textProperty().addListener((_, oldVal, newVal) -> {
			if (newVal != oldVal && !newVal.isEmpty() && Integer.parseInt(newVal) != 0) {
				this.amountStringProperty.setValue(newVal);
				this.amountLeftLabel.textProperty().setValue("Amount left: " + String.valueOf(this.viewModel.getAmountLeft()));
				this.profitLabel.textProperty().setValue("Profit: " + this.viewModel.getProfit());
				this.confirmButton.setDisable(false);
			} else {
				this.confirmButton.setDisable(true);
			}
		});
	}
	
	private void setUpTextFilter() {
		UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change;
            }

            if (newText.matches("[0-9]*")) {
                try {
                    int value = Integer.parseInt(newText);
                    if (value >= 0 && value <= this.viewModel.getHoldingToSell().getAmountHeld()) {
                        return change; 
                    }
                } catch (NumberFormatException ignored) { }
            }
            return null; 
        };
        TextFormatter<String> amountToSellFormatter = new TextFormatter<>(integerFilter);
        this.amountTextBox.setTextFormatter(amountToSellFormatter);
	}
	
	/**
	 * Sets the data when launching the page passing information from the LandingPageCodeBehind to this
	 * @param user the user 
	 * @param holdings the holdings of user
	 * @param holding the holding selected to sell
	 * @param fundsAvailable the string property of funds available of the user
	 * @param holdingsListView the list view of holdings from the PortfolioPage
	 * 
	 * @post this.holdingsListView == holdingsListView, this.viewModel != null
	 */
	public void setData(Account user, List<Holding> holdings, Holding holding, StringProperty fundsAvailable, ListView<Holding> holdingsListView) {
		this.holdingsListView = holdingsListView;
		this.viewModel = new SellPageViewModel(user, holding, holdings, fundsAvailable);
	}
	
	/**
	 * Sets the stage of the Code behind
	 * @param stage the stage to set 
	 * @post this.stage == stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
