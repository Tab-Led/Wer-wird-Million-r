package com.tabled.millioner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main entry point for the "Who Wants to Be a Millionaire" application.
 *
 * This class initializes the JavaFX application and loads the start screen.
 * It sets up the primary stage with the specified FXML layout and window properties.
 *
 * Authors:
 * - Marat
 * - Tatiana
 */
public class MainApplication extends Application {
    /**
     * Starts the JavaFX application.
     *
     * This method is called automatically when the application is launched. It initializes
     * the primary stage with the start screen defined in the `start.fxml` layout file.
     *
     * @param stage The primary stage for the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        /**
         * The main method for launching the JavaFX application.
         *
         * @param args Command-line arguments (not used in this application).
         */
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/tabled/millioner/start.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("Who Wants To Be A Millionaire");
        stage.setScene(scene);

        // stage.setX(11);
        // stage.setY(11);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
