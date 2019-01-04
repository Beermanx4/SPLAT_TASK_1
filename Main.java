package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main_window.fxml"));
        primaryStage.setTitle("Task 1");
        primaryStage.setScene(new Scene(root, 640, 480));
        root.minHeight(480);
        root.minWidth(640);
        primaryStage.minHeightProperty().setValue(480);
        primaryStage.minWidthProperty().setValue(640);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
