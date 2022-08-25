package com.zilogic.relay;

import com.zilogic.relay.model.SerialCommunication;
import com.zilogic.relay.model.SerialReceiver;


/*
 * @author jagadish
 */
public class Relay {

    static SerialReceiver serrec = new SerialReceiver();
    static String VERSION = "V", MODEL = "M", PRODUCT_NAME = "np", POWER_STATUS = "ps", READ = "R", WRITE = "W", SET = "S", CLEAR = "C", TOGGLE = "T", DELAT_COUNT = "PL", HSM = "MO";
    static int MAXIMUM_PORT = 8;

    static public void run_thread() throws Exception {
        SerialReceiver.start();
        SerialReceiver.thread.join();
    }

    /*
    @param function is used to read the version of the Relay Board
     */
    public static String version() throws Exception {
        SerialCommunication.serialOperation(VERSION);
        run_thread();
        return SerialReceiver.response;

    }

    /*
    @param  this  method is used to provide the Board Model
     */
    public static String model() throws Exception {
        SerialCommunication.serialOperation(MODEL);
        run_thread();
        return SerialReceiver.response;
    }

    /*
    @param this method is used to provide the Name_of_Board and then which organization is provided
     */
    public static String product_name() throws Exception {
        SerialCommunication.serialOperation(PRODUCT_NAME);
        run_thread();
        return SerialReceiver.response;
    }

    /*
    @param this method is used to provide the Power_Status for the Board 
     */
    public static String power_status() throws Exception {
        SerialCommunication.serialOperation(POWER_STATUS);
        run_thread();
        return SerialReceiver.response;
    }

    /*
    @param this method is used to manipulate the port  in the relay Board  using this Method
     */
    public static int toggle(int i) throws Exception {

        SerialCommunication.serialOperation(TOGGLE + Integer.toHexString(i));
        run_thread();
        return SerialReceiver.response.contains("OK") ? i : 0;
    }

    /*
    @param this method is used to provide the state of the port  which are turn ON/OFF  
     */
    public static String get_bulk() throws Exception {
        SerialCommunication.serialOperation(READ);
        run_thread();
        if (SerialReceiver.response.contains("R")) {
            System.out.println(SerialReceiver.response.substring(3, 11));
            return SerialCommunication.BinarytoHex(SerialReceiver.response.substring(3, 11).trim());
        } else {
            return "WRONG";
        }
    }

    /*
    @param this method is used to manipulate the port to Turn ON/OFF in single  manipulation itself
     */
    public static int set_bulk(int a) throws Exception {
        if (OptionBuilding.ZERO == '0') {
            SerialCommunication.serialOperation(WRITE + "0" + Integer.toHexString(a));
            run_thread();
        } else {
            SerialCommunication.serialOperation(WRITE + Integer.toHexString(a));
            run_thread();
        }
        return SerialReceiver.response.contains("OK") ? a : 0;
    }

    /*
    @param function is used to set the RelayPort in the Relay board
     */
    public static int set(int i) throws Exception {

        SerialCommunication.serialOperation(SET + Integer.toHexString(i));
        run_thread();
        return SerialReceiver.response.contains("OK") ? 1 : 0;
    }

    /*
    @param this function is used to clear the State of the port in uniquely 
     */
    public static int clear(int i) throws Exception {
        SerialCommunication.serialOperation(CLEAR + Integer.toHexString(i));
        run_thread();
        return SerialReceiver.response.contains("OK") ? i : 0;
    }

    public static int handShaking_mode(String a) throws Exception {
        SerialCommunication.serialOperation(HSM + a);
        run_thread();
        return SerialReceiver.response.contains("OK") ? 1 : 0;
    }

    public static void main(String[] args) throws Exception {
        SerialCommunication.open_port();
        OptionBuilding.option_creator();
        OptionBuilding.argument_Parser(OptionBuilding.options, args);
    }

}
