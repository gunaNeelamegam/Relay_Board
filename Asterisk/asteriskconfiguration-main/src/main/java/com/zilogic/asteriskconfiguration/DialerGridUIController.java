/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.asteriskconfiguration;

import static com.zilogic.asteriskconfiguration.FileUtil.validateFileCreation;
import static com.zilogic.asteriskconfiguration.RegisterUserController.wobj;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.ini4j.Wini;
import webphone.webphone;

/**
 *
 * @author user
 */
public class DialerGridUIController {

    @FXML
    public TextField calleeText;

    String txt = "";
    public static String callee;
    public static String caller;
    @FXML
    Button btn1;

    @FXML
    private GridPane grid;
    @FXML
    private Button btn2;
    @FXML
    private Button btn3;
    @FXML
    private Button btn4;
    @FXML
    private Button btn5;
    @FXML
    private Button btn6;
    @FXML
    private Button btn7;
    @FXML
    private Button btn8;
    @FXML
    private Button btn9;
    @FXML
    private Button btnStar;
    @FXML
    private Button btn0;
    @FXML
    private Button btnHash;
    @FXML
    private Button btncall;
    @FXML
    private Button deleteBtn;

    @FXML
    public BorderPane maingridbp;

    public static Stage outgoingStage;

    /**
     * This method is used to establish the call to the callee number.
     *
     * @param calleeNumber The string calleeNumber is passed to establish the
     * call.
     *
     * @return Boolean It will return true if call gets connected else returns
     * false
     */
    public static Boolean callOperation(String calleeNumber) {
        try {
            File file = null;
            Boolean fileCreationStatus;

            file = FileUtil.createFile(file, "userdetails.ini");
            fileCreationStatus = validateFileCreation(file);

            Wini ini = null;
            ini = FileUtil.createIniFile(ini, file);
            String username = FileUtil.readFromIniFile(ini, "userLogin", "username");
            caller = username;
            System.out.println("user name : " + caller);
            String password = FileUtil.readFromIniFile(ini, "userLogin", "password");
            System.out.println("password : " + password);
            String domainName = FileUtil.readFromIniFile(ini, "userLogin", "serveraddress");

            if (calleeNumber.length() != 0 && !(calleeNumber.equals(caller))) {

                wobj.API_SetParameter("serveraddress", domainName);
                wobj.API_SetParameter("username", username);
                wobj.API_SetParameter("password", password);
                wobj.API_SetParameter("loglevel", "1");
                wobj.API_Call(-1, calleeNumber);

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * This method is used to dial the number.
     *
     * @param event The event is triggered when dial pad buttons are pressed.
     *
     */
    @FXML
    public void dialNumbers(ActionEvent event) throws IOException {

        try {
            /* The event.getSource method which return's the object of the action thet invokes 
                Button (#cbbccb) >--- Button@27099741[styleClass=button]'Done'
             */
            txt = ((Button) event.getSource()).getText();
            String[] str_Array = txt.split("\n");
            txt = String.valueOf(str_Array[0]);
            calleeText.appendText(txt);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This method is used to initiate the call.
     *
     * @param event The event is triggered when call button is pressed.
     *
     */
    @FXML
    public void initiateCall(ActionEvent event) throws IOException {
        try {
            callee = calleeText.getText();
            if (callOperation(callee)) {
                outgoingStage = new Stage();
                /*
                 *Stage outgoing stage is the stage object outgoingStage obj that invokes the method name
                 *initModality that takes the other stage based on the priority like enum like NONE means the bg stage 
                 * takes place priority and then initOwner it  takes the Stage as the Argument 
                 */
                outgoingStage.initOwner(App.mainstage);
                outgoingStage.initModality(Modality.WINDOW_MODAL);
                FXMLLoader fxmlLoader = new FXMLLoader();
                Pane root = (FXMLLoader.load(getClass().getResource("OutGoingCallMenu.fxml")));
                outgoingStage.setScene(new Scene(root, 400, 700));
                outgoingStage.setTitle("OutGoing Call Menu");
                outgoingStage.show();
            }
        } catch (Exception e) {
            try {
                throw new Exception("CALLEE IS NOT IN USE (OR) OFFLINE");
            } catch (Exception ex) {
                Logger.getLogger(DialerGridUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(e);
        }
    }

    /**
     * This method is used to clear the entered callee number.
     *
     * @param event The event is triggered when deletebutton is pressed.
     *
     */
    @FXML
    private void deleteCalleeNumber(ActionEvent event) {
        if (calleeText.getLength() != 0) {
            calleeText.deleteText(calleeText.getLength() - 1, calleeText.getLength());
        }
    }
}
