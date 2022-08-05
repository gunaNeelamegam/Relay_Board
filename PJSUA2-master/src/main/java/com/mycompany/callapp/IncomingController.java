package com.mycompany.callapp;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.SdpSession;
import org.pjsip.pjsua2.pjsip_status_code;
import org.pjsip.pjsua2.pjsua_call_flag;

class IncomingController {
    
    @FXML
    private JFXButton inHold;
    
    @FXML
    private JFXButton inMute;
    @FXML
    private JFXButton answer;
    @FXML
    private JFXButton inHangUp;
    @FXML
    private JFXButton inUnMute;
    private MyCall call = null;
    
    MyApp app = new MyApp();
    
    @FXML
    void IncomingUnMute(ActionEvent event) throws Exception {
        if (call != null) {
            System.out.println("Call Unmute");
            if (call.getInfo().getStateText().equalsIgnoreCase("CONNECTED")) {
                call.am.adjustTxLevel(1F);
            }
        } else {
            System.out.println("Call instance is  " + call);
        }
    }
    
    @FXML
    public void incomingUnHold() throws Exception {
        System.out.println("Call is Unholding");
        if (call != null) {
            CallOpParam prm = new CallOpParam();
            prm.setStatusCode(pjsua_call_flag.PJSUA_CALL_UNHOLD);
            call.reinvite(prm);
        } else {
            System.out.println("Call instance is :" + call);
        }
    }
    
    @FXML
    void answerIncomingCall(MyCall call) {
        MyAccount acc = app.accList.get(0);
        call = new MyCall(acc, 0);
        CallOpParam prm = new CallOpParam(true);
        SdpSession sdp = new SdpSession();
        sdp.setPjSdpSession(sdp.getPjSdpSession());
        prm.setSdp(sdp);
        prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
        System.out.println("Call Accepted Successfully");
    }
    
    @FXML
    public void incomingHold(ActionEvent event) throws Exception {
        CallOpParam prm = new CallOpParam(true);
        prm.setStatusCode(pjsua_call_flag.PJSUA_CALL_UPDATE_CONTACT);
        call.setHold(prm);
        System.out.println(" Call is Successfully put on Hold");
    }
    
    @FXML
    public void incomingMute(ActionEvent event) throws Exception {
        System.out.println("Call muted ");
        if (call != null) {
            if (call.hasMedia()) {
                CallOpParam prm = new CallOpParam(true);
                call.am.adjustTxLevel(0);
            } else {
                System.err.println("Call has No media");
            }
        } else {
            System.out.println("Call Instance is not created");
        }
        
    }
    
    @FXML
    public void incomingHangUp() throws Exception {
        System.out.println("Call is hangUp successFully");
        CallInfo ci = call.getInfo();
        if (ci.getStateText().equalsIgnoreCase("CONFIRMED") || ci.getStateText().equalsIgnoreCase("EARLY") || ci.getStateText().equalsIgnoreCase("CONNECTING")) {
            CallOpParam prm = new CallOpParam(true);
            prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
            call.hangup(prm);
        }
    }
}
