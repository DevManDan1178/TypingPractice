/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */


/**
 *
 * @author nagatd
 */
import javafx.application.Application;

public class MainApp {
         //MainApp.java launches the app and sets up the main scene.

    /**
     * @param args the command line arguments
     */
       public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.setTitle("Typing Tutor - JavaFX Assignment");

        Pane root = new Pane();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

       
    }
}
