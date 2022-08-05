/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zilogic.jssc;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Collectors;

/**
 *
 * @author user
 */
public class SerialCommunication {

    SerialPort serialPort;
    OutputStream serialOut;
    InputStream serialIn;

    public SerialCommunication() {
        this.serialPort = null;
        this.serialOut = null;
        this.serialIn = null;
        for (SerialPort port : SerialPort.getCommPorts()) {
            System.out.println(port.getSystemPortName() + ": " + port.getDescriptivePortName() + " (" + port.getPortDescription() + ")");
        }
    }

    public void setupInterface(String portName) {
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
            this.serialPort.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
            this.serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
            this.serialPort.openPort();
            System.out.println(serialPort.isOpen());

            this.serialOut = this.serialPort.getOutputStream();
            this.serialIn = this.serialPort.getInputStream();
           
        } else {

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

    public void getDataAvailable() {
        try {
            byte[] newData = new byte[this.serialIn.available()];
            int numRead = this.serialIn.read();
            System.out.println((char) numRead);
        } catch (IOException e) {
            System.out.println("ERROR");
        }
        try {
            String result = new BufferedReader(new InputStreamReader(this.serialIn))
                    .lines().parallel().collect(Collectors.joining("\n"));
        } catch (Exception e) {
        }
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
