package com.tabled.millioner.models;

import java.util.List;

public class Question {
    /**
     * Represents a question in the "Who Wants to Be a Millionaire" game.
     *
     * This class defines the structure of a question, including its associated language, difficulty level,
     * question text, possible answers, and the correct answer. It is used to display questions to the player
     * and validate their answers.
     *
     * Key Features:
     * - Stores the language of the question (`sprache`).
     * - Maintains the difficulty level of the question (`level`).
     * - Contains the question text and a list of possible answers.
     * - Tracks the correct answer for validation purposes.
     *
     * Constructors:
     * - Initializes a question with language, level, text, answers, and the correct answer.
     *
     * Getters and Setters:
     * - Accessors and mutators for all fields, allowing dynamic modification of question properties.
     */
    private String sprache;
    private String level;

    private String text;
    private List<String> answers;
    private String correctAnswer;

    public Question(String sprache, String level, String text, List<String> answers, String correctAnswer) {
        this.sprache = sprache;
        this.level = level;
        this.text = text;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    public String getSprache() {
        return sprache;
    }

    public void setSprache(String sprache) {
        this.sprache = sprache;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}