
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

/**
 *
 * @author nagatd
 */
public class TypingController {
    //STATIC VALUES - KEYS 
    private static final HashMap<String, String> KEYCODE_TO_SYMBOL = new HashMap() {
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
    
    private static final HashMap<String, Function<String, String>> SYMBOL_FUNCTIONS = new HashMap() {
        {
            put("←", (Function<String, String>) (String str) -> str.substring(0, str.length() - 1));
            put("SPACE", (Function<String, String>) (String str) -> str + " ");
            put("ENTER", (Function<String, String>) (String str) -> str + "\n");
            put("⇧ Shift", Function.identity());
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
    
    //STATIC VALUES - UI DISPLAY
    private static final double DEFAULT_KEY_SIZE = 50.0;
    private static final double VERTICAL_SPACING = 5;
    private static final double HORIZONTAL_SPACING = 5;
    
    //NON-STATIC VALUES
    private boolean shiftPressed = false;
    private HashMap<String, Button> keyboardMap;
    private GridPane keyboardGrid;
    
    public TypingController() {
        keyboardGrid = new GridPane();
        keyboardMap = createKeyboard(keyboardGrid);
    }
    
    //STATIC FUNCTIONS - KEYBOARD
    /**
     * Displays the virtual keyboard and returns a map of the keys
     * @param keyboardGrid Parent GridPane containing the keyboard
     * @return map of key buttons with their own key string
     */
    public static HashMap<String, Button> createKeyboard(GridPane keyboardGrid) {
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
    public static String getKeySymbol(KeyCode keyCode) {
        String keyStr = keyCode.toString();
        return KEYCODE_TO_SYMBOL.getOrDefault(keyStr, keyStr);
    }
    
    /**
     * Gets the event handler for keyboard pressing / letting go
     * @param keyboardMap map of keyboard symbols to their buttons
     * @param pressed is the event for key pressed or key released?
     * @param processFunction function to call by event handler after the key is confirmed to be a keyboard key (key symbol -> void)
     * @Param shiftPressedCheck predicate to check when shift is pressed
     * @return the corresponding event handler
     */
    public EventHandler<KeyEvent> getKeyEventHandler(boolean pressed, Consumer<String> processFunction) {
        String style = pressed ? "-fx-background-color: #ff0000;" : "";
        return (KeyEvent event) -> {
            String keySymbol = TypingController.getKeySymbol(event.getCode()); 
            Button keyboardButton = keyboardMap.get(keySymbol);
            if (keyboardButton == null) {
                return;
            }
            keyboardButton.setStyle(style);
            
            if (keySymbol.equals("⇧ Shift")) {
                shiftPressed = pressed;
                return;
            }
            
            if (processFunction != null) {
                if(keySymbol.length() == 1 && Character.isLetter(keySymbol.toCharArray()[0])) {
                    char symbol = keySymbol.toCharArray()[0];
                    processFunction.accept((shiftPressed ? Character.toUpperCase(symbol) : Character.toLowerCase(symbol)) + "");
                    return;
                }
                processFunction.accept(keySymbol);
            }
        };
    }
    
    //STATIC FUNCTIONS - TEXT DISPLAY
    
    public static Label getTextDisplay() {
        Label txtLabel = new Label();
        txtLabel.setMinSize(DEFAULT_KEY_SIZE * 8, DEFAULT_KEY_SIZE);
        txtLabel.setFont(Font.font("Arial", 20));
        return txtLabel;
    }
    
    /**
     * Applies the symbol to the string
     * @param startStr initial string
     * @param symbol symbol to apply
     * @return the new string
     */
    public static String applySymbolToString(String startStr, String symbol) {
        return SYMBOL_FUNCTIONS.getOrDefault(symbol, (Function<String, String>) (String str) -> (str + symbol)).apply(startStr);
    }
    
    public GridPane getKeyboardGrid() {
        return keyboardGrid;
    }
    
}

