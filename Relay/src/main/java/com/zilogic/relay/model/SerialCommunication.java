package com.zilogic.relayboard;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 *
 * @author Guna_N
 */
public class SerialCommunication {

    SerialPort serialPort;
    OutputStream serialOut;
    InputStream serialIn;

    public SerialCommunication() {
        this.serialPort = null;
        this.serialOut = null;
        this.serialIn = null;
    }

    public void setupInterface(String portName, long baudrate) {
        if (this.serialPort != null) {
            if (this.serialOut != null) {
                try {
                    this.serialOut.flush();
                    this.serialOut.close();
                } catch (IOException e) {
                }
                this.serialOut = null;
            }
            if (this.serialIn != null) {
                try {
                    this.serialIn.close();
                } catch (IOException e) {
                }
                this.serialIn = null;
            }
            this.serialPort.removeDataListener();
            this.serialPort.closePort();
            this.serialPort = null;
        }
        this.serialPort = SerialPort.getCommPort(portName);
        if (this.serialPort != null) {
            this.serialPort.setComPortParameters((int) baudrate, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
            this.serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
            this.serialPort.openPort();
            this.serialOut = this.serialPort.getOutputStream();
            this.serialIn = this.serialPort.getInputStream();
           this.serialPort.addDataListener(new SerialReceiver(this));
        } else {

        }
    }

    public void send(String text) throws Exception {
        try {
            this.serialOut.write(text.getBytes());
        } catch (IOException e) {
            System.err.println(" outputStream error ");
        }
    }
    byte[] newData;

    public byte[] getDataAvailable() {
        try {
             Arrays.fill(newData, (byte) 0);
            newData = new byte[this.serialIn.available()];
            for (byte b : newData) {
                if (b != 0) {
                    System.out.println((char) b);
                }
            }
        } catch (IOException e) {
            System.out.println("ERROR");
        }
        return newData;
    }
}

class SerialReceiver implements SerialPortDataListener {

    private SerialCommunication comm;

    public SerialReceiver(SerialCommunication comm) {
        this.comm = comm;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
            this.comm.getDataAvailable();
        }
    }

}
