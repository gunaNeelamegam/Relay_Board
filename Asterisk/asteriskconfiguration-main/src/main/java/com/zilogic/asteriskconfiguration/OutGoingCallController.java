package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.pjsip_status_code;
import org.pjsip.pjsua2.pjsua_call_flag;

public class OutGoingCallController {

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

    void UpdateCallstate() {
        System.out.println(" Updating the call state in the label");

    }

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
            MyApp.ep.libHandleEvents(100);
            System.out.println("current call mute : " + currentCall.audioMedia.getTxLevel());
        }
    }

    @FXML
    void unMute_Call() throws Exception {
        System.out.println("unmute call");
        if (currentCall != null) {
            CallOpParam prm = new CallOpParam(true);
            currentCall.audioMedia.adjustRxLevel((float) 1);
            MyApp.ep.libHandleEvents(100);
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
            MyApp.ep.libHandleEvents(100);
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
            MyApp.ep.libHandleEvents(100);
        }
    }

    /*
    The Method is used for creating the OutGoingcall
     */
    @FXML
    void makeCall() {

        Thread t1 = new Thread(() -> {
            try {
                var a = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (i == 0) {
                                i++;
                                //   System.out.println(" Current Thread : " + Thread.currentThread().getName());
                                MyAccount acc = MyApp.accList.get(0);
                                acc.setDefault();
                                System.out.println(" Account : " + acc.isValid() + acc.isDefault());
                                currentCall = new MyCall(acc, 0);
                                currentCall.makeCall("sip:6002@192.168.0.132", new CallOpParam(true));
                            }

                            System.out.println("Call thread : " + Thread.currentThread().getName());
                            if (currentCall.getInfo().getState() != 6) {
                                System.out.println("++++++++++++++++++");
                                MyApp.ep.libHandleEvents(10L);
                                callStatus.setText(currentCall.getInfo().getStateText());
                                observer.check_OutGoinig_CallDeletion(currentCall);
                            }
                        } catch (Exception ex) {
                            System.err.println(" Exception while  loading at make OutgoingCall Method");
                            exitThreadCalling = true;
                        }

                    }

                };
                while (!exitThreadCalling) {
                    calling.sleep(100);
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
        Thread.currentThread()
                .setName("calling");
        t1.setDaemon(
                true);
        t1.start();
    }
}
