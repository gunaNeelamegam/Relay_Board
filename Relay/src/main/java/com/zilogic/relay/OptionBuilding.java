package com.zilogic.relay;

import com.zilogic.relay.model.SerialCommunication;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author user
 */
public class OptionBuilding {

    static String command;
    static Options options = new Options();

    private static String getRelay_Command() {

        return command = "";
    }

    public static Options Options_Builder(String smallOption, String longOption, String argname, boolean required, String desc) {
        options.addOption(Option.builder(smallOption).longOpt(longOption).hasArg().argName(argname).required(required).desc(desc).build());
        return options;
    }

    static public void option_creator() {
        Options_Builder("C", "c", "Clear the Relay port ", false, "Relay board to Clear the Specfic port : example : C1 ");
        Options_Builder("S", "s", "Set the register", false, "Relay board to Specific port to turn on : example : S1 ");
        Options_Builder("V", "v", "version of the Relay Board", false, "Relay board to SetAll the Specfic port : example : sa");
        Options_Builder("W", "w", " w 10101011", false, "write the specfic port to ON (or) OFF ex : w101010101");
        Options_Builder("R", "r", "Read all the port status", false, "Read the port status of port in Relay Board ex : -r r");
        Options_Builder("PS", "ps", "PS", false, "Taggle the Single port in Relay Board");
        Options_Builder("HSM", "hsm", "HSM", false, "Hand Shaking Mode ");
        Options_Builder("NP", "np ", "NP", false, "Product Name");
        Options_Builder("M", "m", "M", false, "Mode ");
        Options_Builder("T", "t", "M", false, "Toggle ");

    }

    static char ZERO;

    static public void argument_Parser(Options options, String[] args) throws Exception {
        HelpFormatter helpFormatter = new HelpFormatter();
        try {
            CommandLineParser commandLineParser = new DefaultParser();
            CommandLine cmd = commandLineParser.parse(options, args);

            if (cmd.hasOption("S")) {
                System.out.println(verify_ok((Relay.set(Integer.parseInt(cmd.getOptionValue("S"), 16)))));
            }
            if (cmd.hasOption("C") && getRelay_Command().equals("")) {
                System.out.print(verify_ok((Relay.clear(Integer.parseInt(cmd.getOptionValue("C"), 16)))));
            }
            if (cmd.hasOption("V") && getRelay_Command().equals("")) {
                System.out.println(Relay.version());
            }
            if (cmd.hasOption("R") && getRelay_Command().equals("")) {
                System.out.println((Relay.get_bulk()));
            }
            if (cmd.hasOption("W") && getRelay_Command().equals("")) {
                char ch = cmd.getOptionValue("W").toCharArray()[0];
                if (ch == '0') {
                    ZERO = ch;
                }
                System.out.println(verify_ok((Relay.set_bulk(Integer.parseInt(cmd.getOptionValue("W"), 16)))));
            }

            if (cmd.hasOption("PS") && getRelay_Command().equals("")) {
                System.out.println(Relay.power_status());
            }
            if (cmd.hasOption("M") && getRelay_Command().equals("")) {
                System.out.println(Relay.model());
            }
            if (cmd.hasOption("NP") && getRelay_Command().equals("")) {
                System.out.println(Relay.product_name());
            }
            if (cmd.hasOption("HSM") && getRelay_Command().equals("")) {
                System.out.println(verify_ok((Relay.handShaking_mode(cmd.getOptionValue("HSM")))));
            }
            if (cmd.hasOption("T") && getRelay_Command().equals("")) {
                System.out.println(verify_ok((Relay.toggle(Integer.parseInt(cmd.getOptionValue("T"), 16)))));
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

    private static String verify_ok(int i) {
        if (i != 0) {
            return "OK";
        }
        return "0";
    }
}
