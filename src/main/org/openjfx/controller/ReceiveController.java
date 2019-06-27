package org.openjfx.controller;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
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
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReceiveController implements Initializable, SerialPortDataListener {

    private SerialPortService portService;

    @FXML
    public TextArea receivedText;

    @FXML
    public Button btn_ping;

    @FXML
    public Button btn_return;




    private String truncateReceivedTextToTerminator(String receivedText) {
        return receivedText.split(terminatorCharactersAsString(PortSettings.getTerminatorChars()))[0];
    }

    private String terminatorCharactersAsString(List<Character> terminatorCharacters) {
        String result = "";
        for(Character character : terminatorCharacters) {
            result += character;
        }
        return result;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        portService = new SerialPortService();
        openPort();

    }

    @FXML
    public void ping(ActionEvent event) {
        receivedText.setText(portService.ping(PortSettings.openedPort));
    }

    @FXML
    public void returnToSettings(ActionEvent event) throws Exception{
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        SceneLoader.loadScene("view/main_scene.fxml",
                "SerialPortCommunication", stage);
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        String data;
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
            return;
        byte[] readBuffer = new byte[PortSettings.openedPort.bytesAvailable()];
        data = readBuffer.toString();
        int numRead = PortSettings.openedPort.readBytes(readBuffer, readBuffer.length);
        if(data.equals("ping")){
            portService.sendString(PortSettings.openedPort,"pong");
            System.out.println("Ktos mnie pinguje");
        }
        else{

        System.out.println("Przeczytano " + numRead + " bajtow.");
        System.out.println("Wiadomośc: " + data);
        receivedText.setText(truncateReceivedTextToTerminator(data));
        }


    }
    private void openPort(){
        SerialPort port = new SerialPortService().getInitializedPort(new PortSettings(),this);
        PortSettings.openedPort = port;
    }
}
