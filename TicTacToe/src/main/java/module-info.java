module com.zilogic.tictactoe {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.zilogic.tictactoe to javafx.fxml;
    exports com.zilogic.tictactoe;
}
