package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Controller.Controller;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        try{
            Controller controller=new Controller();
            Stage stage=new Stage();
            stage.setTitle("My Search Engine");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("View.fxml").openStream());
            Scene scene = new Scene(root, 731.0, 485.0);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            View Main_control = fxmlLoader.getController();
            Main_control.setController(controller);
            stage.show();


        } catch (Exception e) {

        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
