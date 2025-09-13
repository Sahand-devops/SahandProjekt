package com.friends;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Main application class for the calculator.
 * This class extends JavaFX Application and initializes the calculator UI.
 */
public class App extends Application {

    /**
     * Initializes and displays the primary stage of the application.
     *
     * @param primaryStage the primary stage for this application.
     * @throws IOException if the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/calculator.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("404.Kalkylator");
        primaryStage.show();
    }

    /**
     * The main entry point for the application.
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
