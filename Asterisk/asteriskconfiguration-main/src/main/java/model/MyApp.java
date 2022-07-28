package com.zilogic.pjproject;

import java.io.File;
import java.util.ArrayList;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.BuddyConfig;
import org.pjsip.pjsua2.ContainerNode;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.IpChangeParam;
import org.pjsip.pjsua2.JsonDocument;
import org.pjsip.pjsua2.LogConfig;
import org.pjsip.pjsua2.LogWriter;
import org.pjsip.pjsua2.PersistentObject;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.UaConfig;

class MyApp {

    public static Endpoint ep = new Endpoint();

    public static MyAppObserver observer;

    public static ArrayList<MyAccount> accList = new ArrayList<>();

    public static ArrayList<MyAccountConfig> accCfgs = new ArrayList<>();

    private EpConfig epConfig = new EpConfig();

    private TransportConfig sipTpConfig = new TransportConfig();

    private String appDir;

    private MyLogWriter logWriter;

    private final String configName = "pjsua2.json";

    // private final int SIP_PORT = 5080;
    private final int LOG_LEVEL = 4;

    public void init(MyAppObserver paramMyAppObserver, String paramString) {
        init(paramMyAppObserver, paramString, false);
    }

    public void init(MyAppObserver paramMyAppObserver, String paramString, boolean paramBoolean) {
        observer = paramMyAppObserver;
        this.appDir = paramString;
        try {
            ep.libCreate();
        } catch (Exception exception) {
            return;
        }
        String str = this.appDir + "/" + "pjsua2.json";
        File file = new File(str);
        if (file.exists()) {
            loadConfig(str);
        } else {
            this.sipTpConfig.setPort(6000L);
        }
        this.epConfig.getLogConfig().setLevel(4L);
        this.epConfig.getLogConfig().setConsoleLevel(4L);
        LogConfig logConfig = this.epConfig.getLogConfig();
        this.logWriter = new MyLogWriter();
        logConfig.setWriter((LogWriter) this.logWriter);
        logConfig.setDecor(logConfig.getDecor() & 0xFFFFFFFFFFFFFE7FL);
        UaConfig uaConfig = this.epConfig.getUaConfig();
        uaConfig.setUserAgent("Guna Application " + ep.libVersion().getFull());
        if (paramBoolean) {
            uaConfig.setThreadCnt(0L);
            uaConfig.setMainThreadOnly(true);
        }
        try {
            ep.libInit(this.epConfig);
        } catch (Exception exception) {
            return;
        }
        try {
            ep.transportCreate(1, this.sipTpConfig);
        } catch (Exception exception) {
            System.out.println(exception);
        }
        try {
            ep.transportCreate(2, this.sipTpConfig);
        } catch (Exception exception) {
            System.out.println(exception);
        }
        try {
            this.sipTpConfig.setPort(6001L);
            ep.transportCreate(3, this.sipTpConfig);
        } catch (Exception exception) {
            System.out.println(exception);
        }
        this.sipTpConfig.setPort(6000L);
        for (int b = 0; b < this.accCfgs.size(); b++) {
            MyAccountConfig myAccountConfig = this.accCfgs.get(b);
            myAccountConfig.accCfg.getNatConfig().setIceEnabled(true);
            myAccountConfig.accCfg.getVideoConfig().setAutoTransmitOutgoing(true);
            myAccountConfig.accCfg.getVideoConfig().setAutoShowIncoming(true);
            myAccountConfig.accCfg.getMediaConfig().setSrtpUse(1);
            myAccountConfig.accCfg.getMediaConfig().setSrtpSecureSignaling(0);
            MyAccount myAccount = addAcc(myAccountConfig.accCfg);
            if (myAccount != null) {
                for (int b1 = 0; b1 < myAccountConfig.buddyCfgs.size(); b1++) {
                    BuddyConfig buddyConfig = myAccountConfig.buddyCfgs.get(b1);
                    myAccount.addBuddy(buddyConfig);
                }
            }
        }
        try {
            ep.libStart();
        } catch (Exception exception) {
            return;
        }
    }

    public MyAccount addAcc(AccountConfig paramAccountConfig) {
        MyAccount myAccount = new MyAccount(paramAccountConfig);
        try {
            myAccount.create(paramAccountConfig);
        } catch (Exception exception) {
            System.out.println(exception);
            myAccount = null;
            return null;
        }
        this.accList.add(myAccount);
        return myAccount;
    }

    public void delAcc(MyAccount paramMyAccount) {
        this.accList.remove(paramMyAccount);
    }

    private void loadConfig(String paramString) {
        JsonDocument jsonDocument = new JsonDocument();
        try {
            jsonDocument.loadFile(paramString);
            ContainerNode containerNode1 = jsonDocument.getRootContainer();
            this.epConfig.readObject(containerNode1);
            ContainerNode containerNode2 = containerNode1.readContainer("SipTransport");
            this.sipTpConfig.readObject(containerNode2);
            this.accCfgs.clear();
            ContainerNode containerNode3 = containerNode1.readArray("accounts");
            while (containerNode3.hasUnread()) {
                MyAccountConfig myAccountConfig = new MyAccountConfig();
                myAccountConfig.readObject(containerNode3);
                this.accCfgs.add(myAccountConfig);
            }
        } catch (Exception exception) {
            System.out.println(exception);
        }
        jsonDocument.delete();
    }

    private void buildAccConfigs() {
        this.accCfgs.clear();
        for (byte b = 0; b < this.accList.size(); b++) {
            MyAccount myAccount = this.accList.get(b);
            MyAccountConfig myAccountConfig = new MyAccountConfig();
            myAccountConfig.accCfg = myAccount.cfg;
            myAccountConfig.buddyCfgs.clear();
            for (byte b1 = 0; b1 < myAccount.buddyList.size(); b1++) {
                MyBuddy myBuddy = myAccount.buddyList.get(b1);
                myAccountConfig.buddyCfgs.add(myBuddy.cfg);
            }
            this.accCfgs.add(myAccountConfig);
        }
    }

     void saveConfig(String paramString) {
        JsonDocument jsonDocument = new JsonDocument();
        try {
            jsonDocument.writeObject((PersistentObject) this.epConfig);
            ContainerNode containerNode1 = jsonDocument.writeNewContainer("SipTransport");
            this.sipTpConfig.writeObject(containerNode1);
            buildAccConfigs();
            ContainerNode containerNode2 = jsonDocument.writeNewArray("accounts");
            for (byte b = 0; b < this.accCfgs.size(); b++) {
                ((MyAccountConfig) this.accCfgs.get(b)).writeObject(containerNode2);
            }
            jsonDocument.saveFile(paramString);
        } catch (Exception exception) {
        }
        jsonDocument.delete();
    }

    public void handleNetworkChange() {
        try {
            System.out.println("Network change detected");
            IpChangeParam ipChangeParam = new IpChangeParam();
            ep.handleIpChange(ipChangeParam);
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    public void deinit() {
        String str = this.appDir + "/" + "pjsua2.json";
        saveConfig(str);
        Runtime.getRuntime().gc();
        try {
            ep.libDestroy();
        } catch (Exception exception) {
        }
        ep.delete();
        ep = null;
    }
}
