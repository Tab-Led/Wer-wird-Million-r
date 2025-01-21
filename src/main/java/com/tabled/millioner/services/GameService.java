package com.tabled.millioner.services;

import com.tabled.millioner.models.GameState;
import com.tabled.millioner.models.Question;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.List;


public class GameService {
    /**
     * Service for managing the game logic in "Who Wants to Be a Millionaire".
     *
     * The `GameService` class handles the core mechanics of the game, including:
     * - Loading questions from a JSON file.
     * - Validating answers and updating the game state.
     * - Managing difficulty levels and safe amounts.
     * - Handling lifelines like "Second Chance".
     *
     * Key Features:
     * - Tracks the player's progress and updates the current level.
     * - Dynamically adjusts the difficulty of questions based on the player's level.
     * - Provides methods for resetting the game and reloading questions.
     *
     * Dependencies:
     * - `QuestionLoader` for loading questions.
     * - `GameState` for managing game progress and state.
     *
     * Logging:
     * - Uses Log4j for detailed logging of game events and errors.
     */
    private static final Logger logger = LogManager.getLogger(GameService.class);
    private List<Question> questions;
    private GameState gameState;
    private String lvl = "easy";
    private final String filePath;


    public GameService() {
        /**
         * Initializes the `GameService` and loads the questions for the game.
         *
         * This constructor locates the questions file, sets the initial game state,
         * and loads the questions based on the default language and difficulty level.
         *
         * @throws RuntimeException If the questions file cannot be located or loaded.
         */
        try {
            URL resource = getClass().getResource("/com/tabled/millioner/data/questions.json");
            if (resource == null) {
                logger.error("File not found: /com/tabled/millioner/data/questions.json");
                throw new IllegalArgumentException("File not found: /com/tabled/millioner/data/questions.json");
            }
            filePath = new File(resource.toURI()).getAbsolutePath();
            logger.info("Questions file path: {}", filePath);
        } catch (Exception e) {
            logger.error("Failed to load questions.json file", e);
            throw new RuntimeException("Failed to load questions.json file", e);
        }
        this.gameState = new GameState();
        this.questions = QuestionLoader.loadQuestionsByParams(filePath, gameState.getLanguage(), lvl);
        logger.info("Questions loaded for language: {}", gameState.getLanguage());
        System.out.println("Questions loaded for language: " + gameState.getLanguage());
    }

    public String processAnswer(String answer, int index) {
        /**
         * Processes the player's answer to a question.
         *
         * Validates the given answer, updates the game state based on correctness,
         * manages lifeline usage, and determines whether the game proceeds, ends in a win,
         * or ends with the player's loss.
         *
         * @param answer The player's selected answer.
         * @param index The index of the current question in the list.
         * @return A result string indicating the outcome ("win", "next", "lose:{safeAmount}", or "tryAgain").
         */
        boolean isCorrect = checkAnswer(answer, index);
        if (isCorrect) {
            logger.info("Correct answer for question index: {}", index);
            System.out.println("Correct answer!");
            gameState.setCurrentLevel(gameState.getCurrentLevel() + 1);
            logger.info("Current level updated to: {}", gameState.getCurrentLevel());

            if (gameState.getCurrentLevel() == 6) {
                gameState.setSafeAmount(500);
                logger.info("Safe amount updated to 500€ at level 6.");
            } else if (gameState.getCurrentLevel() == 11) {
                gameState.setSafeAmount(16000);
                logger.info("Safe amount updated to 16,000€ at level 11.");
            }

            if (gameState.getCurrentLevel() > 14) {
                logger.info("Player won the game!");
                this.gameState = new GameState();
                return "win";
            } else {
                questions.remove(index);
                setLvl();
                if (gameState.getCurrentLevel() == 5){
                    setQuestions();
                } else if (gameState.getCurrentLevel() == 11) {
                    setQuestions();
                }
                gameState.setSecondChanceActive(false);
                return "next";
            }

        } else {
            if (gameState.isSecondChanceActive()) {
                gameState.setSecondChanceActive(false);
                gameState.setSecondChanceUsed(true);
                return "tryAgain";
            }

            int safeAmount = gameState.getSafeAmount();
            logger.info("Player lost. Safe amount: {}€", safeAmount);
            this.gameState = new GameState();
            setLvl();
            return "lose:" + safeAmount;
        }
    }

    public boolean checkAnswer(String answer, int index) {
        /**
         * Validates the player's answer.
         *
         * Compares the player's selected answer with the correct answer for the specified question.
         *
         * @param answer The player's selected answer.
         * @param index The index of the current question in the list.
         * @return `true` if the answer is correct, `false` otherwise.
         */
        return answer.equals(questions.get(index).getCorrectAnswer());
    }

    public List<Question> getQuestions() {
        /**
         * Retrieves the current list of questions.
         *
         * @return A list of `Question` objects currently available in the game.
         */
        logger.debug("Fetching questions list. Total questions: {}", questions.size());
        return questions;
    }

    public void setQuestions() {
        /**
         * Reloads the questions for the game based on the current language and difficulty level.
         *
         * This method dynamically adjusts the question pool based on the player's progress.
         */
        setLvl();
        this.questions = QuestionLoader.loadQuestionsByParams(filePath, gameState.getLanguage(), lvl);
        logger.info("Questions reloaded for language: {}, difficulty: {}", gameState.getLanguage(), lvl);
        logger.info("Number of questions loaded: {}", questions.size());
        System.out.println("Questions reloaded for language: " + gameState.getLanguage() + ", difficulty: " + lvl);
        System.out.println("Number of questions loaded: " + questions.size());
    }

    public GameState getGameState() {
        /**
         * Reloads the questions for the game based on the current language and difficulty level.
         *
         * This method dynamically adjusts the question pool based on the player's progress.
         */
        logger.debug("Fetching current game state.");
        return gameState;
    }

    public void setLvl() {
        /**
         * Adjusts the difficulty level based on the player's current progress.
         *
         * Difficulty levels:
         * - "easy": Levels 1–5.
         * - "medium": Levels 6–10.
         * - "hard": Levels 11–15.
         */
        if(gameState.getCurrentLevel() < 5) lvl = "easy";
        else if (gameState.getCurrentLevel() > 10) lvl = "hard";
        else lvl = "medium";logger.info("Difficulty level set to: {}", lvl);
    }

    public void resetGameState() {
        /**
         * Resets the game state to its initial values.
         *
         * This method clears the current game progress, creates a new `GameState` instance,
         * and reloads the question pool.
         */
        this.gameState = new GameState(); // Создаем новое состояние игры
        setQuestions(); // Загружаем вопросы заново
    }

}
