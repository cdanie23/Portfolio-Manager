package portfoliomanager.view;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import portfoliomanager.model.Account;
import portfoliomanager.viewmodel.AddFundsViewModel;

/**
 * Codebehind class for add funds fxml
 * 
 * @author Group 2
 * @version Spring 2025
 */
public class AddFundsCodeBehind {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField amountTextBox;
    
    @FXML
    private Label addFundsButton;
    
    private AddFundsViewModel viewModel;
    
    @FXML
    void addFundsClicked(MouseEvent event) {
    	try {
    		this.viewModel.addFunds();
    	} catch (Exception exception) {
    		Alert alert = new Alert(AlertType.ERROR);
    		alert.setContentText(exception.getMessage());
    		alert.showAndWait();
    	}
    	Stage stage = (Stage) this.addFundsButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {
    	this.setUpTextFilter();
    }
    
    private void setUpTextFilter() {
		UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.isEmpty()) {
                return change;
            }

            if (newText.matches("\\d*(\\.\\d{0,2})?")) {
                try {
                	Double value = Double.parseDouble(newText);
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
    
    /**
     * Sets the viewModel data
     * 
     * @param user the user accessing the view
     * @param fundsAvailable the property for funds
     */
    public void setData(Account user, StringProperty fundsAvailable) {
    	this.viewModel = new AddFundsViewModel(user, fundsAvailable);
    	this.viewModel.getAmountProperty().bind(this.amountTextBox.textProperty());
    }
}
