/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.asteriskconfiguration;

import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.AriFactory;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.models.AsteriskInfo;
import ch.loway.oss.ari4java.generated.models.Channel;
import ch.loway.oss.ari4java.generated.models.Endpoint;
import static com.zilogic.asteriskconfiguration.FileUtil.writeToFile;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import model.Speaker;
import org.ini4j.Wini;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.LoadFiles;

/**
 * FXML Controller class
 *
 * @author user
 */
public class NodeConfigController implements Initializable {

    @FXML
    private BorderPane bp;
    @FXML
    private Text hostText;
    @FXML
    private TextField hostTextField;
    @FXML
    private ListView listView;
    @FXML
    private Button configBtn;

    public FadeTransition ft;

    public static List<Speaker> speakers = new ArrayList<>();

    public static Speaker speaker = new Speaker();

    public FileChooser fileChooser;

    /**
     * Initializes the controller class.
     */
    Thread statusThread;
    public ARI ari;
    
    List<String> list1 = new ArrayList<>();
    List<String> list2 = new ArrayList<>();

    public static String remoteHost = "";
    public static String nodeuserName = "zilogic";
    public static String nodePassword = "zilogic";

    public NodeConfigController() {
        //run_statusThread();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        System.out.println("class Name :  " + this.getClass().toString());
        try {
           
            listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            fileChooser = FileUtil.createFileChooser();
            loadFile();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Boolean writeNodeConfigFile(String filePath, List<Speaker> speakers) throws Exception {
        File file = null;
        Boolean fileCreationStatus;

        //file = FileUtil.createFile(file, "nodeDetails.conf");
        file = FileUtil.createFile(file, filePath);

        FileOutputStream fooStream = new FileOutputStream(file, false);

        fooStream.write("".getBytes());
        fooStream.close();
        fileCreationStatus = FileUtil.validateFileCreation(file);
        if (!fileCreationStatus) {
            return true;
        }

        FileWriter fw = new FileWriter(file);

        Wini ini = null;

        ini = FileUtil.createIniFile(ini, file);
        ini.getConfig().setMultiSection(true);
        int i = 0;

        for (i = 0; i < speakers.size(); i++) {

            fw = writeToFile(fw, "username" + "=" + speakers.get(i).getUserName() + "\n");
            fw = writeToFile(fw, "password" + "=" + speakers.get(i).getPassword() + "\n");
            fw = writeToFile(fw, "serverIP" + "=" + hostTextField.getText() + "\n");

            if (speakers.get(i).getCodecAllow().contentEquals("speex32")) {

                fw = writeToFile(fw, "wb" + "=" + 0 + "\n");
                fw = writeToFile(fw, "uwb" + "=" + 0 + "\n");
                fw = writeToFile(fw, "xuwb" + "=" + 3 + "\n");
            } else if (speakers.get(i).getCodecAllow().contentEquals("speex16")) {

                fw = writeToFile(fw, "wb" + "=" + 0 + "\n");
                fw = writeToFile(fw, "uwb" + "=" + 3 + "\n");
                fw = writeToFile(fw, "xuwb" + "=" + 0 + "\n");
            } else if (speakers.get(i).getCodecAllow().contentEquals("speex")) {

                fw = writeToFile(fw, "wb" + "=" + 3 + "\n");
                fw = writeToFile(fw, "uwb" + "=" + 0 + "\n");
                fw = writeToFile(fw, "xuwb" + "=" + 0 + "\n");
            }

        }
        fw.close();
        return false;
    }

    @FXML
    private void configNode(ActionEvent event) throws Exception {

        remoteHost = hostTextField.getText();

        if (!(remoteHost.isEmpty())) {
            Speaker sp = (Speaker) listView.getSelectionModel().getSelectedItem();

            List<Speaker> selectedNodeList = new ArrayList<>();
            selectedNodeList.add(sp);
            //FileUtil.writeToConfigFile("/home/user/node.txt", selectedNodeList);
            writeNodeConfigFile("node.ini", selectedNodeList);

            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession(nodeuserName, "162.168.100.2");
                session.setPassword(nodePassword);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();

                ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
                sftpChannel.connect();
                sftpChannel.put("node.ini", "/home/zilogic/.");

                ButtonType OK = new ButtonType("Ok");

                Label lbl = new Label();
                lbl.setText("Node Configured Successfully");

                Alert a = new Alert(Alert.AlertType.INFORMATION, "", OK);
                Optional<ButtonType> result;

                a.getDialogPane().setContent(lbl);
                result = a.showAndWait();

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void updateListView() {
        int i = 0;
        listView.getItems().clear();

        for (i = 0; i < speakers.size(); i++) {
            listView.getItems().add(speakers.get(i));
        }
    }

    public void displayFileNotFound() {
        ButtonType OK = new ButtonType("Ok");

        Label lbl = new Label();
        lbl.setText("File Not Found");

        Alert a = new Alert(Alert.AlertType.WARNING, "", OK);
        Optional<ButtonType> result;

        a.getDialogPane().setContent(lbl);
        result = a.showAndWait();
    }

    public void displayFileLoadSuccessfully(int fileContentSize) {
        ButtonType OK = new ButtonType("Ok");

        Label lbl = new Label();
        if (fileContentSize != 0) {
            lbl.setText("File Loaded Successfully");
        } else {
            lbl.setText("Empty file Loaded");
        }
        Alert a = new Alert(Alert.AlertType.INFORMATION, "", OK);
        Optional<ButtonType> result;

        a.getDialogPane().setContent(lbl);
        result = a.showAndWait();

    }

    public void loadFile() throws Exception {

        speakers.clear();
        File pjsipfile = LoadFiles.getPjsipFile();
        listView.getItems().clear();

        if (FileUtil.readconfigFile(pjsipfile.getAbsolutePath(), speakers)) {
            displayFileNotFound();
        } else {
            //displayFileLoadSuccessfully(speakers.size());
            updateListView();
        }
    }
}
