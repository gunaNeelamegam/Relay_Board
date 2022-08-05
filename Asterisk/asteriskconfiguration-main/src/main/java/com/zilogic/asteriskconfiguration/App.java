package com.zilogic.asteriskconfiguration;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.stage.Modality;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    public static Stage mainstage = new Stage();

    private static Stage popupwindow;
    public static Stage addNodeStage;
    private static FXMLLoader fxmlLoader;

    /**
     * This method is used to launch the stage.
     *
     * @param stage The stage to be launch is passed as parameter.
     *
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("mainScreenUI"), 960, 790);
        mainstage.setScene(scene);
        mainstage.setTitle("Powered by SIP");
        mainstage.show();
    }

    /**
     * This method is used to set the root for the scene.
     *
     * @param fxml The string fxml is passed to be set as root to the scene
     *
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * This method is used to load the fxml which is passed as parameter.
     *
     * @param fxml The string fxml is passed to be loaded by the fxmlLoader.
     *
     * @return Parent The parent to be loaded as fxml is returned.
     */
    private static Parent loadFXML(String fxml) throws IOException {
        fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * This method is used to launch the stage.
     *
     */
    public static void main(String[] args) {
        launch();
    }
//Setting the Popview

    public static void setPopUpView(String fxml) throws IOException {
        popupwindow = new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        System.out.println("String to loading the fxml for : "+fxml.toString());
        Scene scene = new Scene(loadFXML(fxml));
        popupwindow.setScene(scene);
//        popupwindow.showAndWait();

    }

    public static void showPopUP() {
        popupwindow.showAndWait();
    }

    public static void closePopUP() {
        popupwindow.close();
    }

    public static FXMLLoader getLoader() {
        return fxmlLoader;
    }

}
