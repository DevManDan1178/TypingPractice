
/**
 *
 * @author Daniel Orejuela Liu
 */
import java.util.HashMap;
import javafx.application.Application;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends Application {
    //MainApp.java launches the app and sets up the main scene.

    
    private static String[] PRACTICE_STRINGS = {
        "Try typing this text. Do it as quickly and accurately as you can.",
        "Next type another line of input data.",
        "The quick brown fox jumps over the lazy dog.",
        "Five big quacking zephyrs jolt my wax bed.",
        "Sympathizing would fix Quaker objectives.",
        "A large fawn jumped quickly over white zinc boxes."
    };
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Typing Tutor - JavaFX Assignment");

        BorderPane root = new BorderPane();
        TypingController typingController = new TypingController(PRACTICE_STRINGS);
        root.setCenter(typingController.getRoot());
        Scene scene = new Scene(root, 900, 600);
        scene.setOnKeyPressed(typingController.getKeyEventHandler(true));
        scene.setOnKeyReleased(typingController.getKeyEventHandler(false));
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
