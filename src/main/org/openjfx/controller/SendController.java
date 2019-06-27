package org.openjfx.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.openjfx.model.SceneLoader;
import org.openjfx.model.SerialPortService;
import org.openjfx.model.settings.PortSettings;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SendController implements Initializable {

    private SerialPortService portService;

    @FXML
    public Button btn_send;

    @FXML
    public  TextArea inputText;

    @FXML
    public Button btn_ping;

    @FXML
    public Button btn_return;

    @FXML
    public void send(ActionEvent event) throws Exception {
         portService.sendString(portService.getInitializedPort(new PortSettings()), inputText.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        portService = new SerialPortService();
    }

    @FXML
    public void ping(ActionEvent event) {
        portService.ping();
    }

    @FXML
    public void returnToSettings(ActionEvent event) throws Exception{
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneLoader.loadScene("view/main_scene.fxml",
                "SerialPortCommunication", stage);
    }

}
