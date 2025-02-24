package portfoliomanager.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import portfoliomanager.model.Account;

/**
 * The sign up page code behind
 * 
 * @author Colby
 * @author Sam
 * @version Spring 2025
 */
public class SignUpPageCodeBehind  implements Initializable {
    @FXML
    private ImageView logoImageView;
    @FXML
    private Label signUpLabel;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button signUpButton;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image image = new Image(getClass().getResource("/CryptoVaultLogo.jpg").toExternalForm());
        this.logoImageView.setImage(image);
	}
    
    @FXML
    private void handleSignUp() {
    	if (this.emailField.getText().isEmpty() || this.passwordField.getText().isEmpty()) {
    		this.showAlert("Error", "Email or password are empty.");
    		
    		return;
    	}
    	
    	String email = this.emailField.getText();
    	String password = this.passwordField.getText();
    	
    	Account account = new Account(email, password);
    	this.showAlert("Signed Up!", "Account email: " + email);
    	
    	Stage stage = (Stage) this.signUpButton.getScene().getWindow();
        stage.close();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
