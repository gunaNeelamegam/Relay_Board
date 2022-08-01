package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AddAccountController {
    
    String domainAddress;
    String username;
    String password;
    private MainStageController mainstage = new MainStageController();
    @FXML
    private JFXButton closebtn;
    @FXML
    private JFXButton saveBtn;
    @FXML
    TextField domain;
    @FXML
    TextField user;
    @FXML
    PasswordField pass;
    Thread accountRegThread;
    static boolean exitRegThread = false;
    String callStatus;
    private MyObserver observer = new MyObserver();
    public static ArrayList<AddAccount> userdetails = new ArrayList<AddAccount>();
    
    @FXML
    public  synchronized void save() throws IOException, Exception {
        
        System.out.println("Thread name : " + Thread.currentThread().getName());
        
        var addaccount = new AddAccount();
        domainAddress = domain.getText();
        username = user.getText();
        password = pass.getText();
        addaccount.setUsername(username);
        addaccount.setDomainAddress(domainAddress);
        addaccount.setPassword(password);
        userdetails.add(addaccount);
        MainStageController.addAccountStage.close();
        accountRegThread = new Thread(new Runnable() {
            @Override
            public void run() {
                var updater = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            MainStageController.createAccount();
                            MyApp.ep.libHandleEvents(10L);
                            if (MainStageController.account.getId() != -1) {
                                MainStageController.account.getInfo().setRegExpiresSec(500000);
                                exitRegThread = true;
                            }
                        } catch (Exception ex) {
                            System.err.println("Exception while Runlater Method....");
                        }
                    }
                };
                while (!exitRegThread) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    Platform.runLater(updater);
                }
            }
        });
        accountRegThread.setName("AccountReg");
        accountRegThread.start();
    }
    
    @FXML
    public void close() {
        MainStageController.addAccountStage.close();
        System.out.println(" add Account close SuccessFully");
    }
}
