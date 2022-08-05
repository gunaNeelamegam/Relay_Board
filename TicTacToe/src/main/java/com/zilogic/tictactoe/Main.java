package com.zilogic.tictactoe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
	static Scene scene;
	static private String[] mybtns = { "btn1", "btn2", "btn3", "btn4", "btn5", "btn6", "btn7", "btn8", "btn9" };
	static AudioClip audio = new AudioClip(
			new File("/home/user/Downloads/Thattiputta-MassTamilan.so.mp3").toURI().toString());
	Logger log = Logger.getLogger(getClass().toString());

	@Override
	public void start(Stage primaryStage) {
		try {
			PropertyConfigurator.configure(getClass().getResource("log4j.properties"));
			log.info("Program starts");
			Parent root = FXMLLoader.load(getClass().getResource("/application/tik.fxml"));
			scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("GAME");
			scene.setFill(Color.web("BLUE"));
			scene.setRoot(root);
			primaryStage.show();
			primaryStage.setTitle("XO GAME");
			log.info("audio starts playing");
			audio.play();

		} catch (Exception e) {
			log.fatal(e);
			e.printStackTrace();

		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static List<Button> getNode() {
		List<Button> mybts = new ArrayList<>();
		for (String str : mybtns) {
			Button node = (Button) scene.lookup("#" + str);
			mybts.add(node);
		}
		return mybts;
	}

}
