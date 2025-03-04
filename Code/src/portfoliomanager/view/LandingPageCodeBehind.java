package portfoliomanager.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;
import portfoliomanager.model.Crypto;
import portfoliomanager.viewmodel.LandingPageViewModel;

/**
 * The code behind for the landing page of the user
 * 
 * @author Colby
 * @version Spring 2025
 */
public class LandingPageCodeBehind implements Initializable {

	@FXML
	private ListView<Crypto> cryptoListView;

	@FXML
	private Tab cryptoTabPage;

	@FXML
	private TabPane landingTabPage;

	@FXML
	private Ellipse logInButton;

	@FXML
	private Tab portfolioTabPage;

	@FXML
	private Ellipse signUpButton;

	@FXML
	private ImageView logoImageView;
	
	private LandingPageViewModel viewModel;

	@FXML
	void logInClicked(MouseEvent event) {
		try {
			Stage primaryStage = new Stage();
			Pane root = FXMLLoader.load(getClass().getResource("/portfoliomanager/view/LoginPage.fxml"));
			Scene scene = new Scene(root, 375, 400);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@FXML
	void signUpClicked(MouseEvent event) {
		try {
			Stage primaryStage = new Stage();
			Pane root = FXMLLoader.load(getClass().getResource("/portfoliomanager/view/SignUpPage.fxml"));
			Scene scene = new Scene(root, 375, 400);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image image = new Image(getClass().getResource("/CryptoVaultLogo.jpg").toExternalForm());
        this.logoImageView.setImage(image);
        this.viewModel = new LandingPageViewModel();
        this.setUpDataBinding();
	}
	/**
	 * Sets up the data binding from the view model to view
	 */
	public void setUpDataBinding() {
		this.cryptoListView.setItems(this.viewModel.getCryptoCollection());
	}

}
