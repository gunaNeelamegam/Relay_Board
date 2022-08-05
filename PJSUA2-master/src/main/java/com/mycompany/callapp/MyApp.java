package com.mycompany.callapp;

import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.LogConfig;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.UaConfig;
import org.pjsip.pjsua2.pjsip_transport_type_e;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.pjsip.pjsua2.*;

interface MyAppObserver {

    abstract void notifyRegState(int code, String reason,
            int expiration);

    abstract void notifyIncomingCall(MyCall call);

    abstract void notifyCallState(MyCall call);

    abstract void notifyCallMediaState(MyCall call);

    abstract void notifyBuddyState(MyBuddy buddy);

    public void onTyingIndication(OnTypingIndicationParam prm);

    public void notifyInstantMessage(OnInstantMessageParam prm);

}

class MyLogWriter extends LogWriter {

    @Override
    public void write(LogEntry entry) {
        System.out.println(entry.getMsg() + "    " + entry.getThreadName());
    }
}

public class MyApp {

    public static MyAppObserver obs = new ControllerClass();

    static {
        try {
            System.loadLibrary("openh264");
            System.loadLibrary("pjsua2");
//            System.loadLibrary("speex");
//            System.loadLibrary("yuv");
//            System.loadLibrary("opus");
//            System.loadLibrary("gsmcodec");
//            System.loadLibrary("g7221codec");

            System.out.println("Library loaded");
        } catch (Exception e) {
            System.out.println("Dynamic library loading exception");
        }
    }

    public static Endpoint ep = new Endpoint();
    public ArrayList<MyAccount> accList = new ArrayList<MyAccount>();
    static Scanner scanner = new Scanner(System.in);
    ArrayList<MyAccountConfig> accCfgs = new ArrayList<MyAccountConfig>();
    private EpConfig epConfig = new EpConfig();
    private TransportConfig sipTpConfig = new TransportConfig();
    private String appDir = ".";
    /* Maintain reference to log writer to avoid premature cleanup by GC */
    private MyLogWriter logWriter;

    private final String configName = "pjsua2.json";
    private final int SIP_PORT = 5080;
    private final int LOG_LEVEL = 5;
    static Call call = null;

    public void start(String app_dir, boolean own_worker_thread) throws IOException, Exception {
        appDir = app_dir;
            System.out.println();
        /* Create endpoint */
        try {
            System.out.println("=========================================");
            ep.libCreate();
        } catch (Exception e) {
            return;
        }

        /* Load config */
        String configPath = appDir + "/" + configName;
        File f = new File(configPath);
        if (!f.exists()) {
            f.createNewFile();
        }

        sipTpConfig.setPort(SIP_PORT);

//        epConfig.getLogConfig().setConsoleLevel(LOG_LEVEL);
        epConfig.getLogConfig().setFilename(appDir + "/" + "conf");
        /* Set log config. */
        LogConfig log_cfg = epConfig.getLogConfig();
        logWriter = new MyLogWriter();
        log_cfg.setWriter(logWriter);

        /* Set ua config. */
        UaConfig ua_cfg = epConfig.getUaConfig();
        if (own_worker_thread) {
            ua_cfg.setThreadCnt(1);
            ua_cfg.setMainThreadOnly(false);
            
        }

        /* Init endpoint */
 /* Create transports. */
        try {
            ep.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_UDP,
                    sipTpConfig);
        } catch (Exception e) {
            System.out.println("exception at Creating the Transport inself");
            System.out.println(e);
        }

        try {
            ep.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_TCP,
                    sipTpConfig);
        } catch (Exception e) {
            System.out.println(e);
        }
        try {
            ep.libInit(epConfig);
        } catch (Exception e) {
            return;
        }
        /* Start. */
        try {
            ep.libStart();
        } catch (Exception e) {
            return;
        }

        /* Create accounts. */
        for (int i = 0; i < accCfgs.size(); i++) {
            MyAccountConfig my_cfg = accCfgs.get(i);
            System.out.println(" Account xco-------------- " + my_cfg.accCfg);
            /* Customize account config */
            my_cfg.accCfg.getNatConfig().setIceEnabled(true);
            my_cfg.accCfg.getVideoConfig().setAutoTransmitOutgoing(true);
            my_cfg.accCfg.getVideoConfig().setAutoShowIncoming(true);

            MyAccount acc = addAcc(my_cfg.accCfg);
            if (acc == null) {
                System.out.println("Account is not created");
                continue;

            }

        }

    }

    private void loadConfig(String filename) {
        JsonDocument json = new JsonDocument();

        try {
            /* Load file */
            json.loadFile(filename);
            ContainerNode root = json.getRootContainer();

            /* Read endpoint config */
            epConfig.readObject(root);

            /* Read transport config */
            ContainerNode tp_node = root.readContainer("SipTransport");
            sipTpConfig.readObject(tp_node);

            /* Read account configs */
            accCfgs.clear();
            ContainerNode accs_node = root.readArray("accounts");
            while (accs_node.hasUnread()) {
                MyAccountConfig acc_cfg = new MyAccountConfig();
                acc_cfg.readObject(accs_node);
                accCfgs.add(acc_cfg);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        /* Force delete json now, as I found that Java somehow destroys it
	* after lib has been destroyed and from non-registered thread.
         */
        json.delete();
    }

    void saveConfig(String filename) {
        JsonDocument json = new JsonDocument();

        try {
            /* Write endpoint config */
            json.writeObject(epConfig);

            /* Write transport config */
            ContainerNode tp_node = json.writeNewContainer("SipTransport");
            sipTpConfig.writeObject(tp_node);

            /* Write account configs */
            ContainerNode accs_node = json.writeNewArray("accounts");
            for (int i = 0; i <= accCfgs.size(); i++) {
                accCfgs.get(i).writeObject(accs_node);
            }

            /* Save file */
            json.saveFile(filename);
        } catch (Exception e) {

        }
        json.delete();
    }

    public MyAccount addAcc(AccountConfig cfg) {
        MyAccount acc = new MyAccount(cfg);
        try {
            System.out.println("=================Account created Succesfully===============");
            acc.create(cfg);
        } catch (Exception e) {
            System.out.println("=======================");
            acc = null;
            return null;
        }

        accList.add(acc);
        return acc;
    }

    public void delAcc(MyAccount acc) {
        accList.remove(acc);
    }

    public void deinit() {
        Runtime.getRuntime().gc();
        try {
            ep.libDestroy();
        } catch (Exception e) {
        }
        ep.delete();
        ep = null;

    }

    public MyBuddy createBuddy(String buddyUri) throws Exception {
        System.out.println("create the Account  Buddy For the Default Account .....!");
        BuddyConfig bdy = new BuddyConfig();
        bdy.setSubscribe(true);
        bdy.setUri("sip:" + buddyUri + "@192.168.0.132");
        for (MyAccount acc : accList) {
            if (acc.isDefault() && acc.isValid()) {
                MyBuddy buddy = acc.addBuddy(bdy);
                System.out.println("==================" + "\n" + "buddy Created Successfuly " + "\n" + "=====================");
                acc.buddyList.add(buddy);
                return buddy;
            }
        }
        return null;
    }

    public String CallMessage(Call call) throws Exception {
        if (call.getInfo().getStateText().equalsIgnoreCase("CONFIRMED")) {
            SendTypingIndicationParam typing = new SendTypingIndicationParam();
            typing.setIsTyping(true);
            call.sendTypingIndication(typing);
            SendInstantMessageParam mes = new SendInstantMessageParam();
            System.out.println("please type the message..........!");
            mes.setContent(scanner.next());
            mes.setContentType("text/plain");
            call.sendInstantMessage(mes);
            System.out.println("Sended SuccessFully........");
            return "SuccessFully";
        } else {
            return "Call is Not in active ";
        }
    }

    public void buddyInstantMessage(String username) throws Exception {
        BuddyConfig bdy = new BuddyConfig();
        bdy.setUri("sip:" + username + "@192.168.0.132");
        bdy.setSubscribe(false);
        for (MyAccount acc : accList) {
            if (acc.isDefault() && acc.isValid()) {
                MyBuddy mybud = acc.addBuddy(bdy);
                System.out.println(" Buddy status : " + bdy.getUri());
                SendInstantMessageParam prm = new SendInstantMessageParam();
                prm.setContent("Hello Buddy");
                mybud.sendInstantMessage(prm);
            } else {
                return;
            }
        }
    }
}
