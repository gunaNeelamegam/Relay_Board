/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import static com.zilogic.pjproject.AddAccountController.exitRegThread;
import static com.zilogic.pjproject.MyApp.accList;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 *
 * @author user
 */
public class AddBuddyController {

    Thread createBuddy;
    static boolean exitCreateBuddy = false;
    @FXML
    private TextField buddyName;

    @FXML
    private JFXButton closeBtn;

    @FXML
    private JFXButton saveBtn;

    public ArrayList<AddBuddy> buddyDetails = new ArrayList<AddBuddy>();
    MyBuddy mybud = null;

    @FXML
    void close(ActionEvent event) {
        MainStageController.add_buddy_stage.close();
    }

    @FXML
    void save() {
        MainStageController.add_buddy_stage.close();
    }
}
// @FXML
/* synchronized void save(ActionEvent event) throws Exception {

        createBuddy = new Thread(() -> {
            Runnable updater = () -> {
                System.out.println("creating the Buddy Account ");
                BuddyConfig bdy = new BuddyConfig();
                try {
                    MyAccount acc = MyApp.accList.get(0);
                    bdy.setUri("sip:" + buddyName.getText().trim() + "@" + acc.getInfo().getUri().substring(9));
                    bdy.setSubscribe(true);
                    AddBuddy buddy = new AddBuddy(buddyName.getText());
                    buddyDetails.add(buddy);
                    mybud = acc.addBuddy(bdy);
                    MyApp.ep.libHandleEvents(10L);
                    System.out.println(" Buddy status : " + bdy.getUri());
                    var app = new MyApp();
                    if (mybud.getId() != -1) {
                        exitCreateBuddy = true;
                        app.saveConfig("pjsua2.json");
                        MainStageController.add_buddy_stage.close();
                    }
                } catch (Exception e) {
                    System.err.println(" BUDDY CREATION FAILED");
                }
            };
            while (!exitCreateBuddy) {
                try {
                    Thread.sleep(100);
                    Platform.runLater(updater);
                } catch (Exception e) {
                }
            }
        });
        createBuddy.start();
        System.out.println(" Create buddy is Alive : " + createBuddy.isAlive());
    }*/
