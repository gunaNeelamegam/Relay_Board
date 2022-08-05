module com.mycompany.callapp {
    requires javafx.controls;
    requires javafx.fxml;
 requires pjsip;
 requires lombok;
 requires com.jfoenix;
    opens com.mycompany.callapp to javafx.fxml;
    exports com.mycompany.callapp;
}
