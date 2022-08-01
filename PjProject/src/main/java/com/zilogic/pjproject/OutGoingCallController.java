package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.CallSetting;
import org.pjsip.pjsua2.SendInstantMessageParam;
import org.pjsip.pjsua2.pjsip_status_code;
import org.pjsip.pjsua2.pjsua_call_flag;

public class OutGoingCallController {
    
    @FXML
    FadeTransition ft;
    @FXML
    private Label messageStatus;
    
    @FXML
    private TextField textmes;
    
    @FXML
    private JFXButton send;
    @FXML
    private Pane c;
    int i = 0;
    @FXML
    private JFXButton call;
    
    @FXML
    Label callStatus;
    
    @FXML
    private Circle circle;
    
    @FXML
    private JFXButton hangUp;
    
    @FXML
    private JFXButton mute;
    
    @FXML
    private JFXButton unHoldCall;
    
    @FXML
    private JFXButton unMute;
    @FXML
    private JFXButton holdCall;
    
    @FXML
    private TextField text;
    Thread calling;
    static boolean exitThreadCalling = false;
    MyCall currentCall = null;
    private MyObserver observer = new MyObserver();
    
    Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            c.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #00FF00, #FFFFFF);");
            ft = new FadeTransition(Duration.millis(1000), c);
            ft.setFromValue(1.0);
            ft.setToValue(0.6);
            c.opacityProperty().set(0);
            ft.setCycleCount(Timeline.INDEFINITE);
            ft.setAutoReverse(true);
            ft.play();
            return null;
        }
    };
    
    @FXML
    void hangUpCall() throws Exception {
        if (currentCall != null) {
            System.out.println(" Call : " + call.toString());
            CallOpParam callParam = new CallOpParam();
            callParam.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
            currentCall.hangup(callParam);
            System.out.println("hangup");
            MyApp.ep.libHandleEvents(10L);
            MainStageController.outGoingCallStage.close();
        }
    }
    
    @FXML
    void muteCall(ActionEvent event) throws Exception {
        if (currentCall != null) {
            currentCall.audioMedia.adjustRxLevel(0);
            MyApp.ep.libHandleEvents(10L);
            System.out.println("current call mute : " + currentCall.audioMedia.getTxLevel());
        }
    }
    
    @FXML
    void unMute_Call() throws Exception {
        System.out.println("unmute call");
        if (currentCall != null) {
            CallOpParam prm = new CallOpParam(true);
            currentCall.audioMedia.adjustRxLevel((float) 1);
            MyApp.ep.libHandleEvents(10L);
        }
    }
    
    @FXML
    void hold() throws Exception {
        System.out.println(" Hold the Call");
        if (currentCall != null) {
            CallOpParam callOp = new CallOpParam(true);
            callOp.setStatusCode(pjsua_call_flag.PJSUA_CALL_UPDATE_CONTACT);
            callOp.setReason("hold");
            currentCall.setHold(callOp);
            MyApp.ep.libHandleEvents(10L);
        }
    }

    //method is used for unHold the call
    @FXML
    void unHold() throws Exception {
        if (currentCall != null) {
            System.out.println("call is Unloaded");
            CallOpParam callOp = new CallOpParam(true);
            callOp.getOpt().setFlag(1);
            currentCall.reinvite(callOp);
            MyApp.ep.libHandleEvents(10L);
        }
    }

    /*
    The Method is used for creating the OutGoingcall
     */
    @FXML
    void makeCall() throws Exception {
        
        Thread t1 = new Thread(() -> {
            try {
                var a = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (i == 0) {
                                i++;
                                System.out.println(" Current Thread : " + Thread.currentThread().getName());
                                MyAccount acc = MyApp.accList.get(0);
                                acc.setDefault();
                                currentCall = new MyCall(acc, 0);
                                CallOpParam prm = new CallOpParam();
                                currentCall.makeCall("sip:" + text.getText().trim() + "@" + acc.getInfo().getUri().substring(9), prm);
                                Thread fade = new Thread(task);
                                fade.start();
                            }
                            MyApp.ep.libHandleEvents(10L);
                            callStatus.setText(currentCall.getInfo().getStateText());
                            call.setDisable(true);
                        } catch (Exception ex) {
                            System.err.println(" Exception while  loading at make OutgoingCall Method");
                            exitThreadCalling = true;
                        }
                    }
                };
                while (!exitThreadCalling) {
                    Thread.sleep(10);
                    Platform.runLater(a);
                }
            } catch (Exception ex) {
                System.out.println(" Excpetion while loading the outgoingcall");
            } finally {
                exitThreadCalling = false;
                i = 0;
            }
        }
        );
        Thread.currentThread().setName("calling");
        t1.start();
    }
    
    @FXML
    void send(ActionEvent event) {
        //sending the instant message
        if (currentCall != null) {
            //sending the instant Message
            try {
                SendInstantMessageParam prm = new SendInstantMessageParam();
                prm.setContentType("text/plain");
                prm.setContent(textmes.getText());
                currentCall.sendInstantMessage(prm);
                messageStatus.setText("sended   :)");
            } catch (Exception e) {
                messageStatus.setText("failed --");
            }
        }
        
    }
}
//    @FXML
//    synchronized void makeCall() throws Exception {
//        MyApp.ep.codecSetPriority("speex/32000", (short) 128);
//        System.out.println(" Current Thread : " + Thread.currentThread().getName());
//        MyAccount acc = MyApp.accList.get(0);
//        acc.setDefault();
//        System.out.println(" Account : " + acc.isValid() + acc.isDefault());
//        currentCall = new MyCall(acc, 0);
//        currentCall.makeCall("sip:" + text.getText().trim() + "@" + acc.getInfo().getUri().substring(9),new CallOpParam(true));
//    }
