package controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Date: 03/24/2024
Name: Library Application
Description:
The purpose of this program is to allow users to manage the contents of a library.
The library collection is represented by Book objects stored in a relational database.
This program allows users to view the books in the library's database, add new books
by providing text files, delete existing books using their title or barcode, and
check out/check in books using their title.

This is the insertion point of the JavaFX program.
 */
public class LibraryApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryApplication.class.getResource("/view/library-management-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 625, 725);

        // Attempt to apply library management style sheet to the UI
        try {
            scene.getStylesheets().add(getClass().getResource("/styles/library-management.css").toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }

        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}