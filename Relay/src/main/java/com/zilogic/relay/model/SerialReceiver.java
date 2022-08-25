
package com.zilogic.relay.model;

import com.fazecast.jSerialComm.SerialPort;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class SerialReceiver {

    public static String response;
    public static Thread thread;

    static public void start() {

        thread = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(SerialReceiver.class.getName()).log(Level.SEVERE, null, ex);
            }

            getListeningEvents();
            response = serialEvent();
        });
        thread.start();
    }

    public SerialReceiver() {

    }

    static public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    static public String serialEvent() {

        int bytesAvailable = SerialCommunication.serialPort.bytesAvailable();
        if (bytesAvailable <= 0) {
            return "";
        }

        byte buffer[] = new byte[30];
        Arrays.fill(buffer, (byte) 0);
        int bytesRead = SerialCommunication.serialPort.readBytes(buffer, Math.min(buffer.length, bytesAvailable));
        response = new String(buffer, 0, bytesRead);
        return response;

    }
}
