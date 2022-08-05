/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.zilogic.asteriskconfiguration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.GroupedSpeakers;
import model.Speaker;
import util.PjSIPParser;

/**
 * FXML Controller class
 *
 * @author sridhar
 */
public class UpdateconfController implements Initializable {

    /**
     * Initializes the controller class.
     *
     */
    @FXML
    ListView speakerlist;

    @FXML
    TextField groupName;

    static List<Speaker> speakerList = new ArrayList<Speaker>();

    public static GroupedSpeakers groupedSpeaker = new GroupedSpeakers();
    public static GroupedSpeakers selectedSpeaker;

    @FXML
    ListView grouplist;

    public static ObservableList<Speaker> groupedSpeakerObservable = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        speakerlist.getItems().addAll(CreatefxmlController.speakerList);

        grouplist.setItems(groupedSpeakerObservable);

//            grouplist.getItems().clear();
//            grouplist.setItems(groupedSpeakerObservable);
//            speakerList = PjSIPParser.getSpeakers(new File("/home/sridhar/config-asterisk/asterisk/pjsip.conf"));
//            speakerlist.getItems().clear();
//            speakerlist.getItems().addAll(speakerList);
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
        // TODO
    }

    @FXML
    private void leftDrag() {
        Speaker selected = (Speaker) speakerlist.getSelectionModel().getSelectedItem();
        if (!grouplist.getItems().contains(selected)) {
            groupedSpeakerObservable.add(selected);
        }
    }

    @FXML
    private void rightDrag() {
        Speaker selected = (Speaker) grouplist.getSelectionModel().getSelectedItem();
        groupedSpeakerObservable.remove(selected);
    }

    @FXML
    private void submit() {

        groupedSpeaker.setGroupName(groupName.getText());

        groupedSpeaker.getSpeakers().addAll(grouplist.getItems());

        // To add the new groupedSpeaker
        ExtensionconfController.groupedSpeakersObserverList.remove(selectedSpeaker);
        ExtensionconfController.groupedSpeakersObserverList.add(groupedSpeaker);

        App.closePopUP();
    }

    @FXML
    private void cancel() {
        App.closePopUP();
    }

}
