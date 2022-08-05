/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.zilogic.asteriskconfiguration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.GroupedSpeakers;
import model.Speaker;
import util.LoadFiles;
import util.PjSIPParser;
import static com.zilogic.asteriskconfiguration.ExtensionconfController.groupedSpeakersObserverList;

/**
 * FXML Controller class
 *
 * @author sridhar
 */
public class CreatefxmlController implements Initializable {

    @FXML
    private ListView speakers;

    @FXML
    private ListView groups;

    @FXML
    private TextField groupName;

    static List<Speaker> speakerList;
    
    //static File pjsipfile;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //speakerList = PjSIPParser.getSpeakers(ExtensionconfController.pjsipFile);//new File("/home/sridhar/config-asterisk/asterisk/pjsip.conf"));
            //pjsipfile = FileUtil.openFileChooser(FileUtil.createFileChooser(), App.mainstage);
            File pjsipfile = LoadFiles.getPjsipFile();            
            speakerList = PjSIPParser.getSpeakers(pjsipfile);
            speakers.getItems().clear();
            speakers.getItems().addAll(speakerList);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void cancel() throws IOException {
        App.closePopUP();
    }

    @FXML
    private void submit() throws IOException {

        GroupedSpeakers group = new GroupedSpeakers();
        group.setSpeakers(groups.getItems());

        group.setGroupName(groupName.getText());

        if (groups.getItems().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("zero speakers to group");
            alert.showAndWait();

        }
        if (groupName.getText().trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Group Name is empty");
            alert.showAndWait();

        }
        if (!ExtensionconfController.groupedSpeakersObserverList.contains(group)
                && !(groupName.getText().trim().equals(""))
                && !(groups.getItems().size() == 0)) {
            ExtensionconfController.groupedSpeakersObserverList.add(group);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Same group exist" + " " + group.getGroupName());
            alert.showAndWait();
        }
        App.closePopUP();

    }

    @FXML
    private void leftDrag() throws IOException {
        Speaker selected = (Speaker) speakers.getSelectionModel().getSelectedItem();
        if (!groups.getItems().contains(selected)) {
            groups.getItems().add(selected);
        }
    }

    @FXML
    private void rightDrag() throws IOException {

        Speaker selected = (Speaker) groups.getSelectionModel().getSelectedItem();
        groups.getItems().remove(selected);
    }

}
