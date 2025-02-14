package portfoliomanager.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;

/**
 * Code Behind class
 * 
 * @author Group 2
 * @version Spring 2025
 */
public class CodeBehind {
	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<?, ?> cryptoList;

    @FXML
    private TableColumn<?, ?> priceList;

    @FXML
    void initialize() {
        assert this.cryptoList != null : "fx:id=\"cryptoList\" was not injected: check your FXML file 'PortfolioManager.fxml'.";
        assert this.priceList != null : "fx:id=\"priceList\" was not injected: check your FXML file 'PortfolioManager.fxml'.";
    }
    
    private void initializeElements() {
    	//TODO binding
    }
}
