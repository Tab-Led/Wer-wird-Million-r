package com.tabled.millioner.controller;

import com.tabled.millioner.MainApplication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;

public class StartController {

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
        // Устанавливаем логотип (замените "logo.png" на ваш файл)
        logo.setImage(new Image(Objects.requireNonNull(MainApplication.class.getResource("images/logo.png")).toString()));

        // Обработчики для выбора языка
        btnEnglish.setOnAction(event -> selectedLanguage = "en");
        btnDeutsch.setOnAction(event -> selectedLanguage = "de");

        // Обработчик для кнопки Start
        btnStart.setOnAction(event -> {
            try {
                startGame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void startGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("view.fxml"));
        Scene gameScene = new Scene(fxmlLoader.load(), 1200, 800);

        // Получаем контроллер игры
        com.tabled.millioner.controller.Controller gameController = fxmlLoader.getController();

        // Устанавливаем выбранный язык перед загрузкой вопросов
        gameController.setLanguage(selectedLanguage);

        Stage stage = (Stage) btnStart.getScene().getWindow();
        stage.setScene(gameScene);
    }
}
