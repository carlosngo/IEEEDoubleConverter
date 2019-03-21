package grp1Topic8;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Starter extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("DoublePrecision.fxml"));
        primaryStage.setTitle("Converter");
        primaryStage.setScene(new Scene(root, 900, 400)); /*.. length, width*/
        primaryStage.show();
		
	}

}
