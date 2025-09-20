package bong.gui;


import bong.BongCore;

import bong.command.Command;
import bong.exception.BongException;
import bong.parser.Parser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private BongCore bongCore;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/UserCat.png"));
    private Image bongImage = new Image(this.getClass().getResourceAsStream("/images/BongCat.png"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());

        sendButton.setOnAction(event -> handleUserInput());
        userInput.setOnAction(event -> handleUserInput());
    }

    /**
     * Injects the BongCore instance into this controller.
     * This method is called by Main after loading the FXML.
     *
     * @param b BongCore instance containing the application's logic.
     */
    public void setBongCore(BongCore b) {
        assert b != null : "BongCore must not be null when injected into controller";
        bongCore = b;
      
        String welcomeMessage = bongCore.getWelcomeMessage();
        dialogContainer.getChildren().add(DialogBox.getBongDialog(welcomeMessage, bongImage));
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isEmpty()) {
            return;
        }

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage)
        );

        String response = bongCore.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getBongDialog(response, bongImage)
        );

        userInput.clear();

        try {
            Command parsedCommand = Parser.parse(input);
            if (parsedCommand.isExit()) {
                Platform.exit();
            }
        } catch (BongException ignored) {

        }
    }
}

