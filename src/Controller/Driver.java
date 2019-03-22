import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Driver extends Application{
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
//        this.primaryStage = primaryStage;
//        converterWindow();
        Parent root = FXMLLoader.load(getClass().getResource("ConverterGUI.fxml"));
        primaryStage.setTitle("Converter");
        primaryStage.setScene(new Scene(root, 900, 400)); /*.. length, width*/
        primaryStage.show();
    }

    public void converterWindow(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ConverterGUI.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Controller controller = loader.getController();
            controller.initialize();

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) { e.printStackTrace();}
    }

    public static void main(String[] args){
        launch(args);
    }
}
