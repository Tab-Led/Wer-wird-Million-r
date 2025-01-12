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
    private Button a;
    @FXML
    private Button b;
    @FXML
    private Button c;
    @FXML
    private Button d;
    @FXML
    private TextArea question;

    private GameService gameService;
    private int randomIndex;
    Random random = new Random();

    @FXML
    public void initialize() {
        // todo by default is "en" -> GameState to change
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
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("start.fxml"));
            Scene startScene = new Scene(fxmlLoader.load(), 1200, 800);

            // Получаем текущее окно и устанавливаем стартовую страницу
            Stage stage = (Stage) exit.getScene().getWindow();
            stage.setScene(startScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onAButtonClick() throws NoSuchFieldException, IllegalAccessException {
        String answer = gameService.processAnswer(a.getText(), randomIndex);
        handleAnswerResult(answer);
    }

    @FXML
    protected void onBButtonClick() throws NoSuchFieldException, IllegalAccessException {
        String answer = gameService.processAnswer(b.getText(), randomIndex);
        handleAnswerResult(answer);
    }

    @FXML
    protected void onCButtonClick() throws NoSuchFieldException, IllegalAccessException {
        String answer = gameService.processAnswer(c.getText(), randomIndex);
        handleAnswerResult(answer);
    }

    @FXML
    protected void onDButtonClick() throws NoSuchFieldException, IllegalAccessException {
        String answer = gameService.processAnswer(d.getText(), randomIndex);
        handleAnswerResult(answer);
    }

    @FXML
    protected void on50ButtonClick() {
        // Проверяем, использована ли функция ранее
        if (gameService.getGameState().isFiftyUsed()) {
            showAlert("used", "used", "used");
            System.out.println("50:50 used!");
            return;
        }

        // Отмечаем, что 50:50 используется
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
        System.out.println("50:50 применено!");
    }


    @FXML
    protected void onJokerButtonClick() {
        if (gameService.getGameState().isJokerUsed()) {
            showAlert("used", "used", "Joker is already used");
            System.out.println("used!");
            return;
        }
        gameService.getGameState().setJokerUsed(true);
        String correctAnswer = gameService.getQuestions().get(randomIndex).getCorrectAnswer();
        a.setText(correctAnswer);
        b.setText(correctAnswer);
        c.setText(correctAnswer);
        d.setText(correctAnswer);

    }

    private void setQuestion(){
        System.out.println("gameService.getQuestions().size(): -> " + gameService.getQuestions().size());
        int size = gameService.getQuestions().size();
        randomIndex = random.nextInt(size);
        System.out.println("random index: " + randomIndex);
        // get random question
        if (!gameService.getQuestions().isEmpty()) {
            Question question = gameService.getQuestions().get(randomIndex);
            updateUIWithQuestion(question);
        } else {
            question.setText("No questions available!");
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
        switch (result) {
            case "win":
                // Показать экран победы
                System.out.println("Congratulations, you won!");
                // todo Make animation and sounds?
                setButtonsDisabled(true);
                highlightLevel(gameService.getGameState().getCurrentLevel());
                showAlert("WIN", "WIN", "Congratulations!!! You won 1.000.000€");
                break;
            case "lose":
                System.out.println("Game over!");
                // todo Make animation and sounds?
                setButtonsDisabled(true);
                showAlert("Lose", "Lose", "Sorry, you have lost надо тут сумму написать ");
                break;
            case "next":
                System.out.println("Next question...");
                highlightLevel(gameService.getGameState().getCurrentLevel());
                // todo Make animation and sounds?
                setQuestion();
                enableAllButtons(false);
                break;
        }
    }

    private void setButtonsDisabled(boolean disabled) {
        a.setDisable(disabled);
        b.setDisable(disabled);
        c.setDisable(disabled);
        d.setDisable(disabled);
        question.setDisable(disabled);
        fifty.setDisable(disabled);
        joker.setDisable(disabled);
        // convert
    }

    private void enableAllButtons(boolean enabled) {
        a.setDisable(enabled);
        b.setDisable(enabled);
        c.setDisable(enabled);
        d.setDisable(enabled);
        fifty.setDisable(gameService.getGameState().isFiftyUsed());
        joker.setDisable(gameService.getGameState().isJokerUsed());
    }

    public void setLanguage(String language) {
        gameService.getGameState().setLanguage(language); // Устанавливаем язык
        gameService.setQuestions(); // Перезагружаем вопросы
        setButtonsDisabled(false); // Делаем кнопки активными
        setQuestion(); // Загружаем первый вопрос
        System.out.println("Language set to: " + language);
    }

    // css for level
    public void highlightLevel(int level) throws NoSuchFieldException, IllegalAccessException {
        // remove color
        for (int i = 1; i <= 15; i++) {
            Label label = (Label) getClass().getDeclaredField("label_" + i).get(this);
            label.getStyleClass().remove("highlighted");
        }
        // add color
        Label currentLabel = (Label) getClass().getDeclaredField("label_" + level).get(this);
        currentLabel.getStyleClass().add("highlighted");
    }

    public void showAlert(String title, String textHeader, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(textHeader);
        alert.setContentText(text);
        alert.showAndWait();
    }
}