package org.openjfx.controller;

import com.fazecast.jSerialComm.SerialPort;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.openjfx.model.settings.PortSettings;
import org.openjfx.model.SceneLoader;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    public ChoiceBox<Integer> baudRate;

    @FXML
    public ChoiceBox<Integer> dataFieldLength;

    @FXML
    public ChoiceBox<Integer> stop;

    @FXML
    public TextField terminatorChar;

    @FXML
    public ChoiceBox<String> port;

    @FXML
    public ChoiceBox<Character> parity;

    @FXML
    public ChoiceBox<String> terminator;

    @FXML
    public Button btn_master;

    @FXML
    public ChoiceBox<String> flowControl;

    @FXML
    public Button btn_slave;



    @FXML
    public void master_chosen(ActionEvent event) throws Exception {
        fillSettings();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneLoader.loadScene("view/master_mode.fxml", "MASTER", stage);
    }

    @FXML
    public void slave_chosen(ActionEvent event) throws Exception{
        fillSettings();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneLoader.loadScene("view/slave_mode.fxml", "SLAVE", stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> availableTerminators = FXCollections.observableArrayList("none","CR",
                "LF", "CR-LF","custom" );
        terminator.setItems(availableTerminators);
        terminator.getSelectionModel().select(0);

        SerialPort[] ports = SerialPort.getCommPorts();
        ObservableList<String> availablePorts = FXCollections.observableArrayList();
        for(SerialPort port: ports){
            availablePorts.add(port.getDescriptivePortName());
        }
        port.setItems(availablePorts);
        port.getSelectionModel().select(0);

        stop.setItems(FXCollections.observableArrayList(1,2));
        stop.getSelectionModel().select(0);
        parity.setItems(FXCollections.observableArrayList('E', 'O', 'N'));
        parity.getSelectionModel().select(0);
        dataFieldLength.setItems(FXCollections.observableArrayList(7,8));
        dataFieldLength.getSelectionModel().select(0);
        flowControl.setItems(FXCollections.observableArrayList("none", "hardware", "software"));
        flowControl.getSelectionModel().select(0);
        baudRate.setItems(FXCollections.observableArrayList(150, 300, 1200, 2400, 4800, 9600, 19200, 38400, 57600 ,117760));
        baudRate.getSelectionModel().select(0);

    }


    private void fillSettings(){
        PortSettings.baudRate = baudRate.getValue();
        PortSettings.dataFieldLength = dataFieldLength.getValue();
        PortSettings.flowControl = flowControl.getValue();
        PortSettings.parity = parity.getValue();
   //     PortSettings.port = SerialPort.getCommPort(port.getValue());
        PortSettings.port = port.getValue();
        PortSettings.stop = stop.getValue();
        switch (terminator.getValue()) {
            // jeśli none, to ustawimy poniżej tylko terminator na "none"
            case "CR": {
                PortSettings.terminatorChars.add((char)13);
                break;
            }
            case "LF": {
                PortSettings.terminatorChars.add((char)10);
                break;
            }
            case "CR-LF": {
                PortSettings.terminatorChars.add((char)13);
                PortSettings.terminatorChars.add((char)10);
                break;
            }
            case "custom": {
                String input = terminatorChar.getText(0, 2);
                for (int i = 0; i < input.length(); i++) {
                    PortSettings.terminatorChars.add(input.charAt(i));
                }
                break;
            }
        }
        PortSettings.terminator = terminator.getValue();

    }
}
