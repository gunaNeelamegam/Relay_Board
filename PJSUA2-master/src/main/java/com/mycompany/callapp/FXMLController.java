package com.mycompany.callapp;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.Endpoint;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.pjsip_status_code;

public class FXMLController implements Initializable {

    public String getCallString() {
        return callString;
    }

    public void setCallString(String callString) {
        FXMLController.callString = callString;
    }

    @FXML
    private Button no1;
    @FXML
    private Button no2;
    @FXML
    private Button no3;
    @FXML
    private Button no4;
    @FXML
    private Button no5;
    @FXML
    private Button no6;
    @FXML
    private Button no7;
    @FXML
    private Button no8;
    @FXML
    private Button no9;
    @FXML
    private Button no0;
    @FXML
    private Label status;
    @FXML
    private Button aster;
    @FXML
    private Button ash;
    @FXML
    private Button del;
    @FXML
    private TextField callText;
    @FXML
    private Button hangUp;
    @FXML
    private Button mute;
    @FXML
    public Button outGoingCall;
    @FXML
    Label displayText;
    @FXML
    public static Stage stage = null;
    @FXML
    public static Stage addAccountStage = null;
    @FXML
    JFXButton accColor = new JFXButton();
    @FXML
    Button AccountStage = null;
    @FXML
    JFXListView<String> listview = new JFXListView<>();
    public static CallOpParam callStatusparam = new CallOpParam();
    public static Endpoint endpoint = null;
    public static String callString = "";
    private String callStatus;
    public MyCall call = null;
    public static Account account = null;
    AddAccount addaccount = new AddAccount();

    MyApp app = new MyApp();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {

            app.start(".", false);
        } catch (IOException ex) {
            System.out.println(" exception at Starting the Stack ");
        } catch (Exception ex) {
           // Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void numberCal(ActionEvent event) {
        System.out.println("numberCal function");
        Button btn = (Button) event.getSource();
        callString += btn.getText();
        System.out.println(" Number Callculation : " + callString);
        callText.setText(callString);
    }

    @FXML
    public void outGoingCall(ActionEvent event) throws Exception {
        makeCall();
    }

    @FXML
    public void deleteString(ActionEvent event) {
        int deleteBtnCnt = 0;
        deleteBtnCnt++;
        callString = callString.substring(0, callString.length() - deleteBtnCnt);
        callText.setText(callString);
        try {
            if (callString.length() == 0) {
                deleteBtnCnt = 0;
                System.out.println("DeleteButton is Clicked");
            }
        } catch (Exception e) {
            System.out.println("No Number is Present in call Text");
        }
    }

    @FXML
    public void hangUp(ActionEvent event) throws Exception {
        System.out.println("Call is hangUp successFully");
        CallInfo ci = call.getInfo();
        if (ci.getStateText().equalsIgnoreCase("CONFIRMED") || ci.getStateText().equalsIgnoreCase("EARLY") || ci.getStateText().equalsIgnoreCase("CONNECTING")) {
            CallOpParam prm = new CallOpParam(true);
            prm.setStatusCode(pjsip_status_code.PJSIP_SC_OK);
            call.hangup(prm);
        }
    }

    @FXML
    public void muteCall() throws Exception {
        System.out.println("Call muted ");
        if (call != null) {
            if (call.hasMedia()) {
                CallOpParam prm = new CallOpParam(true);
                call.am.adjustTxLevel(0);
            } else {
                System.err.println("Call has No media");
            }
        } else {
            System.out.println("Call Instance is not created");
        }
    }

    @FXML
    public void unMuteCall() throws Exception {
        if (call != null) {
            System.out.println("Call Unmute");
            if (call.getInfo().getStateText().equalsIgnoreCase("CONNECTED")) {
                call.am.adjustTxLevel(1F);
            }
        } else {
            System.out.println("Call instance is  " + call);
        }
    }
//outgoing call is handle

    @FXML
    public void makeCall() throws Exception {
        System.out.println("outgoing call ");
        MyAccount myacc = createAccount();
        System.out.println("Account i defualt");
        CallOpParam prm = new CallOpParam(true);
        call = new MyCall(myacc, 0);
        call.makeCall("sip:" + callString + "@" + AddAccountController.userdetails.get(0).getDomainAddress(), prm);
    }

    public void showAddAccount() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("AddAccount.fxml"));
        addAccountStage = new Stage();
        Scene scene = new Scene(root);
        addAccountStage.setScene(scene);
        addAccountStage.setTitle("ADD ACCOUNT");
        addAccountStage.centerOnScreen();
        addAccountStage.initModality(Modality.WINDOW_MODAL);
        addAccountStage.show();
    }

    @FXML
    public MyAccount createAccount() throws Exception {
        app.accList.clear();
        MyAccountConfig accCfg = new MyAccountConfig();
        AddAccount addaccount = AddAccountController.userdetails.get(0);
        AuthCredInfo auth = new AuthCredInfo("digest", "*", addaccount.getUsername(), 0, addaccount.getPassword());
        AccountConfig accCon = new AccountConfig();
        accCon.setIdUri("sip:" + addaccount.getUsername() + "@" + addaccount.getDomainAddress().trim());
        accCon.getRegConfig().setRegistrarUri("sip:" + addaccount.getDomainAddress().trim());
        accCon.getSipConfig().getAuthCreds().add(auth);
        accCon.getNatConfig().setIceEnabled(true);
        accCon.getVideoConfig().setAutoTransmitOutgoing(true);
        accCon.getVideoConfig().setAutoShowIncoming(true);
        MyAccount myacc = app.addAcc(accCon);
        app.accCfgs.add(accCfg);
        for (MyAccount acc : app.accList) {
            AccountInfo accinfo = acc.getInfo();
            listview.getItems().add(acc.getInfo().getUri());
            acc.setDefault();
            System.out.println(" Registeration Status Expiry time : " + acc.getInfo().getRegExpiresSec());
            System.out.println(" Account default : " + acc.isDefault() + "    ");
            System.out.println(acc.isValid());
            acc.getInfo().setOnlineStatus(true);
            System.out.println(accinfo.getOnlineStatus() + "Account status  text :" + accinfo.getOnlineStatusText());
            app.saveConfig("pjsua2");
        }

        if (myacc != null) {
            return myacc;
        } else {
            return null;
        }
    }

    @FXML
    public void DefaultAccountColor() {
        if (app.accList.get(0).isDefault() && app.accList.get(0).isValid()) {
            accColor.setStyle("-fx-background-color: green;-fx-text-fill:white;-fx-background-radius: 2em;-fx-min-width: 10px;-fx-min-height: 10px;-fx-max-width: 12px;-fx-max-height: 12px;");
        } else if (!app.accList.get(0).isDefault() && app.accList.get(0).isValid()) {
            accColor.setStyle("-fx-background-color: red;-fx-text-fill:white;-fx-background-radius: 2em;-fx-min-width: 10px;-fx-min-height: 10px;-fx-max-width: 12px;-fx-max-height: 12px;");
        } else {
            System.out.println(" Account is not added into the asterisk");
        }
    }

}
