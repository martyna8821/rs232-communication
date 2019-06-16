package org.openjfx.model.settings;


public class MasterSettings {

    private int retransmitionNumber;
    private double timeout;
    private double frameSpace;
    private String inputText;
    private int slaveAddress;

    public int getSlaveAddress() {
        return slaveAddress;
    }

    public void setSlaveAddress(int slaveAddress) {
        this.slaveAddress = slaveAddress;
    }

    public MasterSettings(int retransmitionNumber, double timeout, double frameSpace, String inputText, int slaveAddress) {
        this.retransmitionNumber = retransmitionNumber;
        this.timeout = timeout;
        this.frameSpace = frameSpace;
        this.inputText = inputText;
        this.slaveAddress = slaveAddress;
    }

    public int getRetransmitionNumber() {
        return retransmitionNumber;
    }

    public void setRetransmitionNumber(int retransmitionNumber) {
        this.retransmitionNumber = retransmitionNumber;
    }

    public double getTimeout() {
        return timeout;
    }

    public void setTimeout(double timeout) {
        this.timeout = timeout;
    }

    public double getFrameSpace() {
        return frameSpace;
    }

    public void setFrameSpace(double frameSpace) {
        this.frameSpace = frameSpace;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }
}
