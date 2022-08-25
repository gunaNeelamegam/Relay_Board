package com.zilogic.relay.model;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author
 */
public class SerialCommunication {

    public static SerialPort serialPort;
    public static OutputStream serialOut;
    public static InputStream serialIn;

    public static SerialReceiver serrec = new SerialReceiver();

    public static String BinarytoHex(String hex) {
        String binary = "";
        String frst_string = hex.substring(0, 4);
        System.out.println(frst_string);
        String second_half = hex.substring(4);
        System.out.println(second_half);
        String[] str = {frst_string, second_half};
        HashMap<Character, String> hashMap
                = new HashMap<Character, String>();
        hashMap.put('0', "0000");
        hashMap.put('1', "0001");
        hashMap.put('2', "0010");
        hashMap.put('3', "0011");
        hashMap.put('4', "0100");
        hashMap.put('5', "0101");
        hashMap.put('6', "0110");
        hashMap.put('7', "0111");
        hashMap.put('8', "1000");
        hashMap.put('9', "1001");
        hashMap.put('A', "1010");
        hashMap.put('B', "1011");
        hashMap.put('C', "1100");
        hashMap.put('D', "1101");
        hashMap.put('E', "1110");
        hashMap.put('F', "1111");

        for (String s : str) {
            for (Character key : hashMap.keySet()) {
                if (s.equals(hashMap.get(key))) {
                    binary += key;
                }
            }
        }
        return binary;

    }

    public SerialCommunication() {
        serialPort = null;
        serialOut = null;
        serialIn = null;
    }

    static public void setupInterface(String portName, long baudrate) {
        if (serialPort != null) {
            if (serialOut != null) {
                try {
                    serialOut.flush();
                    serialOut.close();
                } catch (IOException e) {
                }
                serialOut = null;
            }
            if (serialIn != null) {
                try {
                    serialIn.close();
                } catch (IOException e) {
                }
                serialIn = null;
            }
            serialPort.removeDataListener();
            serialPort.closePort();
            serialPort = null;
        }
        serialPort = SerialPort.getCommPort(portName);
        if (serialPort != null) {
            serialPort.setComPortParameters((int) baudrate, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
            serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
            serialPort.openPort();
            serialOut = serialPort.getOutputStream();
            serialIn = serialPort.getInputStream();
        } else {

        }
    }

    static public void send(String text) throws Exception {
        try {
            serialOut.write(text.getBytes());
        } catch (IOException e) {
            System.err.println("Sending Error");
        }
    }
    static byte[] newData = new byte[25];

    //reading the input Stream 
    static public void serialOperation(String command) throws Exception {
        char[] com = command.trim().toCharArray();
        System.out.println(Arrays.toString(com));
        for (char c : com) {
            send(String.valueOf(c));
            Thread.sleep(1);
        }
        send("\r");
        Thread.sleep(1);
        send("\n");
    }

    static public void open_port() {
        SerialPort[] serial = serialPort.getCommPorts();
        System.out.println(Arrays.toString(serial));
        for (SerialPort s : serial) {
            if (s.getSystemPortName().contains("USB")) {
                System.out.println(s.getSystemPortName());
                setupInterface(s.getSystemPortName(), 115200);
            }
        }
    }

    public static String intToBinary(String hex) {

        // variable to store the converted
        // Binary Sequence
        String binary = "";

        // converting the accepted Hexadecimal
        // string to upper case
        hex = hex.toUpperCase();

        // initializing the HashMap class
        HashMap<Character, String> hashMap
                = new HashMap<Character, String>();

        // storing the key value pairs
        hashMap.put('0', "0000");
        hashMap.put('1', "0001");
        hashMap.put('2', "0010");
        hashMap.put('3', "0011");
        hashMap.put('4', "0100");
        hashMap.put('5', "0101");
        hashMap.put('6', "0110");
        hashMap.put('7', "0111");
        hashMap.put('8', "1000");
        hashMap.put('9', "1001");
        hashMap.put('A', "1010");
        hashMap.put('B', "1011");
        hashMap.put('C', "1100");
        hashMap.put('D', "1101");
        hashMap.put('E', "1110");
        hashMap.put('F', "1111");

        int i;
        char ch;

        // loop to iterate through the length
        // of the Hexadecimal String
        for (i = 0; i < hex.length(); i++) {
            // extracting each character
            ch = hex.charAt(i);

            // checking if the character is
            // present in the keys
            if (hashMap.containsKey(ch)) // adding to the Binary Sequence
            // the corresponding value of
            // the key
            {
                binary += hashMap.get(ch);
            } // returning Invalid Hexadecimal
            // String if the character is
            // not present in the keys
            else {
                binary = "Invalid Hexadecimal String";
                return binary;
            }
        }

        // returning the converted Binary
        System.out.println(binary);
        return binary;
    }
    public static String response;

}
