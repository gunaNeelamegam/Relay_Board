
package com.zilogic.mypjsip;

import org.pjsip.pjsua2.*;






class Mypjsip {

      
    static {
        System.loadLibrary("pjsua2");
        System.out.println("Library loaded");
       // System.loadLibrary("open264");
        System.out.println("Library loaded");
    }

//    public static void main(String args[]) {
//        try {
//            // Create endpoint
//            Endpoint ep = new Endpoint();
//            ep.libCreate();
//            // Initialize endpoint
//            EpConfig epConfig = new EpConfig();
//            ep.libInit(epConfig);
//            // Create SIP transport. Error handling sample is shown
//            TransportConfig sipTpConfig = new TransportConfig();
//            sipTpConfig.setPort(5080);
//            ep.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_UDP, sipTpConfig);
//            // Start the library
//            ep.libStart();
//
//            AccountConfig acfg = new AccountConfig();
//            acfg.setIdUri("sip:6002@192.168.0.180");
//            acfg.getRegConfig().setRegistrarUri("sip:192.168.0.180");
//            AuthCredInfo cred = new AuthCredInfo("digest", "*", "6002", 0, "1234");
//            acfg.getSipConfig().getAuthCreds().add(cred);
//            // Create the account
//            MyAccount acc = new MyAccount();
//            acc.create(acfg);
//            
//            Call call = new Call(acc);
//            
//            call.makeCall("sip:6001@192.168.0.180", new CallOpParam(true));
//
//            while (call.isActive()) {
//                Thread.sleep(100);
//                System.out.println("Active call");
//            }
//            
//            
//            // Here we don't have anything else to do..
//            
//            /* Explicitly delete the account.
//           * This is to avoid GC to delete the endpoint first before deleting
//           * the account.
//             */
//            acc.delete();
//
//            // Explicitly destroy and delete endpoint
//            ep.libDestroy();
//            ep.delete();
//            
//
//        } catch (Exception e) {
//            System.out.println(e);
//            
//        }
//
//    }
//
//}
}