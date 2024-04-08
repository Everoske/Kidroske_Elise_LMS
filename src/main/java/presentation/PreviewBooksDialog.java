package presentation;

import controller.BookCellFactory;
import domain.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.Objects;

/*
Project By: Elise Kidroske
Class: Software Development I CEN-3024C
Date: 03/24/2024
Name: Preview Books Dialog
Description:
This dialog is used to display books the user provided to add to the library database.
It asks them for confirmation before handing off control back to the controller layer.
 */
public class PreviewBooksDialog extends Dialog<ButtonType> {
    private ListView<Book> booksList;
    private final ObservableList<Book> observableBooks;

    public PreviewBooksDialog(ArrayList<Book> books) {
        super();
        this.setTitle("Confirm Books");
        observableBooks = FXCollections.observableList(books);
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
        Pane pane = createVBoxPane();
        getDialogPane().setContent(pane);

        // Initialize Book List to display new books
        booksList.setCellFactory(new BookCellFactory());
        booksList.setItems(observableBooks);

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
    public Pane createVBoxPane() {
        VBox pane = new VBox();
        pane.alignmentProperty().setValue(Pos.CENTER);
        Label confirmLabel = new Label("Confirm Books to Add");
        confirmLabel.prefWidth(300);
        confirmLabel.setTextAlignment(TextAlignment.CENTER);
        this.booksList = new ListView<>();

        ImageView iconView = new ImageView();
        iconView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/book-image-100.png"))));
        // If clicked, show source image on the icon in an attribution dialog
        iconView.setOnMouseClicked(mouseEvent -> {
            MessageDialog attributionDialog = new MessageDialog(
                    "Book Icon by Icons8\nSource: https://icons8.com/icon/XLa4HP4kJj7b/book",
                    iconView.getImage());
            attributionDialog.show();
        });
        iconView.prefHeight(50);
        iconView.prefWidth(50);

        HBox buttonsBox = new HBox();
        Button confirmButton = new Button("Confirm");
        confirmButton.setPrefWidth(150);
        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefWidth(150);

        confirmButton.setOnAction(event ->
        {
            setResult(ButtonType.OK);
            close();
        });

        cancelButton.setOnAction(event -> {
            setResult(ButtonType.CANCEL);
            close();
        });

        pane.setSpacing(10);
        buttonsBox.setSpacing(10);

        buttonsBox.getChildren().add(confirmButton);
        buttonsBox.getChildren().add(cancelButton);

        pane.getChildren().add(iconView);
        pane.getChildren().add(confirmLabel);
        pane.getChildren().add(booksList);
        pane.getChildren().add(buttonsBox);

        return pane;
    }
}
