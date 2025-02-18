package portfoliomanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
/**
 * The main class of our application
 * @author Colby
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
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	/**
	 * The startup method of our main class
	 * @param args the args to start it with
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
