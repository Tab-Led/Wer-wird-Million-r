package com.tabled.millioner.controller;

import com.tabled.millioner.MainApplication;
import com.tabled.millioner.utils.WavPlayer;
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

import java.io.File;
import java.io.IOException;


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
    private Button btnEnglish;
    @FXML
    private Button btnDeutsch;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnExit;
    @FXML
    private Button btnRules;

    private Button selectedLanguageButton;
    private String selectedLanguage = "en";
    private static boolean mp3PlayOnes = false;

    @FXML
    public void initialize() {
        /**
         * Initializes the start screen controller.
         *
         * Plays the trailer (GIF and audio) and delays the display of the start screen for 23 seconds.
         */
        logger.info("Initializing StartController...");

        if (!mp3PlayOnes) {
            showGif();
            WavPlayer player = new WavPlayer();
            new Thread(() -> {
                try {
                    player.play("src/main/resources/com/tabled/millioner/media/audio/trailer.wav");
                } catch (Exception e) {
                    logger.error("Error playing audio: {}", e.getMessage(), e);
                }
            }).start();

            mp3PlayOnes = true;
        }
        enableButtons();
    }

    private void showGif() {
        /**
         * Displays a GIF animation in a new stage and closes it after 8 seconds.
         * After closing the GIF stage, loads the main application window.
         *
         * @param gifPath The path to the GIF file.
         */
        try {
            Image gif = new Image(new File(
                    "src/main/resources/com/tabled/millioner/images/trailer.gif").toURI().toString());
            ImageView gifView = new ImageView(gif);
            gifView.setPreserveRatio(true);
            gifView.setFitWidth(1200);

            StackPane root = new StackPane(gifView);
            Scene scene = new Scene(root, 1200, 800);

            Stage gifStage = new Stage();
            // gifStage.setX(400);
            // gifStage.setY(100);
            gifStage.setAlwaysOnTop(true);
            gifStage.setScene(scene);
            gifStage.setTitle("Trailer Animation");
            gifStage.show();
            logger.info("GIF displayed successfully.");

            // close window in 12.83 sec
            PauseTransition delay = new PauseTransition(Duration.seconds(12.83));
            delay.setOnFinished(event -> {
                gifStage.close();
                logger.info("GIF stage closed. Loading main window...");
            });
            delay.play();
        } catch (Exception e) {
            logger.error("Error displaying GIF: {}", e.getMessage(), e);
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

    @FXML
    protected void onEnglishButtonClick() {
        /**
         * Handles the "English" button click event.
         *
         * Sets the selected language to English and logs the selection.
         */
        setActiveLanguageButton(btnEnglish);
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
        setActiveLanguageButton(btnDeutsch);
        selectedLanguage = "de";
        logger.info("Language selected: Deutsch");
    }

    private void setActiveLanguageButton(Button button) {
        /**
         * Sets the active language button's style.
         *
         * This method highlights the selected language button by applying the "active" style class
         * and removes the "active" style class from all other language buttons.
         * It also updates the `selectedLanguageButton` field to keep track of the currently selected button.
         *
         * @param button The button that was clicked and should be highlighted as active.
         */
        btnEnglish.getStyleClass().remove("active");
        btnDeutsch.getStyleClass().remove("active");
        button.getStyleClass().add("active");
        selectedLanguageButton = button;
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
        WavPlayer player = new WavPlayer();
        player.play("src/main/resources/com/tabled/millioner/media/audio/start_3sek.wav");

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
