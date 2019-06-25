package org.openjfx.model;

import com.fazecast.jSerialComm.SerialPort;
import org.openjfx.model.settings.MasterSettings;
import org.openjfx.model.settings.PortSettings;
import org.openjfx.model.settings.SlaveSettings;

public class SerialPortService {


    public SerialPort GetInitializedPort(PortSettings settings){
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
        }
        else{
            System.out.println("Nie udało sie dostać żadnego portu => JESTESMY W DUPIE");
        }
        return port;



    }

    public void SendString(SerialPort port,String message){
        port.openPort();

        int bytesWritten = port.writeBytes(message.getBytes(), message.length());
        if(bytesWritten >0)  System.out.println("Coś sie wysłało, jest dobrze (y)");

        port.closePort();


    }

    public void ReciveData(SerialPort port){
        port.openPort();

        try {
            while (true)
            {
                while (port.bytesAvailable() == 0)
                    Thread.sleep(20);

                byte[] readBuffer = new byte[port.bytesAvailable()];
                String data = readBuffer.toString();
                int numRead = port.readBytes(readBuffer, readBuffer.length);
                System.out.println("Przeczytano " + numRead + " bajtow.");
                System.out.println("Wiadomośc: " + data);
            }
        } catch (Exception e) { e.printStackTrace(); }
        port.closePort();
    }

    public boolean ping(){
        return true;
    }
}
