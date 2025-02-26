package portfoliomanager.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
/**
 * The sign up page code behind 
 * @author Colby
 * @version Spring 2025
 */
public class SignUpPageCodeBehind  implements Initializable {

    @FXML
    private ImageView logoImageView;

    @FXML
    private Label signUpLabel;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image image = new Image(getClass().getResource("/CryptoVaultLogo.jpg").toExternalForm());
        this.logoImageView.setImage(image);
	}
    
    
    
}
