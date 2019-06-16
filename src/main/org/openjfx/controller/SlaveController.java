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
import org.openjfx.model.settings.SlaveSettings;

import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class SlaveController implements Initializable {

    private SerialPortService portService;

    @FXML
    public Button btn_send;

    @FXML
    public ChoiceBox<Integer> address;

    @FXML
    public TextArea receivedText;

    @FXML
    public ChoiceBox<String> frameSpace;

    @FXML
    public Button btn_ping;

    @FXML
    public Button btn_return;

    @FXML
    public void receive(ActionEvent event) throws  Exception{

        NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
        double frameSpaceValue = format.parse(frameSpace.getValue()).doubleValue();

        SlaveSettings settings = new SlaveSettings(address.getValue(), frameSpaceValue, receivedText.getText());

        receivedText.setText(portService.ReceiveFromMaster(settings).getReceivedText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
        address.setItems(availableAddresses);
        address.getSelectionModel().select(0);

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
