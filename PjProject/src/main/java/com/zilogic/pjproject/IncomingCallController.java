package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.pjsip_status_code;

class IncomingCallController {

    protected MyObserver observer = new MyObserver();

    @FXML
    private JFXButton answer;

    @FXML
    private Label callStatus;

    @FXML
    private JFXButton hangUp;

    Thread incoming_ui;
    boolean exit_incomingUi = false;
    int i = 0;
    static Stage incoming_ui_Stage;

    //create the Thread for showing the IncomingCall
    public void IncomingCallUI() throws Exception {
        incoming_ui_Stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("IncomingCall.fxml"));
        Scene scene = new Scene(root);
        incoming_ui_Stage.setScene(scene);
        incoming_ui_Stage.showAndWait();
    }

    @FXML
    void AcceptCall() {
        incoming_ui = new Thread(() -> {
            try {
                Runnable updater = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (i == 0) {
                                i++;
                                CallOpParam callOp = new CallOpParam(true);
                                callOp.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
                                MyObserver.currentCall.answer(callOp);
                            }
                            MyApp.ep.libHandleEvents(10L);
                        } catch (Exception e) {
                            System.err.println(" Exception while at incomingcall answer");
                        }
                    }
                };
                while (!exit_incomingUi) {
                    Platform.runLater(incoming_ui);
//                    Thread.sleep(100);
                }

            } catch (Exception e) {
                System.out.println(" Exception loading loading at IncomingCall Thread ");

            } finally {
                exit_incomingUi = true;
                i = 0;
            }
        });
        incoming_ui.start();
        incoming_ui.setDaemon(true);
        incoming_ui.setName("incomingCallUi");
    }

    @FXML
    void hangUpCall() {

    }

}
