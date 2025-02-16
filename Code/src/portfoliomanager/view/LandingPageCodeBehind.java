package portfoliomanager.view;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Ellipse;

/**
 * The code behind for the landing page of the user
 * 
 * @author Colby
 * @version Spring 2025
 */
public class LandingPageCodeBehind {

	@FXML
	private ListView<?> cryptoListView;

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
	void logInClicked(MouseEvent event) {
		System.out.println("login in clicked");
		// TODO implement the login in process
	}

	@FXML
	void signUpClicked(MouseEvent event) {
		System.out.println("sign up clicked");
		// TODO implement the sign up process
	}

}
