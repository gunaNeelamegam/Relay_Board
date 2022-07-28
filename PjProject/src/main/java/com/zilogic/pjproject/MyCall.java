package com.zilogic.pjproject;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfo;
import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.VideoPreview;
import org.pjsip.pjsua2.VideoWindow;

class MyCall extends Call {

    @Override
    public String toString() {
        return "MyCall{" + "audioMedia=" + audioMedia + ", vidWin=" + vidWin + ", vidPrev=" + vidPrev + '}';
    }

    AudioMedia audioMedia;
    public VideoWindow vidWin;

    public VideoPreview vidPrev;

    MyCall(MyAccount paramMyAccount, int paramInt) {
        super((Account) paramMyAccount, paramInt);
        this.vidWin = null;
    }

    public void onCallState(OnCallStateParam paramOnCallStateParam) {
        try {
            CallInfo callInfo = getInfo();
            //6 means that pjsip_inv_state id disConneted
            MyApp.ep.libHandleEvents(1000);
            if (callInfo.getState() == 6) {
//                MyApp.ep.utilLogWrite(3, "MyCall", dump(true, ""));
                MainStageController.outGoingCallStage.close();
            }
        } catch (Exception exception) {
            System.err.println(" Exeption  while onCallState method");
        }
        MyApp.observer.notifyCallState(this);
    }

    public void onCallMediaState(OnCallMediaStateParam paramOnCallMediaStateParam) {
        CallInfo callInfo;
        try {
            callInfo = getInfo();
        } catch (Exception exception) {
            return;
        }
        CallMediaInfoVector callMediaInfoVector = callInfo.getMedia();
        for (byte b = 0; b < callMediaInfoVector.size(); b++) {
            CallMediaInfo callMediaInfo = callMediaInfoVector.get(b);
            if (callMediaInfo.getType() == 1 && (callMediaInfo
                    .getStatus() == 1 || callMediaInfo
                            .getStatus() == 3)) {
                try {
                    audioMedia = getAudioMedia(b);
                    MyApp.ep.audDevManager().getCaptureDevMedia()
                            .startTransmit(audioMedia);
                    audioMedia.startTransmit(MyApp.ep.audDevManager()
                            .getPlaybackDevMedia());
                } catch (Exception exception) {
                    System.out.println("Failed connecting media ports" + exception
                            .getMessage());
                }
            } else if (callMediaInfo.getType() == 2 && callMediaInfo
                    .getStatus() == 1 && callMediaInfo
                            .getVideoIncomingWindowId() != -1) {
                this.vidWin = new VideoWindow(callMediaInfo.getVideoIncomingWindowId());
                this.vidPrev = new VideoPreview(callMediaInfo.getVideoCapDev());
            }
        }
        MyApp.observer.notifyCallMediaState(this);
    }
}
