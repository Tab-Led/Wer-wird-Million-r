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

    private String selectedLanguage = "en"; // Язык по умолчанию

    @FXML
    public void initialize() {

        logger.info("Initializing StartController...");

        // Устанавливаем логотип (замените "logo.png" на ваш файл)
        logo.setImage(new Image(Objects.requireNonNull(MainApplication.class.getResource("images/logo.png")).toString()));
        logger.info("Logo successfully loaded.");

        // Обработчики для выбора языка
        btnEnglish.setOnAction(event -> selectedLanguage = "en");
        logger.info("Language selected: English");

        btnDeutsch.setOnAction(event -> selectedLanguage = "de");
        logger.info("Language selected: Deutsch");


        // Обработчик для кнопки Start
        btnStart.setOnAction(event -> {
            logger.info("Start button clicked.");
            try {
                startGame();
            } catch (IOException e) {
                logger.error("Failed to start game: {}", e.getMessage(), e);
            }
        });
    }

    private void startGame() throws IOException {
        logger.info("Starting the game with language: {}", selectedLanguage);

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("view.fxml"));
        Scene gameScene = new Scene(fxmlLoader.load(), 1200, 800);

        // Получаем контроллер игры
        com.tabled.millioner.controller.Controller gameController = fxmlLoader.getController();
        logger.debug("Game controller initialized.");

        // Устанавливаем выбранный язык перед загрузкой вопросов
        gameController.setLanguage(selectedLanguage);
        logger.info("Language set in GameController: {}", selectedLanguage);

        Stage stage = (Stage) btnStart.getScene().getWindow();
        stage.setScene(gameScene);
        logger.info("Game scene set successfully.");
    }
}
