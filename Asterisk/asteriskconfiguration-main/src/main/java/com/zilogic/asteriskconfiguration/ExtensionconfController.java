/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.zilogic.asteriskconfiguration;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import model.GroupedSpeakers;
import model.Speaker;
import util.ExtensionFileParser;
import util.FileWritterUtil;
import util.LoadFiles;
import util.PjSIPParser;

/**
 * FXML Controller class
 *
 * @author sridhar
 */
public class ExtensionconfController implements Initializable {

    @FXML
    private ListView speakerslist;

    @FXML
    private ListView groupedlist;

    static ObservableList<GroupedSpeakers> groupedSpeakersObserverList = FXCollections.observableArrayList();
    static HashSet<GroupedSpeakers> groupedHashSet = new HashSet<GroupedSpeakers>();

    static ObservableList<Speaker> speakersObservableList = FXCollections.observableArrayList();
    static HashSet<Speaker> speakersObservableHashSet = new HashSet<Speaker>();

    public FileChooser fileChooser;
    public File file = null;
    public static File pjsipFile = null;
    public static File extFile = null;
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {

        } catch (Exception ex) {
            Logger.getLogger(ExtensionconfController.class.getName()).log(Level.SEVERE, null, ex);
        }
        groupedSpeakersObserverList.addAll(groupedHashSet);
        groupedlist.getItems().clear();
        groupedlist.setItems(groupedSpeakersObserverList);

        speakersObservableList.addAll(speakersObservableHashSet);
        speakerslist.getItems().clear();
        speakersObservableList.clear();
        speakerslist.setItems(speakersObservableList);

        fileChooser = FileUtil.createFileChooser();
    }

    @FXML
    private void createGroup() throws IOException {
        System.out.println("Create Extension");
        App.setPopUpView("createext");
        App.showPopUP();
    }

    @FXML
    private void deleteGroup() throws IOException {
        GroupedSpeakers gp = (GroupedSpeakers) groupedlist.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(
                AlertType.CONFIRMATION, "Delete" + " " + gp.toString(),
                ButtonType.YES,
                ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            groupedSpeakersObserverList.remove(groupedlist.getSelectionModel().getSelectedItem());
            speakerslist.getItems().clear();

        }

    }
//Method is Not invoking 

    @FXML
    private void updateGroup() throws IOException {

        if (groupedlist.getSelectionModel().getSelectedItem() != null) {
            System.out.println("Succesfully entered into Update method ....");
            GroupedSpeakers extension = (GroupedSpeakers) groupedlist.getSelectionModel().getSelectedItem();

            App.setPopUpView("updateconf");

            UpdateconfController updateController = App.getLoader().getController();
            updateController.groupName.setText(extension.getGroupName());
            updateController.selectedSpeaker = extension;

            updateController.groupedSpeakerObservable.removeAll();
            updateController.groupedSpeakerObservable.clear();

            HashSet<Speaker> speakerset = new HashSet<Speaker>();
            speakerset.addAll(extension.getSpeakers());

            updateController.groupedSpeakerObservable.addAll(speakerset);
            App.showPopUP();

        }

    }

    @FXML
    private void saveConfig() throws IOException {
//        if (pjsipFile == null || (!(pjsipFile.exists()))) {
//            //nodeListView.getItems().clear();
//            pjsipFile = FileUtil.saveFileChooser(fileChooser, App.mainstage);
//        }
        File pjsipfile = LoadFiles.getPjsipFile();
        List<Speaker> speakers = PjSIPParser.getSpeakers(pjsipfile);//new File("/home/sridhar/config-asterisk/asterisk/pjsip.conf"));

        FileWritterUtil.writeExtension("extension.conf", groupedlist.getItems(), speakers);
        Alert alert = new Alert(AlertType.INFORMATION, "Exported to" + " " + "extension.conf");
        alert.setTitle("Exported to file");

        alert.showAndWait();

    }

    @FXML
    private void loadExtConfig() throws IOException {

//        if (pjsipFile == null || (!(pjsipFile.exists()))) {
//            //nodeListView.getItems().clear();
//            pjsipFile = FileUtil.openFileChooser(fileChooser, App.mainstage);
//        }
//        if (extFile == null || (!(extFile.exists()))) {
//            //nodeListView.getItems().clear();
//            extFile = FileUtil.openFileChooser(fileChooser, App.mainstage);
//        }
        pjsipFile = LoadFiles.getPjsipFile();
        extFile = LoadFiles.getExtFile();

        List<GroupedSpeakers> groupedSpeakers = ExtensionFileParser.parseExtensionFile(extFile, pjsipFile);
        System.out.println("=======================================");
        System.out.println(groupedSpeakers);
        for (GroupedSpeakers gp : groupedSpeakers) {
            System.out.println(gp.getSpeakers());

        }

        groupedSpeakersObserverList.clear();
        groupedSpeakersObserverList.addAll(groupedSpeakers);

    }

    public HashSet<GroupedSpeakers> getGroupedSpeakersList() {
        return groupedHashSet;
    }

    public void setGroupedSpeakersList(HashSet<GroupedSpeakers> groupedHashSet) {
        this.groupedHashSet = groupedHashSet;
    }

    @FXML
    private void groupSelected() throws IOException {

        GroupedSpeakers groupedSpeakers = (GroupedSpeakers) groupedlist.getSelectionModel().getSelectedItem();
        speakersObservableHashSet.clear();
        speakersObservableHashSet.addAll(groupedSpeakers.getSpeakers());
        System.out.println(groupedSpeakers.getSpeakers());
        speakersObservableList.clear();
        speakersObservableList.addAll(speakersObservableHashSet);

    }

}
