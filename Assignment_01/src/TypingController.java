
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 *
 * @author nagatd
 */
public class TypingController {
    
    private static final HashMap<String, String> SYMBOL_MAP = new HashMap() {
        {
            put("PERIOD", ".");
            put("SLASH", "/");
            put("SEMICOLON", ";");
            put("BACK_SPACE", "←");
            put("QUOTE", "'");
            put("BACK_SLASH", "\\");
            put("SHIFT", "⇧ Shift");
            put("COMMA", ",");
            put("OPEN_BRACKET", "[");
            put("CLOSE_BRACKET", "]");
            put("BACK_QUOTE", "`");
            put("MINUS", "-");
            put("EQUALS", "=");
            for (int i = 0; i < 10; i++) {
                put("DIGIT" + String.valueOf(i), String.valueOf(i));
            }
        }
    };
    private static final String[][] KEYBOARD_CHARACTERS = {
        {"`","1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "←"},
        {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "[", "]", "\\"}, 
        {"A", "S", "D", "F", "G", "H", "J", "K", "L", ";", "\'", "ENTER"},
        {"⇧ Shift", "Z", "X", "C", "V", "B", "N", "M", ",", ".", "/"},
        {"SPACE"}
    };
    
    private static final HashMap<String, Double> KEY_WIDTH_EXCEPTIONS = new HashMap() {
        {
            put("⇧ Shift", 100.0);
            put("ENTER", 100.0);
            put("SPACE", 300.0);
            put("←", 100.0);
        }
    };
    
    private static final double DEFAULT_KEY_SIZE = 50.0;
    private static final double VERTICAL_SPACING = 5;
    private static final double HORIZONTAL_SPACING = 5;
    /**
     * Displays the virtual keyboard and returns a map of the keys
     * @param keyboardGrid Parent GridPane containing the keyboard
     * @return map of key buttons with their own key string
     */
    public static HashMap<String, Button> DisplayOntoRootPane(GridPane keyboardGrid) {
        HashMap<String, Button> keyboardMap = new HashMap();
        for (int i = 0; i < KEYBOARD_CHARACTERS.length; i++) {
            String[] rowCharacters = KEYBOARD_CHARACTERS[i];
            GridPane rowGrid = new GridPane();
            keyboardGrid.add(rowGrid, 0, i);
            rowGrid.setAlignment(Pos.CENTER);
            for (int j = 0; j < rowCharacters.length; j++) {
                String keyboardCharacter = rowCharacters[j];
                Button keyButton = new Button(keyboardCharacter);
                keyButton.setDisable(true);
                keyButton.setMinSize(KEY_WIDTH_EXCEPTIONS.getOrDefault(keyboardCharacter, DEFAULT_KEY_SIZE), DEFAULT_KEY_SIZE);
                rowGrid.add(keyButton, j, 0);
                keyboardMap.put(keyboardCharacter, keyButton);
            }
            rowGrid.setPadding(new Insets(VERTICAL_SPACING));
            rowGrid.setHgap(HORIZONTAL_SPACING);
        }
        return keyboardMap;
    }
    
    /**
     * Gets the string of the key code that corresponds to the keyboard key
     * @param keyCode
     * @return String for the key in the keyboard
     */
    public static String getKeyString(KeyCode keyCode) {
        String keyStr = keyCode.toString();
        return SYMBOL_MAP.getOrDefault(keyStr, keyStr);
    }
}
