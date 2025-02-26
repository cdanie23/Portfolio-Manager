package portfoliomanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import portfoliomanager.datareader.DataReader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;

/**
 * Entry Point for the program
 * 
 * @author Group 2
 * @version Spring 2025
 */


public class Main extends Application {
	private static final String FILEPATH = "resources/BTC-USD_data.txt";
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
	 * Primary Java entry point
	 * 
	 * @precondition none
	 * @postcondition none
	 * 
	 * @param args command line arguments

	public static void main(String[] args) {
		//Reads the data in
		DataReader reader  = new DataReader(FILEPATH);
		reader.readCryptoData();
		launch(args);
	}
}
