module com.zilogic.training {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.zilogic.training to javafx.fxml;
    exports com.zilogic.training;
}
