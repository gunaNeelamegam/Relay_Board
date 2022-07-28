/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zilogic.relayboard;

import javafx.concurrent.Task;

/**
 *
 * @author user
 */
public class UpdateReciveingData extends Task<String> {

    static byte[] Byte = new byte[10];
    mainController controller = new mainController();

    @Override
    protected String call() throws Exception {
        for (int i = 0; i < controller.utility.buffer.length; i++) {
            controller.response.setText("guna");
            updateValue(String.valueOf(controller.utility.buffer[i]));
            // System.out.println(Arrays.toString(Byte));
        }
        return "SucessFully";
    }
}
//updater = new UpdateReciveingData();
//                    updater.valueProperty().addListener(new ChangeListener<String>() {
//                        @Override
//                        public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
//                            data.setText(newValue);
//                            System.out.println("changing");
//                        }
//
//                    });
//                    new Thread(updater).start();
//}
