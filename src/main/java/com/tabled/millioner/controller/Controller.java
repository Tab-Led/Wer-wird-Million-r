package com.tabled.millioner.controller;

import com.tabled.millioner.models.Question;
import com.tabled.millioner.services.GameService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.*;

public class Controller {
    @FXML
    public Label label_15;
    @FXML
    public Label label_14;
    @FXML
    public Label label_13;
    @FXML
    public Label label_12;
    @FXML
    public Label label_11;
    @FXML
    public Label label_10;
    @FXML
    public Label label_9;
    @FXML
    public Label label_8;
    @FXML
    public Label label_7;
    @FXML
    public Label label_6;
    @FXML
    public Label label_5;
    @FXML
    public Label label_4;
    @FXML
    public Label label_3;
    @FXML
    public Label label_2;
    @FXML
    public Label label_1;

    @FXML
    private Button start;
    @FXML
    public Button fifty;
    @FXML
    public Button joker;
    @FXML
    private Button language;
    @FXML
    private Button a;
    @FXML
    private Button b;
    @FXML
    private Button c;
    @FXML
    private Button d;
    @FXML
    private TextArea question;

    private GameService gameService;
    private int randomIndex;
    Random random = new Random();

    @FXML
    public void initialize() {
        // todo by default is "en" -> GameState to change
        gameService = new GameService();
        setButtonsDisabled(true);

        question.setText(
                "Herzlich Willkommen zu unserer Version von 'Wer wird Millionär?!'\n" +
                        "Liebe Teilnehmerinnen und Teilnehmer, heute seid ihr die Stars dieses spannenden Quizspiels.\n" +
                        "Stellt euer Wissen, eure Intuition und euren Mut unter Beweis und kämpft euch durch die 15 Fragen, um den ultimativen Hauptgewinn von 1 Million Euro zu erreichen!\n\n" +
                        "Vergesst nicht:\n" +
                        "- Jede richtige Antwort bringt euch eine Stufe höher.\n" +
                        "- Ihr habt zwei Joker: 50:50 und den 100% richtige Antwort Joker. Setzt sie klug ein!\n" +
                        "Seid ihr bereit, in die heiße Quizrunde einzutauchen und euch der Herausforderung zu stellen?\n" +
                        "Dann lasst uns keine Zeit verlieren und starten wir das Spiel.\n" +
                        "Viel Erfolg, viel Spaß und möge der Beste gewinnen!"
        );

    }
}