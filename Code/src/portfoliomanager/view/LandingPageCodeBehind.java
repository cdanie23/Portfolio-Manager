package portfoliomanager.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;
import portfoliomanager.client.TrendClient;
import portfoliomanager.model.Account;
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
    private Label welcomeUsernameLabel;
	@FXML
	private Label nameLabel;
	@FXML
	private Tab portfolioTabPage;

	@FXML
	private Label sellButton;
	
	@FXML
	private Label buyCryptoButton;
	
	@FXML
	private Label logInLandingLabel;
	
	@FXML
	private Ellipse logOutLandingButton;
	
	@FXML
	private Label logOutLandingLabel;

	@FXML
    private Label portfolioNameLabel;
	@FXML
    private Label welcomeLabel;
	
	@FXML
    private Label logOutPortfolioButton;

	@FXML
	private Label totalFundsLabel;
	
	@FXML
	private ObjectProperty<Holding> selectedHolding;
	private LandingPageViewModel viewModel;
	private LoginPageCodeBehind loginPageCodeBehind;
	
	@FXML
	private Label listNameLabel;
	@FXML
	private Label listPriceLabel;
	@FXML
	private Label listTrendLabel;

	private TrendClient trendSubscriber;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Image image = new Image(getClass().getResource("/CryptoVaultLogo.jpg").toExternalForm());
		this.logoImageView.setImage(image);
		this.viewModel = new LandingPageViewModel();
		this.selectedHolding = new SimpleObjectProperty<Holding>();
		this.setUpDataBinding();
		this.cryptoListView.refresh();
		this.portfolioTabPage.setDisable(true);
		this.setUpListeners();
		this.sellButton.setDisable(true);
		this.disableLogOutButtons();
		this.loginPageCodeBehind = null;
		this.trendSubscriber = new TrendClient();
		this.startTrendUpdates();
	}

	private void setUpDataBinding() {
		this.cryptoListView.setItems(this.viewModel.getCryptoListProperty());
		this.holdingsListView.setItems(this.viewModel.getHoldingsProperty());
		this.welcomeLabel.textProperty().bindBidirectional(this.viewModel.getWelcomeLabelProperty());
		this.totalFundsLabel.textProperty().bindBidirectional(this.viewModel.getFundsAvailabe());
		this.portfolioNameLabel.textProperty().bindBidirectional(this.viewModel.getPortfolioNameProperty());
		this.listNameLabel.textProperty().bind(this.viewModel.getNameLabel());
		this.listPriceLabel.textProperty().bind(this.viewModel.getPriceLabel());
		this.listTrendLabel.textProperty().bind(this.viewModel.getTrendLabel());
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
	}
	
	@FXML
	void logInClicked(MouseEvent event) {
		try {
			Stage primaryStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/portfoliomanager/view/LoginPage.fxml"));
			Parent root = loader.load();
			this.loginPageCodeBehind = loader.getController();
			this.loginPageCodeBehind.setLandingPageCodeBehind(this);
			Scene scene = new Scene(root, 376, 471);
			primaryStage.setScene(scene);
			primaryStage.setOnCloseRequest(_ -> {
				this.welcomeLabel.setLayoutX(280);
			});
			this.loginPageCodeBehind.setData(this.viewModel.getUser(), this.viewModel.getIsLoggedIn());
			primaryStage.show();
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@FXML
	void logOutClicked(MouseEvent event) {

		if (this.viewModel.handleLogout()) {
			this.disableLogOutButtons();
			this.disableTransactionAbility();
			this.enableLogInButton();
		}

	}

	@FXML
	void sellClicked(MouseEvent event) {
		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/portfoliomanager/view/SellPage.fxml"));
			Parent root = loader.load();
			SellPageCodeBehind controller = loader.getController();

			Stage stage = new Stage();
			controller.setData(this.viewModel.getUser().getValue(), this.selectedHolding.getValue(), this.viewModel.getFundsAvailabe(), this.holdingsListView);
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
			addFundController.setData(this.viewModel.getUser().getValue(), this.viewModel.getFundsAvailabe());
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
			buyCryptoController.setData(this.viewModel.getUser().getValue(), this.viewModel.getCryptoList(), this.viewModel.getHoldingsProperty(), this.viewModel.getFundsAvailabe());

			Stage stage = new Stage();
			stage.setScene(new Scene(root));
			stage.show();
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	@FXML
    private void sortByNameClicked() {
		try {
			this.viewModel.sortByName();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
    }

    @FXML
    private void sortByPriceClicked() {
    	try {
			this.viewModel.sortByPrice();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
    }

    @FXML
    private void sortByTrendClicked() {
    	try {
			this.viewModel.sortByTrend();
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
	public Label getPortfolioLogOutButton() {
		return this.logOutPortfolioButton;
	}
	
	/**
	 * Sets the logged in user
	 * 
	 * @param user logged in user
	 */
	public void setLoggedInUser(ObjectProperty<Account> user) {
    	this.enableLogOutButtons();
    	this.enableTransactionAbility();
    	this.disableLogInButton();
		this.viewModel.getUser().set(user.get());
		this.viewModel.getIsLoggedIn().set(true);
		this.viewModel.updateLabels();
	}
	
	/**
	 * Enables the log in button
	 */
	private void enableLogInButton() {
		this.logInButton.setDisable(false);
		this.logInButton.setVisible(true);
		this.logInLandingLabel.setDisable(false);
		this.logInLandingLabel.setVisible(true);
	}
	
	/**
	 * Disables the log in button
	 */
	private void disableLogInButton() {
		this.logInButton.setDisable(true);
		this.logInButton.setVisible(false);
		this.logInLandingLabel.setDisable(true);
		this.logInLandingLabel.setVisible(false);
		this.landingTabPage.getSelectionModel().select(this.portfolioTabPage);
	}

	/**
	 * Enables the portfolio page and buy button
	 */
	private void enableTransactionAbility() {
		this.portfolioTabPage.setDisable(false);
		this.buyCryptoButton.setDisable(false);
		this.buyCryptoButton.setVisible(true);
	}
	
	/**
	 * Enables the portfolio page and buy button
	 */
	private void disableTransactionAbility() {
		this.portfolioTabPage.setDisable(true);
		this.buyCryptoButton.setDisable(true);
		this.buyCryptoButton.setVisible(false);
	}
	
	/**
	 * Enables the log out button
	 */
	private void enableLogOutButtons() {
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
	private void disableLogOutButtons() {
		this.logOutLandingButton.setDisable(true);
		this.logOutLandingButton.setVisible(false);
		this.logOutLandingLabel.setDisable(true);
		this.logOutLandingLabel.setVisible(false);
		this.logOutPortfolioButton.setDisable(true);
		this.logOutPortfolioButton.setVisible(false);
		this.landingTabPage.getSelectionModel().select(this.cryptoTabPage);
	}
	
	private void startTrendUpdates() {
		this.trendSubscriber.start(trendUpdate -> {
			this.viewModel.updateCryptoPrice(this.cryptoListView.getItems(), trendUpdate);
			this.cryptoListView.refresh();
		});
	}
}
