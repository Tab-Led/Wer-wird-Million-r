package com.tabled.millioner.controller;

import com.tabled.millioner.MainApplication;
import com.tabled.millioner.models.Question;
import com.tabled.millioner.services.GameService;
import com.tabled.millioner.utils.WavPlayer;
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
    private TextArea question;

    private GameService gameService;
    private int randomIndex;
    Random random = new Random();

    private static final Logger logger = LogManager.getLogger(Controller.class);

    /**
     * Initializes the game controller.
     * Called automatically after the FXML file is loaded. This method initializes the game service
     * and prepares the first question.
     */
    @FXML
    public void initialize() {
        logger.info("Initializing the Controller...");
        gameService = new GameService();
        setButtonsDisabled(false);
        setQuestion();
    }


    /**
     * @return the instance of the GameService.
     */
    public GameService getGameService() {
        return gameService;
    }

    /**
     * Handles the "Exit" button click event.
     * Returns the user to the start screen by loading the start.fxml layout
     * and replacing the current scene with the start screen.
     */
    @FXML
    protected void onExitButtonClick() {
        try {
            logger.info("Exit button clicked. Returning to the start screen.");
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("start.fxml"));
            Scene startScene = new Scene(fxmlLoader.load(), 1200, 800);

            Stage stage = (Stage) exit.getScene().getWindow();
            stage.setScene(startScene);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error while loading the start screen", e);
        }
    }

    /**
     * Handles the "A" button click event.
     * Passes the selected answer (Button A's text) to the game service for evaluation.
     *
     * @throws NoSuchFieldException   If the reflection process fails for a required field.
     * @throws IllegalAccessException If access to the specified field is denied.
     */
    @FXML
    protected void onAButtonClick() throws NoSuchFieldException, IllegalAccessException {
        logger.debug("Button A clicked with answer: {}", a.getText());
        handleAnswer(a.getText());
    }

    /**
     * Handles the "B" button click event.
     * Passes the selected answer (Button B's text) to the game service for evaluation.
     *
     * @throws NoSuchFieldException   If the reflection process fails for a required field.
     * @throws IllegalAccessException If access to the specified field is denied.
     */
    @FXML
    protected void onBButtonClick() throws NoSuchFieldException, IllegalAccessException {
        logger.debug("Button B clicked with answer: {}", b.getText());
        handleAnswer(b.getText());
    }

    /**
     * Handles the "C" button click event.
     * Passes the selected answer (Button C's text) to the game service for evaluation.
     *
     * @throws NoSuchFieldException   If the reflection process fails for a required field.
     * @throws IllegalAccessException If access to the specified field is denied.
     */
    @FXML
    protected void onCButtonClick() throws NoSuchFieldException, IllegalAccessException {
        logger.debug("Button C clicked with answer: {}", c.getText());
        handleAnswer(c.getText());
    }

    /**
     * Handles the "D" button click event.
     * Passes the selected answer (Button D's text) to the game service for evaluation.
     *
     * @throws NoSuchFieldException   If the reflection process fails for a required field.
     * @throws IllegalAccessException If access to the specified field is denied.
     */
    @FXML
    protected void onDButtonClick() throws NoSuchFieldException, IllegalAccessException {

        logger.debug("Button D clicked with answer: {}", d.getText());
        handleAnswer(d.getText());
    }

    /**
     * Activates the "50:50" lifeline.
     * This lifeline disables two incorrect answers on the answer buttons, leaving
     * only one incorrect answer and the correct answer active.
     * If the lifeline has already been used, displays an alert to inform the user.
     */
    @FXML
    protected void on50ButtonClick() {
        if (gameService.getGameState().isFiftyUsed()) {
            logger.warn("50:50 lifeline was already used.");
            showAlert("used", "used", "used");
            System.out.println("50:50 used!");
            return;
        }
        logger.info("Activating 50:50 lifeline.");
        gameService.getGameState().setFiftyUsed(true);
        String correctAnswer = gameService.getQuestions().get(randomIndex).getCorrectAnswer();
        List<Button> answerButtons = Arrays.asList(a, b, c, d);
        List<Button> wrongButtons = new ArrayList<>();

        for (Button button : answerButtons) {
            if (!button.getText().equals(correctAnswer)) {
                wrongButtons.add(button);
            }
        }
        Collections.shuffle(wrongButtons);
        for (int i = 0; i < 2; i++) {
            wrongButtons.get(i).setDisable(true);
        }
        logger.debug("50:50 activated. Disabled two incorrect answers.");
    }

    /**
     * Handles the "Joker" lifeline button click event.
     * This lifeline repeats the correct answer across all answer buttons (A, B, C, and D).
     * It ensures that the player cannot make an incorrect selection. If the Joker lifeline
     * has already been used, it displays an alert notifying the player.
     * Steps:
     * 1. Check if the Joker lifeline has already been used.
     * 2. If not used, activate the lifeline by:
     *    - Marking it as used in the game state.
     *    - Replacing the text on all answer buttons with the correct answer.
     * 3. Log all relevant actions and outcomes.
     */
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

    /**
     * Handles the "Second Chance" lifeline button click event.
     * This lifeline allows the player to retry if they select an incorrect answer.
     * Once activated, it marks the lifeline as used and enables the second chance feature.
     * If the lifeline has already been used, it displays an alert notifying the player.
     * Steps:
     * 1. Check if the Second Chance lifeline has already been used.
     * 2. If not used, activate the lifeline by:
     *    - Marking it as used in the game state.
     *    - Enabling the second chance feature in the game state.
     * 3. Notify the player with an alert that the lifeline is active.
     * 4. Log all relevant actions and outcomes.
     */
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

    /**
     * Handles the "Restart" button click event.
     * Resets the game state, reenables all buttons, and starts the game over with
     * the first question.
     */
    @FXML
    protected void onRestartButtonClick() {
        logger.info("Restart button clicked. Restarting the game.");
        gameService.resetGameState();
        setButtonsDisabled(false);
        setQuestion();
        enableAllButtons(false);
        logger.debug("Game successfully restarted.");
    }

    /**
     * Processes the selected answer.
     * Validates the selected answer, determines the game outcome, and handles special cases such as
     * the "Second Chance" lifeline if it is active.
     *
     * @param selectedAnswer The text of the selected answer button.
     * @throws NoSuchFieldException   If the reflection process fails for a required field.
     * @throws IllegalAccessException If access to the specified field is denied.
     */
    private void handleAnswer(String selectedAnswer) throws NoSuchFieldException, IllegalAccessException {
        logger.info("Processing answer: {}", selectedAnswer);
        String result = gameService.processAnswer(selectedAnswer, randomIndex);

        if (result.equals("tryAgain")) {
            logger.warn("First answer was incorrect, but Second Chance is active.");
            showAlert("Second Chance",
                    "Your first answer was incorrect!",
                    "But you have a second chance. Please choose another answer.");
            return;
        }
        handleAnswerResult(result);
    }

    /**
     * Sets a new random question for the game.
     * Selects a question from the available question pool at random, updates the UI with the
     * question and possible answers. If no questions are available, displays an error message.
     */
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

    /**
     * Updates the user interface with the provided question data.
     * Sets the question text and populates the answer buttons with the corresponding answers.
     *
     * @param questionData The question to display, including its text and possible answers.
     */
    private void updateUIWithQuestion(Question questionData) {
        question.setText(questionData.getText());
        a.setText(questionData.getAnswers().get(0));
        b.setText(questionData.getAnswers().get(1));
        c.setText(questionData.getAnswers().get(2));
        d.setText(questionData.getAnswers().get(3));
    }

    /**
     * Handles the result of an answered question.
     * Depending on the result, the game either proceeds to the next question, ends with a loss,
     * or ends with a win.
     *
     * @param result The result string indicating the game outcome ("win", "lose", "next").
     * @throws NoSuchFieldException   If the reflection process fails for a required field.
     * @throws IllegalAccessException If access to the specified field is denied.
     */
    private void handleAnswerResult(String result) throws NoSuchFieldException, IllegalAccessException {
        if (result.startsWith("lose")) {
            logger.error("Player lost the game.");

            setButtonsDisabled(true);

            WavPlayer player = new WavPlayer();
            player.play("src/main/resources/com/tabled/millioner/media/audio/lose.wav");

            int safeAmount = Integer.parseInt(result.split(":")[1]);
            logger.info("Displaying safe amount: {}€", safeAmount);

            showAlert("Lose", "Game Over", "Sorry, you lost. Your safe amount is " + safeAmount + "€.");

        } else {
            switch (result) {
                case "win":
                    logger.info("Player won the game!");

                    setButtonsDisabled(true);

                    WavPlayer player = new WavPlayer();
                    player.play("src/main/resources/com/tabled/millioner/media/audio/start1.wav");

                    highlightLevel(gameService.getGameState().getCurrentLevel());
                    showAlert("WIN", "WIN", "Congratulations!!! You won 1.000.000€");
                    break;
                case "next":
                    logger.info("Loading the next question.");
                    System.out.println("Next question...");
                    highlightLevel(gameService.getGameState().getCurrentLevel());
                    setQuestion();
                    enableAllButtons(false);
                    break;
                default:
                    logger.warn("Unknown result state: {}", result);
            }
        }
    }

    /**
     * Enables or disables all answer buttons and lifeline buttons.
     * This is used to lock or unlock the game interface during specific actions (e.g., after a question is answered).
     *
     * @param disabled A boolean flag indicating whether to disable (true) or enable (false) the buttons.
     */
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
    }

    /**
     * Resets the state of all answer and lifeline buttons.
     * Re-enables all buttons that are not marked as already used (e.g., lifelines like "50:50").
     *
     * @param enabled A boolean flag indicating whether to enable (false) or disable (true) the buttons.
     */
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

    /**
     * Sets the language for the game and reloads the questions.
     * Updates the game state with the selected language, reloads the question pool,
     * and resets the game interface for the new language.
     *
     * @param language The selected language as a string (e.g., "en" or "de").
     */
    public void setLanguage(String language) {
        gameService.getGameState().setLanguage(language);
        gameService.setQuestions();
        setButtonsDisabled(false);
        setQuestion();
        logger.info("Language set to: {}", language);
        logger.debug("Questions reloaded for language: {}", language);
        System.out.println("Language set to: " + language);
    }

    /**
     * Highlights the current level in the prize ladder.
     * Visually updates the prize ladder in the UI to show the player's current progress.
     * Removes highlighting from all other levels.
     *
     * @param level The current level number to highlight.
     * @throws NoSuchFieldException   If the reflection process fails for a required field.
     * @throws IllegalAccessException If access to the specified field is denied.
     */
    public void highlightLevel(int level) throws NoSuchFieldException, IllegalAccessException {
        for (int i = 1; i <= 15; i++) {
            Label label = (Label) getClass().getDeclaredField("label_" + i).get(this);
            label.getStyleClass().remove("highlighted");
            logger.debug("Highlighting level: {}", level);
        }
        // add color
        Label currentLabel = (Label) getClass().getDeclaredField("label_" + level).get(this);
        currentLabel.getStyleClass().add("highlighted");
    }

    /**
     * Displays an alert dialog with the specified title, header, and content.
     * Used to notify the user about important game events such as losing, winning,
     * or activating lifelines.
     *
     * @param title      The title of the alert dialog.
     * @param textHeader The header text of the alert dialog.
     * @param text       The content text of the alert dialog.
     */
    public void showAlert(String title, String textHeader, String text) {
        logger.debug("Showing alert with title: {}, header: {}, content: {}", title, textHeader, text);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(textHeader);
        alert.setContentText(text);
        alert.showAndWait();
    }
}