package com.zilogic.relayboard;

import com.fazecast.jSerialComm.SerialPort;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.zilogic.relayboard.Model.Utility;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class mainController implements Initializable {

    @FXML
    public TextField response;
    @FXML
    private JFXComboBox baudrate;
    @FXML
    BorderPane mainPane;
    @FXML
    private TextField comText;

    @FXML
    private JFXListView commands;

    @FXML
    private JFXComboBox parity;

    @FXML
    private JFXComboBox port;

    @FXML
    private JFXButton send = new JFXButton();

    @FXML
    private JFXButton openBtn;
    @FXML
    private JFXButton closeBtn;
    @FXML
    private JFXComboBox startBit;
    static boolean exit = false;
    static Thread t1 = null;
    @FXML
    private JFXComboBox stopBit;
    public Utility utility = null;
    String command[] = {"V", "S1", "S2", "S3", "S4", "S5", "S6", "S7", "sa", "ca"};
    String parityArr[] = {"PARITY_NONE", "PARITY_ODD", "PARITY_EVEN", "PARITY_MARK"};
    String start_BitArr[] = {"DATABITS_5", "DATABITS_6", "DATABITS_7", "DATABITS_8"};
    String stop_BitArr[] = {"STOPBITS_1", "STOPBITS_1_5", "STOPBITS_2", "BAUDRATE_110"};
    int Baud_rate[] = {110, 300, 600, 1200, 2400, 4800, 9600, 14400, 19200, 38400, 57600, 115200, 128000, 256000};

    static int STARTFIRE = 0;

    //Task class is implemented used to create the multiple task behind the Scene for the Acknownledgement from the Port  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        load_Library();
    }

    @FXML

    public void loadPort() {
        SerialPort seriallist[] = SerialPort.getCommPorts();
        System.out.println(Arrays.toString(seriallist));
        for (SerialPort serial : seriallist) {
            if (serial.getSystemPortName().toString().contains("USB")) {
                port.getItems().add(serial.getSystemPortName());
            }
        }
    }

    @FXML
    public void loadParity() {
        for (String par : parityArr) {
            parity.getItems().add(par);
        }
        parity.getSelectionModel().select("PARITY_NONE");
    }

    @FXML
    public void loadStart_Bit() {
        for (String par : start_BitArr) {
            startBit.getItems().add(par);
        }
        startBit.getSelectionModel().select("DATABITS_8");
    }

    @FXML
    public void loadStop_Bit() {
        for (String par : stop_BitArr) {
            stopBit.getItems().add(par);
        }
        stopBit.getSelectionModel().select("STOPBITS_1");
    }

    @FXML
    public void loadBaud_Rate() {
        for (int par : Baud_rate) {
            baudrate.getItems().add(par);
        }
        baudrate.getSelectionModel().select(11);
    }

    @FXML
    public void list_Commands() {
        for (String par : command) {
            commands.getItems().add(par);
        }
        commands.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void load_Library() {
        loadPort();
        loadBaud_Rate();
        loadParity();
        loadStart_Bit();
        loadStop_Bit();
        list_Commands();
    }

    @FXML
    private void refresh_port() {
        port.getItems().clear();
        loadPort();
    }

    private boolean verifyPortName() {
        if (port.getSelectionModel().getSelectedItem() == null || port.getSelectionModel().getSelectedItem().toString().trim() == "") {
            Alert no_Port = new Alert(Alert.AlertType.ERROR);
            no_Port.setTitle(" FILL THE PORT ");
            no_Port.setHeaderText("please select the Specfic Port ....!");
            no_Port.showAndWait();
            return true;
        }
        return false;
    }

    //This method is used to send the data to the Port
    @FXML
    private void send_port() throws Exception {
        System.out.println("Sending data");
        if (!comText.getText().isEmpty()) {
            if (STARTFIRE == 1) {
                intial_start();
            } else {
                commands.getSelectionModel().select(-1);
                serialOperation(comText.getText().trim());
                comText.setText("");
            }
        } else {
            if (STARTFIRE == 1) {
                intial_start();
            } else {
                serialOperation(commands.getSelectionModel().getSelectedItem().toString());
            }
        }
    }

    //method  is used to do the Process benhid the Scence using the lisener
    @FXML
    private void serialOperation(String command) throws Exception {
        int i = 0;
        char[] com = command.trim().toCharArray();
        while (i == 0) {
            i++;
            utility.send(String.valueOf(com[0]));
            Thread.sleep(8);
            utility.send(String.valueOf(com[1]));
            Thread.sleep(8);
            utility.send("\r");
            Thread.sleep(8);
            utility.send("\n");
        }
    }

    @FXML
    void showOpen() {
        if (!verifyPortName()) {
            Alert openAlert = new Alert(Alert.AlertType.CONFIRMATION);
            openAlert.setTitle("OPEN ALERT");
            openAlert.setHeaderText("Opening the Serial communication port");
            Optional<ButtonType> result = openAlert.showAndWait();
            if (result.get() == ButtonType.OK) {
                utility = new Utility(port.getSelectionModel().getSelectedItem().toString());
                utility.open_Port(port.getSelectionModel().getSelectedItem().toString());
                if (utility.serialPort.isOpen()) {
                    STARTFIRE = 1;
                    System.out.println(" start the Listening task");
                    labelState();
                    send.fire();
                }
            }
        }
    }

    @FXML
    void showClose() {
        try {

            if (utility.serialPort != null) {
                Alert closeAlert = new Alert(Alert.AlertType.CONFIRMATION);
                closeAlert.setTitle("OPEN SERIAL PORT");
                closeAlert.setHeaderText(" Ready to start the Serial communication port ");
                Optional<ButtonType> result = closeAlert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    System.out.println(port.getSelectionModel().getSelectedItem().toString());
                    utility.serialPort.closePort();
                } else {
                    var alertClose = new Alert(Alert.AlertType.ERROR);
                    alertClose.setTitle("PORT IS NOT IN OPEN");
                    alertClose.setHeaderText("Please Open the port ");
                }
            }
        } catch (Exception e) {
            System.out.println(utility.serialPort == null);
        }
    }

    /*
    @param is used to create the external thread to listen the incoming BufferStream using this method
     */
    void labelState() {
        t1 = new Thread(() -> {
            Runnable updater = new Runnable() {
                @Override
                public void run() {
                    if (utility.serialPort.isOpen()) {
                        byte Byte[] = utility.buffer;
                        for (byte b : Byte) {
                            if (b != 0) {
                                System.out.print((char) b);
                                response.setText(String.valueOf((char) b));
                            }
                        }
                    } else {
                        exit = true;
                    }
                }
            };
            while (!exit) {

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    exit = false;
                }
                Platform.runLater(updater);
            }
        });
        t1.start();
    }

    private void intial_start() throws Exception {
        while (STARTFIRE == 1) {
            utility.send("S");
            Thread.sleep(8);
            utility.send("1");
            Thread.sleep(8);
            utility.send("\r");
            Thread.sleep(8);
            utility.send("\n");

            STARTFIRE = 0;
        }
    }
}
