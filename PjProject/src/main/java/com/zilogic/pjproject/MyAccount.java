/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zilogic.pjproject;

/**
 *
 * @author user
 */
import java.util.ArrayList;
import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.OnRegStateParam;

class MyAccount extends Account {

    public ArrayList<MyBuddy> buddyList = new ArrayList<>();

    public AccountConfig cfg;

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

    public void onIncomingCall(OnIncomingCallParam paramOnIncomingCallParam) {
        System.out.println("======== Incoming call ======== ");
         MyApp.ep.libHandleEvents(100);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException ex) {
//            System.err.println(" Error while at Incoming call at the Account");        }
        MyCall myCall = new MyCall(this, paramOnIncomingCallParam.getCallId());
        MyApp.observer.notifyIncomingCall(myCall);
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
