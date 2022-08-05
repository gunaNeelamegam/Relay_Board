package com.mycompany.callapp;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.OnTypingIndicationParam;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_status_code;

public class ControllerClass implements MyAppObserver {

    Thread callConnectedThread;
    boolean exitCallConnectedThreadFlag = false;
    String callStatus;
    Stage stage;

    public Stage incomingUi() throws IOException {
        stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("IncomingCall.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();
        return stage;
    }

    public void disconnectUi() {
        if (stage != null) {
            stage.close();
        }

    }

    @Override
    public void notifyRegState(int code, String reason, int expiration) {

        System.out.println("  regestration code : " + code + "Registeration Reason : " + reason + "Registerartion expiration :" + expiration);
    }

    public void run_callConnectedThread(MyCall call) {
        callConnectedThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Runnable updater = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            callStatus = call.getInfo().getStateText();
                            // if (callStatus.equalsIgnoreCase("Trying") || callStatus.equalsIgnoreCase("RINGING") || callStatus.equalsIgnoreCase("Early")||callStatus.contains("CONFIRME")) {
                            if (!callStatus.equalsIgnoreCase("DISCONNTED")) {
                                incomingUi();
                            } else if (callStatus.equalsIgnoreCase("DISCONNTED")) {

                                try {
                                    disconnectUi();
                                    exitCallConnectedThreadFlag = true;
                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                            }
                        } catch (Exception ex) {
                            System.err.println(" Exception in Running Thread");
                        }
                    }
                };
                while (!exitCallConnectedThreadFlag) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("callStatusss" + call.getInfo().getStateText());
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                    Platform.runLater(updater);
                }
            }
        });
        callConnectedThread.start();
    }

    @Override
    public void notifyIncomingCall(MyCall call) {
        CallOpParam prm = new CallOpParam(true);
        try {
            System.out.println(" Incoming call For the Current Account :" + call.getInfo());
            CallInfo ci = call.getInfo();
            System.out.println(
                    " INCOMING CALL a FOR ANSWER AND h FOR HANGUP.....");
            System.out.println(
                    "call is Calling state ");
            prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
            call.answer(prm);
        } catch (Exception ex) {
            System.err.println(" Excpetion in Notify INcoming call Method");
        }
    }

    @Override
    public void notifyCallState(MyCall call) {
        try {
            System.out.println("Call  state is changeing : " + call.getInfo().getStateText() + "  " + call.getInfo().getState());
            if (call.getInfo().getState() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
                MyApp app = new MyApp();
                // app.CallMessage(call);
            }
        } catch (Exception ex) {
            System.err.println(" EXception in Notify Call State method");
        }
    }

    @Override
    public void notifyCallMediaState(MyCall call) {
        try {
            //    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            System.out.println(" Call Media State Is  changing : " + call.getInfo().getMedia().toString());
        } catch (Exception ex) {
            System.err.println(" exception whilr in Notify media state ");
        }
    }

    @Override
    public void notifyBuddyState(MyBuddy buddy) {
        //  throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        System.out.println(" Muddy State :  " + buddy.getStatusText());
    }

    @Override
    public void onTyingIndication(OnTypingIndicationParam prm) {
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void notifyInstantMessage(OnInstantMessageParam prm) {
     //   throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
