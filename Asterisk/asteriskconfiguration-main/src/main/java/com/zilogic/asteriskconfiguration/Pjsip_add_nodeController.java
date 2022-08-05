/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.asteriskconfiguration;

import static com.zilogic.asteriskconfiguration.Pjsip_node_configurationController.speakers;
import static com.zilogic.asteriskconfiguration.RegisterNodeController.ignoreDuplicateNodeFlag;

import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Speaker;
import org.json.simple.parser.JSONParser;

/**
 * FXML Controller class
 *
 * @author user
 */
public class Pjsip_add_nodeController implements Initializable {

    @FXML
    public TextField NodeNameTextField;
    @FXML
    public TextField DisplayNameTextField;
    @FXML
    public PasswordField NodePwdTextField;
    @FXML
    public ChoiceBox<String> ProtocolChoiceBox;
    @FXML
    public ChoiceBox<String> VoiceQualityChoiceBox;

    public Stage nodeStage = new Stage();

    public String nodeName;
    public String nodePassword;
    public String protocol;
    public String voiceQuality;
    public String displayName;

    public Speaker speaker = new Speaker();
    
   public static List<Speaker> speakerList = new ArrayList<>();

    public Pjsip_node_configurationController nodeConfigFile = new Pjsip_node_configurationController();

    public JSONObject nodeDetails = new JSONObject();
    
    public static int nodePosition;

    @FXML
    private Button OKButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Text errorText;
    @FXML
    private BorderPane bp;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<String> protocolList = new ArrayList<>();
        List<String> voiceQualityList = new ArrayList<>();

        protocolList.add("UDP");
        protocolList.add("TCP");

        voiceQualityList.add("High Quality");
        voiceQualityList.add("Medium");
        voiceQualityList.add("Low");

        ProtocolChoiceBox.getItems().addAll(protocolList);
        ProtocolChoiceBox.setValue("UDP");
        VoiceQualityChoiceBox.getItems().addAll(voiceQualityList);
        VoiceQualityChoiceBox.setValue("High Quality");
    }

    /**
     * This method is used to get input for node.
     *
     */
    public void getInput() {
        nodeName = getNodeName();
        displayName = getDisplayName();
        nodePassword = getNodePassword();
        protocol = getProtocol();
        voiceQuality = getVoiceQuality();
    }

    /**
     * This method is used to get Name of node.
     *
     * @return String - It is used to return the node Name.
     */
    public String getNodeName() {
        String nodeName = NodeNameTextField.getText();
        return nodeName;
    }

    /**
     * This method is used to get DisplayName of node.
     *
     * @return String - It is used to return the display Name of the node.
     */
    public String getDisplayName() {
        String displayName = DisplayNameTextField.getText();
        return displayName;
    }

    /**
     * This method is used to get Password of node.
     *
     * @return String - It is used to return the node password.
     */
    public String getNodePassword() {
        String nodePassword = NodePwdTextField.getText();
        return nodePassword;
    }

    /**
     * This method is used to get protocol of node.
     *
     * @return String - It is used to return the protocol of the node.
     */
    public String getProtocol() {
        String protocol = ProtocolChoiceBox.getValue();
        return protocol;
    }

    /**
     * This method is used to get VoiceQuality of node.
     *
     * @return String - It is used to return the voice Quality .
     */
    public String getVoiceQuality() {

        String voiceQuality = VoiceQualityChoiceBox.getValue();
        return voiceQuality;
    }

    /**
     * This method is used to validate the node name.
     *
     * @return Boolean - It is used to return whether node name is empty or not.
     */
    public Boolean validateNodeName(String nodeName) {
        if (nodeName.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * This method is used to submit the node entered based on node validation.
     *
     * @return event - The event is triggered when Ok/Cancel is pressed.
     */
    @FXML
    private void submitForm(ActionEvent event) {
        getInput();
        if (!(validateNodeName(nodeName))) {

            System.out.println("Node Name Cannot be Empty");
            NodeNameTextField.setPromptText("Cannot be Empty");
            NodeNameTextField.setStyle("-fx-border-color: red;");

        } else {
            try {

                if (writeToFile() == false) {
                    System.out.println("same node Name exists");
                    NodeNameTextField.setPromptText("");
                    NodeNameTextField.setStyle("-fx-border-color: red;");
                    errorText.setText("Same node Exists");
                } else {
                    //Pjsip_node_configurationController.stage.close();
                    RegisterNodeController.registerNodeOperationStatus = true;
                    ignoreDuplicateNodeFlag = false;
                    App.addNodeStage.close();
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    /**
     * This method is used to add the node to the listView.
     *
     * @return Boolean - It returns false when same node exist in the listView
     * returns true when node is successfully added to the list View.
     */
    public Boolean writeToFile() throws Exception {
        nodeDetails = new JSONObject();
        if (checkNodeExists() && (ignoreDuplicateNodeFlag == false)) {
            NodeNameTextField.clear();
            NodePwdTextField.clear();
            return false;
        } else if (ignoreDuplicateNodeFlag == true) {
            speaker.setDisplayName(displayName);
            speaker.setUserName(nodeName);
            speaker.setPassword(nodePassword);
            speaker.setProtocol(protocol);
            speaker.setVoiceQuality(voiceQuality);
            speaker.setCodecAllow(voiceQuality);
            

            if (speakers.contains(speaker)) {
                nodePosition = speakers.indexOf(speaker);
                speakers.set(nodePosition, speaker);
            } else {
                speakers.add(speaker);
                nodePosition = speakers.size() - 1;
            }
            return true;
        } else {

            //speaker.setCodecAllow(speaker.getCodecAllow());

            speaker.setDisplayName(displayName);
            speaker.setUserName(nodeName);
            speaker.setPassword(nodePassword);
            speaker.setProtocol(protocol);
            speaker.setVoiceQuality(voiceQuality);
            speaker.setCodecAllow(voiceQuality);

            speakers.add(speaker);

            return true;
        }
    }

    /**
     * This method is used to check whether node exists in the list View.
     *
     * @return Boolean - Return true if same node exists. Return false if node
     * doesn't exist in the list.
     */
    public Boolean checkNodeExists() {
        int i = 0;

        for (i = 0; i < speakers.size(); i++) {

            if (speakers.get(i).getUserName().contentEquals(nodeName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method is used to cancel the addition of the node.
     *
     * @return event - event gets triggered when cancel button is pressed.
     */
    @FXML
    private void cancelForm(ActionEvent event) {
        //Pjsip_node_configurationController.stage.close();
        RegisterNodeController.registerNodeOperationStatus = false;
        App.addNodeStage.close();
    }
}
