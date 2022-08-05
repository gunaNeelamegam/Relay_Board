/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.asteriskconfiguration;

import static com.zilogic.asteriskconfiguration.RegisterNodeController.ignoreDuplicateNodeFlag;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.ini4j.Wini;
import webphone.webphone;

/**
 * FXML Controller class
 *
 * @author ja
 */
public class UnRegisterUserController implements Initializable {

    @FXML
    private BorderPane borderpane;
    @FXML
    private VBox vbox;
    @FXML
    private Text domainNameLabel;
    @FXML
    private Text domainNameText;
    @FXML
    private Text userNameLabel;
    @FXML
    private Text userNameText;
    @FXML
    private Text passwordLabel;
    @FXML
    private PasswordField passwordText;
    @FXML
    private Button RegisterDetails;

    /**
     * This method is used to add timeline Transition to the user information.
     *
     */
    public void addTimelineTransition() {
        int duration = 1200;

        Timeline timeline1 = new Timeline();
        userNameText.translateXProperty().set(80);

        KeyValue kv1 = new KeyValue(userNameText.translateXProperty(), 0, Interpolator.EASE_OUT);
        KeyFrame kf1 = new KeyFrame(Duration.millis(duration), kv1);
        timeline1.getKeyFrames().add(kf1);
        timeline1.play();

        Timeline timeline2 = new Timeline();
        passwordText.translateXProperty().set(80);

        KeyValue kv2 = new KeyValue(passwordText.translateXProperty(), 0, Interpolator.EASE_OUT);
        KeyFrame kf2 = new KeyFrame(Duration.millis(duration), kv2);
        timeline2.getKeyFrames().add(kf2);
        timeline2.play();

        Timeline timeline3 = new Timeline();
        domainNameText.translateXProperty().set(80);

        KeyValue kv3 = new KeyValue(domainNameText.translateXProperty(), 0, Interpolator.EASE_OUT);
        KeyFrame kf3 = new KeyFrame(Duration.millis(duration), kv3);
        timeline3.getKeyFrames().add(kf3);
        timeline3.play();

        Timeline timeline4 = new Timeline();
        domainNameLabel.translateYProperty().set(80);

        KeyValue kv4 = new KeyValue(domainNameLabel.translateYProperty(), 0, Interpolator.EASE_OUT);
        KeyFrame kf4 = new KeyFrame(Duration.millis(duration), kv4);
        timeline4.getKeyFrames().add(kf4);
        timeline4.play();

        Timeline timeline5 = new Timeline();
        userNameLabel.translateYProperty().set(80);

        KeyValue kv5 = new KeyValue(userNameLabel.translateYProperty(), 0, Interpolator.EASE_OUT);
        KeyFrame kf5 = new KeyFrame(Duration.millis(duration), kv5);
        timeline5.getKeyFrames().add(kf5);
        timeline5.play();

        Timeline timeline6 = new Timeline();
        passwordLabel.translateYProperty().set(80);

        KeyValue kv6 = new KeyValue(passwordLabel.translateYProperty(), 0, Interpolator.EASE_OUT);
        KeyFrame kf6 = new KeyFrame(Duration.millis(duration), kv6);
        timeline6.getKeyFrames().add(kf6);
        timeline6.play();

    }

    /**
     * This method is used to display the user details
     *
     */
    public void displayUserDetails() throws Exception {

        File file = null;
        Boolean fileCreationStatus;

        file = FileUtil.createFile(file, "userdetails.ini");
        fileCreationStatus = FileUtil.validateFileCreation(file);
        Wini ini = null;
        ini = FileUtil.createIniFile(ini, file);
        if (!fileCreationStatus) {

            userNameText.setText("-");
            domainNameText.setText("-");

        } else {

            userNameText.setText(FileUtil.readFromIniFile(ini, "userLogin", "username"));
            passwordText.setText(FileUtil.readFromIniFile(ini, "userLogin", "password"));
            domainNameText.setText(FileUtil.readFromIniFile(ini, "userLogin", "serveraddress"));
        }
    }

    /**
     * This method is used to call the displayUserDetails and add timeline
     * transition to it.
     *
     * @param url The url is passed as parameter
     *
     * @param rb The rb is passed as parameter
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {

            borderpane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #68b7bd, #c9c9c9);");
            displayUserDetails();
            addTimelineTransition();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This method is used to call the registerUser fxml.
     *
     * @param event The event is triggered when user presses the the register
     * button to register the details.
     */
    @FXML
    private void unregisterUser(ActionEvent event) throws Exception {
        try {
            ignoreDuplicateNodeFlag = false;
            //RegisterUserController.wobj.API_Unregister();
            //RegisterUserController.wobj = null;
            
            RegisterNodeController.wobj.API_Unregister();
            RegisterNodeController.wobj = null;
        } catch (Exception e) {
            System.out.println(e);

        }
        //borderpane.setCenter(FXMLLoader.load(getClass().getResource("registerUser.fxml")));
        borderpane.setCenter(FXMLLoader.load(getClass().getResource("registerNode.fxml")));
    }
}
