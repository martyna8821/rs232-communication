package org.openjfx.model.settings;

import com.fazecast.jSerialComm.SerialPort;

public class PortSettings {

   // public static SerialPort port;
    public static String port;
    public static int baudRate;
    public static int dataFieldLength;
    public static char parity;
    public static int stop;
    public static String flowControl;
    public static String terminator;
    public static String terminatorChar;


 public String getPortDescription() {
  return port;
 }

 public int getBaudRate() {
  return baudRate;
 }

 public int getDataBits() {
  return dataFieldLength;
 }

 public int getStopBits() {
  if(stop==1) return SerialPort.ONE_STOP_BIT;
  else return  SerialPort.TWO_STOP_BITS;
 }

 public int getParity() {
  int parity = SerialPort.NO_PARITY;
  switch(PortSettings.parity){
   case 'E': parity=SerialPort.EVEN_PARITY; break;
   case 'O': parity=SerialPort.ODD_PARITY; break;
   default: break;
  }
  return parity;
 }

 public int getFlowControl() {
  int flowControlToReturn;
  switch(PortSettings.flowControl){
   case "DTR/DSR":
    flowControlToReturn = SerialPort.FLOW_CONTROL_DTR_ENABLED | SerialPort.FLOW_CONTROL_DSR_ENABLED;
    break;
   case "RTS/CTS":
    flowControlToReturn = SerialPort.FLOW_CONTROL_RTS_ENABLED | SerialPort.FLOW_CONTROL_CTS_ENABLED;
    break;
   case "XON/XOFF":
    flowControlToReturn = SerialPort.FLOW_CONTROL_XONXOFF_IN_ENABLED | SerialPort.FLOW_CONTROL_XONXOFF_OUT_ENABLED;
    break;
   default: // "none" flow control
    flowControlToReturn = SerialPort.FLOW_CONTROL_DISABLED;
    break;
  }
  return flowControlToReturn;
 }
}
