package Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Driver extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
//        this.primaryStage = primaryStage;
//        converterWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/View/ConverterGUI.fxml"));
        primaryStage.setTitle("Converter");
        primaryStage.setScene(new Scene(root, 900, 400)); /*.. length, width*/
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
