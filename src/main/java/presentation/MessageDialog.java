package presentation;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Window;

import java.util.Objects;

/**
 * This class represents a dialog used to display a message to the user.
 * @author Elise Kidroske
 */
public class MessageDialog extends Dialog<ButtonType> {
    /** Message to display in the dialog.*/
    private final String message;
    /** The expected type of message to display. Determines the behavior of the dialog.*/
    private final MessageType type;
    /** The image to display in the dialog.*/
    private final Image icon;


    public MessageDialog(String message, Image icon) {
        super();
        this.message = message;
        // If an Image is given, the message dialog is an attribution dialog
        this.type = MessageType.ATTRIBUTION;
        this.setTitle("Attribution");
        this.icon = icon;

        buildUI();
    }

    public MessageDialog(String message, MessageType type) {
        super();
        this.message = message;
        this.type = type;
        if (this.type.equals(MessageType.INFORMATIVE)) {
            this.setTitle("Information");
            this.icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/informative-image-100.png")));
        } else {
            this.setTitle("Error");
            this.icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/error-image-100.png")));
        }

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
     * @param attributionString String representing attribution message.
     */
    private void displayAttributeDialog(ImageView iconView, String attributionString) {
        MessageDialog attributionDialog = new MessageDialog(
                attributionString,
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
        ImageView iconView = new ImageView(icon);
        if (type != MessageType.ATTRIBUTION) {
            String attributionString = type == MessageType.INFORMATIVE ?
                    "Idea Icon by Icons8\nSource: https://icons8.com/icon/67370/idea" :
                    "Cancel Icon by Icons8\nSource: https://icons8.com/icon/97743/cancel";
            iconView.setOnMouseClicked(mouseEvent -> {
                displayAttributeDialog(iconView, attributionString);
            });
        }
        iconView.prefHeight(50);
        iconView.prefWidth(50);
        confirmLabel.prefWidth(300);
        confirmLabel.prefHeight(300);
        confirmLabel.setTextAlignment(TextAlignment.CENTER);

        Button okayButton = new Button("Okay");
        okayButton.setPrefWidth(150);

        okayButton.setOnAction(event ->
        {
            setResult(ButtonType.CLOSE);
            close();
        });

        pane.setSpacing(10);

        pane.getChildren().add(iconView);
        pane.getChildren().add(confirmLabel);
        pane.getChildren().add(okayButton);

        return pane;
    }
}
