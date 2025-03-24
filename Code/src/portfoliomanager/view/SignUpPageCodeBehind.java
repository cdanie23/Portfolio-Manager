package portfoliomanager.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import portfoliomanager.viewmodel.SignUpPageViewModel;

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
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField passwordConfirmField;
    @FXML
    private Label signUpButton;
    private SignUpPageViewModel addAccount;
    
    @FXML
    private LandingPageCodeBehind view;
    private boolean isSignedUp;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image image = new Image(getClass().getResource("/CryptoVaultLogo.jpg").toExternalForm());
        this.logoImageView.setImage(image);
        this.addAccount = new SignUpPageViewModel();
        this.bindDataElements();
        this.isSignedUp = false;
	}
    
    @FXML
    private void handleSignUp() {
    	try {
    		this.addAccount.createAccount();
    		this.isSignedUp = this.addAccount.getSignedUpStatus();
    	} catch (Exception exception) {
    		this.showAlert("Error", exception.getMessage());
    	}
    	if (this.isSignedUp) {
    		Stage stage = (Stage) this.signUpButton.getScene().getWindow();
    		this.enableAccountOptions();
    		stage.close();
    	}
    }
    
    private void enableAccountOptions() {
    	this.view.enableLogOutButtons();
    	this.view.enableTransactionAbility();
    	this.view.disableLogInButton();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void bindDataElements() {
        this.addAccount.getUserNameProperty().bind(this.usernameField.textProperty());
        this.addAccount.getPasswordProperty().bind(this.passwordField.textProperty());
        this.addAccount.getPasswordConfirmProperty().bind(this.passwordConfirmField.textProperty());
    }
    
    /** Sets the SignUpPageCodeBehind's LandingPageCodeBehind
     * @precondition nothing
     * @param view the view
     */
    public void setLandingPageCodeBehind(LandingPageCodeBehind view) {
    	this.view = view;
    }
}
