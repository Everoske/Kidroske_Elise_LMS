package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This is the insertion point of the library management system application.
 * This call initializes the user interface and launches the application.
 * @author Elise Kidroske
 */
public class LibraryApplication extends Application {
    /**
     * Initializes the user interface and creates the application window.
     * @param stage Top-level container for the JavaFX application.
     * @throws IOException Thrown if error occurs when loading user interface.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryApplication.class.getResource("/view/library-management-view.fxml"));

        int preferredWindowWidth = 625;
        int preferredWindowHeight = 725;

        Scene scene = new Scene(fxmlLoader.load(), preferredWindowWidth, preferredWindowHeight);

        try {
            scene.getStylesheets().add(getClass().getResource("/styles/library-management.css").toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }

        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the application.
     * @param args String array containing command line arguments
     */
    public static void main(String[] args) {
        launch();
    }
}