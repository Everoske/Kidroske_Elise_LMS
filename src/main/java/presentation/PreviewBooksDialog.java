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

/**
 * This class represents a dialog used to display books the user provided to add
 * to the library database.It asks them for confirmation before handing off control
 * back to the controller layer.
 * @author Elise Kidroske
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

    /**
     * This method is responsible for constructing and initializing the dialog.
     */
    private void buildUI() {
        Pane pane = createVBoxPane();
        getDialogPane().setContent(pane);

        booksList.setCellFactory(new BookCellFactory());
        booksList.setItems(observableBooks);

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
                "Book Icon by Icons8\nSource: https://icons8.com/icon/XLa4HP4kJj7b/book",
                iconView.getImage());
        attributionDialog.show();
    }

    /**
     * This method constructs the VBox containing all dialog components.
     * @return Pane representing the assembled dialog.
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

        iconView.setOnMouseClicked(mouseEvent -> {
            displayAttributeDialog(iconView);
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
