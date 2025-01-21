package com.tabled.millioner.services;

import com.tabled.millioner.models.GameState;
import com.tabled.millioner.models.Question;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.List;


public class GameService {
    private static final Logger logger = LogManager.getLogger(GameService.class);
    private List<Question> questions;
    private GameState gameState;
    private String lvl = "easy";
    private final String filePath;


    public GameService() {
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
            // Проверяем, активирована ли подсказка "Second Chance"
            if (gameState.isSecondChanceActive()) {
                gameState.setSecondChanceActive(false); // Сбрасываем активное состояние подсказки
                gameState.setSecondChanceUsed(true);    // Отмечаем, что подсказка использована
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
        return answer.equals(questions.get(index).getCorrectAnswer());
    }

    public List<Question> getQuestions() {
        logger.debug("Fetching questions list. Total questions: {}", questions.size());
        return questions;
    }

    public void setQuestions() {
        setLvl();
        this.questions = QuestionLoader.loadQuestionsByParams(filePath, gameState.getLanguage(), lvl);
        logger.info("Questions reloaded for language: {}, difficulty: {}", gameState.getLanguage(), lvl);
        logger.info("Number of questions loaded: {}", questions.size());
        System.out.println("Questions reloaded for language: " + gameState.getLanguage() + ", difficulty: " + lvl);
        System.out.println("Number of questions loaded: " + questions.size());
    }

    public GameState getGameState() {
        logger.debug("Fetching current game state.");
        return gameState;
    }

    public void setLvl() {
        if(gameState.getCurrentLevel() < 5) lvl = "easy";
        else if (gameState.getCurrentLevel() > 10) lvl = "hard";
        else lvl = "medium";logger.info("Difficulty level set to: {}", lvl);
    }

    public void resetGameState() {
        this.gameState = new GameState(); // Создаем новое состояние игры
        setQuestions(); // Загружаем вопросы заново
    }

}
