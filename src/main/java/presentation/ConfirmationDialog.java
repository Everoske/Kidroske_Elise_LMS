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


public class ConfirmationDialog extends Dialog<ButtonType> {
    private final String message;

    public ConfirmationDialog(String message) {
        super();
        this.setTitle("Confirmation");
        this.message = message;
        buildUI();
    }

    private void buildUI() {
        Pane pane = createVBoxPane();
        getDialogPane().setContent(pane);

        Window window = getDialogPane().getScene().getWindow();
        window.setOnCloseRequest(event -> window.hide());
    }

    public Pane createVBoxPane() {
        VBox pane = new VBox();
        pane.alignmentProperty().setValue(Pos.CENTER);
        Label confirmLabel = new Label(message);
        confirmLabel.prefWidth(300);
        confirmLabel.prefHeight(300);
        confirmLabel.setTextAlignment(TextAlignment.CENTER);

        ImageView iconView = new ImageView();
        iconView.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/trash-image-100.png"))));
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
