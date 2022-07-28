module com.zilogic.relayboard {
    requires javafx.controls;
    requires javafx.fxml;
 requires com.jfoenix;
 requires com.fazecast.jSerialComm;
    opens com.zilogic.relayboard to javafx.fxml;
    exports com.zilogic.relayboard;
   requires org.apache.commons.text;
}
