/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.asteriskconfiguration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.ini4j.Wini;
import webphone.webphone;

/**
 * FXML Controller class
 *
 * @author ja
 */
public class RegisterUserController implements Initializable {

    @FXML
    private BorderPane borderpane;
    @FXML
    private VBox vbox;
    @FXML
    private Text domainNameLabel;
    @FXML
    private TextField domainNameText;
    @FXML
    private Text userNameLabel;
    @FXML
    private TextField userNameText;
    @FXML
    private Text passwordLabel;
    @FXML
    private PasswordField passwordText;
    @FXML
    private Button registerBtn;
    private Stage stage;
    private Scene scene;

    public File file = new File("userdetails.ini");
    public Boolean fileCreationStatus;

    public static webphone wobj = new webphone();

    public Wini ini = null;

    /**
     * This method is used to create and display the contents of ini file.
     *
     * @param url The url is passed as parameter
     *
     * @param rb The rb is passed as parameter
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        borderpane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #6aabd1, #c9c9c9);");

        try {
            fileCreationStatus = FileUtil.validateFileCreation(file);
            ini = FileUtil.createIniFile(ini, file);
            System.out.println("fileeeee" + fileCreationStatus);

            displayFileContents();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    /**
     * This method is used to display the file contents.
     *
     */
    public void displayFileContents() {
        try {

            if (!fileCreationStatus) {
                userNameText.setText("-");
                domainNameText.setText("-");

            } else {

                userNameText.setText(FileUtil.readFromIniFile(ini, "userLogin", "username"));
                passwordText.setText(FileUtil.readFromIniFile(ini, "userLogin", "password"));
                domainNameText.setText(FileUtil.readFromIniFile(ini, "userLogin", "serveraddress"));
            }
        } catch (Exception e) {

        }
    }

    /**
     * This method is used to write the userName, Password and Domain Name to
     * the .ini file.
     *
     * @param event The event is triggered when user presses the the unregister
     * button to make changes to the user details.
     */
    @FXML
    private void registerUser(ActionEvent event) throws Exception {

        try {
            System.out.println("fileeeee111" + fileCreationStatus);
            if (!fileCreationStatus) {
                file.createNewFile();
            }
            ini = FileUtil.createIniFile(ini, file);
            FileUtil.writeToIniFile(ini, "userLogin", "username", userNameText.getText());
            FileUtil.writeToIniFile(ini, "userLogin", "password", passwordText.getText());
            FileUtil.writeToIniFile(ini, "userLogin", "serveraddress", domainNameText.getText());

            wobj.API_SetParameter("serveraddress", domainNameText.getText());
            wobj.API_SetParameter("username", userNameText.getText());
            wobj.API_SetParameter("password", passwordText.getText());
            wobj.API_SetParameter("loglevel", "1");
            wobj.API_Start();
            System.out.println("username" + userNameText.getText() + "\n" + "pass" + passwordText.getText());
            wobj.API_Register();
            borderpane.setCenter(FXMLLoader.load(getClass().getResource("unregisterUser.fxml")));
        } catch (Exception e) {

        }
    }
}
