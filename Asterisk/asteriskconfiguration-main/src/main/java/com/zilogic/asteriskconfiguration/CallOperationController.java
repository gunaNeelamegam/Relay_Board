/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.asteriskconfiguration;

import static com.zilogic.asteriskconfiguration.RegisterUserController.wobj;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Speaker;
import org.ini4j.Wini;
import util.ExtensionFileParser;
import util.LoadFiles;
import webphone.webphone;

/**
 * FXML Controller class
 *
 * @author user
 */
public class CallOperationController implements Initializable {

    @FXML
    private TextField searchTextField;
    @FXML
    private ListView speakerList;
    @FXML
    private Button callBtn;

    public FileChooser fileChooser;
    public File extensionFile = null;
    public static List<Speaker> speakers = new ArrayList<>();

    public static Stage outgoingStage;
    public static Speaker sp = new Speaker();
    @FXML
    private RadioButton singleNodeRadioBtn;
    @FXML
    private RadioButton MultipleNodeRadioBtn;

    public Map<String, List<String>> map;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            speakers.clear();
            speakerList.getItems().clear();
            fileChooser = FileUtil.createFileChooser();
            loadFile();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
//
//    public void updateListView() {
//        int i = 0;
//        speakerList.getItems().clear();
//
//        for (i = 0; i < speakers.size(); i++) {
//            speakerList.getItems().add(speakers.get(i));
//        }
//
//    }

    public void loadFile() throws Exception {

        try {
//            extensionFile = LoadFiles.getPjsipFile();
//
//            speakerList.getItems().clear();
//            FileUtil.readconfigFile(extensionFile.getAbsolutePath(), speakers);
//            updateListView();

            extensionFile = LoadFiles.getExtFile();
            map = ExtensionFileParser.getDialList(extensionFile);

            System.out.println("-------------------");
            System.out.println(map.get("Single"));

            System.out.println("-------------------");
            System.out.println(map.get("Multiple"));
            System.out.println("-------------------");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void performCallOperation(ActionEvent event) throws Exception {

        System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqq" + wobj);
        System.out.println("asaad" + speakerList.getSelectionModel().getSelectedItem());
        if (wobj != null) {

            System.out.println("adasd" + speakerList.getSelectionModel().getSelectedItem());
            
            File file = null;
            Boolean fileCreationStatus;

            file = FileUtil.createFile(file, "userdetails.ini");
            fileCreationStatus = FileUtil.validateFileCreation(file);
            //System.out.println("sssssss" + sp.getUserName());
            if (fileCreationStatus) {
                Wini ini = null;
                ini = FileUtil.createIniFile(ini, file);
                String username = FileUtil.readFromIniFile(ini, "userLogin", "username");
                String password = FileUtil.readFromIniFile(ini, "userLogin", "password");
                String domainName = FileUtil.readFromIniFile(ini, "userLogin", "serveraddress");
                wobj.API_SetParameter("serveraddress", domainName);
                wobj.API_SetParameter("username", username);
                wobj.API_SetParameter("password", password);
                wobj.API_SetParameter("loglevel", "1");
                
                //System.out.println("sssssss" + sp.getUserName() + username);
                wobj.API_Call(-1, speakerList.getSelectionModel().getSelectedItem().toString());
                outgoingStage = new Stage();
                outgoingStage.initOwner(App.mainstage);
                outgoingStage.initModality(Modality.WINDOW_MODAL);
                FXMLLoader fxmlLoader = new FXMLLoader();
                Pane root = (FXMLLoader.load(getClass().getResource("OutGoingCallMenu.fxml")));
                outgoingStage.setScene(new Scene(root, 400, 700));
                outgoingStage.setTitle("OutGoing Call Menu");
                outgoingStage.show();
            }
        }
    }

    @FXML
    private void displaySingleNode(ActionEvent event) {

        MultipleNodeRadioBtn.setSelected(false);
        singleNodeRadioBtn.setSelected(true);
        speakerList.getItems().clear();
        speakerList.getItems().addAll(map.get("Single"));
    }

    @FXML
    private void displayMultipleNode(ActionEvent event) {
        singleNodeRadioBtn.setSelected(false);
        MultipleNodeRadioBtn.setSelected(true);
        speakerList.getItems().clear();
        speakerList.getItems().addAll(map.get("Multiple"));
    }
}
