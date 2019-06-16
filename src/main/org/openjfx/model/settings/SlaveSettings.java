package org.openjfx.model.settings;

public class SlaveSettings {

    private int address;
    private double frameSpace;
    private String receivedText;

    public SlaveSettings(int address, double frameSpace, String receivedText) {
        this.address = address;
        this.frameSpace = frameSpace;
        this.receivedText = receivedText;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public double getFrameSpace() {
        return frameSpace;
    }

    public void setFrameSpace(double frameSpace) {
        this.frameSpace = frameSpace;
    }

    public String getReceivedText() {
        return receivedText;
    }

    public void setReceivedText(String receivedText) {
        this.receivedText = receivedText;
    }
}
