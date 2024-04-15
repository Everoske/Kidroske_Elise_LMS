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

/**
 * This class represents a dialog used to obtain connection information for an
 * external database provided by the user.
 * @author Elise Kidroske
 */
public class DatabaseFormDialog extends Dialog<ButtonType> {

    /** Selects MySQL as the database type.*/
    private RadioButton mySQL;
    /** Selects SQLite as the database type.*/
    private RadioButton sqlite;
    /** Obtains the name of the table to access in the database.*/
    private TextField tableNameField;

    /** Container for storing form components for accessing a MySQL database.*/
    private VBox mySQLGroup;
    /** Obtains the server hosting a MySQL database.*/
    private TextField dbSourceField;
    /** Obtains the name of the database to access.*/
    private TextField dbNameField;
    /** Obtains username used to access a MySQL database.*/
    private TextField usernameField;
    /** Obtains password corresponding to the provided username.*/
    private PasswordField passwordField;

    /** Container for storing form components for accessing a SQLite database.*/
    private VBox sqliteGroup;
    /** Obtains absolute path to a SQLite database file.*/
    private TextField dbPathField;


    public DatabaseFormDialog() {
        super();
        this.setTitle("Database Form");
        buildUI();
    }

    /**
     * This method is responsible for constructing and initializing the dialog.
     */
    private void buildUI() {
        Pane pane = createVBox();
        getDialogPane().setContent(pane);

        try {
            getDialogPane().getScene().getStylesheets().add(getClass().getResource("/styles/library-dialog.css").toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ensureXWillCloseDialog();
    }

    /**
     * Ensures pressing the 'X' button will close the dialog.
     */
    private void ensureXWillCloseDialog() {
        Window window = getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());
    }

    /**
     * Creates a message dialog providing attribution information on a given
     * icon image.
     * @param iconView ImageView containing the icon being attributed.
     */
    private void displayAttributeDialog(ImageView iconView) {
        MessageDialog attributionDialog = new MessageDialog(
                "Add Database Icon by Icons8\nSource: https://icons8.com/icon/pu2MfAM7qtF0/trash",
                iconView.getImage());
        attributionDialog.show();
    }

    /**
     * This method constructs the VBox containing all dialog components.
     * @return Pane representing the assembled dialog.
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

        iconView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/add-database-100.png"))));
        iconView.setOnMouseClicked(mouseEvent -> {
            displayAttributeDialog(iconView);
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

        mySQL.setOnAction(event -> {
            if (mySQL.isSelected()) {
                mySQLGroup.setVisible(true);
                sqliteGroup.setVisible(false);
            }
        });

        sqlite.setOnAction(event -> {
            if (sqlite.isSelected()) {
                sqliteGroup.setVisible(true);
                mySQLGroup.setVisible(false);
            }
        });

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

        mySQLGroup.getChildren().add(dataSourceLabel);
        mySQLGroup.getChildren().add(dbSourceField);
        mySQLGroup.getChildren().add(databaseNameLabel);
        mySQLGroup.getChildren().add(dbNameField);
        mySQLGroup.getChildren().add(usernameLabel);
        mySQLGroup.getChildren().add(usernameField);
        mySQLGroup.getChildren().add(passwordLabel);
        mySQLGroup.getChildren().add(passwordField);

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

        openDatabaseButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();

            fileChooser.setTitle("Open Database File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Database Files", "*.db"));

            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null) {
                dbPathField.setText(selectedFile.getAbsolutePath());
            }
        });

        sqliteGroup.getChildren().add(dbPathLabel);
        sqliteGroup.getChildren().add(dbPathField);
        sqliteGroup.getChildren().add(openDatabaseButton);

        StackPane groupPane = new StackPane();
        groupPane.getChildren().add(mySQLGroup);
        groupPane.getChildren().add(sqliteGroup);

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
            SQLForm form = getSQLForm();
            if (form != null) {
                setResult(ButtonType.OK);
                getDialogPane().setUserData(form);
                close();
            }
        });

        cancelButton.setOnAction(event -> {
            setResult(ButtonType.CANCEL);
            close();
        });

        buttonHBox.getChildren().add(confirmButton);
        buttonHBox.getChildren().add(cancelButton);

        mainPane.getChildren().add(addDatabaseLabel);
        mainPane.getChildren().add(iconView);
        mainPane.getChildren().add(databaseTypeLabel);
        mainPane.getChildren().add(radioHBox);
        mainPane.getChildren().add(tableName);
        mainPane.getChildren().add(tableNameField);

        mainPane.getChildren().add(groupPane);
        mainPane.getChildren().add(buttonHBox);

        mySQL.setSelected(true);
        sqliteGroup.setVisible(false);

        return mainPane;
    }

    /**
     * This method is responsible for processing the user's input into a
     * database form that can be used to form a connection with an
     * external SQL database
     * @return SQLForm filled with external database connection information.
     */
    private SQLForm getSQLForm() {
        SQLSourceType sourceType = mySQL.isSelected() ? SQLSourceType.MYSQL : SQLSourceType.SQLITE;

        String tableName = tableNameField.getText().trim();
        if (tableName.isEmpty()) {
            return null;
        }

        if (sourceType.equals(SQLSourceType.MYSQL)) {
            String dataSource = dbSourceField.getText().trim();
            String databaseName = dbNameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (!dataSource.isEmpty() && !databaseName.isEmpty() &&
                    !username.isEmpty() && !password.isEmpty()) {
                return new SQLForm(sourceType, tableName, dataSource, databaseName, username, password);
            }
        } else {
            String databasePath = dbPathField.getText().trim();
            if (!databasePath.isEmpty()) {
                return new SQLForm(sourceType, tableName, databasePath);
            }
        }
        return null;
    }
}
