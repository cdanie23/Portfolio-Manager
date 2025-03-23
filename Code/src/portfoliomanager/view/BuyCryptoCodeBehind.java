package portfoliomanager.view;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import portfoliomanager.model.Account;
import portfoliomanager.model.Crypto;
import portfoliomanager.model.Holding;
import portfoliomanager.viewmodel.BuyCryptoViewModel;

/**
 * CodeBehind class for buy crypto fxml
 * 
 * @author Group 2
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
    private Label buyCryptoButton;

    @FXML
    private Label cryptoDetails;
    
    @FXML
    private ListView<Crypto> buyCryptoListView;
    
    @FXML
    private LineChart<String, Double> lineGraph;
    
    @FXML
    private ComboBox<String> rangeSelection;

	private ObjectProperty<Crypto> selectedCrypto;
    
    private BuyCryptoViewModel viewModel;

    @FXML
    void cancelButtonClicked(MouseEvent event) {
    	Stage stage = (Stage) this.buyCryptoButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void buyCryptoButtonClicked(MouseEvent event) {
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
    	this.selectedCrypto = new SimpleObjectProperty<Crypto>();
    	this.setUpListeners();
    	this.buyCryptoButton.setVisible(false);
    	this.rangeSelection.getItems().addAll("3 days", "7 days", "30 days", "180 days", "375 days");
    	this.rangeSelection.getSelectionModel().select("30 days");
    	this.rangeSelection.setOnAction(_ -> this.updateLineChart());
    }
    
    /**
     * Passes in data to the viewModel
     * 
     * @param user the user that tries to buy crypto
     * @param cryptoList list of the cryptos
     * @param holdingsProperty observable list of holdings
     * @param fundsAvailable string property for the funds
     */
    public void setData(Account user, ObservableList<Crypto> cryptoList, ListProperty<Holding> holdingsProperty, StringProperty fundsAvailable) {
    	this.viewModel = new BuyCryptoViewModel(user, cryptoList, holdingsProperty, fundsAvailable);
    	this.buyCryptoListView.itemsProperty().bindBidirectional(this.viewModel.getCryptoList());
    	this.viewModel.getAmountProperty().bind(this.amountTextBox.textProperty());
    	this.lineGraph.getData().add(this.viewModel.getLineChartSeriesProperty());
    	CategoryAxis xAxis = (CategoryAxis) this.lineGraph.getXAxis();
    	xAxis.setAutoRanging(true);
    }
    
    private void setUpListeners() {
		this.buyCryptoListView.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
			if (newVal != null) {
				this.selectedCrypto.setValue(newVal);
				this.viewModel.getSelectedCrypto().bind(this.selectedCrypto);
				this.viewModel.updateLineChart(this.rangeSelection.getSelectionModel().getSelectedItem());
				this.buyCryptoButton.setVisible(true);
			} else {
				this.buyCryptoButton.setVisible(false);
			}
		});
	}
    
    private void updateLineChart() {
    	String response = this.rangeSelection.getSelectionModel().getSelectedItem();
    	this.viewModel.updateLineChart(response);
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

