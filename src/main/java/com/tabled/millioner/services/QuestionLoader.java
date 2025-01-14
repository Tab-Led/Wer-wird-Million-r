package com.tabled.millioner.services;

import com.tabled.millioner.models.Question;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class QuestionLoader {

    private static final Logger logger = LogManager.getLogger(QuestionLoader.class);

    public static List<Question> loadQuestionsByParams(String filePath, String language, String difficulty) {
        List<Question> questions = new ArrayList<>();
        logger.info("Starting to load questions from file: {}", filePath);
        logger.info("Language: {}, Difficulty: {}", language, difficulty);

        try (FileReader reader = new FileReader(filePath)) {
            // Парсим JSON файл
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            logger.debug("Successfully parsed JSON file.");

            // Получаем секцию для указанного языка
            JsonObject languageSection = jsonObject.getAsJsonObject(language);
            if (languageSection == null) {
                logger.error("Language '{}' not found in the questions file.", language);
                throw new IllegalArgumentException("Language not found: " + language);
            }
            logger.debug("Language section '{}' found.", language);

            // Получаем секцию для указанной сложности
            JsonObject difficultySection = languageSection.getAsJsonObject(difficulty);
            if (difficultySection == null) {
                logger.error("Difficulty '{}' not found in the language section '{}'.", difficulty, language);
                throw new IllegalArgumentException("Difficulty not found: " + difficulty);
            }
            logger.debug("Difficulty section '{}' found for language '{}'.", difficulty, language);

            // Преобразуем вопросы в объекты
            for (String key : difficultySection.keySet()) {
                JsonObject questionObj = difficultySection.getAsJsonObject(key);

                String questionText = questionObj.get("question").getAsString();
                List<String> options = new ArrayList<>();
                for (JsonElement option : questionObj.getAsJsonArray("options")) {
                    options.add(option.getAsString());
                }
                String answer = questionObj.get("answer").getAsString();

                // Создаём объект Question
                questions.add(new Question(language, difficulty, questionText, options, answer));
                logger.debug("Loaded question: {}", questionText);
            }
            logger.info("Successfully loaded {} questions for language '{}' and difficulty '{}'.", questions.size(), language, difficulty);
        } catch (Exception e) {
            logger.error("Failed to load questions from file: {}", filePath, e);
            throw new RuntimeException("Failed to load questions from file: " + filePath, e);
        }

        return questions;
    }
};