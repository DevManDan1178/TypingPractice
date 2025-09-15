[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/hjjy5_H5)
# Typing Tutor - JavaFX Assignment 01

## Objective

Typing quickly and correctly is an essential skill for working effectively with computers and the Internet. In this assignment, you will build an app that helps users learn to **“touch type”** – i.e., type correctly without looking at the keyboard. Please:
- Follow coding standards (naming, comments, @author).
- Use a clean UI layout with proper indentation.
- Demonstrate your working program to the teacher.

## Requirements

1. **Virtual Keyboard**
   - Display a virtual keyboard using **JavaFX Buttons**.
   - The layout should mimic a standard computer keyboard.
   - You may omit keys not needed (F-keys, arrows, Insert, Home, Page Up, numeric keypad, Caps Lock, Tab, Alt, Ctrl).
   - Shift key must be included.
  
2. **Text Display**
   - The character should automatically appear in the Textbox or TextArea that shows the typed text.

3. **Key Highlighting**
   - Highlight the corresponding Button when a key is pressed:
     ```java
     button.setStyle("-fx-background-color: #ff0000;");
     ```
   - Reset the Button style when the key is released (return the background color to its normal color.)
   - Provide a button to “Go to next input text” and a text box to indicate which text is currently being displayed such as “1 of 6”, etc.  When the next text item is chosen, clear the typed output Textbox. Provide a Reset button to clear the typed text and revert Input Text to choice 1. 

4. **User Accuracy Monitoring**
   - Keep track of how many keystrokes the user types correctly and incorrectly.
   - Develop a method to display and count errors.
   - Allow the user to use backspace to remove errors.

5. **Other Features**
   - textbox to display the “keyvalue”  of the key being pressed.
   - display a message in red to indicate when the “keyvalue” is Not handled by your program.
    
6. **Starter Files**
   - `MainApp.java`: JavaFX main application.
   - `TypingController.java`: Add your methods, key event handling, and logic here.

### Sample Texts for Testing:
1. Try typing this text. Do it as quickly and accurately as you can.
2. Next type another line of input data.
3. The quick brown fox jumps over the lazy dog.
4. Five big quacking zephyrs jolt my wax bed.
5. Sympathizing would fix Quaker objectives.
6. A large fawn jumped quickly over white zinc boxes.

The following links provide some JavaFX code to handle various keyboard events.  

https://docs.oracle.com/javafx/2/events/handlers.htm 
https://docs.oracle.com/javase/8/javafx/api/javafx/scene/input/KeyEvent.html 

The following links provide examples of typing tutors that monitor typing accuracy and speed. They may provide useful for ideas for your assignment.   
Two entry points to the same typing program:  

https://www.typing.com/student/lessons/328/common-english-words  
https://www.typing.com/student/lessons/359/j-f-and-space  
