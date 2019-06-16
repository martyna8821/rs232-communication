package org.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openjfx.model.SceneLoader;


public class App extends Application {

    @Override
    public void start(Stage stage) throws  Exception{
        SceneLoader.loadScene("view/main_scene.fxml",
                "SerialPortCommunication", stage);

    }
    public static void main(String[] args) {
        launch();
    }

}
