/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zilogic.jssc;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author user
 */
public class Program1 {

    //create the  function for access the Port 
    static String DEFAULTPORT = "dev/tty/USB0";

    public static void main(String[] args) throws Exception, ParseException {

        Options options = new Options();

//        Option option = new Option();
//        options.addOption(option);
        Option port = Option.builder("p").longOpt("port").hasArg().argName("Port Name").required(true).desc("Specfic Port").build();
        options.addOption(port);

        CommandLine cmd;
        CommandLineParser parser = new BasicParser();
        HelpFormatter helper = new HelpFormatter();
        String arg[] = {"port", "baudrate", "stopBit", "Startbit", "command"};
        try {
            cmd = parser.parse(options, arg);
            if (cmd.hasOption("c")) {
                String opt_port = cmd.getOptionValue("c");
                System.out.println(opt_port);
            }
            if (cmd.hasOption("b")) {
                String baudrate = cmd.getOptionValue("p");
                System.out.println("Baudrate " + baudrate);
            }
        } catch (ParseException e) {
            System.out.printf(" The Help %s ", e.getMessage());
            System.out.println(e.getMessage());
            helper.printHelp("USAGE : ", options);

        }
    }
}
