package com.zilogic.relayboard.Model;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.zilogic.relayboard.mainController;

public class SerialReceiver implements SerialPortDataListener {

    mainController mainController = new mainController();
    Utility u = mainController.utility;

    public SerialReceiver(Utility u) {
        this.u = u;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
            try {
                this.u.getDataAvailable();
            } catch (Exception ex) {
                System.out.println(" EXCEPTION IN SERIAL EVENT");
            }
        }
    }

}
