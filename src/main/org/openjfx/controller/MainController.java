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
    public Button btn_receive;

    @FXML
    public ChoiceBox<String> flowControl;

    @FXML
    public Button btn_send;



    @FXML
    public void receive_chosen(ActionEvent event) throws Exception {
        fillSettings();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneLoader.loadScene("view/receive_mode.fxml", "receive", stage);
    }

    @FXML
    public void send_chosen(ActionEvent event) throws Exception{
        fillSettings();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneLoader.loadScene("view/send_mode.fxml", "receive", stage);
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
        flowControl.setItems(FXCollections.observableArrayList("none", "DTR/DSR", "RTS/CTS", "XON/XOFF"));
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
        PortSettings.terminator = terminator.getValue();
        if(PortSettings.terminator.equals("custom"))
            PortSettings.terminatorChar = terminatorChar.getText(0,1);

    }
}
