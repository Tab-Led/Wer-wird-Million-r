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
    }
}