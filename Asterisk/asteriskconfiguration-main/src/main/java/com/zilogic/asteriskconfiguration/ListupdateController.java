/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.asteriskconfiguration;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.AriFactory;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.models.AsteriskInfo;
import ch.loway.oss.ari4java.generated.models.Channel;
import ch.loway.oss.ari4java.generated.models.Endpoint;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author user
 */
public class ListupdateController implements Initializable {

    @FXML
    private BorderPane bp;
    @FXML
    public ListView listView;
    public FadeTransition ft;

    Thread statusThread;
    public ARI ari;

    List<String> list1 = new ArrayList<>();
    List<String> list2 = new ArrayList<>();
    @FXML
    public TextField serverAddressTextField;
    @FXML
    public Button serverConnectBtn;

    public Boolean exitThreadFlag = false;

    public static String serveripAddress = "";
    @FXML
    public ProgressIndicator progressIndicator;
    @FXML
    public Text serverStatus;

    public ListupdateController() {
        //progressIndicator.setDisable(true);
        //progressIndicator.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            progressIndicator.setVisible(false);
            serverConnectBtn.fire();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void run_statusThread() {
        statusThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Runnable updater = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            progressIndicator.setVisible(false);
                            ari = AriFactory.nettyHttp(
                                    "http://" + serveripAddress + ":8088/",
                                    "admin", "p@ssword",
                                    AriVersion.IM_FEELING_LUCKY);

                            AsteriskInfo info = ari.asterisk().getInfo().execute();
                            List<Channel> channels = ari.channels().list().execute();
                            List<Endpoint> endpoints = ari.endpoints().list().execute();

                            String nodeState;
                            list1.clear();
                            for (int i = 0; i < endpoints.size(); i++) {
                                list1.add(endpoints.get(i).getResource() + endpoints.get(i).getState());
                                
                            }

                            if (!(list2.equals(list1))) {
                                progressIndicator.setVisible(false);
                                serverStatus.setVisible(false);
                                listView.getItems().clear();
                                for (int i = 0; i < endpoints.size(); i++) {

                                    nodeState = endpoints.get(i).getState();
                                    final Button btn = new Button();
                                    btn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                                    ft = new FadeTransition(Duration.millis(1000), btn);
                                    ft.setFromValue(0.4);
                                    ft.setToValue(1);
                                    ft.setCycleCount(3);
                                    ft.setAutoReverse(true);
                                    ft.play();
                                    if (nodeState.equalsIgnoreCase("online")) {
                                        btn.setStyle("-fx-background-color: green;-fx-text-fill:white;-fx-background-radius: 2em;-fx-min-width: 10px;-fx-min-height: 10px;-fx-max-width: 12px;-fx-max-height: 12px;");
                                    } else if (nodeState.equalsIgnoreCase("offline")) {
                                        btn.setStyle("-fx-background-color: red;-fx-text-fill:white;-fx-background-radius: 2em;-fx-min-width: 10px;-fx-min-height: 10px;-fx-max-width: 12px;-fx-max-height: 12px;");
                                    } else {
                                        btn.setStyle("-fx-background-color: grey;-fx-text-fill:white;-fx-background-radius: 2em;-fx-min-width: 10px;-fx-min-height: 10px;-fx-max-width: 12px;-fx-max-height: 12px;");
                                    }

                                    HBox hbox = new HBox();
                                    Label lbl = new Label(endpoints.get(i).getResource());

                                    hbox.setSpacing(10);
                                    hbox.getChildren().addAll(lbl, btn);
                                    listView.getItems().addAll(hbox);

                                }
                                list2 = new ArrayList<>(list1);
                                 
                                
                            }

                        } catch (Exception e) {
                            listView.getItems().clear();
                            exitThreadFlag = true;
                            list2.clear();
                            System.out.println(e);
                            progressIndicator.setVisible(false);
                            serverStatus.setVisible(true);
                            serverStatus.setText("Server Unreachable");
                            serverStatus.setStyle("-fx-fill: red;");

                        }
                    }
                };

                while (!exitThreadFlag) {
                    try {
                        statusThread.sleep(3000);

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    Platform.runLater(updater);
                }
            }
        });
        statusThread.start();
    }

    @FXML
    private void connectToServer(ActionEvent event) {
        exitThreadFlag = false;
        serveripAddress = serverAddressTextField.getText();
        progressIndicator.setVisible(true);
        serverStatus.setVisible(false);
        run_statusThread();
    }

    @FXML
    public static void autoConnectButton() {

    }
}
