package com.zilogic.pjproject;

import org.pjsip.pjsua2.CallOpParam;

class RunLaterThread {

    static MyApp app = new MyApp();
    static MyObserver observer = new MyObserver();
    static MyCall currentCall;

    public void runWorker() throws Exception {

        MyAccount acc = MyApp.accList.get(0);
        acc.setDefault();
        System.out.println(" Account : " + acc.isValid() + acc.isDefault());
        currentCall = new MyCall(acc, 0);
        currentCall.makeCall("sip:6002@192.168.0.132", new CallOpParam(true));
        while (currentCall.getInfo().getState() != 6) {
            MyApp.ep.libHandleEvents(10L);
            Thread.sleep(1000);
        }
        if (currentCall.getInfo().getState() == 6) {
        OutGoingCallController.exitThreadCalling=true;
        }

//        OutGoingCallController.exitThreadCalling = true;
//        currentCall.delete();
//        currentCall = null;
        // app.deinit();
    }

    /*
    * This method is used to send to verfica
     */
    public static boolean verifyregisteration() throws Exception {

        if (MainStageController.account.getInfo().getIsDefault()) {
            return false;
        } else {
            return true;
        }

    }
}
