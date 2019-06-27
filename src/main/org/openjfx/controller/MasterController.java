package org.openjfx.controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.openjfx.model.SceneLoader;
import org.openjfx.model.SerialPortService;
import org.openjfx.model.settings.MasterSettings;
import org.openjfx.model.settings.PortSettings;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MasterController implements Initializable {

    private SerialPortService portService;

    @FXML
    public ChoiceBox<Integer> retransmitionNumber;

    @FXML
    public Button btn_send;

    @FXML
    public  TextArea inputText;

    @FXML
    public  ChoiceBox<String> frameSpace;

    @FXML
    public ChoiceBox<String> timeout;

    @FXML
    public ChoiceBox<Integer> slaveAddress;

    @FXML
    public Button btn_ping;

    @FXML
    public Button btn_return;

    @FXML
    public void send(ActionEvent event) throws Exception {
        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        double frameSpaceValue = format.parse(frameSpace.getValue()).doubleValue();
        double timeoutValue = format.parse(timeout.getValue()).doubleValue();

        MasterSettings settings = new MasterSettings(retransmitionNumber.getValue(), timeoutValue, frameSpaceValue,
              inputText.getText(), slaveAddress.getValue());

        String messageToSend = inputText.getText() + appendTerminatorCharacters(PortSettings.getTerminatorChars());
        //portService.SendString(settings, messageToSend);

    }

    private String appendTerminatorCharacters(List<Character> terminatorCharacters) {
        String result = "";
        for(Character character : terminatorCharacters) {
            result += character;
        }
        return result;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        List<String> timeoutValues = new ArrayList<>();
        for(double i =0; i <= 10.0 ; i+=0.1){
            timeoutValues.add(String.format("%.2f",i));
        }

        ObservableList<String> availableTimeout = FXCollections.observableArrayList(timeoutValues);
        timeout.setItems(availableTimeout);
        timeout.getSelectionModel().select(0);

        List<String> frameSpacesValues = new ArrayList<>();
        for(double i =0; i <= 1.00 ; i+=0.01){
            frameSpacesValues.add(String.format("%.2f", i));
        }
        ObservableList<String> availableFrameSpaces = FXCollections.observableArrayList(frameSpacesValues);
        frameSpace.setItems(availableFrameSpaces);
        frameSpace.getSelectionModel().select(0);

        List<Integer> addressValues= new ArrayList<>();
        for(int i = 0; i <=247; i++){
            addressValues.add(i);
        }
        ObservableList<Integer> availableAddresses= FXCollections.observableArrayList(addressValues);
        slaveAddress.setItems(availableAddresses);
        slaveAddress.getSelectionModel().select(0);

        ObservableList<Integer> availableRetransmisionNumber = FXCollections.observableArrayList(0,1,2,3,4,5);
        retransmitionNumber.setItems(availableRetransmisionNumber);
        retransmitionNumber.getSelectionModel().select(0);

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
