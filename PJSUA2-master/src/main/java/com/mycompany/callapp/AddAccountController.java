package com.mycompany.callapp;

import com.jfoenix.controls.JFXListView;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AddAccountController {
    String domainAddress;
    String username;
    String password;

    @FXML
    Button closebtn;
    @FXML
    Button saveBtn;
    @FXML
    TextField domain;
    @FXML
    TextField user;
    @FXML
    PasswordField pass;

    public static ArrayList<AddAccount> userdetails = new ArrayList<AddAccount>();
    FXMLController fxmlcont = new FXMLController();

    @FXML

    public void save(ActionEvent event) throws IOException, Exception {
        AddAccount addaccount = new AddAccount();
        domainAddress = domain.getText();
        username = user.getText();
        password = pass.getText();
        addaccount.setUsername(username);
        addaccount.setDomainAddress(domainAddress);
        addaccount.setPassword(password);
        userdetails.add(addaccount);
        fxmlcont.createAccount();
        fxmlcont.DefaultAccountColor();
        FXMLController.addAccountStage.close();
    }

    @FXML
    public void close() {
        FXMLController.addAccountStage.close();
        System.out.println(" add Account close SuccessFully");
    }
}
