package portfoliomanager.view;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import portfoliomanager.model.Account;
import portfoliomanager.model.Crypto;
import portfoliomanager.viewmodel.BuyCryptoViewModel;

/**
 * CodeBehind class for buy crypto fxml
 * 
 * @author Group 
 * @version Spring 2025
 */
public class BuyCryptoCodeBehind {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField amountTextBox;

    @FXML
    private Button buyCryptoButton;

    @FXML
    private Label cryptoDetails;
    
    private BuyCryptoViewModel viewModel;

    @FXML
    void cancelButtonClicked(ActionEvent event) {
    	Stage stage = (Stage) this.buyCryptoButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void buyCryptoButtonClicked(ActionEvent event) {
    	try {
    		this.viewModel.buyCrypto();
    	} catch (Exception exception) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setContentText(exception.getMessage());
    		alert.showAndWait();
    	}
    	Stage stage = (Stage) this.buyCryptoButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {
    	this.setUpTextFilter();
    }
    
    /**
     * Passes in data to the viewModel
     * 
     * @param user the user that tries to buy crypto
     * @param selectedCrypto the crypto to be bought
     */
    public void setData(Account user, ObjectProperty<Crypto> selectedCrypto) {
    	this.viewModel = new BuyCryptoViewModel(user, selectedCrypto);
    	this.viewModel.getAmountProperty().bind(this.amountTextBox.textProperty());
    	this.cryptoDetails.textProperty().bind(this.viewModel.getCryptoDetailsProperty());
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
                    if (value >= 0) {
                        return change; 
                    }
                } catch (NumberFormatException ignored) { }
            }
            return null; 
        };
        TextFormatter<String> amountToSellFormatter = new TextFormatter<>(integerFilter);
        this.amountTextBox.setTextFormatter(amountToSellFormatter);
    }

}

