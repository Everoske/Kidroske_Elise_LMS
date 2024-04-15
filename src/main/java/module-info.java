module com.ekidroske.kidroske_elise_lms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens view to javafx.fxml;
    exports application;
    exports controller;
    exports database;
    exports domain;
    exports presentation;
}