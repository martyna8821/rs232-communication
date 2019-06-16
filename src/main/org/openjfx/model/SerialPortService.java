package org.openjfx.model;

import org.openjfx.model.settings.MasterSettings;
import org.openjfx.model.settings.PortSettings;
import org.openjfx.model.settings.SlaveSettings;

public class SerialPortService {

    public void SendToSlave(MasterSettings settings){

    }

    public SlaveSettings ReceiveFromMaster(SlaveSettings settings){
        settings.setReceivedText("received text");
        return settings;
    }

    public boolean ping(){
        return true;
    }
}
