package org.openjfx.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openjfx.App;

public class SceneLoader {

public static void loadScene(String sceneFileName, String windowTitle, Stage stage)
        throws Exception{
    Parent root = FXMLLoader.load(App.class.getResource(sceneFileName));
    Scene scene = new Scene(root);
    stage.setTitle(windowTitle);
    stage.setScene(scene);
    stage.show();

}

}
