package com.zilogic.mypjsip;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.SdpSession;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_status_code;

public class ControllerClass implements MyAppObserver {

    @Override
    public void notifyRegState(int code, String reason, int expiration) {

        System.out.println("  regestration code : " + code + "Registeration Reason : " + reason + "Registerartion expiration :" + expiration);
    }

    @Override
    public void notifyIncomingCall(MyCall call) {
        CallOpParam prm = new CallOpParam(true);
        try {
            //        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            System.out.println(" Incoming call For the Current Account :" + call.getInfo());
            CallInfo ci = call.getInfo();
            //if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CALLING || ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_EARLY) {
            System.out.println(" INCOMING CALL a FOR ANSWER AND h FOR HANGUP.....");
            System.out.println("call is Calling state ");
           // prm.setSdp(new SdpSession());
            prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
            call.answer(prm);

        } catch (Exception ex) {
            Logger.getLogger(ControllerClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void notifyCallState(MyCall call) {
        try {
            System.out.println("Call  state is changeing : " + call.getInfo().getStateText() + "  " + call.getInfo().getState());
            if (call.getInfo().getState() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
                MyApp app = new MyApp();
              //  app.CallMessage(call);
            }
        } catch (Exception ex) {
            Logger.getLogger(ControllerClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void notifyCallMediaState(MyCall call) {
        try {
            //    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            System.out.println(" Call Media State Is  changing : " + call.getInfo().getMedia().toString());
        } catch (Exception ex) {
            Logger.getLogger(ControllerClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void notifyBuddyState(MyBuddy buddy) {
        //  throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        System.out.println(" Muddy State :  " + buddy.getStatusText());
    }

}
