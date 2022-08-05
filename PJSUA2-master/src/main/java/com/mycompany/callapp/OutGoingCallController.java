package com.mycompany.callapp;

//outgoingcallController
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.pjsip.pjsua2.*;

public class OutGoingCallController {

    FXMLController fxmlcontroller = new FXMLController();
    static CallOpParam callStatusparam = new CallOpParam();

    @FXML
    public void hangUpCall(ActionEvent event) throws Exception {

        System.out.println("ending up");
        FXMLController.stage.close();

    }

    @FXML
    public void muteCall(ActionEvent event) {

    }

    public void call() {

    }

}
