package com.zilogic.relayboard.Model;

import com.fazecast.jSerialComm.*;
import com.zilogic.relayboard.mainController;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class Utility {

    public SerialPort serialPort;
    public InputStream serialIn;
    public OutputStream serialOut;
    public String portName = null;
    mainController maincon = new mainController();
    public byte[] buffer = new byte[10];

    public Utility(String portName) {
        this.portName = portName;
    }

    public Utility() {
    }

    public void close_Port(String portName) {
        serialPort.closePort();
        serialPort = null;
    }

    //This Method is load the SerialPort...
    public void open_Port(String portName) {

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
        System.out.println(" Port location" + serialPort.getPortLocation());
        if (this.serialPort != null) {
            System.out.println("=======================");
            this.serialPort.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
            this.serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
            this.serialPort.openPort();
            System.out.println(serialPort.isOpen());
            this.serialOut = this.serialPort.getOutputStream();
            this.serialIn = this.serialPort.getInputStream();
            this.serialPort.addDataListener(new SerialReceiver(this));
        } else {
            System.out.println("Serial port is not null");
        }
    }

    public void send(String text) throws Exception {
        try {
            this.serialOut.write(text.getBytes());
            System.out.print(text);
        } catch (IOException e) {
            System.err.println(" outputStream error ");
        }
    }

    public byte[] getDataAvailable() throws Exception {
        serialPort.readBytes(buffer, 10);
        for (byte b : buffer) {
            if (b != 0) {
//                maincon.data.setText(String.valueOf((char) b));
                System.out.println((char) b);
            }
        }
//        System.out.println(mainController.updater.isRunning());
        System.out.println(Arrays.toString(buffer));
        return buffer;
    }

}
