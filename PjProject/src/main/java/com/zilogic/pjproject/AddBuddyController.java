package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

class AddBuddyController {

    @FXML
    private TextField accountPass;

    @FXML
    private TextField buddyuserName;

    @FXML
    private JFXButton closeBtn;

    @FXML
    private JFXButton saveBtn;

    protected ArrayList<AddBuddy> buddyList = new ArrayList<AddBuddy>();

    @FXML
    void save() {
        System.out.println("Save button is Clicked");
        var addbuddy = new AddBuddy();
        addbuddy.setAccountPass(accountPass.getText());
        addbuddy.setBuddyUserName(buddyuserName.getText());
        buddyList.add(addbuddy);
        MainStageController.add_buddy_stage.close();
        System.out.println(" Succesfully Added");
    }

    @FXML
    void close() {
        System.out.println(" Close button is clicked");
        MainStageController.add_buddy_stage.close();
    }

}
