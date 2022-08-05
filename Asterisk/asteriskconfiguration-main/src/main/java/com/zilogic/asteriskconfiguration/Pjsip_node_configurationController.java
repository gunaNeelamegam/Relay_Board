/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.asteriskconfiguration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Speaker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import util.LoadFiles;

/**
 * FXML Controller class
 *
 * @author user
 */
public class Pjsip_node_configurationController implements Initializable {

    @FXML
    public AnchorPane anchorPane;
    @FXML
    public Text nodeNameText;
    @FXML
    public PasswordField nodePwdText;
    @FXML
    public Text protocolText;
    @FXML
    public Text voiceQualityText;
    @FXML
    public Text displayNameText;
    @FXML
    public ListView<Speaker> nodeListView;
    @FXML
    public Button addBtn;
    @FXML
    public Button deleteBtn;
    @FXML
    public Button updateBtn;
    @FXML
    public Button loadFileBtn;

    public static Scene scene;

    //public static Stage stage;
    @FXML
    public Button saveBtn;

    public String nodeName;
    public String nodePassword;
    public String nodeProtocol;

    public EventHandler<ActionEvent> deleteEvent;

    public static List<Speaker> speakers = new ArrayList<>();

    public static Speaker speaker = new Speaker();

    public static JSONArray overwrittenList = new JSONArray();
    public static JSONArray editNodeDetailsList = new JSONArray();
    @FXML
    private BorderPane bp;

    public FileChooser fileChooser;
    public File pjsipFile = null;

    /**
     * This method is used to get the name of the selected node from the
     * listView.
     *
     * @return String getNodeName() - Return the selected node Name return null
     * if List is empty.
     */
    public String getNodeName() throws Exception {
        int i = 0;
        //Speaker sp = nodeListView.getSelectionModel().getSelectedItem();
        //System.out.println("sp" + sp + sp.getPassword() + sp.getDisplayName());
        String nodeSelection = nodeListView.getSelectionModel().getSelectedItem().toString();

        for (i = 0; i < speakers.size(); i++) {

            if (nodeSelection.contentEquals(speakers.get(i).getUserName())) {
                return speakers.get(i).getUserName();
            }
        }
        return null;
    }

    public Speaker getNode() throws Exception {
        int i = 0;
        Speaker sp = nodeListView.getSelectionModel().getSelectedItem();
        return sp;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            fileChooser = FileUtil.createFileChooser();
            nodeListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    try {

                        Speaker s = getNode();

                        String nodeSelection = getNodeName();
                        if (nodeSelection != null) {
                            nodeNameText.setText(nodeSelection);

                            nodePwdText.setText(s.getPassword());
                            protocolText.setText(s.getProtocol());
                            voiceQualityText.setText(s.getCodecAllow());
                            displayNameText.setText(s.getUserName());

                            if (speakers.isEmpty()) {
                                clearTextEntries();
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            });

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This method is used to add node to the list view.
     *
     * @Param event - The event is triggered when add button is clicked.
     */
    @FXML
    private void addNode(ActionEvent event) throws Exception {
        App.addNodeStage = new Stage();
        scene = new Scene(FXMLLoader.load(getClass().getResource("pjsip_add_node.fxml")), 640, 480);
        App.addNodeStage.setScene(scene);
        App.addNodeStage.setOnHiding(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                App.addNodeStage.close();
                clearTextEntries();
                try {

                    updateListView();

                } catch (Exception e) {

                }
            }
        });
        App.addNodeStage.initModality(Modality.APPLICATION_MODAL);
        App.addNodeStage.initOwner(App.mainstage);
        App.addNodeStage.requestFocus();
        App.addNodeStage.show();
    }

    /**
     * This method is used to update the listview with nodes.
     *
     */
    public void updateListView() {
        int i = 0;
        nodeListView.getItems().clear();
        clearTextEntries();

        for (i = 0; i < speakers.size(); i++) {
            nodeListView.getItems().add(speakers.get(i));
        }

    }

    public void displayFileLoadSuccessfully(int fileContentSize) {
        ButtonType OK = new ButtonType("Ok");

        Label lbl = new Label();
        if (fileContentSize != 0) {
            lbl.setText("File Loaded Successfully");
        } else {
            lbl.setText("Empty file Loaded");
        }
        Alert a = new Alert(AlertType.INFORMATION, "", OK);
        Optional<ButtonType> result;

        a.getDialogPane().setContent(lbl);
        result = a.showAndWait();

    }

    public void displayFileNotFound() {
        ButtonType OK = new ButtonType("Ok");

        Label lbl = new Label();
        lbl.setText("File Not Found");

        Alert a = new Alert(AlertType.WARNING, "", OK);
        Optional<ButtonType> result;

        a.getDialogPane().setContent(lbl);
        result = a.showAndWait();
    }

    /**
     * This method is used to populate the listview by reading the configuration
     * file
     *
     * @param event - The event is triggered when load button is clicked.
     */
    @FXML
    private void loadFile(ActionEvent event) throws Exception {

        speakers.clear();

        try {
            pjsipFile = LoadFiles.getPjsipFile();
            nodeListView.getItems().clear();
            if (FileUtil.readconfigFile(pjsipFile.getAbsolutePath(), speakers)) {
                displayFileNotFound();
            } else {
                displayFileLoadSuccessfully(speakers.size());
                updateListView();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to clear the node entries.
     *
     */
    public void clearTextEntries() {

        nodeNameText.setText("");
        nodePwdText.clear();
        protocolText.setText("");
        voiceQualityText.setText("");
        displayNameText.setText("");
    }

    /**
     * This method is used to delete the selected node from the list view.
     *
     * @param event - The event is triggered when delete button is clicked.
     */
    @FXML
    private void deleteNode(ActionEvent event) {
        try {

            Speaker s = getNode();
            String nodeSelection = getNodeName();
            if (nodeSelection != null) {
                int i = 0;
                ButtonType OK = new ButtonType("Yes");
                ButtonType Cancel = new ButtonType("No");

                Label lbl = new Label();
                lbl.setStyle("-fx-text-fill:RED");
                lbl.setText("Are you sure you want to delete the node " + nodeSelection + "?");

                Alert a = new Alert(AlertType.CONFIRMATION, "", OK, Cancel);
                Optional<ButtonType> result;

                a.getDialogPane().setContent(lbl);
                result = a.showAndWait();
                if (result.get().getText().contentEquals(ButtonType.YES.getText())) {
                    speakers.remove(s);
                }
                updateListView();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This method is used to populate the list. file
     *
     * @param jsonarray - The jsonarray which is used to populate the entered
     * details of the node.
     */
    public void populateEditableNodeList(JSONArray jsonarray) {
        try {
            jsonarray.clear();

            JSONObject obj = new JSONObject();
            obj.put("Node Name", nodeNameText.getText());
            obj.put("Display Name", displayNameText.getText());
            obj.put("Password", nodePwdText.getText());
            obj.put("Protocol", protocolText.getText());
            obj.put("Voice Quality", voiceQualityText.getText());

            jsonarray.add(obj);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This method is used to update the node selected from the list View.
     *
     * @param event - The event is triggered when update button is clicked.
     */
    @FXML
    public void updateNode(ActionEvent event) throws Exception {

        try {
            Speaker s = getNode();
            String nodeSelection = getNodeName();
            if (nodeSelection != null) {
                populateEditableNodeList(editNodeDetailsList);
                populateEditableNodeList(overwrittenList);
                App.addNodeStage = new Stage();
                scene = new Scene(FXMLLoader.load(getClass().getResource("pjsip_update_node.fxml")), 640, 480);
                App.addNodeStage.setScene(scene);
                App.addNodeStage.setOnHiding(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent t) {
                        App.addNodeStage.close();

                        try {
                            clearTextEntries();

                            updateListView();
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                });

                App.addNodeStage.initModality(Modality.APPLICATION_MODAL);
                App.addNodeStage.initOwner(App.mainstage);
                App.addNodeStage.requestFocus();
                App.addNodeStage.show();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * This method is used to save the added nodes into the configuration file.
     *
     */
    @FXML
    private void saveFile() throws Exception {

        try {
            if (pjsipFile == null || (!(pjsipFile.exists()))) {
                //nodeListView.getItems().clear();
                pjsipFile = FileUtil.saveFileChooser(fileChooser, App.mainstage);
                LoadFiles.setPjsipFile(pjsipFile);
            }

            FileUtil.writeToConfigFile(pjsipFile.getAbsolutePath(), speakers);

            ButtonType OK = new ButtonType("Ok");

            Label lbl = new Label();
            lbl.setText("File Saved Successfully");

            Alert a = new Alert(AlertType.INFORMATION, "", OK);
            Optional<ButtonType> result;

            a.getDialogPane().setContent(lbl);
            result = a.showAndWait();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
