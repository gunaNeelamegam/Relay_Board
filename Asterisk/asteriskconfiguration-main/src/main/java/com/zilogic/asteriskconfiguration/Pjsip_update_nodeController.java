/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.asteriskconfiguration;

import static com.zilogic.asteriskconfiguration.Pjsip_node_configurationController.editNodeDetailsList;

import static com.zilogic.asteriskconfiguration.Pjsip_node_configurationController.speakers;
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
import javafx.stage.Stage;
import model.Speaker;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * FXML Controller class
 *
 * @author user
 */
public class Pjsip_update_nodeController implements Initializable {

    @FXML
    private TextField NodeNameTextField;
    @FXML
    public TextField DisplayNameTextField;
    @FXML
    private PasswordField NodePwdTextField;
    @FXML
    private ChoiceBox<String> ProtocolChoiceBox;
    @FXML
    private ChoiceBox<String> VoiceQualityChoiceBox;

    public Stage nodeStage = new Stage();

    public String nodeName;
    public String nodePassword;
    public String protocol;
    public String voiceQuality;
    public String displayName;

    public Pjsip_node_configurationController nodeConfigFile = new Pjsip_node_configurationController();

    public Speaker speaker = new Speaker();
    public JSONObject nodeDetails = new JSONObject();

    @FXML
    private Button OKButton;
    @FXML
    private Button cancelButton;

    public String tempName;
    public String tempdisplayName;
    public String temppassword;
    public String tempVoiceQuality;
    public String tempProtocol;
    @FXML
    private Text errorText;
    @FXML
    private BorderPane bp;
    @FXML
    private AnchorPane anchorPane;

    /**
     * This method is used to get entries of the node.
     *
     */
    public void getItems() {

        List<String> protocolList = new ArrayList<>();
        List<String> voiceQualityList = new ArrayList<>();

        protocolList.add("UDP");
        protocolList.add("TCP");

        voiceQualityList.add("High Quality");
        voiceQualityList.add("Medium");
        voiceQualityList.add("Low");
        ProtocolChoiceBox.getItems().addAll(protocolList);
        VoiceQualityChoiceBox.getItems().addAll(voiceQualityList);

        JSONObject obj = (JSONObject) Pjsip_node_configurationController.editNodeDetailsList.get(0);

        NodeNameTextField.setText(obj.get("Node Name").toString());
        DisplayNameTextField.setText(obj.get("Display Name").toString());
        NodePwdTextField.setText(obj.get("Password").toString());

        ProtocolChoiceBox.setValue(obj.get("Protocol").toString());
        VoiceQualityChoiceBox.setValue(obj.get("Voice Quality").toString());

        nodeName = getNodeName();
        displayName = getDisplayName();
        nodePassword = getNodePassword();
        protocol = getProtocol();
        voiceQuality = getVoiceQuality();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getItems();
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
    private void submitForm(ActionEvent event) throws Exception {

        if (!(validateNodeName(NodeNameTextField.getText()))) {

            System.out.println("Node Name Cannot be Empty");
            NodeNameTextField.setPromptText("Cannot be Empty");
            NodeNameTextField.setStyle("-fx-border-color: red;");
        } else {

            if (writeToFile() == false) {
                nodeName = tempName;
                NodeNameTextField.setText(tempName);
                DisplayNameTextField.setText(tempdisplayName);
                NodePwdTextField.setText(temppassword);
                List<String> protocolList = new ArrayList<>();
                List<String> voiceQualityList = new ArrayList<>();

                protocolList.add("UDP");
                protocolList.add("TCP");

                voiceQualityList.add("High Quality");
                voiceQualityList.add("Medium");
                voiceQualityList.add("Low");
                ProtocolChoiceBox.getItems().clear();
                VoiceQualityChoiceBox.getItems().clear();
                ProtocolChoiceBox.getItems().addAll(protocolList);
                VoiceQualityChoiceBox.getItems().addAll(voiceQualityList);

                ProtocolChoiceBox.setValue(tempProtocol);
                VoiceQualityChoiceBox.setValue(tempVoiceQuality);
                System.out.println("same node exists");
                NodeNameTextField.setStyle("-fx-border-color: red;");
                NodeNameTextField.setPromptText("");
                errorText.setText("Same node Exists");
            } else {

                //Pjsip_node_configurationController.stage.close();
                App.addNodeStage.close();
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

        if (checkNodeExists()) {
            return false;
        } else {

            int i = 0;

            speaker.setDisplayName(displayName);
            speaker.setUserName(nodeName);
            speaker.setPassword(nodePassword);
            speaker.setProtocol(protocol);
            speaker.setVoiceQuality(voiceQuality);
            speaker.setCodecAllow(voiceQuality);

            for (i = 0; i < speakers.size(); i++) {
                JSONObject overwrittenobj = (JSONObject) Pjsip_node_configurationController.overwrittenList.get(0);
                if (speakers.get(i).getUserName().contentEquals(overwrittenobj.get("Node Name").toString())) {
                    speakers.set(i, speaker);
                    break;
                }
            }
            return true;
        }
    }

    /**
     * This method is used to check whether node name is already present in the
     * list View.
     *
     * @return Boolean - Return true if same node exists. Return false if node
     * doesn't exist in the list.
     */
    public Boolean checkNodeInList() {
        int i = 0;
        getInput();

        for (i = 0; i < speakers.size(); i++) {
            if (speakers.get(i).getUserName().contentEquals(nodeName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method is used to check whether node exists in the list View.
     *
     * @return Boolean - Return true if same node exists. Return false if node
     * doesn't exist in the list.
     */
    public Boolean checkNodeExists() {
        int i = 0;
        int check_flag = 0;
        tempName = nodeName;
        tempdisplayName = displayName;
        temppassword = nodePassword;
        tempVoiceQuality = voiceQuality;
        tempProtocol = protocol;

        for (i = 0; i < speakers.size(); i++) {
            getInput();
            if (!(tempName.contentEquals(nodeName))) {
                check_flag = 3;
                continue;
            } else if (check_flag == 2) {
                return true;
            } else if (check_flag == 0) {
                return false;
            } else if (check_flag == 3) {
                return true;
            }
        }

        if (check_flag == 2 || check_flag == 3) {
            if (checkNodeInList()) {
                return true;
            } else {
                return false;
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
        App.addNodeStage.close();
    }
}
