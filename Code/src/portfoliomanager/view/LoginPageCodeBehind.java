package portfoliomanager.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import portfoliomanager.model.Account;
import portfoliomanager.viewmodel.LoginPageViewModel;
/**
 * The sign up page code behind
 * 
 * @author Colby
 * @author Sam
 * @author Liam
 * @version Spring 2025
 */

public class LoginPageCodeBehind implements Initializable {
	@FXML
	private ImageView logoImageView;
	@FXML
	private Label loginLabel;
	@FXML
	private TextField usernameField;
	@FXML
	private TextField passwordField;
	@FXML
	private Label loginButton;
	
    @FXML
    private LandingPageCodeBehind view;
    private boolean isLoggedIn;
	private LoginPageViewModel account;
	private SignUpPageCodeBehind signUpPageCodeBehind;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image image = new Image(getClass().getResource("/CryptoVaultLogo.jpg").toExternalForm());
        this.logoImageView.setImage(image);
        this.signUpPageCodeBehind = null;
        this.isLoggedIn = false;
	}

	/**
	 * Sets the data of the controller
	 * 
	 * @precondition none
	 * @postcondition this.account != null, data binding is setup
	 * 
	 * @param isLoggedIn login status of the user
	 * @param user the user that logged in the system
	 */

	public void setData(ObjectProperty<Boolean> isLoggedIn, Account user) {
		this.account = new LoginPageViewModel(isLoggedIn, user);
		this.bindDataElements();
	}

	@FXML
	private void handleLogin() {
		try {
			this.account.verifyLogin();
			this.isLoggedIn = this.account.getLoginStatus().get();
		} catch (Exception exception) {
			this.showAlert("Error", exception.getMessage());
		}
		if (this.isLoggedIn) {
			Stage stage = (Stage) this.loginButton.getScene().getWindow();
			this.enableAccountOptions();
			Event.fireEvent(stage, new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
			stage.close();
		}
	}

	@FXML
	void signUpClicked(MouseEvent event) {
		try {
			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/portfoliomanager/view/SignUpPage.fxml"));
			Pane root = loader.load();
			this.signUpPageCodeBehind = loader.getController();
			this.signUpPageCodeBehind.setLandingPageCodeBehind(this.view);
			Scene scene = new Scene(root, 376, 531);
			currentStage.setScene(scene);
			currentStage.show();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void bindDataElements() {
		this.account.getUserNameProperty().bindBidirectional(this.usernameField.textProperty());
		this.account.getPasswordProperty().bind(this.passwordField.textProperty());
	}
    
    private void enableAccountOptions() {
    	this.view.enableLogOutButtons();
    	this.view.enableTransactionAbility();
    	this.view.disableLogInButton();
    }
    
    /** Sets the LogInPageCodeBehind's LandingPageCodeBehind
     * @precondition nothing
     * @param view the view
     */
    public void setLandingPageCodeBehind(LandingPageCodeBehind view) {
    	this.view = view;
    }
}
