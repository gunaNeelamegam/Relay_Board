package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.pjsip_status_code;
import org.pjsip.pjsua2.pjsua_call_flag;

public class IncomingCallController {

    @FXML
    FadeTransition ft;
    @FXML
    private JFXButton answer;

    @FXML
    private AnchorPane pane;

    @FXML
    private JFXButton hangUp;
    MyCall call = MyAccount.currentCall;

    Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            while (call != null) {
                DisplayUI();
            }
            return null;
        }
    };

    @FXML
    void answer(ActionEvent event) throws Exception {

        CallOpParam prm = new CallOpParam(true);
        prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
        call.answer(prm);
        MainStageController.incoming_stage.setScene(FXMLLoader.load(getClass().getResource("incoming.fxml")));
        MainStageController.incoming_stage.show();
        System.out.println("ANSWER");
    }

    @FXML
    void hangUp(ActionEvent event) throws Exception {
        System.out.println("HANGUP");
        if (call != null) {
            CallOpParam prm = new CallOpParam(true);
            prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
            call.hangup(prm);
        }
    }

    @FXML
    void DisplayUI() {
        pane.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff5d48, #ff5d48);");
        ft = new FadeTransition(Duration.millis(1000), pane);
        ft.setFromValue(1.0);
        ft.setToValue(0.6);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
        pane.setOpacity(1);
    }

    @FXML
    void muteCall(ActionEvent event) throws Exception {
        if (call != null) {
            System.out.println("Current Call Instance :" + call.getInfo().toString());
            call.audioMedia.adjustRxLevel(0);
            MyApp.ep.libHandleEvents(100);
            System.out.println("current call mute : " + call.audioMedia.getTxLevel());
        }
    }

    @FXML
    void unMute_Call() throws Exception {
        System.out.println("unmute call");
        if (call != null) {
            CallOpParam prm = new CallOpParam(true);
            call.audioMedia.adjustRxLevel((float) 1);
            MyApp.ep.libHandleEvents(100);
        }
    }

    @FXML
    void hold() throws Exception {
        System.out.println(" Hold the Call");
        if (call != null) {
            CallOpParam callOp = new CallOpParam(true);
            callOp.setStatusCode(pjsua_call_flag.PJSUA_CALL_UPDATE_CONTACT);
            callOp.setReason("hold");
            call.setHold(callOp);
            MyApp.ep.libHandleEvents(100);
        }
    }

    //method is used for unHold the call
    @FXML
    void unHold() throws Exception {
        if (call != null) {
            System.out.println("call is Unloaded");
            CallOpParam callOp = new CallOpParam(true);
            callOp.getOpt().setFlag(1);
            call.reinvite(callOp);
            MyApp.ep.libHandleEvents(100);
        }
    }

}
