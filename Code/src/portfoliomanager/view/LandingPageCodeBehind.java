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
	private Label nameLabel;

	@FXML
	private Tab portfolioTabPage;

	@FXML
	private Button sellButton;
	
	@FXML
	private Button buyCryptoButton;
	
	@FXML
	private Label logInLandingLabel;
	
	@FXML
	private Ellipse logOutLandingButton;
	
	@FXML
	private Label logOutLandingLabel;
	
	@FXML
	private Label signUpLandingLabel;

	@FXML
	private Ellipse signUpButton;
	
	@FXML
	private Button logOutPortfolioButton;

	@FXML
	private Label totalFundsLabel;
	@FXML
	private ObjectProperty<Holding> selectedHolding;
	private ObjectProperty<Crypto> selectedCrypto;
	
	private LandingPageViewModel viewModel;
	private LoginPageCodeBehind loginPageCodeBehind;
	private SignUpPageCodeBehind signUpPageCodeBehind;

	@FXML
	void logInClicked(MouseEvent event) {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/portfoliomanager/view/LoginPage.fxml"));
			Pane root = loader.load();
			this.loginPageCodeBehind = loader.getController();
			this.loginPageCodeBehind.setLandingPageCodeBehind(this);
			Scene scene = new Scene(root, 375, 400);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}
	
	/**
	 * Enables the sign up button
	 */
	public void enableSignUpButton() {
		this.signUpButton.setDisable(false);
		this.signUpButton.setVisible(true);
		this.signUpLandingLabel.setDisable(false);
		this.signUpLandingLabel.setVisible(true);
	}
	
	/**
	 * Disables the sign up button
	 */
	public void disableSignUpButton() {
		this.signUpButton.setDisable(true);
		this.signUpButton.setVisible(false);
		this.signUpLandingLabel.setDisable(true);
		this.signUpLandingLabel.setVisible(false);
	}
	
	/**
	 * Enables the log in button
	 */
	public void enableLogInButton() {
		this.logInButton.setDisable(false);
		this.logInButton.setVisible(true);
		this.logInLandingLabel.setDisable(false);
		this.logInLandingLabel.setVisible(true);
	}
	
	/**
	 * Disables the log in button
	 */
	public void disableLogInButton() {
		this.logInButton.setDisable(true);
		this.logInButton.setVisible(false);
		this.logInLandingLabel.setDisable(true);
		this.logInLandingLabel.setVisible(false);
	}

	/**
	 * Enables the portfolio page and buy button
	 */
	public void enableTransactionAbility() {
		this.portfolioTabPage.setDisable(false);
		this.buyCryptoButton.setDisable(false);
		this.buyCryptoButton.setVisible(true);
	}
	
	/**
	 * Enables the portfolio page and buy button
	 */
	public void disableTransactionAbility() {
		this.portfolioTabPage.setDisable(true);
		this.buyCryptoButton.setDisable(false);
		this.buyCryptoButton.setVisible(false);
	}
	
	/**
	 * Enables the log out button
	 */
	public void enableLogOutButtons() {
		this.logOutLandingButton.setDisable(false);
		this.logOutLandingButton.setVisible(true);
		this.logOutLandingLabel.setDisable(false);
		this.logOutLandingLabel.setVisible(true);
		this.logOutPortfolioButton.setDisable(false);
		this.logOutPortfolioButton.setVisible(true);
	}
	
	/**
	 * Disables the log out button
	 */
	public void disableLogOutButtons() {
		this.logOutLandingButton.setDisable(true);
		this.logOutLandingButton.setVisible(false);
		this.logOutLandingLabel.setDisable(true);
		this.logOutLandingLabel.setVisible(false);
		this.logOutPortfolioButton.setDisable(true);
		this.logOutPortfolioButton.setVisible(false);
	}
	
	@FXML
	void signUpClicked(MouseEvent event) {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/portfoliomanager/view/SignUpPage.fxml"));
			Pane root = loader.load();
			this.signUpPageCodeBehind = loader.getController();
			this.signUpPageCodeBehind.setLandingPageCodeBehind(this);
			Scene scene = new Scene(root, 375, 420);
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
		this.selectedHolding = new SimpleObjectProperty<Holding>();
		this.selectedCrypto = new SimpleObjectProperty<Crypto>();
		this.setUpDataBinding();
		this.portfolioTabPage.setDisable(true);
		this.setUpListeners();
		this.sellButton.setDisable(true);
		this.disableLogOutButtons();
		this.buyCryptoButton.setDisable(true);
		this.buyCryptoButton.setVisible(false);
		this.loginPageCodeBehind = null;
		this.signUpPageCodeBehind = null;
	}

	private void update() {
		this.cryptoListView.setItems(this.viewModel.getCryptoCollection());
		this.holdingsListView.setItems(FXCollections.observableList(this.viewModel.getCryptoHoldings()));
	}

	private void setUpDataBinding() {
		this.cryptoListView.setItems(this.viewModel.getCryptoCollection());
		this.holdingsListView.setItems(FXCollections.observableList(this.viewModel.getCryptoHoldings()));
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
		this.disableTransactionAbility();
		this.enableLogInButton();
		this.enableSignUpButton();
	}

	@FXML
	void sellClicked(MouseEvent event) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/portfoliomanager/view/SellPage.fxml"));
			Parent root = loader.load();
			SellPageCodeBehind controller = loader.getController();

			Stage stage = new Stage();
			stage.setOnCloseRequest(_ -> {
				System.out.println("closed");
				this.update();
			});
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
	
	/** Returns the landing page's log out label
	 * 
	 * @return this.logOutLandingLabel
	 */
	public Label getLandingLogOutLabel() {
		return this.logOutLandingLabel;
	}
	
	/** Returns the landing page's log out ellipse
	 * 
	 * @return this.logOutLandingLabel
	 */
	public Ellipse getLandingLogOutEllipse() {
		return this.logOutLandingButton;
	}
	
	/** Returns the portfolio page's log out button
	 * 
	 * @return this.logOutButton
	 */
	public Button getPortfolioLogOutButton() {
		return this.logOutPortfolioButton;
	}
}
