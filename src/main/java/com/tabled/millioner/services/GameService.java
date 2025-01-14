package com.tabled.millioner.services;

import com.tabled.millioner.models.GameState;
import com.tabled.millioner.models.Question;

import java.io.File;
import java.net.URL;
import java.util.List;


public class GameService {
    private List<Question> questions;
    private GameState gameState;
    private String lvl = "easy";
    private final String filePath;


    public GameService() {
        try {
            URL resource = getClass().getResource("/com/tabled/millioner/data/questions.json");
            if (resource == null) {
                throw new IllegalArgumentException("File not found: /com/tabled/millioner/data/questions.json");
            }
            filePath = new File(resource.toURI()).getAbsolutePath();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load questions.json file", e);
        }
        this.gameState = new GameState();
        this.questions = QuestionLoader.loadQuestionsByParams(filePath, gameState.getLanguage(), lvl);
        System.out.println("Questions loaded for language: " + gameState.getLanguage());
    }

    public String processAnswer(String answer, int index) {
        boolean isCorrect = checkAnswer(answer, index);
        if (isCorrect) {
            System.out.println("Correct answer!");
            gameState.setCurrentLevel(gameState.getCurrentLevel() + 1);

            if (gameState.getCurrentLevel() > 14) {
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
            // Если активирован Second Chance
            if (gameState.isSecondChanceActive()) {
                System.out.println("Second Chance in use!");
                gameState.setSecondChanceActive(false); // Сбрасываем активное состояние
                gameState.setSecondChanceUsed(true);    // Отмечаем, что шанс использован
                return "tryAgain";
            }

            // Если Second Chance ещё доступен, активируем его
            if (!gameState.isSecondChanceUsed()) {
                System.out.println("Activating Second Chance...");
                gameState.setSecondChanceActive(true); // Активируем второй шанс
                return "tryAgain";
            }

            // Если Second Chance уже использован, игрок проигрывает
            System.out.println("Game over!");
            this.gameState = new GameState(); // Перезапуск состояния игры
            setLvl();
            return "lose";
        }
    }

    public boolean checkAnswer(String answer, int index) {
        return answer.equals(questions.get(index).getCorrectAnswer());
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions() {
        setLvl();
        this.questions = QuestionLoader.loadQuestionsByParams(filePath, gameState.getLanguage(), lvl);
        System.out.println("Questions reloaded for language: " + gameState.getLanguage() + ", difficulty: " + lvl);
        System.out.println("Number of questions loaded: " + questions.size());
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setLvl() {
        if(gameState.getCurrentLevel() < 5) lvl = "easy";
        else if (gameState.getCurrentLevel() > 10) lvl = "hard";
        else lvl = "medium";
    }

}
