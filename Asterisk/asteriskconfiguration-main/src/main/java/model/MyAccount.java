/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author user
 */
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.OnRegStateParam;
import org.pjsip.pjsua2.pjsip_status_code;

public class MyAccount extends Account {

    @Override
    public String toString() {
        return "MyAccount{" + "i=" + i + ", incomingcall=" + incomingcall + ", exitIncomingCall=" + exitIncomingCall + ", buddyList=" + buddyList + ", cfg=" + cfg + '}';
    }

    int i = 0;
    Thread incomingcall;
    boolean exitIncomingCall = false;
    static MyCall currentCall;
    public ArrayList<MyBuddy> buddyList = new ArrayList<>();

    public AccountConfig cfg;

    public MyAccount() {
    }

    MyAccount(AccountConfig paramAccountConfig) {
        this.cfg = paramAccountConfig;
    }

    public MyBuddy addBuddy(BuddyConfig paramBuddyConfig) {
        MyBuddy myBuddy = new MyBuddy(paramBuddyConfig);
        try {
            myBuddy.create(this, paramBuddyConfig);
        } catch (Exception exception) {
            myBuddy.delete();
            myBuddy = null;
        }
        if (myBuddy != null) {
            this.buddyList.add(myBuddy);
            if (paramBuddyConfig.getSubscribe())
        try {
                myBuddy.subscribePresence(true);
            } catch (Exception exception) {
            }
        }
        return myBuddy;
    }

    public void delBuddy(MyBuddy paramMyBuddy) {
        this.buddyList.remove(paramMyBuddy);
        paramMyBuddy.delete();
    }

    public void delBuddy(int paramInt) {
        MyBuddy myBuddy = this.buddyList.get(paramInt);
        this.buddyList.remove(paramInt);
        myBuddy.delete();
    }

    public void onRegState(OnRegStateParam paramOnRegStateParam) {
        MyApp.observer.notifyRegState(paramOnRegStateParam.getCode(), paramOnRegStateParam.getReason(), paramOnRegStateParam
                .getExpiration());
    }

    public synchronized void onIncomingCall(OnIncomingCallParam paramOnIncomingCallParam) {
        System.out.println(" INCOMING CALL");
        try {
            currentCall = new MyCall(this, paramOnIncomingCallParam.getCallId());
            CallOpParam prm = new CallOpParam(true);
            prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
        } catch (Exception ex) {
        }
    }

    public void onInstantMessage(OnInstantMessageParam paramOnInstantMessageParam) {
        System.out.println("======== Incoming pager ======== ");
        System.out.println("From     : " + paramOnInstantMessageParam.getFromUri());
        System.out.println("To       : " + paramOnInstantMessageParam.getToUri());
        System.out.println("Contact  : " + paramOnInstantMessageParam.getContactUri());
        System.out.println("Mimetype : " + paramOnInstantMessageParam.getContentType());
        System.out.println("Body     : " + paramOnInstantMessageParam.getMsgBody());
    }

}
