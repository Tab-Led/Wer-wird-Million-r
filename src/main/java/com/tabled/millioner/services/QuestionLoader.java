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
    /**
     * Service for loading questions from a JSON file.
     *
     * The `QuestionLoader` class provides functionality to parse and load questions
     * from a structured JSON file based on specified language and difficulty level.
     * It transforms the JSON data into a list of `Question` objects for use in the game.
     *
     * Key Features:
     * - Loads questions from a JSON file.
     * - Filters questions by language and difficulty level.
     * - Handles parsing errors and logs detailed messages for debugging.
     *
     * Methods:
     * - `loadQuestionsByParams`: Loads and parses questions based on the given file path, language, and difficulty.
     *
     * Logging:
     * - Uses the Log4j library to provide detailed logs for the loading process, including successes and failures.
     *
     * Exceptions:
     * - Throws `IllegalArgumentException` if the specified language or difficulty is not found in the JSON file.
     * - Throws `RuntimeException` for general failures during file reading or parsing.
     */


    private static final Logger logger = LogManager.getLogger(QuestionLoader.class);

    public static List<Question> loadQuestionsByParams(String filePath, String language, String difficulty) {
        /**
         * Loads questions from a JSON file based on the specified language and difficulty level.
         *
         * This method parses a JSON file, extracts questions for the given language and difficulty,
         * and converts them into a list of `Question` objects. Each question includes its text,
         * possible answers, and the correct answer.
         *
         * @param filePath  The path to the JSON file containing the questions.
         * @param language  The language of the questions to load (e.g., "en", "de").
         * @param difficulty The difficulty level of the questions to load (e.g., "easy", "hard").
         * @return A list of `Question` objects for the specified language and difficulty level.
         * @throws IllegalArgumentException If the specified language or difficulty is not found in the JSON file.
         * @throws RuntimeException If an error occurs during file reading or parsing.
         *
         * Key Steps:
         * 1. Parse the JSON file.
         * 2. Navigate to the correct language and difficulty sections.
         * 3. Extract questions and their corresponding answers.
         * 4. Log the progress and handle any errors during loading.
         *
         * Example usage:
         * <pre>
         *     List<Question> questions = QuestionLoader.loadQuestionsByParams("questions.json", "en", "easy");
         * </pre>
         */
        List<Question> questions = new ArrayList<>();
        logger.info("Starting to load questions from file: {}", filePath);
        logger.info("Language: {}, Difficulty: {}", language, difficulty);

        try (FileReader reader = new FileReader(filePath)) {
            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
            logger.debug("Successfully parsed JSON file.");

            JsonObject languageSection = jsonObject.getAsJsonObject(language);
            if (languageSection == null) {
                logger.error("Language '{}' not found in the questions file.", language);
                throw new IllegalArgumentException("Language not found: " + language);
            }
            logger.debug("Language section '{}' found.", language);

            JsonObject difficultySection = languageSection.getAsJsonObject(difficulty);
            if (difficultySection == null) {
                logger.error("Difficulty '{}' not found in the language section '{}'.", difficulty, language);
                throw new IllegalArgumentException("Difficulty not found: " + difficulty);
            }
            logger.debug("Difficulty section '{}' found for language '{}'.", difficulty, language);

            for (String key : difficultySection.keySet()) {
                JsonObject questionObj = difficultySection.getAsJsonObject(key);

                String questionText = questionObj.get("question").getAsString();
                List<String> options = new ArrayList<>();
                for (JsonElement option : questionObj.getAsJsonArray("options")) {
                    options.add(option.getAsString());
                }
                String answer = questionObj.get("answer").getAsString();

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