package org.openjfx.model.settings;

import com.fazecast.jSerialComm.SerialPort;

import java.util.ArrayList;
import java.util.List;

public class PortSettings {

   // public static SerialPort port;
    public static String port;
    public static int baudRate;
    public static int dataFieldLength;
    public static char parity;
    public static int stop;
    public static String flowControl;
    public static String terminator; // opis terminatora, "CR", "LF", "custom", "none"
    public static List<Character> terminatorChars = new ArrayList(); // znaki terminatora, 1 lub 2

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
  switch(parity){
   case 'E': parity=SerialPort.EVEN_PARITY;
   case 'O': parity=SerialPort.ODD_PARITY;
  }
  return parity;
 }

 public String getTerminator() {
  return terminator;
 }

 public static List<Character> getTerminatorChars() {
  return terminatorChars;
 }
}
