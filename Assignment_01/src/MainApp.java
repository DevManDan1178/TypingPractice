
/**
 *
 * @author Daniel Orejuela Liu
 */
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    //MainApp.java launches the app and sets up the main scene.

    
    private static String[] PRACTICE_STRINGS = {
        "Try typing this text. Do it as quickly and accurately as you can.",
        "Next type another line of input data.",
        "The quick brown fox jumps over the lazy dog.",
        "Five big quacking zephyrs jolt my wax bed.",
        "Sympathizing would fix Quaker objectives.",
        "A large fawn jumped quickly over white zinc boxes.",
        "What if there were \"special\" symbols?",
        "I paid 100$ for 10% of a fish & chips company's stock.",
        "Something cool: [triangles] <| |> /\\",
        "System.out.println(NO_MORE_LINES_ALERT + \"- I guess this is it?\")"

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
        Scene scene = new Scene(root, 900, 800);
        scene.setOnKeyPressed(typingController.getKeyEventHandler(true));
        scene.setOnKeyReleased(typingController.getKeyEventHandler(false));
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
