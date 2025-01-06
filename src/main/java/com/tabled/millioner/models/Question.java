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
}