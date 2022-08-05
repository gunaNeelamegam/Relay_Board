package com.zilogic.tictactoe;

import java.io.IOException;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Contoller extends Exception {

	@FXML
	private int count;
	@FXML
	private Label label;
	@FXML
	private Button bt;
	@FXML
	private Button btn1;
	@FXML
	private Button btn2;
	@FXML
	private Button btn3;
	@FXML
	private Button btn4;
	@FXML
	private Button btn5;
	@FXML
	private Button btn6;
	@FXML
	private Button btn7;
	@FXML
	private Button btn8;
	@FXML
	private Button btn9;

	@FXML
	public int turn = 1;
	@FXML
	Button restart;
	@FXML
	TextField name1;
	@FXML
	TextField name2;
	@FXML
	Button submit;
	static final Logger log = Logger.getLogger(Main.class);
	static Runtime runtime = Runtime.getRuntime();

	public void winnerChecking() throws Exception {

		PropertyConfigurator.configure(getClass().getResource("log4j.properties"));
		//DOMConfigurator.configure(getClass().getResource("log44j.xml"));
		String result = "";

		for (int i = 1; i <= 8; i++) {
			switch (i) {
			case 1:
				result = btn1.getText() + btn2.getText() + btn3.getText();
				log.info(result);
				break;
			case 2:
				result = btn1.getText() + btn4.getText() + btn7.getText();
				log.info(result);
				break;
			case 3:
				result = btn4.getText() + btn5.getText() + btn6.getText();
				log.info(result);
				break;
			case 4:
				result = btn7.getText() + btn8.getText() + btn9.getText();
				log.info(result);
				break;
			case 5:
				result = btn1.getText() + btn5.getText() + btn9.getText();
				log.info(result);
				break;
			case 6:
				result = btn2.getText() + btn5.getText() + btn8.getText();
				log.info(result);
				break;
			case 7:
				result = btn3.getText() + btn5.getText() + btn7.getText();
				log.info(result);
				break;
			case 8:
				result = btn3.getText() + btn6.getText() + btn9.getText();
				log.info(result);
				break;
			}
			try {
				if (result.equals("XXX")) {
					label.setText("WINNER IS " + name1.getText().toString().toUpperCase());
					runtime.exec("spd-say X is the Winner");
					log.info("Winner is " + label.getText().toString());
					for (Button b : Main.getNode()) {
						log.info("Button starts Disableing");
						b.setDisable(true);

					}

				} else if (result.equals("OOO")) {
					label.setText("WINNER IS " + name2.getText().toString().toUpperCase());
					runtime.exec("spd-say O is the Winner ");
					log.info("Winner is " + label.getText().toString());
					log.info("Button starts Disableing");
					for (Button b : Main.getNode()) {
						b.setDisable(true);

					}
				}
			} catch (Exception e) {
				System.err.println("Exception on winner Checking ");
				log.fatal(e, fillInStackTrace());
				e.printStackTrace();
				throw e;
			}
		}
	}

	@FXML
	public void Submit(ActionEvent event) {
		boolean bool = true;
		label.setTextFill(Color.BLACK);
		if (!name1.getText().equals("") && !name2.getText().equals("")) {
			name1.setDisable(bool);
			name2.setDisable(bool);
			log.info(name1.getText().toString());
			log.info(name2.getText().toString());
			log.info("Sumbit button is pressed");
		} else {
			bool = false;
			name1.setDisable(bool);
			name2.setDisable(bool);
		}

	}

	@FXML
	public void reStart(ActionEvent action) throws IOException {
		Stage primaryStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("/application/tik.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.setTitle("TIK TAK TOE");
		primaryStage.show();
		log.warn("New Fxml is loading");
	}

	@FXML
	private void keyPressed(ActionEvent event) throws Exception {
		Button bt = (Button) event.getSource();
		if (turn % 2 != 0) {
			bt.setText("X");
			log.debug(bt.getText());
			runtime.exec("spd-say X");
			bt.setTextFill(Color.web("#FF0000"));
			bt.setDisable(true);
			turn++;
			winnerChecking();
		} else if (turn % 2 == 0) {
			bt.setText("O");
			log.info(bt.getText());
			runtime.exec("spd-say O");
			bt.setTextFill(Color.web("#0000FF"));
			bt.setDisable(true);
			winnerChecking();
			turn++;

		}
		if (turn > 9) {
			bt.getText();
			label.setText("MATCH IS TIED!");
		}
	}

	public void exit(ActionEvent action) {
		Main.audio.stop();
		log.info(action);
		log.info("Exit button is Clicked ");
		System.exit(0);
	}

}
