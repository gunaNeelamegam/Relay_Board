package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import static com.zilogic.pjproject.MyAccount.INCOMINGCALL;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.AuthCredInfo;

public class MainStageController implements Initializable {

//Account user authication
    AuthCredInfo auth;
    boolean exitIncomingCall = false;
    //MyApp has all utility function are created...
    private static MyApp app = new MyApp();
    //which is used to show all the account in the list
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
    public static BorderPane borderPane;
    static Stage incoming_stage = null;
    static int i = 0;

    @FXML
    JFXButton incomingCallBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            app.init((MyAppObserver) observer, ".", true);
            loadingIncomingUI();
        } catch (Exception ex) {
            System.err.println(" Excpetion while loading the runWorker");
        }
    }

    @FXML
    public void loadIncomingUI() {
        try {
            incoming_stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("ic.fxml"));
            Scene scene = new Scene(root);
            incoming_stage.setScene(scene);
            incoming_stage.setTitle("Incomingcall");
            incoming_stage.showAndWait();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    Task<Void> task;

    @FXML
    public void loadingIncomingUI() {

        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    Thread.sleep(100);
                    try {
                        System.out.println("INCOMING CALL");
                        if (INCOMINGCALL == 1) {
                            INCOMINGCALL++;
                            System.out.println("INCOMING CALL fired");
                            Platform.runLater(() -> {
                                loadIncomingUI();
                            });
                            task.cancel();
                        }
                    } catch (Exception e) {
                        System.out.println("Exception while loading the incoming call ");
                        System.out.println(e.getMessage());
                    }
                }
                //   return null;
            }

        };
        new Thread(task).start();
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

        add_buddy_stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("AddBuddy.fxml"));
        Scene scene = new Scene(root);
        add_buddy_stage.setScene(scene);
        add_buddy_stage.setTitle("ADD BUDDY");
        add_buddy_stage.showAndWait();
    }

    @FXML
    public void nodeConfig() throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("NodeConfig.fxml")));
        addAccountStage.setScene(scene);
        addAccountStage.setTitle("NODE CONFIG");
        addAccountStage.show();
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
            // listView.getItems().add(acc.getInfo().getUri());
            acc.setDefault();
            acc.getInfo().setRegExpiresSec(500000L);
            System.out.println(" Registeration Status Expiry time : " + acc.getInfo().getRegExpiresSec());
            System.out.println(" Account default : " + acc.isDefault() + "    ");
            System.out.println(acc.isValid());
            acc.getInfo().setOnlineStatus(true);
            System.out.println(accinfo.getOnlineStatus() + "Account status  text :" + accinfo.getOnlineStatusText());

        }
        if (account != null) {
            return account;
        } else {
            return null;
        }
    }
}
