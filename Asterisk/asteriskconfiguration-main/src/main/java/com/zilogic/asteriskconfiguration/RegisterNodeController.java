/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.asteriskconfiguration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.AddAccount;
import model.MyAccount;
import model.MyAccountConfig;
import model.MyApp;
import model.MyAppObserver;
import model.MyObserver;
import model.Speaker;
import org.ini4j.Wini;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.AuthCredInfo;
import util.LoadFiles;
import webphone.webphone;

/**
 * FXML Controller class
 *
 * @author user
 */
public class RegisterNodeController implements Initializable {

    @FXML
    public BorderPane bp;

    public static Scene scene;
    @FXML
    public Button registerBtn;

    public File file = new File("userdetails.ini");
    public Boolean fileCreationStatus;
    public static webphone wobj;

    public static Boolean registerNodeOperationStatus = false;

    public static Boolean ignoreDuplicateNodeFlag = false;

    public Wini ini = null;

    public FileChooser fileChooser;
    public File pjsipFile = null;
    @FXML
    public TextField serverTextField;
    @FXML
    public Text serverAddrLabel;

    public static String serverip = "";
    MyApp app = new MyApp();
    MyObserver observer = new MyObserver();

    public static MyAccount account;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // start the Stack for the Pjsua2

            app.init(observer, ".", true);
            RegisterNodeController register = new RegisterNodeController();
            try {
                register.save();
            } catch (Exception ex) {
                Logger.getLogger(UpdateconfController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            Logger.getLogger(RegisterNodeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        serverTextField.setText(serverip);
    }

    public void performOperation() {
        try {
            fileCreationStatus = FileUtil.validateFileCreation(file);
            ini = FileUtil.createIniFile(ini, file);
            registerUser();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void writeToFile() {
        try {

            FileUtil.writeToConfigFile(pjsipFile.getAbsolutePath(), Pjsip_node_configurationController.speakers);

            ButtonType OK = new ButtonType("Ok");

            Label lbl = new Label();
            lbl.setText("File Saved Successfully");

            Alert a = new Alert(AlertType.INFORMATION, "", OK);
            Optional<ButtonType> result;

            a.getDialogPane().setContent(lbl);
            result = a.showAndWait();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void registerUser() {
        try {
            if (registerNodeOperationStatus) {
                if (!fileCreationStatus) {
                    file.createNewFile();
                }
                ini = FileUtil.createIniFile(ini, file);
                Speaker sp = Pjsip_node_configurationController.speakers.get(Pjsip_add_nodeController.nodePosition);

                FileUtil.writeToIniFile(ini, "userLogin", "username", sp.getUserName());
                FileUtil.writeToIniFile(ini, "userLogin", "password", sp.getPassword());
                FileUtil.writeToIniFile(ini, "userLogin", "serveraddress", serverip);
                wobj = new webphone();
                wobj.API_SetParameter("serveraddress", serverip);
                wobj.API_SetParameter("username", sp.getUserName());
                wobj.API_SetParameter("password", sp.getPassword());
                wobj.API_SetParameter("loglevel", "1");
                wobj.API_Start();
                System.out.println("username" + sp.getUserName() + "\n" + "pass" + sp.getPassword());
                writeToFile();
                wobj.API_Register();
                //FileUtil.writeToConfigFile(pjsipFile.getAbsolutePath(), Pjsip_node_configurationController.speakers);
                bp.setCenter(FXMLLoader.load(getClass().getResource("unregisterUser.fxml")));

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void RegisterBtn(ActionEvent event) {
        try {
            if (!serverTextField.getText().isEmpty()) {
                serverip = serverTextField.getText();
                ignoreDuplicateNodeFlag = true;
                Pjsip_node_configurationController.speakers.clear();
                pjsipFile = LoadFiles.getPjsipFile();

                FileUtil.readconfigFile(pjsipFile.getAbsolutePath(), Pjsip_node_configurationController.speakers);

                App.addNodeStage = new Stage();
                scene = new Scene(FXMLLoader.load(getClass().getResource("pjsip_add_node.fxml")), 640, 480);
                App.addNodeStage.setScene(scene);
                App.addNodeStage.setOnHiding(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent t) {
                        App.addNodeStage.close();
                        try {
                            performOperation();
                        } catch (Exception e) {

                        }
                    }
                });
                App.addNodeStage.initModality(Modality.APPLICATION_MODAL);
                App.addNodeStage.initOwner(App.mainstage);
                App.addNodeStage.requestFocus();
                App.addNodeStage.show();
            } else {
                serverTextField.setPromptText("Cannot be empty");
                serverTextField.setStyle("-fx-border-color: red;");
                serverAddrLabel.setStyle("-fx-fill: red;");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    ArrayList<AddAccount> userdetails = new ArrayList();
    static Thread accountRegThread;
    static boolean exitRegThread = false;

    public void save() throws IOException, Exception {

        System.out.println("Thread name : " + Thread.currentThread().getName());

        AddAccount addaccount = new AddAccount();
        String domainAddress = "192.168.0.132";
        String username = "6003";
        String password = "1234";
        addaccount.setUsername(username);
        addaccount.setDomainAddress(domainAddress);
        addaccount.setPassword(password);
        userdetails.add(addaccount);
        accountRegThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Runnable updater = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            createAccount();
                            MyApp.ep.libHandleEvents(10L);
                            exitRegThread = observer.verifyregisteration();
                            if (account.getId() == 0) {
                                exitRegThread = true;
                            }
                        } catch (Exception ex) {
                            System.err.println("Exception while Runlater Method....");
                        }
                    }
                };
                while (!exitRegThread) {
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    Platform.runLater(updater);
                }
            }
        });
        accountRegThread.setName("AccountReg");
//        accountRegThread.setDaemon(true);
        accountRegThread.start();
    }

    public MyAccount createAccount() throws Exception {
        MyApp.accList.clear();
        MyAccountConfig accCfg = new MyAccountConfig();
        AddAccount addaccount = userdetails.get(0);
        AuthCredInfo auth = new AuthCredInfo("digest", "*", addaccount.getUsername(), 0, addaccount.getPassword());
        AccountConfig accCon = new AccountConfig();
        accCon.setIdUri("sip:" + addaccount.getUsername() + "@" + addaccount.getDomainAddress().trim());
        accCon.getRegConfig().setRegistrarUri("sip:" + addaccount.getDomainAddress().trim());
        accCon.getSipConfig().getAuthCreds().add(auth);
        accCon.getNatConfig().setIceEnabled(true);
        accCon.getVideoConfig().setAutoTransmitOutgoing(true);
        accCon.getVideoConfig().setAutoShowIncoming(true);
        account = app.addAcc(accCon);
        app.saveConfig("pjsua2.json");
        System.out.println("========================================");
        MyApp.accCfgs.add(accCfg);
        for (MyAccount acc : MyApp.accList) {
            AccountInfo accinfo = acc.getInfo();
            if (acc.isDefault()) {
                System.out.println("account uri : " + acc.getInfo().getUri());
            }
            if (acc.getId() == -1) {
                // listView.getItems().add(acc.getInfo().getUri());
                acc.setDefault();
                // acc.getInfo().setRegExpiresSec(3600L);
                System.out.println(" Registeration Status Expiry time : " + acc.getInfo().getRegExpiresSec());
                System.out.println(" Account default : " + acc.isDefault() + "    ");
                System.out.println(acc.isValid());
                acc.getInfo().setOnlineStatus(true);
                System.out.println(accinfo.getOnlineStatus() + "Account status  text :" + accinfo.getOnlineStatusText());
            }
        }
        if (account != null) {
            return account;
        } else {
            return null;
        }
    }
}
