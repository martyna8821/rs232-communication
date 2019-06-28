package org.openjfx.controller;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.openjfx.model.SerialPortService;
import org.openjfx.model.settings.PortSettings;
import org.openjfx.model.SceneLoader;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable, SerialPortDataListener {

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
    public ChoiceBox<String> flowControl;
    @FXML
    public Button sendBtn;
    @FXML
    public TextArea receivedText;
    @FXML
    public Button openPortBtn;
    @FXML
    public Button closePortBtn;
    @FXML
    public Button pingBtn;
    @FXML
    public TextArea inputText;

    private SerialPortService portService = new SerialPortService();

    private boolean portOpened = false;
    private String data="";

    @FXML
    public void ping(ActionEvent event) {
        if(portOpened) {
            receivedText.setText(portService.ping(PortSettings.openedPort));
        }
        else System.out.println("port nie jest otwarty!");
    }

    @FXML
     public void sendText(ActionEvent event) {
        if(portOpened) {
            String messageToSend = inputText.getText() + appendTerminatorCharacters(PortSettings.getTerminatorChars());
            portService.sendString(PortSettings.openedPort, messageToSend);
        }
        else System.out.println("port nie jest otwarty!");
    }

    private String appendTerminatorCharacters(List<Character> terminatorCharacters) {
        StringBuilder result = new StringBuilder();
        for(Character character : terminatorCharacters) {
            result.append(character);
        }
        return result.toString();
    }

    @FXML
    public void openPort(ActionEvent event) {
        fillSettings();
        PortSettings.openedPort = new SerialPortService().getInitializedPort(new PortSettings(),this);
        if(PortSettings.openedPort == null){
            receivedText.setText("nie udalo sie otworzyc portu");
        }
        else{
            portOpened = true;
        }
    }

    @FXML
    public void closePort(ActionEvent event) {
        PortSettings.openedPort.closePort();

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        terminator.setItems(FXCollections.observableArrayList("none","CR","LF", "CR-LF","custom" ));
        terminator.getSelectionModel().select(0);
        terminator.getSelectionModel().selectedItemProperty()
                .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                   if (newValue.equals("custom") && terminatorChar.getText().equals("")){
                       openPortBtn.setDisable(true);
                   }
                   else{
                       openPortBtn.setDisable(false);
                   }
                });

        terminatorChar.textProperty().addListener((observable, oldValue, newValue) -> {
            if(terminator.getValue().equals("custom") && newValue.equals("")){
                openPortBtn.setDisable(true);
            }
            else{
                openPortBtn.setDisable(false);
            }
        });


        SerialPort[] ports = SerialPort.getCommPorts();
        if(ports.length == 0){
            openPortBtn.setDisable(true);
        }
        ObservableList<String> availablePorts = FXCollections.observableArrayList();
        for(SerialPort port: ports){
            availablePorts.add(port.getSystemPortName());
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
                    String input = terminatorChar.getText(0, 1);
                    for (int i = 0; i < input.length(); i++) {
                        PortSettings.terminatorChars.add(input.charAt(i));
                    }

                break;

            }
        }
        PortSettings.terminator = terminator.getValue();
    }
    private String truncateReceivedTextToTerminator(String receivedText) {
        String [] strings = receivedText.split(terminatorCharactersAsString(PortSettings.getTerminatorChars()));
        return strings[strings.length-1];
    }

    private String terminatorCharactersAsString(List<Character> terminatorCharacters) {
        String result = "";
        for(Character character : terminatorCharacters) {
            result += character;
        }
        return result;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {

        char input;
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
            return;
        System.out.println("input handle");
        byte[] readBuffer = new byte[PortSettings.openedPort.bytesAvailable()];

        int numRead = PortSettings.openedPort.readBytes(readBuffer, readBuffer.length);
        int i = (int)readBuffer[0];
        input = (char)i;
        int begin = truncateReceivedTextToTerminator(data).length()-3;
        data+=input;
        if(begin >=0 && truncateReceivedTextToTerminator(data).substring(begin).equals("ping")){
            portService.sendString(PortSettings.openedPort,"pong");
            System.out.println("Ktos mnie pinguje");
        }
        if(begin >=0 && truncateReceivedTextToTerminator(data).substring(begin).equals("pong")){
            portService.pongReceived();
            System.out.println("Ktos mnie pinguje");
        }
        else{

            System.out.println("Przeczytano " + numRead + " bajtow.");
            System.out.println("Wiadomośc: " + input);
            receivedText.setText(truncateReceivedTextToTerminator(data));
        }
    }
}
