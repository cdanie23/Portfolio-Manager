package portfoliomanager.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import portfoliomanager.model.Holding;
import portfoliomanager.viewmodel.LandingPageViewModel;

/**
 * The code behind for the landing page of the user
 * 
 * @author Colby
 * @version Spring 2025
 */
public class LandingPageCodeBehind implements Initializable {

	@FXML
	private Button addFundsButton;

	@FXML
	private ListView<Crypto> cryptoListView;

	@FXML
	private Tab cryptoTabPage;

	@FXML
	private ListView<Holding> holdingsListView;

	@FXML
	private TabPane landingTabPage;

	@FXML
	private Ellipse logInButton;

	@FXML
	private ImageView logoImageView;

	@FXML
	private Button logoutButton;
	
	@FXML
	private Button buyCryptoButton;

	@FXML
	private Label nameLabel;

	@FXML
	private Tab portfolioTabPage;

	@FXML
	private Button sellButton;
	
	@FXML
	private Label logInPortfLabel;
	
	@FXML
	private Ellipse logOutPortfButton;
	
	@FXML
	private Label logOutPortfLabel;

	@FXML
	private Ellipse signUpButton;

	@FXML
	private Label totalFundsLabel;
	
	@FXML
	private ObjectProperty<Holding> selectedHolding;
	private ObjectProperty<Crypto> selectedCrypto;

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
		this.enableLogOutButtons();
		this.portfolioTabPage.setDisable(false);
	}
	
	private void disableLogOutButtons() {
		this.logOutPortfButton.setDisable(true);
		this.logOutPortfButton.setVisible(false);
		this.logOutPortfLabel.setDisable(true);
		this.logOutPortfLabel.setVisible(false);
		this.logoutButton.setDisable(true);
		this.logoutButton.setVisible(false);
	}

	private void enableLogOutButtons() {
		this.logOutPortfButton.setDisable(false);
		this.logOutPortfButton.setVisible(true);
		this.logOutPortfLabel.setDisable(false);
		this.logOutPortfLabel.setVisible(true);
		this.logoutButton.setDisable(false);
		this.logoutButton.setVisible(true);
	}

	@FXML
	void signUpClicked(MouseEvent event) {
		try {
			Stage primaryStage = new Stage();
			Pane root = FXMLLoader.load(getClass().getResource("/portfoliomanager/view/SignUpPage.fxml"));
			Scene scene = new Scene(root, 375, 420);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		this.enableLogOutButtons();
		this.portfolioTabPage.setDisable(false);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image image = new Image(getClass().getResource("/CryptoVaultLogo.jpg").toExternalForm());
		this.logoImageView.setImage(image);
		this.viewModel = new LandingPageViewModel();
		this.selectedHolding = new SimpleObjectProperty<Holding>();
		this.selectedCrypto = new SimpleObjectProperty<Crypto>();
		this.setUpDataBinding();
		this.portfolioTabPage.setDisable(true);
		this.setUpListeners();
		this.sellButton.setDisable(true);
		this.disableLogOutButtons();
	}



	private void setUpDataBinding() {
		this.cryptoListView.itemsProperty().bindBidirectional(this.viewModel.getCryptoListProperty()); 
		this.holdingsListView.itemsProperty().bindBidirectional(this.viewModel.getHoldingsProperty());
		this.totalFundsLabel.textProperty().bindBidirectional(this.viewModel.getFundsAvailabe());
	}

	private void setUpListeners() {
		this.holdingsListView.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
			if (newVal != null) {
				this.selectedHolding.setValue(newVal);
				this.sellButton.setDisable(false);
			} else {
				this.sellButton.setDisable(true);
			}
		});
		
		this.cryptoListView.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) -> {
			if (newVal != null) {
				this.selectedCrypto.setValue(newVal);
				this.buyCryptoButton.setDisable(false);
			} else {
				this.buyCryptoButton.setDisable(true);
			}
		});
	}

	@FXML
	void logOutClicked(MouseEvent event) {
		this.disableLogOutButtons();
		this.portfolioTabPage.setDisable(true);
	}

	@FXML
	void sellClicked(MouseEvent event) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/portfoliomanager/view/SellPage.fxml"));
			Parent root = loader.load();
			SellPageCodeBehind controller = loader.getController();

			Stage stage = new Stage();
			controller.setData(this.viewModel.getUser(), this.viewModel.getCryptoHoldings(),
					this.selectedHolding.getValue(), this.viewModel.getFundsAvailabe(), this.holdingsListView);
			controller.setUpCodeBehind();
			controller.setStage(stage);
			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@FXML
	void addFundsClicked(MouseEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/portfoliomanager/view/AddFunds.fxml"));
			Parent root = loader.load();
			AddFundsCodeBehind addFundController = loader.getController();
			Stage stage = new Stage();
			addFundController.setData(this.viewModel.getUser(), this.viewModel.getFundsAvailabe());
			stage.setScene(new Scene(root));
			stage.show();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	@FXML
	void buyButtonClicked(MouseEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/portfoliomanager/view/BuyCrypto.fxml"));
			Parent root = loader.load();
			BuyCryptoCodeBehind buyCryptoController = loader.getController();
			Stage stage = new Stage();
			buyCryptoController.setData(this.viewModel.getUser(), this.selectedCrypto, this.viewModel.getHoldingsProperty(), this.viewModel.getFundsAvailabe());
			stage.setScene(new Scene(root));
			stage.show();
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
