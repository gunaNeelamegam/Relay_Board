/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.asteriskconfiguration;

import java.io.IOException;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author user
 */
public class MainScreenUIController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    public Text titleText;
    private Button homeScreenBtn;
    @FXML
    private Button pjsipCreatorBtn;
    @FXML
    private Button extensionBtn;
    @FXML
    private Button nodeStatusBtn;
    @FXML
    private Button config;
    @FXML
    private Button Callbtn;
    @FXML
    private Button RegisterBtn;
    public static Stage outGoingCallStage = null;

    /**
     *
     * @param url The url is passed as parameter
     *
     * @param rb The rb is passed as parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {

            titleText.setText("Extension Node Configurator");
            borderPane.setCenter(FXMLLoader.load(getClass().getResource("extensionconf.fxml")));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This method is used to display dialer grid UI.
     *
     * @param event The event is triggered when phone icon is pressed.
     *
     */
    private void displayDialPad(ActionEvent event) {
        try {

            titleText.setText("Dialer Menu");
            borderPane.setCenter(FXMLLoader.load(getClass().getResource("dialerGridUI.fxml")));
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This method is used to display the audio list UI.
     *
     * @param event The event is triggered when music icon is pressed.
     *
     */
    private void playMusic(ActionEvent event) throws IOException {
        titleText.setText("Play Soundtrack Menu");
        borderPane.setCenter(FXMLLoader.load(getClass().getResource("audioList.fxml")));
    }

    /**
     * This method is used to display the main Menu UI.
     *
     * @param event The event is triggered when home screen icon is pressed.
     *
     */
    private void displayMainMenu(ActionEvent event) throws Exception {
        titleText.setText("Main Menu");

        Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        Scene scene = homeScreenBtn.getScene();
        root.translateYProperty().set(scene.getHeight());
        borderPane.setCenter(root);
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    /**
     * This method is used to display the Unregister User UI.
     *
     * @param event The event is triggered when settings icon is pressed.
     *
     */
//    @FXML
//    private void UnRegisterUser(ActionEvent event) throws Exception {
//        titleText.setText("Register User Menu");
//        if (RegisterUserController.wobj == null) {
//            borderPane.setCenter(FXMLLoader.load(getClass().getResource("registerUser.fxml")));
//        } else {
//            borderPane.setCenter(FXMLLoader.load(getClass().getResource("unregisterUser.fxml")));
//        }
//    }
    /* This is for updating when user registers his credentials onto the pjsip file */
    @FXML
    private void UnRegisterUser(ActionEvent event) throws Exception {
        titleText.setText("Register User Menu");

        if (RegisterNodeController.wobj == null) {
            borderPane.setCenter(FXMLLoader.load(getClass().getResource("registerNode.fxml")));
        } else {
            borderPane.setCenter(FXMLLoader.load(getClass().getResource("unregisterUser.fxml")));
        }
    }

    @FXML
    private void extensionCreator(ActionEvent event) throws Exception {

        titleText.setText("Extension Node Configurator");
        borderPane.setCenter(FXMLLoader.load(getClass().getResource("extensionconf.fxml")));

    }

    @FXML
    private void nodeCreator(ActionEvent event) throws Exception {

        System.out.println("Node Creator");
        titleText.setText("PJSIP Node Creator");
        borderPane.setCenter(FXMLLoader.load(getClass().getResource("pjsip_node_configuration.fxml")));

    }

    @FXML
    private void checkNodeStatus(ActionEvent event) throws Exception {
        titleText.setText("Node Status");
        borderPane.setCenter(FXMLLoader.load(getClass().getResource("listupdate.fxml")));
    }

    @FXML
    private void configNode(ActionEvent event) throws Exception {
        titleText.setText("Config Node");
        borderPane.setCenter(FXMLLoader.load(getClass().getResource("nodeconfig.fxml")));
    }

    @FXML
    private void PerformCallOperation(ActionEvent event) throws Exception {

        titleText.setText("Call Menu");
        outGoingCallStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("outGoingCall.fxml"));
        Scene scene = new Scene(root);
        outGoingCallStage.setScene(scene);
        outGoingCallStage.showAndWait();
    }
}
