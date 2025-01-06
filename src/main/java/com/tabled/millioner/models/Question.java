package com.tabled.millioner.models;

import java.util.List;

public class Question {
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