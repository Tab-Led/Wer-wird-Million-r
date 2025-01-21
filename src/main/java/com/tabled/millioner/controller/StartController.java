package com.tabled.millioner.controller;

import com.tabled.millioner.MainApplication;
import com.tabled.millioner.services.MP3Player;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class StartController {
    /**
     * Controller for the Start Screen of the "Who Wants to Be a Millionaire" game.
     *
     * This class handles the initialization of the start screen, including setting up the logo,
     * enabling buttons, and handling user interactions for selecting the language, viewing the rules,
     * starting the game, or exiting the application.
     */

    private static final Logger logger = LogManager.getLogger(StartController.class);

    @FXML
    private ImageView logo;
    @FXML
    private Button btnEnglish;
    @FXML
    private Button btnDeutsch;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnExit;
    @FXML
    private Button btnRules;

    private String selectedLanguage = "en"; // Язык по умолчанию

    @FXML
    public void initialize() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        /**
         * Initializes the start screen controller.
         *
         * Sets up the logo image, enables all buttons, and prepares the screen for user interactions.
         *
         * @throws UnsupportedAudioFileException If the audio file format is unsupported.
         * @throws IOException If an error occurs during file handling.
         * @throws LineUnavailableException If the audio line is unavailable.
         */

        logger.info("Initializing StartController...");
        MP3Player player = new MP3Player();
        //player.play("src/main/resources/com/tabled/millioner/media/audio/trailer.wav");


        // Воспроизведение WAV-файла
        new Thread(() -> {
            try {
                player.play("src/main/resources/com/tabled/millioner/media/audio/trailer.wav");
            } catch (Exception e) {
                logger.error("Error playing audio: {}", e.getMessage(), e);
            }
        }).start();

        // Отображение GIF
        showGif("src/main/resources/com/tabled/millioner/images/trailer.gif");



        setLogoImage();
        enableButtons();
    }

    private void showGif(String path) {
        try {
            Image gif = new Image(new File(path).toURI().toString());
            ImageView gifView = new ImageView(gif);
            gifView.setPreserveRatio(true);
            gifView.setFitWidth(800);

            // Добавляем GIF в интерфейс (например, в StackPane)
            StackPane root = new StackPane(gifView);
            Scene scene = new Scene(root, 1200, 800);

            Stage gifStage = new Stage();
            gifStage.setScene(scene);
            gifStage.setTitle("Trailer Animation");
            gifStage.show();
        } catch (Exception e) {
            logger.error("Error displaying GIF: {}", e.getMessage(), e);
        }
    }


    private void setLogoImage() {
        /**
         * Sets the logo image for the start screen.
         *
         * Loads the image from the resources folder and displays it in the logo placeholder.
         * Logs an error if the image fails to load.
         */
        try {
            logo.setImage(new Image(Objects.requireNonNull(MainApplication.class.getResource("images/logo.png")).toString()));
            logger.info("Logo successfully loaded.");
        } catch (NullPointerException e) {
            logger.error("Failed to load logo image: {}", e.getMessage());
        }
    }

    private void enableButtons() {
        /**
         * Enables all buttons on the start screen.
         *
         * Ensures that all buttons are active and ready for user interactions.
         */
        btnEnglish.setDisable(false);
        btnDeutsch.setDisable(false);
        btnStart.setDisable(false);
        btnExit.setDisable(false);
        btnRules.setDisable(false);
    }

    // Button handlers

    @FXML
    protected void onEnglishButtonClick() {
        /**
         * Handles the "English" button click event.
         *
         * Sets the selected language to English and logs the selection.
         */
        selectedLanguage = "en";
        logger.info("Language selected: English");
    }

    @FXML
    protected void onDeutschButtonClick() {
        /**
         * Handles the "Deutsch" button click event.
         *
         * Sets the selected language to German and logs the selection.
         */
        selectedLanguage = "de";
        logger.info("Language selected: Deutsch");
    }

    @FXML
    protected void onStartButtonClick() {
        /**
         * Handles the "Start" button click event.
         *
         * Starts the game by loading the main game screen and passing the selected language to the game controller.
         * Logs an error if the game fails to start.
         */
        logger.info("Start button clicked.");
        try {
            startGame();
        } catch (IOException e) {
            logger.error("Failed to start game: {}", e.getMessage(), e);
        }
    }

    @FXML
    protected void onExitButtonClick() {
        /**
         * Handles the "Exit" button click event.
         *
         * Exits the application by terminating the JVM process.
         */
        logger.info("Exit button clicked. Exiting application.");
        System.exit(0);
    }

    @FXML
    protected void onRulesButtonClick() {
        /**
         * Handles the "Rules" button click event.
         *
         * Displays the game rules in an informational alert dialog. The rules include the gameplay instructions
         * and the use of lifelines.
         */
        logger.info("Rules button clicked. Displaying game rules.");

        Alert rulesAlert = new Alert(Alert.AlertType.INFORMATION);
        rulesAlert.setTitle("Game Rules");
        rulesAlert.setHeaderText("Rules of the Game");
        rulesAlert.setContentText(
                """
                Welcome to "Who Wants to Be a Millionaire"!
                - Answer 15 questions correctly to win the grand prize of 1,000,000€.
                - Use three hints to assist you:
                  * 50:50 – Eliminates two incorrect answers.
                  * Joker – Repeats the correct answer multiple times.
                  * Second Chance – Gives you a second chance to answer if you’re wrong.
                - If you lose, you'll take home the last safe amount you reached.
                Good luck and have fun!
                """
        );

        rulesAlert.showAndWait();
        logger.info("Rules displayed successfully.");
    }

    private void startGame() throws IOException {
        /**
         * Starts the main game.
         *
         * Loads the main game scene, initializes the game controller, and sets the selected language.
         * Updates the current stage with the game scene.
         *
         * @throws IOException If an error occurs while loading the game scene.
         */
        logger.info("Starting the game with language: {}", selectedLanguage);

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("view.fxml"));
        Scene gameScene = new Scene(fxmlLoader.load(), 1200, 800);

        Controller gameController = fxmlLoader.getController();
        logger.debug("Game controller initialized.");

        gameController.setLanguage(selectedLanguage);
        logger.info("Language set in GameController: {}", selectedLanguage);

        Stage stage = (Stage) btnStart.getScene().getWindow();
        stage.setScene(gameScene);
        logger.info("Game scene set successfully.");
    }
}
