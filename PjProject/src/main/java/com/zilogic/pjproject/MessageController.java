package com.zilogic.pjproject;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class MessageController implements Initializable {

    @FXML
    private JFXButton search;

    @FXML
    private JFXButton userAccount1;

    @FXML
    private Label userName;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadDefaultInfo();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    static int i = 0;

    private void loadDefaultInfo() throws Exception {
        
        
        List<JFXButton> userAccount = new ArrayList();
        userAccount.add(userAccount1);        
        AddBuddyController adcon = new AddBuddyController();        
        adcon.buddyDetails.forEach((buddyuser) -> {
            userAccount.get(i).setText(buddyuser.getBuddyUserName());
            i += 1;
        });
        // userName.setStyle("-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #00FF00, #FFFFFF)");
    }

}
