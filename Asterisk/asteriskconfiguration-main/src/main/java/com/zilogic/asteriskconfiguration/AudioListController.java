/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.asteriskconfiguration;

import static com.zilogic.asteriskconfiguration.FileUtil.validateFileCreation;
import static com.zilogic.asteriskconfiguration.DialerGridUIController.caller;
import static com.zilogic.asteriskconfiguration.RegisterUserController.wobj;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.ini4j.Wini;
import webphone.webphone;

/**
 * FXML Controller class
 *
 * @author user
 */
public class AudioListController implements Initializable {

    @FXML
    private Text songTitle;
    @FXML
    private Text speakTitle;
    @FXML
    private ListView SongList;
    @FXML
    private ListView speakerList;
    @FXML
    private Button stopBtn;
    @FXML
    private Button playBtn;
    @FXML
    private BorderPane audiobp;

    @FXML
    private Button volumedownBtn;
    @FXML
    private Button volumeupBtn;

    String soundtrack_path;

    int volume = 100;

    /**
     * This method is used to search for .wav files.
     *
     * @return File[] The file array is returned containing the .wav files
     */
    public File[] searchFile() {
        File f = new File("src/main/resources");
        File[] matchingFiles = f.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith("wav");
            }
        });
        return matchingFiles;
    }

    /**
     * This method is used to display the available speakers.
     *
     */
    public void displaySpeakers() {
        speakerList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        SongList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        speakerList.getItems().addAll("6001", "6002", "6003");
    }

    /**
     * This method is used to search and retrieve the .wav files and display the
     * speakers available.
     *
     * @param url The url is passed as parameter
     *
     * @param rb The rb is passed as parameter
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        File[] file = searchFile();
        for (int i = 0; i < file.length; i++) {
            SongList.getItems().add(file[i].getName());
            soundtrack_path = file[i].getAbsolutePath().substring(0, file[i].getAbsolutePath().lastIndexOf('/')) + "/";
        }
        displaySpeakers();
        volumedownBtn.setDisable(true);
        volumeupBtn.setDisable(true);
        stopBtn.setDisable(true);

    }

    /**
     * This method is used to play the .wav files The song will play when
     * speaker and song from the listview are selected by clicking the play
     * button.
     *
     * @param event The event is triggered when the play button is pressed.
     *
     */
    @FXML
    private void playSong(ActionEvent event) {
        try {

            ObservableList<String> selectedSpeakers = speakerList.getSelectionModel().getSelectedItems();
            ObservableList<String> selectedTracks = SongList.getSelectionModel().getSelectedItems();

            File file = null;
            Boolean fileCreationStatus;

            file = FileUtil.createFile(file, "userdetails.ini");
            fileCreationStatus = validateFileCreation(file);

            Wini ini = null;
            ini = FileUtil.createIniFile(ini, file);
            String username = FileUtil.readFromIniFile(ini, "userLogin", "username");

            String password = FileUtil.readFromIniFile(ini, "userLogin", "password");
            String domainName = FileUtil.readFromIniFile(ini, "userLogin", "serveraddress");

            System.out.println("selected Speakers" + selectedSpeakers);
            wobj.API_SetParameter("serveraddress", domainName);
            wobj.API_SetParameter("username", username);
            wobj.API_SetParameter("password", password);
            wobj.API_SetParameter("loglevel", "1");
            wobj.API_Call(-1, selectedSpeakers.get(0));
            wobj.API_SetVolume(0, volume);
            wobj.API_SetVolume(1, volume);
            wobj.API_SetVolume(2, volume);

            wobj.SetCodec("ulaw");

            wobj.API_PlaySound(1, soundtrack_path + selectedTracks.get(0), 0, false, false, true, -1, "", false);
            playBtn.setDisable(true);
            stopBtn.setDisable(false);
            volumedownBtn.setDisable(false);
            volumeupBtn.setDisable(false);

        } catch (Exception e) {
            System.out.println("Caller Node has not been Registered");
        }
    }

    /**
     * This method is used to stop the song playing in the speaker.
     *
     * @param event The event is triggered when stop button is pressed.
     *
     */
    @FXML
    private void stopSong(ActionEvent event) {
        volumedownBtn.setDisable(true);
        volumeupBtn.setDisable(true);
        playBtn.setDisable(false);
        stopBtn.setDisable(true);
        wobj.API_Hangup(-1);
    }

    /**
     * This method is used to decrease the speaker's volume level.
     *
     * @param event The event is triggered when volume down button is pressed.
     *
     */
    @FXML
    private void decreaseVolume(ActionEvent event) {
        volumeupBtn.setDisable(false);
        if ((volume - 20) < 0) {
            volumedownBtn.setDisable(true);
        } else {
            volume = volume - 20;
            volumedownBtn.setDisable(false);
        }
        wobj.API_SetVolume(0, volume);
        wobj.API_SetVolume(1, volume);
        wobj.API_SetVolume(2, volume);
    }

    /**
     * This method is used to increase the speaker's volume level.
     *
     * @param event The event is triggered when volume up button is pressed.
     *
     */
    @FXML
    private void increaseVolume(ActionEvent event) {
        volumedownBtn.setDisable(false);
        if ((volume + 20) > 100) {
            volumeupBtn.setDisable(true);
        } else {
            volume = volume + 20;
            volumeupBtn.setDisable(false);
        }
        wobj.API_SetVolume(0, volume);
        wobj.API_SetVolume(1, volume);
        wobj.API_SetVolume(2, volume);
    }
}
