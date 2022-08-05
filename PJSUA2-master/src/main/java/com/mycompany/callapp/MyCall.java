package com.mycompany.callapp;

import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfo;
import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.OnInstantMessageParam;
import org.pjsip.pjsua2.OnInstantMessageStatusParam;
import org.pjsip.pjsua2.OnTypingIndicationParam;
import org.pjsip.pjsua2.VideoPreview;
import org.pjsip.pjsua2.VideoPreviewOpParam;
import org.pjsip.pjsua2.VideoWindow;
import org.pjsip.pjsua2.pjmedia_type;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsua2;
import org.pjsip.pjsua2.pjsua_call_media_status;

class MyCall extends Call {

    public VideoWindow vidWin;
    public VideoPreview vidPrev;
    public AudioMedia am;

    MyCall(MyAccount acc, int call_id) {
        super(acc, call_id);
        vidWin = null;
    }

    @Override
    public void onInstantMessage(OnInstantMessageParam prm) {
        MyApp.obs.notifyInstantMessage(prm);
        System.out.println("OnInstant Message .......!");
        System.out.println("=======On Instant Message Pager======");
        System.out.println(" From : " + prm.getFromUri());
        System.out.println("To    : " + prm.getToUri());
        System.out.println("Content type :" + prm.getContentType());
        System.out.println("Msg Body : " + prm.getMsgBody());
        System.out.println("=========Message============");

    }

    @Override
    public void onCallState(OnCallStateParam prm) {
        try {
            MyApp.obs.notifyCallState(this);
            CallInfo ci = getInfo();
            System.out.println(" call state in String Format using get stateText " + ci.getStateText());//EARLY
            System.out.println(" call state using last Status code  : " + ci.getLastStatusCode());//180 200 dialog created by the sip 
            System.out.println("call state using state methos : " + ci.getState());//3 different types of dialog created by the sip-invite session
            if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                System.out.println("Call  is SuccessFully Deleted");
                this.hangup(new CallOpParam(true));
                this.delete();
            }
        } catch (Exception e) {
            return;
        }
    }

    @Override
    public void onCallMediaState(OnCallMediaStateParam prm) {
        CallInfo ci = null;
        try {
            ci = getInfo();
        } catch (Exception e) {
            return;
        }

        CallMediaInfoVector cmiv = ci.getMedia();

        for (int i = 0; i < cmiv.size(); i++) {
            CallMediaInfo cmi = cmiv.get(i);
            if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO
                    && (cmi.getStatus()
                    == pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE
                    || cmi.getStatus()
                    == pjsua_call_media_status.PJSUA_CALL_MEDIA_REMOTE_HOLD)) {
                // unfortunately, on Java too, the returned Media cannot be
                // downcasted to AudioMedia 
                System.out.println("CREATING THE CALL MEDIA");
                Media m = getMedia(i);
                am = AudioMedia.typecastFromMedia(m);
                System.out.println(" MEDIA TYPE : " + m.getType());
                if (am.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO) {
                    System.out.println("ITS THE AUDIO CALL");
                }                // connect ports
                try {
                    MyApp.ep.audDevManager().getCaptureDevMedia().
                            startTransmit(am);
                    am.startTransmit(MyApp.ep.audDevManager().
                            getPlaybackDevMedia());
                } catch (Exception e) {
                    continue;
                }
            } else if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_VIDEO
                    && cmi.getStatus()
                    == pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE
                    && cmi.getVideoIncomingWindowId() != pjsua2.INVALID_ID) {
                try {
                    System.out.println("VIDEO CALL ENABLEING THE AUDIO MEDIA");
                    vidWin = new VideoWindow(cmi.getVideoIncomingWindowId());
                    vidWin.setFullScreen(true);
                    vidWin.Show(true);
                    vidPrev = new VideoPreview(cmi.getVideoCapDev());
                    vidPrev.start(new VideoPreviewOpParam());
                } catch (Exception ex) {
                    System.out.println("exception while loading the Video call");
                    ex.printStackTrace();
                    return;
                }
            }
        }
        MyApp.obs.notifyCallMediaState(this);
    }

    @Override
    public void onInstantMessageStatus(OnInstantMessageStatusParam prm) {
        System.out.println(" call Message status : " + prm.getMsgBody() + " call message Status : " + prm.getToUri() + "" + prm.getUserData());
    }

    @Override
    public void onTypingIndication(OnTypingIndicationParam prm) {
        System.out.println(" call tying indication : " + prm.getIsTyping());
    }

}
