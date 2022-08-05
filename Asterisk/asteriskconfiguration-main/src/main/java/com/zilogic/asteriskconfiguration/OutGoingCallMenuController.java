/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.asteriskconfiguration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author ja
 */
public class OutGoingCallMenuController implements Initializable {

    @FXML
    private BorderPane bp;
    public FadeTransition ft;

    @FXML
    public Button callee;
    @FXML
    private Button disconnectBtn;
    @FXML
    private HBox callhbox;
    @FXML
    private HBox callwidgetone;

    @FXML
    private Text callconnectionstatus;
    @FXML
    private Text callTime;

    public int count = 0;

    public int checkUiSpeakingFlag = 0;
    public Boolean exitCallingThreadFlag = false;
    public Boolean exitCallConnectedThreadFlag = false;
    public Boolean exituiThreadFlag = false;
    public String callStatus;

    Thread callingThread;
    Thread callConnectedThread;
    Thread uithread;

    /**
     * This constructor is used to call the calling thread and uiThread.
     *
     */
    public OutGoingCallMenuController() {
        run_callingThread();
        run_uiThread();
    }

    /**
     * This method is used to initialize the border pane and perform fading
     * transition to it.
     *
     * @param url The url is passed as parameter
     *
     * @param rb The rb is passed as parameter
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bp.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff5d48, #c9c9c9);");
        ft = new FadeTransition(Duration.millis(1000), bp);
        ft.setFromValue(1.0);
        ft.setToValue(0.6);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();

    }

    /**
     * This method is used to display the status of the call whether
     * "Connecting" or "Connected" to the call.
     *
     */
    public void performCall() {
        callee.setText(CallOperationController.sp.getUserName());
        if (count >= 3) {
            callconnectionstatus.setText("Connecting");
            count = 0;
        }
        String dot = ".";
        callconnectionstatus.setText(callconnectionstatus.getText() + dot);
    }

    /**
     * This method is used to display the call "Connected".
     *
     */
    public void displaycallConnected() {
        callconnectionstatus.setText("Connected");
        callTime.setText(callStatus.substring(10, (callStatus.length() - 1)));
    }

    /**
     * This method is used to display the "Call Ended".
     *
     */
    public void displayEndCall() {
        callconnectionstatus.setText("Call Ended");
    }

    /**
     * This method is used to disconnect the call by clicking the call end
     * button.
     *
     */
    @FXML
    public void disconnectCall() throws IOException {
        try {

            exitCallingThreadFlag = true;
            exitCallConnectedThreadFlag = true;
            exituiThreadFlag = true;
            uithread.sleep(500);

            CallOperationController.outgoingStage.close();
            
            RegisterUserController.wobj.API_Hangup(-1);

        } catch (Exception e) {
            exitCallingThreadFlag = true;
            exitCallConnectedThreadFlag = true;
            exituiThreadFlag = true;

            CallOperationController.outgoingStage.close();

        }
    }

    /**
     * This method is used to display the fading transition after call gets
     * established.
     *
     */
    public void displayCallSpeakingUI() {
        ft.stop();
        bp.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #6ae2aa, #c9c9c9);");
        ft = new FadeTransition(Duration.millis(1000), bp);
        ft.setFromValue(1.0);
        ft.setToValue(0.6);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
        bp.setOpacity(1);
    }

    /**
     * This method is used to create and run the ui thread.
     *
     */
    public void run_uiThread() {
        uithread = new Thread(new Runnable() {
            @Override
            public void run() {
                Runnable updater = new Runnable() {
                    @Override
                    public void run() {
                        callStatus = RegisterUserController.wobj.API_GetStatus(-2);
                        if (callStatus.contains("Call Finished")) {

                            ft.stop();

                            bp.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff5d48, #ff5d48);");
                            ft = new FadeTransition(Duration.millis(1000), bp);
                            ft.setFromValue(1.0);
                            ft.setToValue(0.6);
                            ft.setCycleCount(Timeline.INDEFINITE);
                            ft.setAutoReverse(true);
                            ft.play();
                            bp.setOpacity(1);

                            displayEndCall();
                        }
                    }
                };
                while (!exituiThreadFlag) {
                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    Platform.runLater(updater);
                }
            }
        });
        uithread.start();
    }

    /**
     * This method is used to create and run the call connected thread.
     *
     */
    public void run_callConnectedThread() {
        callConnectedThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Runnable updater = new Runnable() {
                    @Override
                    public void run() {
                        callStatus = RegisterUserController.wobj.API_GetStatus(-2);
                        if (callStatus.contains("Speaking")) {
                            displaycallConnected();
                            if (checkUiSpeakingFlag == 0) {
                                displayCallSpeakingUI();
                                checkUiSpeakingFlag = 1;
                            }
                        } else if (callStatus.contains("Call Finished")) {

                            try {
                                disconnectCall();

                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        }
                    }
                };
                while (!exitCallConnectedThreadFlag) {
                    try {
                        Thread.sleep(100);
                        System.out.println("callStatusss" + callStatus);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    Platform.runLater(updater);
                }
            }
        });
        callConnectedThread.start();
    }

    /**
     * This method is used to create and run the calling thread.
     *
     */
    public void run_callingThread() {
        callingThread = new Thread(new Runnable() {
            @Override
            public void run() {

                Runnable updater = new Runnable() {
                    @Override
                    public void run() {

                        callStatus = RegisterUserController.wobj.API_GetStatus(-2);
                        if (!callStatus.contains("Speaking")) {
                            performCall();

                            count = count + 1;
                        } else if (callStatus.contains("Call Finished")) {

                            try {

                                disconnectCall();
                            } catch (Exception e) {
                                System.out.println(e);
                            }
                        } else if (callStatus.contains("Speaking")) {
                            exitCallingThreadFlag = true;
                            run_callConnectedThread();
                        }
                    }
                };
                while (!exitCallingThreadFlag) {
                    try {

                        System.out.println("callStatussscon" + callStatus);
                        callingThread.sleep(1000);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    Platform.runLater(updater);
                }
            }
        });
        callingThread.start();
    }
}
