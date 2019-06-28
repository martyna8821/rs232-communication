package org.openjfx.model;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.openjfx.model.settings.PortSettings;

public class SerialPortService {
    private boolean pongReceived = false;

    public SerialPort getInitializedPort(PortSettings settings, SerialPortDataListener dataListener){
        SerialPort port = SerialPort.getCommPort(settings.getPortDescription());
        if(port!=null) {
            System.out.println("otrzymano port:" + port.getDescriptivePortName());
        }
        else{
            port = SerialPort.getCommPorts()[0];
            System.out.println("otrzymano domyślny port:" + port.getDescriptivePortName());

        }
        if(port != null){
            port.setComPortParameters(
                    settings.getBaudRate(),settings.getDataBits(),
                    settings.getStopBits(),settings.getParity()
            );
            port.setFlowControl(settings.getFlowControl());
        }
        else{
            System.out.println("Nie udało sie dostać żadnego portu => JESTESMY W DUPIE");
        }
        port.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING,0,0);
        port.openPort();
        port.addDataListener(dataListener);
        return port;
    }

    public void sendString(SerialPort port,String message){

        int bytesWritten = port.writeBytes(message.getBytes(), message.length());
        if(bytesWritten >0)  System.out.println("Coś sie wysłało, jest dobrze (y)");
        else System.out.println("Nic sie nie wysłało");

    }





    public String ping(SerialPort port){
        long startTime = System.nanoTime();
        long elapsedTime = 0;
        String data="";
        char input;
        String message = "";
        sendString(port,"ping");
        try {
            while (true) {
                while ( !pongReceived && elapsedTime<10000) {
                    Thread.sleep(20);
                    elapsedTime = (System.nanoTime() - startTime)/1000000; //milisekundy
                }
                if(elapsedTime >= 10000) {
                    message = "odbiorca nie odpowiedzial";
                }


                if(this.pongReceived){
                    message = "odbiorca odpowiedzial po:" + elapsedTime + "milisekundach";
                    clearPongReceived();
                    break;
                }
                else continue;
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());


        }
        return message;

    }

    public void pongReceived() {
        pongReceived=true;
    }
    public void clearPongReceived() {
        pongReceived=false;
    }

}
