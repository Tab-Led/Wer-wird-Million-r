package com.tabled.millioner.controller;

import com.tabled.millioner.MainApplication;
import javafx.fxml.FXML;
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
