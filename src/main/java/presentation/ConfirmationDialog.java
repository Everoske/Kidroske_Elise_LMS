package presentation;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Window;

import java.util.Objects;

/**
 * This class represents a dialog used to get a confirmation from the user.
 * @author Elise Kidroske
 */
public class ConfirmationDialog extends Dialog<ButtonType> {
    private final String message;

    public ConfirmationDialog(String message) {
        super();
        this.setTitle("Confirmation");
        this.message = message;
        buildUI();
    }

    /**
     * This method is responsible for constructing and initializing the dialog.
     */
    private void buildUI() {
        Pane pane = createVBoxPane();
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
                "Trash Icon by Icons8\nSource: https://icons8.com/icon/pu2MfAM7qtF0/trash",
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
        Label confirmLabel = new Label(message);
        confirmLabel.prefWidth(300);
        confirmLabel.prefHeight(300);
        confirmLabel.setTextAlignment(TextAlignment.CENTER);

        ImageView iconView = new ImageView();
        iconView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/trash-image-100.png"))));

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
        buttonsBox.setAlignment(Pos.CENTER);

        buttonsBox.getChildren().add(confirmButton);
        buttonsBox.getChildren().add(cancelButton);

        pane.getChildren().add(iconView);
        pane.getChildren().add(confirmLabel);
        pane.getChildren().add(buttonsBox);

        return pane;
    }
}
