package com.zilogic.relayboard;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author Guna_N
 */
/*
* @Param this method  which takes the cli argument form the terminal and Parse those Argument and takes as the Argument
 */
public class Relayboard {

    public static int getThreadTime() {
        return threadTime;
    }

    public static void setThreadTime(int threadTime) {
        Relayboard.threadTime = threadTime;
    }

    static Options options = new Options();

    private static long baudrate;
    private static String port;
    private static String relay_Command = "";
    private static int threadTime;
    SerialCommunication sercom = new SerialCommunication();

    public static long getBaudrate() {
        return baudrate;
    }

    public static void setBaudrate(long baudrate) {
        Relayboard.baudrate = baudrate;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String port) {
        Relayboard.port = port;
    }

    public static String getRelay_Command() {
        return relay_Command;
    }

    public static void setRelay_Command(String relay_Command) {
        Relayboard.relay_Command = relay_Command;
    }

    public Options Options_Builder(String smallOption, String longOption, String argname, boolean required, String desc) {
        options.addOption(Option.builder(smallOption).longOpt(longOption).hasArg().argName(argname).required(required).desc(desc).build());
        return options;
    }

    public void option_creator() {

        Options_Builder("p", "port", "Port Name", true, "Specfic Port  example : /dev/ttyUSB0");
        Options_Builder("b", "braudrate", "baud rate", true, "Data flow baudrate for the Serial Communication port ");
        Options_Builder("C", "clear", "Clear the Relay port ", false, "Relay board to Clear the Specfic port : example : C1 ");
        Options_Builder("S", "Set", "Set the register", false, "Relay board to Specific port to turn on : example : S1 ");
        Options_Builder("ca", "clearAll", "Port Name", false, "Relay board to ClearAll the Specfic port : example : ca ");
        Options_Builder("sa", "setAll", "setAll", false, "Relay board to SetAll the Specfic port : example : sa");
        Options_Builder("v", "version", "version of the Relay Board", false, "Relay board to SetAll the Specfic port : example : sa");
        Options_Builder("w", "write", " w 10101011", false, "write the specfic port to ON (or) OFF ex : w101010101");
        Options_Builder("r", "read", "Read all the port status", false, "Read the port status of port in Relay Board ex : -r r");
        Options_Builder("ta", "taggleAll", "taggle  all the port in relay board", false, " Taggle  all the Port in Relay Board ex : ta ");
        Options_Builder("h", "help", " No arguments", false, "  help ");
        Options_Builder("t", "taggle", "t1", false, "Taggle the Single port in Relay Board");
        Options_Builder("thread", "thread", "Thread time", false, "Thread running time for acknownledgement");
    }

    private static void argument_Parser(Options options, String[] args) {
        HelpFormatter helpFormatter = new HelpFormatter();
        try {
            CommandLineParser commandLineParser = new DefaultParser();
            CommandLine cmd = commandLineParser.parse(options, args);
            if (cmd.hasOption("p")) {
                setPort(cmd.getOptionValue("p"));
            }
            if (cmd.hasOption("b")) {
                setBaudrate(Long.parseLong(cmd.getOptionValue("b")));
            }
            if (cmd.hasOption("S") && getRelay_Command().equals("")) {
                setRelay_Command(cmd.getOptionValue("S"));
            }
            if (cmd.hasOption("C") && getRelay_Command().equals("")) {
                setRelay_Command(cmd.getOptionValue("C"));
            }
            if (cmd.hasOption("ca") && getRelay_Command().equals("")) {
                setRelay_Command(cmd.getOptionValue("ca"));
            }
            if (cmd.hasOption("sa") && getRelay_Command().equals("")) {
                setRelay_Command(cmd.getOptionValue("sa"));
            }
            if (cmd.hasOption("v") && getRelay_Command().equals("")) {
                setRelay_Command(cmd.getOptionValue("v"));
            }
            if (cmd.hasOption("r") && getRelay_Command().equals("")) {
                setRelay_Command(cmd.getOptionValue("r"));
            }
            if (cmd.hasOption("w") && getRelay_Command().equals("")) {
                setRelay_Command(cmd.getOptionValue("w"));
            }
            if (cmd.hasOption("ta") && getRelay_Command().equals("")) {
                setRelay_Command(cmd.getOptionValue("ta"));
            }
            if (cmd.hasOption("t") && getRelay_Command().equals("")) {
                setRelay_Command(cmd.getOptionValue("t"));
            }
            if (cmd.hasOption("thread")) {
                setThreadTime((Integer.parseInt(cmd.getOptionValue("thread"))));
            }
            if (cmd.hasOption("help")) {
                helpFormatter.printHelp("help ", options);
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
            helpFormatter.printHelp("USAGE : ", options);
            System.exit(0);
        }
    }

    public void serialCommunication(String port, long baudrate, String relay_command) throws Exception {
        sercom.setupInterface(port, baudrate);
        char[] command = relay_command.toCharArray();
        for (char c : command) {
            sercom.send(String.valueOf(c));
            Thread.sleep(1);
        }
        sercom.send("\r");
        Thread.sleep(1);
        sercom.send("\n");
    }

    public static void main(String[] args) {
//which return's the entire Option that you created...!
        try {
            Relayboard relay = new Relayboard();
            relay.option_creator();
            Relayboard.argument_Parser(Relayboard.options, args);
            relay.serialCommunication(getPort(), getBaudrate(), getRelay_Command());
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
