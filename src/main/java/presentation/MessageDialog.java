package presentation;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Window;

import java.util.Objects;

public class MessageDialog extends Dialog<ButtonType> {
    private final String message;
    private final MessageType type;

    private final Image icon;

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

    private void buildUI() {
        Pane pane = createVBoxPane();
        getDialogPane().setContent(pane);

        try {
            getDialogPane().getScene().getStylesheets().add(getClass().getResource("/styles/library-dialog.css").toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Window window = getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());
    }

    public Pane createVBoxPane() {
        VBox pane = new VBox();
        pane.alignmentProperty().setValue(Pos.CENTER);
        Label confirmLabel = new Label(message);
        ImageView iconView = new ImageView(icon);
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
