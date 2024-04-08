module com.ekidroske.kidroske_elise_lms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens view to javafx.fxml;
    exports controller;
    exports domain;
}