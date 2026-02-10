package buddy;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Duke using FXML.
 */
public class Main extends Application {

    private final Buddy buddy = new Buddy();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setMinHeight(Constants.MIN_WINDOW_HEIGHT);
            stage.setMinWidth(Constants.MIN_WINDOW_WIDTH);
            fxmlLoader.<MainWindow>getController().setBuddy(buddy); // inject the Buddy instance
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load MainWindow.fxml: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
