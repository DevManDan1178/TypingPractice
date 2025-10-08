import java.util.HashMap;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

/**
 *
 * @author Daniel Orejuela Liu
 */
public class TypingController {
    //STATIC VALUES - KEYS 
    private static final HashMap<String, String> KEYCODE_TO_SYMBOL = new HashMap() {
        {
            String[] keyCodes = {"PERIOD", "SLASH", "SEMICOLON", "BACK_SPACE", "QUOTE", "BACK_SLASH", "COMMA", "OPEN_BRACKET", "CLOSE_BRACKET", "BACK_QUOTE", "MINUS", "EQUALS", "CAPS_LOCK", "SHIFT"};
            String[] symbols = {".", "/", ";", "←", "'", "\\", ",", "[", "]", "`", "-", "=", "CAPS", "⇧ Shift"};
            for (int i = 0; i < keyCodes.length; i++) {
                put(keyCodes[i], symbols[i]);
            }
            for (int i = 0; i < 10; i++) {
                put("DIGIT" + String.valueOf(i), String.valueOf(i));
            }
        }
    };
    
    private static final HashMap<String, String> SHIFTLOCK_SYMBOLS = new HashMap() {
        {
            String preShift = "`1234567890-=[]\\;',./";
            String postShift = "~!@#$%^&*()_+{}|:\"<>?";
            for (int i = 0; i < preShift.length(); i++) {
                put(preShift.charAt(i) + "", postShift.charAt(i) + "");
            }
        }
    };
    
    private static final String[][] KEYBOARD_CHARACTERS = {
        {"`","1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "=", "←"},
        {"TAB", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "[", "]", "\\"}, 
        {"CAPS","A", "S", "D", "F", "G", "H", "J", "K", "L", ";", "\'", "ENTER"},
        {"⇧ Shift", "Z", "X", "C", "V", "B", "N", "M", ",", ".", "/"},
        {"SPACE"}
    };
    
    private static final HashMap<String, Double> KEY_WIDTH_EXCEPTIONS = new HashMap() {
        {
            put("⇧ Shift", 100.0);
            put("ENTER", 100.0);
            put("SPACE", 300.0);
            put("←", 100.0);
            put("CAPS", 87.5);
            put("TAB", 75.0);
        }
    };
    
    //STATIC VALUES - UI DISPLAY
    private static final double DEFAULT_KEY_SIZE = 50.0;
    private static final double VERTICAL_SPACING = 5;
    private static final double HORIZONTAL_SPACING = 5;
    private static final double TEXT_TYPING_FONT_SIZE = 30;
    private static final double STATS_FONT_SIZE = 25;
    
    //NON-STATIC VALUES
    
    //UI
    private final VBox root;
    private final HashMap<String, Button> keyboardMap;
    private final GridPane keyboardGrid;
    private final TextFlow typingTextFlow;
    private final Label accuracyLabel;
    private final Label errorCountLabel;
    private final Label lineCountLabel;
    private final Label errorMessageLabel;
    
    //Variables
    private boolean shiftPressed = false;
    private boolean capsToggled = false;
    private String typedString = "";
    private String[] allStrings;
    private int lineIndex, totalErrors, totalTypedCharacters;
    
    //Map of functions called by special symbols when PRESSED on the keyboard
    private final HashMap<String, Runnable> SYMBOL_FUNCTIONS = new HashMap() {
        {
            put("←", (Runnable) () -> {setTypedString(typedString.substring(0, Math.max(0, typedString.length() - 1)));});
            put("SPACE", (Runnable) () -> {addTypedSymbolToTypedString(" ");});
            put("ENTER", (Runnable) () -> {checkLineComplete();});
            put("TAB", (Runnable) () -> {addTypedSymbolToTypedString("  ");});
            put("⇧ Shift", (Runnable) () -> {}); //handled in input processing
            put("CAPS", (Runnable) () -> {capsToggled = !capsToggled;});
        }
    };
    
    public TypingController(String[] practiceStrings) {
        allStrings = practiceStrings;
        root = new VBox();

        keyboardGrid = new GridPane();
        keyboardGrid.setAlignment(Pos.CENTER);
        keyboardMap = createKeyboard(keyboardGrid);
        
        HBox hBox = new HBox();
       
        //Controls
        VBox controlsVBox = new VBox();
        controlsVBox.setSpacing(10);
        
        Label controlsTitle = new Label("CONTROLS");
        controlsTitle.setFont(Font.font(30));
        controlsVBox.getChildren().add(controlsTitle);
        
        Button resetButton = createButton("RESET");
        controlsVBox.getChildren().add(resetButton);
        resetButton.setFocusTraversable(false);
        resetButton.setOnAction((event) -> {
            resetTypingPractice();
        });
        
        Button nextButton = createButton("NEXT LINE");
        controlsVBox.getChildren().add(nextButton);
        nextButton.setFocusTraversable(false);
        nextButton.setOnAction((event) -> {
            incrementLineToType();
        });
        
        //Stats
        VBox statsVBox = new VBox();
        
        
        Label statsTitle = new Label("STATS");
        statsTitle.setFont(Font.font(STATS_FONT_SIZE * 1.2));
        
        accuracyLabel = new Label("Accuracy : ?/?");
        accuracyLabel.setFont(Font.font(STATS_FONT_SIZE));
        
        errorCountLabel = new Label("Errors : 0");
        errorCountLabel.setFont(Font.font(STATS_FONT_SIZE));
           
        lineCountLabel = new Label("Line ?/?");
        lineCountLabel.setFont(Font.font(STATS_FONT_SIZE));
        statsVBox.getChildren().addAll(statsTitle, accuracyLabel, errorCountLabel, lineCountLabel);
        
        //Error message 
        errorMessageLabel = new Label("");
        errorMessageLabel.setFont(Font.font(25));
        errorMessageLabel.setStyle("-fx-background-color: #ff0000;");
        
        //Text typing
        typingTextFlow = new TextFlow();
        typingTextFlow.setTextAlignment(TextAlignment.CENTER);
        //Structure
        statsVBox.setSpacing(5);
        
        hBox.getChildren().addAll(controlsVBox, statsVBox);  
        hBox.setSpacing(150);
        hBox.setAlignment(Pos.CENTER);
        
        root.getChildren().addAll(hBox, typingTextFlow, keyboardGrid, errorMessageLabel);    
        root.setAlignment(Pos.CENTER);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(25);
        //Initialize
        setLineToType(0);
    }
    
    /**
     * Creates a button (used for creating controls buttons)
     * @param buttonTitle 
     * @return the created button
     */
    private Button createButton(String buttonTitle) {
        Button btn = new Button(buttonTitle);
        btn.setMinSize(DEFAULT_KEY_SIZE * 3, DEFAULT_KEY_SIZE);
        return btn;
    }
    
    
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
            
            errorMessageLabel.setVisible(keyboardButton == null);
            if (keyboardButton == null) {
                showInvalidKeyAlert(keySymbol);
                return;
            }
            
            keyboardButton.setStyle(style);
            
            if (keySymbol.equals("⇧ Shift")) {
                shiftPressed = pressed;
                return;
            }
            
            if (pressed) {
                applySymbolFunction(getTypedSymbol(keySymbol));
            }
        };
    }
    
    /**
     * Applies the symbol to the typed string variable
     * @param symbol symbol to apply to the typed string
     */
    public void applySymbolFunction(String symbol) {
        SYMBOL_FUNCTIONS.getOrDefault(symbol, (Runnable) () -> {addTypedSymbolToTypedString(symbol);}).run();
    }
    
    /**
     * Adds the symbol to the typed strings and updates the total typed characters count and the total error count for the accuracy
     * @param typedSymbol 
     */
    private void addTypedSymbolToTypedString(String typedSymbol) {
        totalTypedCharacters += 1;
        String newTypedString = typedString + typedSymbol;
        int checkMatchingIndex = newTypedString.length() - 1;
        if (allStrings[lineIndex].length() <= checkMatchingIndex || newTypedString.charAt(checkMatchingIndex) != allStrings[lineIndex].charAt(checkMatchingIndex)) {
            totalErrors += 1;
        }
        setTypedString(newTypedString);
    }
    
    /**
     * In the text flow, creates labels of texts of regular (correct) or red (incorrect) strings with respect to the typed text and the goal text
     */
    private void displayTypedText() {
        typingTextFlow.getChildren().clear();
        String goalString = allStrings[lineIndex];
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
                //Create a text from last index to this index, and set its color as correct or incorrect
                String txtStr = goalString.substring(lastIndex, i);
                lastLabel.setText(txtStr);
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
    
    /**
     * Show alert of invalid key inputs
     * @param keyStr the invalid key code to string
     */
    private void showInvalidKeyAlert(String keyStr) {
        errorMessageLabel.setText(String.format(" INVALID KEY INPUT - %S ", keyStr));
    }
    
    
    /**
     * Checks if the typed string matches the string to type, and goes to the next lien to type if yes.
     */
    private void checkLineComplete() {
        if (typedString.equals(allStrings[lineIndex])) {
            incrementLineToType();
        }
    }
    
    /**
     * Increases the index of the line to type by 1 and calls the corresponding functions
     */
    private void incrementLineToType() {
        setLineToType((lineIndex + 1) % allStrings.length);
    }
    
    /**
     * Sets the index of the line to type (does nothing and returns false if index is invalid)
     * @param index
     * @return true if success, false if not
     */
    private boolean setLineToType(int index) {
        int totalLineCount = allStrings.length;
        if (index < 0 || index >= totalLineCount) {
            return false;
        }
        lineIndex = index;
        setTypedString("");
        lineCountLabel.setText(String.format("Line %s of %s", lineIndex + 1, totalLineCount));
        return true;
    }
    
    /**
     * Resets the typed text, the counts of saved errors and total characters
     */
    private void resetTypingPractice() {  
        totalErrors = 0;
        totalTypedCharacters = 0;
        setTypedString("");
    }
    
    /**
     * Sets the typed string and calls other methods to update UI
     * @param str string to set as (set to "" to reset)
     */
    private void setTypedString(String str) {
        typedString = str;
        displayTypedText();
        accuracyLabel.setText(String.format("Accuracy : %s/%s", totalTypedCharacters - totalErrors, totalTypedCharacters));
        errorCountLabel.setText(String.format("Errors : %s", getErrorCount()));
    }

    /**
     * Counts the amount of errors
     * @return the amount of characters in the typed text that is different from that of the goal
     */
    private int getErrorCount() {
        int errorCount = 0;
        String goalString = allStrings[lineIndex];
        for (int i = 0; i < typedString.length(); i++) {
            if (goalString.length() < i || goalString.charAt(i) != typedString.charAt(i)) {
                errorCount += 1;
            }
        }
        return errorCount;
    }


    /**
     * Gets the typed symbol depending on if shift is pressed
     * @param keySymbol the symbol of the key
     * @return the final symbol, after checking for shift presses
     */
    private String getTypedSymbol(String keySymbol) {
        if (keySymbol.length() != 1) {
            return keySymbol;
        }
        char symbol = keySymbol.toCharArray()[0];
        if (!Character.isLetter(symbol)) {
            return shiftPressed ? SHIFTLOCK_SYMBOLS.getOrDefault(keySymbol, keySymbol) : keySymbol;
        }
        
        return ((shiftPressed || capsToggled)? Character.toUpperCase(symbol) : Character.toLowerCase(symbol)) + "";
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
    
    public VBox getRoot() {
        return root;
    }
}

