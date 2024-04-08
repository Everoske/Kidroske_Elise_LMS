package presentation;

import database.SQLForm;
import database.SQLSourceType;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.util.Objects;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Date: 04/07/2024
Name: Database Form Dialog
Description:
This dialog is used to obtain connection information for an
external database provided by the user
 */
public class DatabaseFormDialog extends Dialog<ButtonType> {

    private TextField dbNameField;
    private RadioButton mySQL;
    private RadioButton sqlite;

    private VBox mySQLGroup;
    private TextField dbSourceField;
    private TextField tableNameField;
    private TextField usernameField;
    private PasswordField passwordField;

    private VBox sqliteGroup;
    private TextField dbPathField;


    public DatabaseFormDialog() {
        super();
        this.setTitle("Database Form");
        buildUI();
    }

    /*
    Name: Build UI
    Arguments: None
    Returns: Void
    Description:
    This method is responsible for constructing and initializing the dialog.
     */
    private void buildUI() {
        Pane pane = createVBox();
        getDialogPane().setContent(pane);

        // Attempt to apply dialog style sheet to the UI
        try {
            getDialogPane().getScene().getStylesheets().add(getClass().getResource("/styles/library-dialog.css").toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // This ensures the clicking "X" will close the dialog
        Window window = getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());
    }

    /*
    Name: Create VBox Pane
    Arguments: None
    Returns: Pane
    Description:
    This method constructs a VBox with all the dialog components.
     */
    private Pane createVBox() {
        VBox mainPane = new VBox();
        mainPane.setAlignment(Pos.CENTER);
        mainPane.setSpacing(10);
        mainPane.setPrefWidth(300);
        mainPane.setPrefHeight(400);

        Label addDatabaseLabel = new Label("Add a Database");
        addDatabaseLabel.prefWidth(300);
        addDatabaseLabel.setTextAlignment(TextAlignment.CENTER);

        ImageView iconView = new ImageView();
        // If the image is clicked, an attribution dialog containing information on its source is opened
        iconView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/add-database-100.png"))));
        iconView.setOnMouseClicked(mouseEvent -> {
            MessageDialog attributionDialog = new MessageDialog(
                    "Add Database Icon by Icons8\nSource: https://icons8.com/icon/pu2MfAM7qtF0/trash",
                    iconView.getImage());
            attributionDialog.show();
        });
        iconView.prefHeight(50);
        iconView.prefWidth(300);

        Label tableName = new Label("Table Name");
        this.tableNameField = new TextField();
        this.tableNameField.setPromptText("books_table");
        this.tableNameField.setText("books");
        this.tableNameField.setPrefHeight(30);
        this.tableNameField.setMinHeight(30);
        this.tableNameField.setMaxHeight(30);

        Label databaseTypeLabel = new Label("Database Type");
        HBox radioHBox = new HBox();
        radioHBox.setAlignment(Pos.CENTER);
        radioHBox.setSpacing(10);
        this.mySQL = new RadioButton("MySQL");
        this.sqlite = new RadioButton("SQLite");

        ToggleGroup radioGroup = new ToggleGroup();
        mySQL.setToggleGroup(radioGroup);
        sqlite.setToggleGroup(radioGroup);

        radioHBox.getChildren().add(mySQL);
        radioHBox.getChildren().add(sqlite);

        // If the mySQL radio button is selected, make its group visible
        // and hide the sqlite group
        mySQL.setOnAction(event -> {
            if (mySQL.isSelected()) {
                mySQLGroup.setVisible(true);
                sqliteGroup.setVisible(false);
            }
        });

        // If the sqlite radio button is selected, make its group visible
        // and hide the sqlite group
        sqlite.setOnAction(event -> {
            if (sqlite.isSelected()) {
                sqliteGroup.setVisible(true);
                mySQLGroup.setVisible(false);
            }
        });

        // The mySQL group is used to contain all the fields needed for
        // connection to an external mySQL database
        this.mySQLGroup = new VBox();
        mySQLGroup.setSpacing(5);
        mySQLGroup.setPrefWidth(300);

        Label dataSourceLabel = new Label("Data Source");
        this.dbSourceField = new TextField();
        this.dbSourceField.setPromptText("localhost:3306");
        this.dbSourceField.setPrefHeight(30);
        this.dbSourceField.setMinHeight(30);
        this.dbSourceField.setMaxHeight(30);

        Label databaseNameLabel = new Label("Database Name");
        this.dbNameField = new TextField();
        this.dbNameField.setPromptText("books_r_us");
        this.dbNameField.setPrefHeight(30);
        this.dbNameField.setMinHeight(30);
        this.dbNameField.setMaxHeight(30);

        Label usernameLabel = new Label("Username");
        this.usernameField = new TextField();
        this.usernameField.setPromptText("root");
        this.usernameField.setPrefHeight(30);
        this.usernameField.setMinHeight(30);
        this.usernameField.setMaxHeight(30);

        Label passwordLabel = new Label("Password");
        this.passwordField = new PasswordField();
        this.passwordField.setPromptText("1111");
        this.passwordField.setPrefHeight(30);
        this.passwordField.setMinHeight(30);
        this.passwordField.setMaxHeight(30);

        // Add all mySQL fields to the mySQL group
        mySQLGroup.getChildren().add(dataSourceLabel);
        mySQLGroup.getChildren().add(dbSourceField);
        mySQLGroup.getChildren().add(databaseNameLabel);
        mySQLGroup.getChildren().add(dbNameField);
        mySQLGroup.getChildren().add(usernameLabel);
        mySQLGroup.getChildren().add(usernameField);
        mySQLGroup.getChildren().add(passwordLabel);
        mySQLGroup.getChildren().add(passwordField);

        // The sqlite group is used to contain all the fields needed for
        // connection to an external sqlite database
        this.sqliteGroup = new VBox();
        sqliteGroup.setSpacing(5);
        sqliteGroup.setPrefWidth(300);

        Label dbPathLabel = new Label("Database Path");
        this.dbPathField = new TextField();
        this.dbPathField.setPromptText("..\\books_r_us.db");
        this.dbPathField.setPrefHeight(30);
        this.dbPathField.setMinHeight(30);
        this.dbPathField.setMaxHeight(30);

        Button openDatabaseButton = new Button("Open Database");
        openDatabaseButton.setPrefHeight(30);
        openDatabaseButton.setMinHeight(30);
        openDatabaseButton.setMaxHeight(30);

        // Opens a file chooser and allows the user to select a database file
        // instead of typing it in
        openDatabaseButton.setOnAction(event -> {
            // Open a file chooser that exclusively looks for database files
            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Open Database File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Database Files", "*.db"));

            File selectedFile = fileChooser.showOpenDialog(null);

            // If the user selected a valid file, copy its path to the database path field
            if (selectedFile != null) {
                dbPathField.setText(selectedFile.getAbsolutePath());
            }
        });

        // Add all sqlite fields to the sqlite group
        sqliteGroup.getChildren().add(dbPathLabel);
        sqliteGroup.getChildren().add(dbPathField);
        sqliteGroup.getChildren().add(openDatabaseButton);

        // A stack pane is used for the two data source groups
        // This allows them to take up the same space in the main
        // pane of this dialog
        StackPane groupPane = new StackPane();
        groupPane.getChildren().add(mySQLGroup);
        groupPane.getChildren().add(sqliteGroup);

        // Confirm/Cancel group
        HBox buttonHBox = new HBox();
        buttonHBox.setPrefWidth(300);
        buttonHBox.setSpacing(10);
        Button confirmButton = new Button("Confirm");
        confirmButton.setPrefWidth(150);
        confirmButton.setPrefHeight(30);
        confirmButton.setMinHeight(30);
        confirmButton.setMaxHeight(30);

        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(150);
        cancelButton.setPrefHeight(30);
        cancelButton.setMinHeight(30);
        cancelButton.setMaxHeight(30);

        confirmButton.setOnAction(event ->
        {
            // Attempt to process the user's input into a sql form
            SQLForm form = getSQLForm();
            if (form != null) {
                // If a form is given, set the user data of the
                // dialog pane to the form, so it can be passed to
                // the controller
                setResult(ButtonType.OK);
                getDialogPane().setUserData(form);
                close();
            }
        });

        cancelButton.setOnAction(event -> {
            setResult(ButtonType.CANCEL);
            close();
        });

        // Add Confirm/Cancel buttons to group
        buttonHBox.getChildren().add(confirmButton);
        buttonHBox.getChildren().add(cancelButton);

        // Add elements to main pane
        mainPane.getChildren().add(addDatabaseLabel);
        mainPane.getChildren().add(iconView);
        mainPane.getChildren().add(databaseTypeLabel);
        mainPane.getChildren().add(radioHBox);
        mainPane.getChildren().add(tableName);
        mainPane.getChildren().add(tableNameField);

        mainPane.getChildren().add(groupPane);
        mainPane.getChildren().add(buttonHBox);

        // Select mySQL by default
        // Make the sqlite group invisible
        mySQL.setSelected(true);
        sqliteGroup.setVisible(false);

        return mainPane;
    }

    /*
    Name: Get SQL Form
    Arguments: None
    Returns: SQLForm form filled with user input
    Description:
    This method is responsible for processing the user's input into a
    database form that can be used to form a connection with an
    external SQL database
     */
    private SQLForm getSQLForm() {
        // Determine what source type is currently selected
        SQLSourceType sourceType = mySQL.isSelected() ? SQLSourceType.MYSQL : SQLSourceType.SQLITE;

        // A table name is required to proceed
        String tableName = tableNameField.getText().trim();
        if (tableName.isEmpty()) {
            return null;
        }

        // If the source type is mySQL, process the data source, database name, username, and password fields
        if (sourceType.equals(SQLSourceType.MYSQL)) {
            String dataSource = dbSourceField.getText().trim();
            String databaseName = dbNameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            // If no fields are empty, return a new SQL Form
            if (!dataSource.isEmpty() && !databaseName.isEmpty() &&
                    !username.isEmpty() && !password.isEmpty()) {
                return new SQLForm(sourceType, tableName, dataSource, databaseName, username, password);
            }
        } else {
            // If the source type is sqlite, process the database path field
            String databasePath = dbPathField.getText().trim();
            // If a path is provided, return a new SQL Form
            if (!databasePath.isEmpty()) {
                return new SQLForm(sourceType, tableName, databasePath);
            }
        }
        return null;
    }
}
