package buddy;

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

    private Buddy buddy;

    private final Image userImage = loadImage("/images/DaUser.png");
    private final Image buddyImage = loadImage("/images/DaBuddy.png");

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }
    /** Injects the Duke instance */
    public void setBuddy(Buddy b) {
        buddy = b;
    }

    private Image loadImage(String path) {
        var stream = MainWindow.class.getResourceAsStream(path);
        if (stream == null) {
            throw new IllegalStateException("Missing image resource: " + path);
        }
        return new Image(stream);
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = buddy.getResponse(input);
        CommandType commandType = buddy.getCommandType();
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getBuddyDialog(response, buddyImage, commandType)
        );
        userInput.clear();

        if (commandType == CommandType.BYE) {
            System.exit(0);
        }
    }
}
