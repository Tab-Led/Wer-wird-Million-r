package com.tabled.millioner.services;

import com.tabled.millioner.models.Question;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class QuestionLoader {

    public static List<Question> loadQuestionsByParams(String filePath, String language, String difficulty) {
        List<Question> questions = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath)) {
            // Парсим JSON файл
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

            // Получаем секцию для указанного языка
            JsonObject languageSection = jsonObject.getAsJsonObject(language);
            if (languageSection == null) {
                throw new IllegalArgumentException("Language not found: " + language);
            }
            // Получаем секцию для указанной сложности
            JsonObject difficultySection = languageSection.getAsJsonObject(difficulty);
            if (difficultySection == null) {
                throw new IllegalArgumentException("Difficulty not found: " + difficulty);
            }

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
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load questions from file: " + filePath, e);
        }

        return questions;
    }
};