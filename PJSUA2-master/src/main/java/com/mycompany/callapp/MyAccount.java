package com.mycompany.callapp;

import java.util.ArrayList;
import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.OnIncomingCallParam;
import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.OnInstantMessageStatusParam;
import org.pjsip.pjsua2.OnRegStateParam;
import org.pjsip.pjsua2.OnTypingIndicationParam;
import org.pjsip.pjsua2.pjsip_status_code;

class MyAccount extends Account {

    public ArrayList<MyBuddy> buddyList = new ArrayList<MyBuddy>();
    public AccountConfig cfg;

    MyAccount(AccountConfig config) {
        super();
        cfg = config;
    }

    public MyBuddy addBuddy(BuddyConfig bud_cfg) {
        /* Create Buddy */
        MyBuddy bud = new MyBuddy(bud_cfg);
        try {
            bud.create(this, bud_cfg);
        } catch (Exception e) {
            bud.delete();
            bud = null;
        }

        if (bud != null) {
            buddyList.add(bud);
            if (bud_cfg.getSubscribe())
		try {
                bud.subscribePresence(true);
            } catch (Exception e) {
            }
        }

        return bud;
    }

    public void delBuddy(MyBuddy buddy) {
        buddyList.remove(buddy);
        buddy.delete();
    }

    public void delBuddy(int index) {
        MyBuddy bud = buddyList.get(index);
        buddyList.remove(index);
        bud.delete();
    }

    @Override
    public void onRegState(OnRegStateParam prm) {
        System.out.println("*** On registration state: " + prm.getCode() + prm.getReason());
        MyApp.obs.notifyRegState(prm.getCode(), prm.getReason(), (int) prm.getExpiration());
    }

    @Override
    public void onIncomingCall(OnIncomingCallParam prm) {
        try {
            System.out.println("======== Incoming call ======== ");
            MyCall call = new MyCall(this, prm.getCallId());
            System.out.println(" Incoming call State : " + call.getInfo().getStateText());
            MyApp.obs.notifyIncomingCall(call);
            System.out.println(" Incoming call State : " + call.getInfo().getStateText());
        } catch (Exception ex) {
            System.out.println("call answering stage");
        }
    }

    @Override
    public void onTypingIndication(OnTypingIndicationParam prm) {
        System.out.println("Type of Indication   " + prm.getIsTyping());
        MyApp.obs.onTyingIndication(prm);
        if (this.isDefault() || this.isValid()) {
            prm.setIsTyping(true);
            System.out.println("Type of Indication   " + prm.getIsTyping());
        }
    }

    @Override
    public void onInstantMessageStatus(OnInstantMessageStatusParam prm) {
        System.out.println("INSTANT MESSAGEING CODE :  " + prm.getCode());
        System.out.println("GET TO URI : " + prm.getToUri());
        System.out.println(" On Incoming message Status : " + prm.getMsgBody().toString());
    }

    @Override
    public void onInstantMessage(OnInstantMessageParam prm) {
        System.out.println("======== Incoming pager ======== ");
        System.out.println("From     : " + prm.getFromUri());
        System.out.println("To       : " + prm.getToUri());
        System.out.println("Contact  : " + prm.getContactUri());
        System.out.println("Mimetype : " + prm.getContentType());
        System.out.println("Body     : " + prm.getMsgBody());
        System.out.println("==================================");
    }

}
