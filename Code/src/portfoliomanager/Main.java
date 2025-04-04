package portfoliomanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import portfoliomanager.client.Client;
import portfoliomanager.client.Requests;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;

/**
 * Entry Point for the program
 * 
 * @author Group 2
 * @version Spring 2025
 */

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			TabPane root = FXMLLoader.load(getClass().getResource("/portfoliomanager/view/LandingPage.fxml"));
			Scene scene = new Scene(root, 750, 450);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setOnCloseRequest((_) -> {
				Client client = Client.getInstance();
				client.makeRequest(Requests.exit);
			});
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * The main function 
	 * @param args the args to start the function with
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
