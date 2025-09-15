package bong.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.util.Collections;

/**
 * Represents a dialog box to display text and an image in a chat interface.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    /**
     * Constructs a DialogBox with the given text and image.
     *
     * @param text Text content for the dialog box.
     * @param image Image for the speaker.
     */
    private DialogBox(String text, Image image) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/DialogBox.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        dialog.setText(text);
        displayPicture.setImage(image);

        // Circular image
        Circle clip = new Circle(25, 25, 25);
        displayPicture.setClip(clip);

//        // Styling for text label
//        dialog.setStyle("-fx-text-fill: #5D4037; -fx-font-size: 14px; -fx-wrap-text: true;");
//        dialog.setPadding(new Insets(5, 10, 5, 10));
//        dialog.setMinHeight(Region.USE_PREF_SIZE);
//
//        // Styling for HBox itself
//        this.setPadding(new Insets(10));
//        this.setSpacing(10);
//        this.setStyle(
//            "-fx-background-color: #FFFACD; " +
//            "-fx-border-color: #F0E68C; " +
//            "-fx-border-width: 1; " +
//            "-fx-border-radius: 5; "
//        );
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Creates a DialogBox for a user message, styled and aligned to the right.
     *
     * @param text The message text from the user.
     * @param img The user's display picture.
     * @return A DialogBox instance configured for a user message.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
//        db.getChildren().addAll(db.dialog, db.displayPicture);
        db.setAlignment(Pos.TOP_RIGHT);
        db.getStyleClass().remove("reply-label");
        return db;
    }

    public static DialogBox getBongDialog(String text, Image img) {
        DialogBox db = new DialogBox(text, img);
        db.flip();
        return db;
    }
}
