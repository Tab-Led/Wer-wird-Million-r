package com.tabled.millioner.controller;

import com.tabled.millioner.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class StartController {

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
    public void initialize() {

        logger.info("Initializing StartController...");

        // Set up the logo image
        setLogoImage();

        // Enable buttons
        enableButtons();
    }

    private void setLogoImage() {
        try {
            logo.setImage(new Image(Objects.requireNonNull(MainApplication.class.getResource("images/logo.png")).toString()));
            logger.info("Logo successfully loaded.");
        } catch (NullPointerException e) {
            logger.error("Failed to load logo image: {}", e.getMessage());
        }
    }

    private void enableButtons() {
        btnEnglish.setDisable(false);
        btnDeutsch.setDisable(false);
        btnStart.setDisable(false);
        btnExit.setDisable(false);
        btnRules.setDisable(false);
    }

    // Button handlers

    @FXML
    protected void onEnglishButtonClick() {
        selectedLanguage = "en";
        logger.info("Language selected: English");
    }

    @FXML
    protected void onDeutschButtonClick() {
        selectedLanguage = "de";
        logger.info("Language selected: Deutsch");
    }

    @FXML
    protected void onStartButtonClick() {
        logger.info("Start button clicked.");
        try {
            startGame();
        } catch (IOException e) {
            logger.error("Failed to start game: {}", e.getMessage(), e);
        }
    }

    @FXML
    protected void onExitButtonClick() {
        logger.info("Exit button clicked. Exiting application.");
        System.exit(0);
    }

    @FXML
    protected void onRulesButtonClick() {
        logger.info("Rules button clicked. Displaying game rules.");

        // Создание всплывающего окна
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

        // Показать окно
        rulesAlert.showAndWait();
        logger.info("Rules displayed successfully.");
    }

    private void startGame() throws IOException {
        logger.info("Starting the game with language: {}", selectedLanguage);

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("view.fxml"));
        Scene gameScene = new Scene(fxmlLoader.load(), 1200, 800);

        // Получаем контроллер игры
        Controller gameController = fxmlLoader.getController();
        logger.debug("Game controller initialized.");

        // Устанавливаем выбранный язык перед загрузкой вопросов
        gameController.setLanguage(selectedLanguage);
        logger.info("Language set in GameController: {}", selectedLanguage);

        Stage stage = (Stage) btnStart.getScene().getWindow();
        stage.setScene(gameScene);
        logger.info("Game scene set successfully.");
    }
}
