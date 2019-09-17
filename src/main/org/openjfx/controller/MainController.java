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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.openjfx.model.SerialPortService;
import org.openjfx.model.settings.PortSettings;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable, SerialPortDataListener {

    //region fxml attributes
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
    //endregion

    private SerialPortService portService = new SerialPortService();

    private boolean portOpened = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        closePortBtn.setDisable(true);
        terminator.setItems(FXCollections.observableArrayList("none","CR","LF", "CR-LF","custom" ));
        terminator.getSelectionModel().select(0);
        terminatorChar.setDisable(true);
        terminator.getSelectionModel().selectedItemProperty()
                .addListener( (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    if (newValue.equals("custom") && terminatorChar.getText().equals("")){
                        openPortBtn.setDisable(true);
                        terminatorChar.setDisable(false);
                    }
                    else{
                        openPortBtn.setDisable(false);
                        terminatorChar.setDisable(true);
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

    @FXML
    public void openPort(ActionEvent event) {
        openPortBtn.setDisable(true);
        closePortBtn.setDisable(false);
        fillSettings();
        PortSettings.openedPort = new SerialPortService().getInitializedPort(new PortSettings(),this);
        if(PortSettings.openedPort == null){
          System.out.println("nie udalo sie otworzyc portu");
        }
        else{
            portOpened = true;
            System.out.println("Port otwarty pomyslnie");
        }
    }

    @FXML
    public void closePort(ActionEvent event) {
        openPortBtn.setDisable(false);
        closePortBtn.setDisable(true);
        PortSettings.openedPort.closePort();
        this.portOpened = false;
    }

    private void fillSettings(){
        PortSettings.baudRate = baudRate.getValue();
        PortSettings.dataFieldLength = dataFieldLength.getValue();
        PortSettings.flowControl = flowControl.getValue();
        PortSettings.parity = parity.getValue();
        PortSettings.port = port.getValue();
        PortSettings.stop = stop.getValue();
        PortSettings.terminatorChars.clear();
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

    @FXML
    public void ping(ActionEvent event) {
        if(portOpened) {
            receivedText.setText(portService.ping(PortSettings.openedPort));
        }
        else
            System.out.println("port nie jest otwarty!");
    }

    @FXML
    public void sendText(ActionEvent event) {
        if(portOpened){
            String messageToSend = appendTerminatorCharacters(inputText.getText());
            portService.sendString(PortSettings.openedPort, messageToSend);
        }
        else
            System.out.println("port nie jest otwarty!");
    }

    //region terminator characters
    private String appendTerminatorCharacters(String message) {
        StringBuilder result = new StringBuilder();
        result.append(message);
        PortSettings.terminatorChars.forEach(c -> result.append(c));
        return result.toString();
    }

    private String getLastReceivedString(String receivedText) {
        if(PortSettings.terminator.equals("none")){
            return receivedText;
        }
        else {
            String[] strings = receivedText.split(getTerminatorCharactersAsString());
            return strings[strings.length - 1];
        }
    }

    private String getTerminatorCharactersAsString() {
        StringBuilder result = new StringBuilder();
        PortSettings.terminatorChars.forEach(c -> result.append(c));
        return result.toString();
    }
    //endregion

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {

        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
            return;

        StringBuilder input = new StringBuilder();
        System.out.println("input handle");
        byte[] readBuffer = new byte[PortSettings.openedPort.bytesAvailable()];

        int numRead = PortSettings.openedPort.readBytes(readBuffer, readBuffer.length);

        for (byte b : readBuffer) {
            input.append((char)b);
        }

        String inputAfterTerminator = input.toString();
        // obcięcie wiadomosci do terminatora, jesli go uzywamy
        String currentTerminator = getTerminatorCharactersAsString();
        if (!currentTerminator.isEmpty()) {
            inputAfterTerminator = input.toString().split(currentTerminator)[0];
        }

//        String currentTerminator = getTerminatorCharactersAsString();
//        if(input.toString().contains(currentTerminator)) {
//            if (currentTerminator.length() > 1) {
//                input.delete(input.length() - currentTerminator.length(), input.length() - 1);
//            }
//            else if (currentTerminator.length() == 1 ) {
//                input.deleteCharAt(input.length() - 1);
//            }
//        }

        if(inputAfterTerminator.equals("ping")){
            portService.sendString(PortSettings.openedPort,"pong");
            System.out.println("Ktos mnie pinguje");
        }

        if(inputAfterTerminator.equals("pong")){
            portService.setPongReceived(true);
            System.out.println("Dostalem ponga");
        }

       if(numRead > 0){
            System.out.println("Przeczytano " + numRead + " bajtow.");
            System.out.println("Wiadomość do wyświetlenia: " + inputAfterTerminator);
            System.out.println("długosc wiadomości do wyświetlenia " + inputAfterTerminator.length());
            receivedText.setText(inputAfterTerminator);
        }
    }
}
