/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zilogic.jssc;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author user
 */
public class javajSerialLib extends Thread {

    static InputStream in;
    static OutputStream out;
    static SerialPort serial;
    static SerialCommunication sercom = new SerialCommunication();

    public void enableStreams() {
        in = serial.getInputStream();
        out = serial.getOutputStream();
    }

//    void checkData() {
//        sercom.serialPort.read();
//    }
    @Override
    public void run() {
        System.out.println("=--=-===-=-=-=-=-=-=-=-=-=-=");
        sercom.serialPort.addDataListener(new SerialReceiver(sercom));
    }

    public static void main(String[] args) throws IOException, Exception {
        SerialPort serialports[] = SerialPort.getCommPorts();
        for (SerialPort serial : serialports) {
            javajSerialLib.serial = serial;
            if (serial.getSystemPortName().contains("USB0")) {
                System.out.println(serial.getSystemPortName());
                sercom.setupInterface("/dev/ttyUSB0");
                javajSerialLib lib = new javajSerialLib();
                Thread t1 = new Thread(lib);
                t1.start();
                int i = 0;
                while (i == 0) {
                    i++;
                    sercom.send("s");
                    Thread.sleep(8);
                    sercom.send("1");
                    Thread.sleep(8);
                    sercom.send("\r");
                    Thread.sleep(8);
                    sercom.send("\n");
                    System.out.println("=========");
                }
            }
        }
    }
}
