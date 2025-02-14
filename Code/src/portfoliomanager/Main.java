package portfoliomanager;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
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
			TabPane root = FXMLLoader.load(getClass().getResource("/portfoliomanager/view/PortfolioManager.fxml"));
			Scene scene = new Scene(root, 400, 400);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	/**
	 * Primary Java entry point
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
