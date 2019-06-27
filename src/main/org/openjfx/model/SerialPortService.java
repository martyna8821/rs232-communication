package org.openjfx.model;

import com.fazecast.jSerialComm.SerialPort;
import org.openjfx.model.settings.PortSettings;

public class SerialPortService {

    public SerialPort getInitializedPort(PortSettings settings){
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
        return port;
    }

    public void sendString(SerialPort port,String message){
        port.openPort();
        int bytesWritten = port.writeBytes(message.getBytes(), message.length());
        if(bytesWritten >0)  System.out.println("Coś sie wysłało, jest dobrze (y)");
        else System.out.println("Nic sie nie wysłało");
        port.closePort();
    }


    /*TODO
     * nie trzeba sprawdzac tez terminatora?
     */
    public String reciveData(SerialPort port){
        port.openPort();
        String data;
        try {
            while (true)
            {
                while (port.bytesAvailable() == 0)
                    Thread.sleep(20);

                byte[] readBuffer = new byte[port.bytesAvailable()];
                data = readBuffer.toString();
                int numRead = port.readBytes(readBuffer, readBuffer.length);
                System.out.println("Przeczytano " + numRead + " bajtow.");
                System.out.println("Wiadomośc: " + data);
            }
        }
        catch (Exception e) {
            //e.printStackTrace();
        data = "could not read";
        }
        finally {
            port.closePort();
        }
        return data;
    }

    /*TODO
     * dodac ping
     */
    public boolean ping(){
        return true;
    }

}
