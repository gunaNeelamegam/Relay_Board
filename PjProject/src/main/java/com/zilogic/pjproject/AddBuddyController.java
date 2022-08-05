
package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.pjsip.pjsua2.BuddyConfig;

/**
 *
 * @author Guna
 */
public class AddBuddyController {

    BuddyConfig bdy = new BuddyConfig();
    @FXML
    private TextField buddyName;

    @FXML
    private JFXButton closeBtn;

    @FXML
    private JFXButton saveBtn;

     MyApp app=new MyApp();
    
    public ArrayList<AddBuddy> buddyDetails = new ArrayList<AddBuddy>();
    MyBuddy mybud = null;

    @FXML
    void close(ActionEvent event) {
        MainStageController.add_buddy_stage.close();
    }

    @FXML
    void save() {
        Platform.runLater(() -> {
            try {
                add_Buddy(buddyDetails);
            } catch (Exception ex) {
            }
        });

        MainStageController.add_buddy_stage.close();
    }

    private void add_Buddy(ArrayList<AddBuddy> buddyDetails) throws Exception {
        MyAccount acc = MyApp.accList.get(0);
        bdy.setUri("sip:" + buddyName.getText().trim() + "@" + acc.getInfo().getUri().substring(9));
        bdy.setSubscribe(true);
        AddBuddy buddy = new AddBuddy(buddyName.getText());
        buddyDetails.add(buddy);
        mybud = acc.addBuddy(bdy);
        System.out.println("Buddy added Successfully");
        app.saveConfig("pjsua2.json");
        
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
