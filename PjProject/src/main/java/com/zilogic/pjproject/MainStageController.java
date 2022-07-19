package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.AuthCredInfo;

public class MainStageController implements Initializable {

//Account user authication
    AuthCredInfo auth;
    //MyApp has all utility function are created...
    private static MyApp app = new MyApp();
    //which is used to show all the account in the list
    @FXML
    static JFXListView<String> listView;
    @FXML
    public static Stage addAccountStage;
    @FXML
    public static Stage outGoingCallStage;
    @FXML
    public static Stage add_buddy_stage;
    private static MyObserver observer = new MyObserver();
    public static MyAccount account = null;
    private static AccountConfig accCfg = null;
    @FXML
    public static Pane middlePane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            Runtime.getRuntime().addShutdownHook((Thread) new MyShutdownHook(Thread.currentThread()));
            app.init((MyAppObserver) observer, ".", true);
        } catch (Exception ex) {
            System.err.println(" Excpetion while loading the runWorker");
        }
    }

    @FXML
    public void accountButton() throws IOException {

        addAccountStage = new Stage();
        System.out.println("Account Button is Clicked....");
        Parent root = FXMLLoader.load(getClass().getResource("AddAccount.fxml"));
        Scene scene = new Scene(root);
        addAccountStage.setScene(scene);
        addAccountStage.showAndWait();
    }

    @FXML
    public void callButton() throws IOException {
        outGoingCallStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("outGoingCall.fxml"));
        Scene scene = new Scene(root);
        outGoingCallStage.setScene(scene);
        outGoingCallStage.showAndWait();
    }

    @FXML
    public void buddyButton() throws Exception {
        System.out.println("Buddy button is clicked");
//        add_buddy_stage = new Stage();
//        Parent root = FXMLLoader.load(getClass().getResource("AddBuddy.fxml"));
//        var scene = new Scene(root);
//        add_buddy_stage.setScene(scene);
//        add_buddy_stage.setTitle("ADD BUDDY ACCOUNT :)");
//        add_buddy_stage.showAndWait();
    }

    @FXML
    public static MyAccount createAccount() throws Exception {
        MyApp.accList.clear();
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
        account = app.addAcc(accCon);
        app.saveConfig("pjsua2.json");
        System.out.println("========================================");
        MyApp.accCfgs.add(accCfg);
        for (MyAccount acc : MyApp.accList) {
            AccountInfo accinfo = acc.getInfo();
            if (acc.isDefault()) {
                System.out.println("account uri : " + acc.getInfo().getUri());
            }
            if (acc.getId() == 0) {
                listView.getItems().add(acc.getInfo().getUri());
                acc.setDefault();
                System.out.println(" Registeration Status Expiry time : " + acc.getInfo().getRegExpiresSec());
                System.out.println(" Account default : " + acc.isDefault() + "    ");
                System.out.println(acc.isValid());
                acc.getInfo().setOnlineStatus(true);
                System.out.println(accinfo.getOnlineStatus() + "Account status  text :" + accinfo.getOnlineStatusText());
            }
        }
        if (account != null) {
            return account;
        } else {
            return null;
        }
    }

}
