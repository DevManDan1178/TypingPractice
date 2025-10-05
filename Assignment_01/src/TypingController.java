
import java.util.Map;
import java.util.function.Function;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author nagatd
 */
public class TypingController {

    private static final String[][] keyboardCharacters = {
        {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "[", "]", "\\"}, 
        {"A", "S", "D", "F", "G", "H", "J", "K", "L", ";", "\'"},
        {"SHIFT", "Z", "X", "C", "V", "B", "N", "M", ",", ".", "/"}
    };
    
    public void DisplayOntoRootPane(GridPane rootPane) {
        for (int i = 0; i < keyboardCharacters.length; i++) {
            String[] rowCharacters = keyboardCharacters[i];
            for (int j = 0; j < rowCharacters.length; j++) {
                String keyboardCharacter = rowCharacters[j];
                Button keyButton = new Button(keyboardCharacter);
                keyButton.setMinSize(50, 50);
                rootPane.add(keyButton, j, i);
                
            }
        }
    }
}
