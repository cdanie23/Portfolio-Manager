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
import portfoliomanager.viewmodel.LoginPageViewModel;
/**
 * The sign up page code behind
 * 
 * @author Colby
 * @author Sam
 * @author Liam
 * @version Spring 2025
 */

public class LoginPageCodeBehind  implements Initializable {
    @FXML
    private ImageView logoImageView;
    @FXML
    private Label loginLabel;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;
    private LoginPageViewModel account;
    
    @FXML
    private LandingPageCodeBehind view;
    private boolean isLoggedIn;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image image = new Image(getClass().getResource("/CryptoVaultLogo.jpg").toExternalForm());
        this.logoImageView.setImage(image);
        this.account = new LoginPageViewModel();
        this.bindDataElements();
        this.isLoggedIn = false;
	}
    
    @FXML
    private void handleLogin() {
    	try {
    		this.account.verifyLogin();
    		this.isLoggedIn = this.account.getLoginStatus();
    	} catch (Exception exception) {
    		this.showAlert("Error", exception.getMessage());
    	}
    	
    	if (this.isLoggedIn) {
    		Stage stage = (Stage) this.loginButton.getScene().getWindow();
    		this.enableAccountOptions();
    		stage.close();
    	}
    }
    
    private void enableAccountOptions() {
    	this.view.enableLogOutButtons();
    	this.view.enableTransactionAbility();
    	this.view.disableLogInButton();
    	this.view.disableSignUpButton();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void bindDataElements() {
        this.account.getEmailProperty().bind(this.emailField.textProperty());
        this.account.getPasswordProperty().bind(this.passwordField.textProperty());
    }
    
    /** Sets the LogInPageCodeBehind's LandingPageCodeBehind
     * @precondition nothing
     * @param view the view
     */
    public void setLandingPageCodeBehind(LandingPageCodeBehind view) {
    	this.view = view;
    }
}
