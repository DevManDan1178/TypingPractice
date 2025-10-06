
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextFlow;

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
            put("←", (Function<String, String>) (String str) -> str.substring(0, Math.max(0, str.length() - 1)));
            put("SPACE", (Function<String, String>) (String str) -> str + " ");
            put("ENTER", Function.identity());
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
    private static final double TEXT_TYPING_FONT_SIZE = 30;
    
    
    //NON-STATIC VALUES
    
    //UI
    private VBox root;
    private HashMap<String, Button> keyboardMap;
    private GridPane keyboardGrid;
    private TextFlow typingTextFlow;
    
    private boolean shiftPressed = false;
    private String typedString = "";
    private String goalString = "T";

    
    public TypingController() {
        root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(25);
        keyboardGrid = new GridPane();
        keyboardMap = createKeyboard(keyboardGrid);
        
        HBox hBox = new HBox();
        hBox.setSpacing(25);
        hBox.setAlignment(Pos.CENTER);
        //Controls
        VBox controlsVBox = new VBox();
        controlsVBox.setSpacing(10);
        
        Label controlsTitle = new Label("CONTROLS");
        controlsTitle.setFont(Font.font(30));
        controlsVBox.getChildren().add(controlsTitle);
        
        Button resetButton = createButton("RESET");
        controlsVBox.getChildren().add(resetButton);
        resetButton.setFocusTraversable(false);
        
        Button nextButton = createButton("NEXT LINE");
        controlsVBox.getChildren().add(nextButton);
        nextButton.setFocusTraversable(false);
        
        //Stats
        VBox statsVBox = new VBox();
        statsVBox.setSpacing(20);
        
        Label statsTitle = new Label("STATS");
        statsTitle.setFont(Font.font(30));
        statsVBox.getChildren().add(statsTitle);
        
        Label accuracyLabel = new Label("Accuracy : ?/?");
        accuracyLabel.setFont(Font.font(25));
        statsVBox.getChildren().add(accuracyLabel);
        
        Label lineCountLabel = new Label("Line ?/?");
        lineCountLabel.setFont(Font.font(25));
        statsVBox.getChildren().add(lineCountLabel);
        
        //Text typing
        typingTextFlow = new TextFlow();
        //Structure
        hBox.getChildren().add(controlsVBox);  
        hBox.getChildren().add(statsVBox);
        root.getChildren().add(hBox);
        root.getChildren().add(typingTextFlow);
        root.getChildren().add(keyboardGrid);
        root.setAlignment(Pos.CENTER);
    }
    
    private Button createButton(String buttonTitle) {
        Button btn = new Button(buttonTitle);
        btn.setMinSize(DEFAULT_KEY_SIZE * 3, DEFAULT_KEY_SIZE);
        return btn;
    }
    //For typing -> display the  text and if typed incorrectly, replace letters with red
    
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
     * Gets the event handler for keyboard pressing / letting go
     * @param pressed is the event for key pressed or key released?
     * @Param shiftPressedCheck predicate to check when shift is pressed
     * @return the corresponding event handler
     */
    public EventHandler<KeyEvent> getKeyEventHandler(boolean pressed) {
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
            
            if (pressed) {
                setTypedString(TypingController.applySymbolToString(typedString, getTypedSymbol(keySymbol)));
            }
        };
    }
    
    /**
     * In the text flow, creates labels of texts of regular (correct) or red (incorrect) strings with respect to the typed text and the goal text
     */
    private void displayTypedText() {
        typingTextFlow.getChildren().clear();
        int compareStrLength = Math.min(typedString.length(), goalString.length());
        //Break text into a list of indexes to seperate
        int lastIndex = 0;
        boolean lastCorrect = true;
        Label lastLabel = null;
        for (int i = 0; i < compareStrLength; i++) {
            boolean isCorrect = goalString.charAt(i) == typedString.charAt(i);          
            //If the current letter is the same (correct/incorrect) as previous(es), then continue until finding another index
            if (lastCorrect == isCorrect && i > 0 && i != compareStrLength - 1) { 
                continue;
            }
            if (lastLabel != null) {
                //Create a text from last index to this index, and set it as correct or incorrect
                String txtStr = goalString.substring(lastIndex, i);
                lastLabel.setText(txtStr);
                System.out.println(String.valueOf(i) + String.valueOf(isCorrect) + " " + txtStr);
            }
            lastLabel = new Label(goalString.charAt(i) + "");
            lastLabel.setFont(Font.font(TEXT_TYPING_FONT_SIZE));
            lastLabel.setStyle(isCorrect ? "-fx-background-color: #00FF00;" : "-fx-background-color: #ff0000;"); 
            typingTextFlow.getChildren().add(lastLabel);            


            //Update searching values
            lastIndex = i;
            lastCorrect = isCorrect;
        }
        
        if (typedString.length() == goalString.length()) {
            return;
        }
        String restOfText = (typedString.length() > goalString.length() ? typedString : goalString).substring(compareStrLength);
        Label lbl = new Label(restOfText);
        lbl.setFont(Font.font(TEXT_TYPING_FONT_SIZE));
        lbl.setStyle(typedString.length() > goalString.length() ? "-fx-background-color: #ffb24d;" : "");
        typingTextFlow.getChildren().add(lbl);   
 
    }
    
    public void resetStringToType(String str) {
        setTypedString("");
        goalString = str;
        displayTypedText();
    }
    
    /**
     * Sets the typed string and calls other methods to update UI
     * @param str 
     */
    private void setTypedString(String str) {
        typedString = str;
        displayTypedText();
        //Update error count
       
    }

    /**
     * Counts the amount of errors
     * @return the amount of characters in the typed text that is different from that of the goal
     */
    private int countErrors() {
        int errorCount = 0;
        for (int i = 0; i < typedString.length(); i++) {
            if (goalString.length() < i || goalString.charAt(i) != typedString.charAt(i)) {
                errorCount += 1;
            }
        }
        return errorCount;
    }
    
    public VBox getRoot() {
        return root;
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
    
    
    /**
     * Gets the typed symbol depending on if shift is pressed
     * @param keySymbol the symbol of the key
     * @return the final symbol, after checking for shift presses
     */
    private String getTypedSymbol(String keySymbol) {
        if (!(keySymbol.length() == 1 && Character.isLetter(keySymbol.toCharArray()[0]))) {
            return keySymbol;
        }
        char symbol = keySymbol.toCharArray()[0];
        return (shiftPressed ? Character.toUpperCase(symbol) : Character.toLowerCase(symbol)) + "";
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
    
}

