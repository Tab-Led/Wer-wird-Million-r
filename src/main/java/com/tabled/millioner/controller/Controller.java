package com.tabled.millioner.controller;

import com.tabled.millioner.MainApplication;
import com.tabled.millioner.models.Question;
import com.tabled.millioner.services.GameService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.util.*;


public class Controller {
    @FXML
    public Label label_15;
    @FXML
    public Label label_14;
    @FXML
    public Label label_13;
    @FXML
    public Label label_12;
    @FXML
    public Label label_11;
    @FXML
    public Label label_10;
    @FXML
    public Label label_9;
    @FXML
    public Label label_8;
    @FXML
    public Label label_7;
    @FXML
    public Label label_6;
    @FXML
    public Label label_5;
    @FXML
    public Label label_4;
    @FXML
    public Label label_3;
    @FXML
    public Label label_2;
    @FXML
    public Label label_1;

    @FXML
    private Button exit;
    @FXML
    public Button fifty;
    @FXML
    public Button joker;
    @FXML
    public Button secondChance;
    @FXML
    private Button a;
    @FXML
    private Button b;
    @FXML
    private Button c;
    @FXML
    private Button d;
    @FXML
    private Button btnRestart;
    @FXML
    private TextArea question;

    private GameService gameService;
    private int randomIndex;
    Random random = new Random();

    private static final Logger logger = LogManager.getLogger(Controller.class);


    @FXML
    public void initialize() {
        // todo by default is "en" -> GameState to change
        logger.info("Initializing the Controller...");
        gameService = new GameService();
        setButtonsDisabled(false);
        setQuestion();
    }

    public GameService getGameService() {
        return gameService;
    }

    @FXML
    protected void onExitButtonClick() {
        try {
            // Загружаем стартовую страницу
            logger.info("Exit button clicked. Returning to the start screen.");
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("start.fxml"));
            Scene startScene = new Scene(fxmlLoader.load(), 1200, 800);

            // Получаем текущее окно и устанавливаем стартовую страницу
            Stage stage = (Stage) exit.getScene().getWindow();
            stage.setScene(startScene);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error while loading the start screen", e);
        }
    }

    @FXML
    protected void onAButtonClick() throws NoSuchFieldException, IllegalAccessException {
        logger.debug("Button A clicked with answer: {}", a.getText());
        handleAnswer(a.getText());
    }

    @FXML
    protected void onBButtonClick() throws NoSuchFieldException, IllegalAccessException {
        logger.debug("Button B clicked with answer: {}", b.getText());
        handleAnswer(b.getText());
    }

    @FXML
    protected void onCButtonClick() throws NoSuchFieldException, IllegalAccessException {
        logger.debug("Button C clicked with answer: {}", c.getText());
        handleAnswer(c.getText());
    }

    @FXML
    protected void onDButtonClick() throws NoSuchFieldException, IllegalAccessException {
        logger.debug("Button D clicked with answer: {}", d.getText());
        handleAnswer(d.getText());
    }

    @FXML
    protected void on50ButtonClick() {
        // Проверяем, использована ли функция ранее
        if (gameService.getGameState().isFiftyUsed()) {
            logger.warn("50:50 lifeline was already used.");
            showAlert("used", "used", "used");
            System.out.println("50:50 used!");
            return;
        }

        // Отмечаем, что 50:50 используется
        logger.info("Activating 50:50 lifeline.");
        gameService.getGameState().setFiftyUsed(true);

        // Получаем правильный ответ для текущего вопроса
        String correctAnswer = gameService.getQuestions().get(randomIndex).getCorrectAnswer();

        // Собираем кнопки с ответами
        List<Button> answerButtons = Arrays.asList(a, b, c, d);

        // Находим неправильные кнопки
        List<Button> wrongButtons = new ArrayList<>();
        for (Button button : answerButtons) {
            if (!button.getText().equals(correctAnswer)) {
                wrongButtons.add(button);
            }
        }
        // Удаляем две случайные неправильные кнопки
        Collections.shuffle(wrongButtons);
        for (int i = 0; i < 2; i++) {
            wrongButtons.get(i).setDisable(true); // Делаем кнопку неактивной
        }
        logger.debug("50:50 activated. Disabled two incorrect answers.");
        System.out.println("50:50 was activated!");
    }


    @FXML
    protected void onJokerButtonClick() {
        if (gameService.getGameState().isJokerUsed()) {
            logger.warn("Joker lifeline was already used.");
            showAlert("used", "used", "Joker is already used");
            System.out.println("used!");
            return;
        }

        logger.info("Activating Joker lifeline.");

        gameService.getGameState().setJokerUsed(true);
        String correctAnswer = gameService.getQuestions().get(randomIndex).getCorrectAnswer();
        logger.debug("Correct answer for Joker lifeline: {}", correctAnswer);
        a.setText(correctAnswer);
        b.setText(correctAnswer);
        c.setText(correctAnswer);
        d.setText(correctAnswer);

    }


    @FXML
    protected void onSecondChanceButtonClick() {
        if (gameService.getGameState().isSecondChanceUsed()) {
            logger.warn("Second Chance lifeline was already used.");
            showAlert("Second Chance Used", "You have already used the Second Chance lifeline.", "");
            return;
        }

        logger.info("Activating Second Chance lifeline.");
        gameService.getGameState().setSecondChanceUsed(true);
        gameService.getGameState().setSecondChanceActive(true);
        showAlert("Second Chance Activated", "You now have a second chance if you answer incorrectly!", "");
        logger.info("Second Chance lifeline activated.");
    }

    @FXML
    protected void onRestartButtonClick() {
        logger.info("Restart button clicked. Restarting the game.");

        // Сброс состояния игры
        gameService.resetGameState();
        setButtonsDisabled(false); // Включаем кнопки
        setQuestion(); // Устанавливаем первый вопрос
        enableAllButtons(false); // Убедитесь, что кнопки активны

        logger.debug("Game successfully restarted.");
    }

    private void handleAnswer(String selectedAnswer) throws NoSuchFieldException, IllegalAccessException {
        logger.info("Processing answer: {}", selectedAnswer);
        String result = gameService.processAnswer(selectedAnswer, randomIndex);

        // Если игрок дал неправильный ответ, но Second Chance активен
        if (result.equals("tryAgain")) {
            logger.warn("First answer was incorrect, but Second Chance is active.");
            showAlert("Second Chance",
                    "Your first answer was incorrect!",
                    "But you have a second chance. Please choose another answer.");
            return; // Завершаем обработку, чтобы дать игроку возможность ответить снова
        }

        // Обрабатываем другие результаты (win, lose, next)
        handleAnswerResult(result);
    }


    private void setQuestion(){
        logger.info("Setting a new question.");
        System.out.println("gameService.getQuestions().size(): -> " + gameService.getQuestions().size());
        int size = gameService.getQuestions().size();
        randomIndex = random.nextInt(size);
        logger.debug("Selected random index for question: {}", randomIndex);
        System.out.println("random index: " + randomIndex);
        // get random question
        if (!gameService.getQuestions().isEmpty()) {
            Question question = gameService.getQuestions().get(randomIndex);
            updateUIWithQuestion(question);
        } else {
            question.setText("No questions available!");
            logger.error("No questions available to display!");
        }
    }

    private void updateUIWithQuestion(Question questionData) {
        question.setText(questionData.getText());
        a.setText(questionData.getAnswers().get(0));
        b.setText(questionData.getAnswers().get(1));
        c.setText(questionData.getAnswers().get(2));
        d.setText(questionData.getAnswers().get(3));
    }

    private void handleAnswerResult(String result) throws NoSuchFieldException, IllegalAccessException {
        if (result.startsWith("lose")) {
            logger.error("Player lost the game.");
            setButtonsDisabled(true);

            // Получаем сохраненное значение safeAmount
            int safeAmount = Integer.parseInt(result.split(":")[1]);
            logger.info("Displaying safe amount: {}€", safeAmount);

            // Отображаем всплывающее окно с информацией
            showAlert("Lose", "Game Over", "Sorry, you lost. Your safe amount is " + safeAmount + "€.");

        } else {
            switch (result) {
                case "win":
                    // Показать экран победы
                    logger.info("Player won the game!");
                    System.out.println("Congratulations, you won!");
                    // todo Make animation and sounds?
                    setButtonsDisabled(true);
                    highlightLevel(gameService.getGameState().getCurrentLevel());
                    showAlert("WIN", "WIN", "Congratulations!!! You won 1.000.000€");
                    break;
                case "next":
                    logger.info("Loading the next question.");
                    System.out.println("Next question...");
                    highlightLevel(gameService.getGameState().getCurrentLevel());
                    // todo Make animation and sounds?
                    setQuestion();
                    enableAllButtons(false);
                    break;
                default:
                    logger.warn("Unknown result state: {}", result);
            }
        }
    }

    private void setButtonsDisabled(boolean disabled) {
        logger.debug("Buttons state changed: disabled={}", disabled);
        a.setDisable(disabled);
        b.setDisable(disabled);
        c.setDisable(disabled);
        d.setDisable(disabled);
        question.setDisable(disabled);
        fifty.setDisable(disabled);
        joker.setDisable(disabled);
        secondChance.setDisable(disabled);
        // convert
    }

    private void enableAllButtons(boolean enabled) {
        a.setDisable(enabled);
        b.setDisable(enabled);
        c.setDisable(enabled);
        d.setDisable(enabled);
        fifty.setDisable(gameService.getGameState().isFiftyUsed());
        joker.setDisable(gameService.getGameState().isJokerUsed());
        secondChance.setDisable(gameService.getGameState().isSecondChanceUsed());
        logger.debug("All buttons enabled={}. Fifty used={}, Joker used={}, Second Chance used={}",
                !enabled,
                gameService.getGameState().isFiftyUsed(),
                gameService.getGameState().isJokerUsed(),
                gameService.getGameState().isSecondChanceUsed());
    }

    public void setLanguage(String language) {
        gameService.getGameState().setLanguage(language); // Устанавливаем язык
        gameService.setQuestions(); // Перезагружаем вопросы
        setButtonsDisabled(false); // Делаем кнопки активными
        setQuestion(); // Загружаем первый вопрос
        logger.info("Language set to: {}", language);
        logger.debug("Questions reloaded for language: {}", language);
        System.out.println("Language set to: " + language);
    }

    private void updateSafeAmountUI() throws NoSuchFieldException, IllegalAccessException {
        int safeAmount = gameService.getGameState().getSafeAmount();
        for (int i = 1; i <= 15; i++) {
            Label label = (Label) getClass().getDeclaredField("label_" + i).get(this);
            if (i == 5 || i == 10) {
                label.getStyleClass().add("safe-amount"); // Добавить CSS-стиль для выделения
            } else {
                label.getStyleClass().remove("safe-amount"); // Удалить стиль, если он есть
            }
        }
        logger.debug("Safe amount UI updated.");
    }

    // css for level
    public void highlightLevel(int level) throws NoSuchFieldException, IllegalAccessException {
        // remove color
        for (int i = 1; i <= 15; i++) {
            Label label = (Label) getClass().getDeclaredField("label_" + i).get(this);
            label.getStyleClass().remove("highlighted");
            logger.debug("Highlighting level: {}", level);
        }
        // add color
        Label currentLabel = (Label) getClass().getDeclaredField("label_" + level).get(this);
        currentLabel.getStyleClass().add("highlighted");
    }

    public void showAlert(String title, String textHeader, String text) {
        logger.debug("Showing alert with title: {}, header: {}, content: {}", title, textHeader, text);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(textHeader);
        alert.setContentText(text);
        alert.showAndWait();
    }
}