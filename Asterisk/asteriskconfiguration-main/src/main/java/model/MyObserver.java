package model;

import com.zilogic.asteriskconfiguration.MainScreenUIController;
import com.zilogic.asteriskconfiguration.OutGoingCallController;
import com.zilogic.asteriskconfiguration.RegisterNodeController;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallOpParam;

public class MyObserver implements MyAppObserver {
    
    protected static MyCall currentCall = null;
    
    private boolean del_call_scheduled = false;
    
    public void check_call_deletion() {
        System.out.println("Check call deletion : ");
        if (this.del_call_scheduled && currentCall != null) {
            currentCall.delete();
            currentCall = null;
            this.del_call_scheduled = false;
        }
    }
    
    public void check_OutGoinig_CallDeletion(MyCall call) throws Exception {
        
    }
//    }

    public void notifyRegState(int paramInt, String paramString, long paramLong) {
        
        System.out.println(" Registartion status : " + paramString);
    }
    
    public void notifyIncomingCall(MyCall paramMyCall) {
        System.out.println(" INCOMING CALL  in NOTIFY METHOD");
        try {
            CallOpParam callOpParam = new CallOpParam();
            try {
                currentCall = paramMyCall;
            } catch (Exception exception) {
                System.out.println(exception);
            }
        } catch (Exception ex) {
            System.err.println(" Exception while loading at IncomingCall ui");
        }
        
    }
    
    public void notifyCallMediaState(MyCall paramMyCall) {
        System.out.println("MEDIA STATE : " + paramMyCall.toString());
    }
    
    public void notifyCallState(MyCall paramMyCall) {
        CallInfo callInfo;
        if (currentCall == null || paramMyCall.getId() != currentCall.getId()) {
            return;
        }
        try {
            callInfo = paramMyCall.getInfo();
        } catch (Exception exception) {
            callInfo = null;
        }
        if (callInfo.getState() == 6) {
            System.out.println(" Call state :  " + callInfo.getStateText()); // Logger.getLogger(MyObserver.class.getName()).log(Level.SEVERE, null, ex);
            this.del_call_scheduled = true;
            CallOpParam prm = new CallOpParam(true);
            currentCall.delete();
            currentCall = null;
            OutGoingCallController.call.setDisable(false);
            MainScreenUIController.outGoingCallStage.close();
            OutGoingCallController.exitThreadCalling = true;
            System.out.println(" Call Deleted ");
        }
        
    }
    
    public void notifyBuddyState(MyBuddy paramMyBuddy) {
    }
    
    public void notifyChangeNetwork() {
    }
    
    public boolean verifyregisteration() throws Exception {
        
        if (RegisterNodeController.account.getInfo().getRegIsActive() && RegisterNodeController.account.getInfo().getRegStatus() == 200) {
            System.out.println(" Account Registeration is Done...!");
            return true;
        } else {
            return false;
        }
    }
}
