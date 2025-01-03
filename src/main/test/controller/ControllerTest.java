package controller;

import com.tabled.millioner.controller.Controller;
import com.tabled.millioner.models.Question;
import com.tabled.millioner.services.GameService;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControllerTest {

    @Mock
    private GameService gameService;

    @Mock
    private Label label_1, label_2, label_3, label_4, label_5, label_6, label_7, label_8, label_9, label_10, label_11, label_12, label_13, label_14, label_15;

    @Mock
    private Button a, b, c, d, fifty, joker, start;

    @Mock
    private TextArea question;

    @InjectMocks
    private Controller controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void initialize_setsWelcomeText() {
        controller.initialize();
        verify(question).setText(contains("Herzlich Willkommen"));
        verify(fifty).setDisable(true);
        verify(joker).setDisable(true);
    }

    @Test
    void onStartButtonClick_initializesQuestion() throws Exception {
        Question mockQuestion = new Question("Sample question", Arrays.asList("A", "B", "C", "D"), "A");
        when(gameService.getQuestions()).thenReturn(Collections.singletonList(mockQuestion));

        controller.onStartButtonClick();

        verify(question).setText(mockQuestion.getText());
        verify(a).setText("A");
        verify(b).setText("B");
        verify(c).setText("C");
        verify(d).setText("D");
        verify(start).setDisable(true);
    }

    @Test
    void onAButtonClick_correctAnswer_processesWin() throws Exception {
        when(gameService.processAnswer(anyString(), anyInt())).thenReturn("win");

        controller.onAButtonClick();

        verify(gameService).processAnswer(anyString(), anyInt());
        verify(question).setDisable(true);
        verify(a).setDisable(true);
    }

    @Test
    void on50ButtonClick_disablesTwoWrongButtons() {
        Question mockQuestion = new Question("Sample question", Arrays.asList("A", "B", "C", "D"), "A");
        when(gameService.getQuestions()).thenReturn(Collections.singletonList(mockQuestion));
        when(a.getText()).thenReturn("A");
        when(b.getText()).thenReturn("B");
        when(c.getText()).thenReturn("C");
        when(d.getText()).thenReturn("D");

        controller.on50ButtonClick();

        verify(fifty).setDisable(true);
        verify(a, never()).setDisable(true); // Correct answer should not be disabled
        verify(b, atLeastOnce()).setDisable(true);
        verify(c, atLeastOnce()).setDisable(true);
    }

    @Test
    void onJokerButtonClick_replacesAllAnswersWithCorrect() {
        Question mockQuestion = new Question("Sample question", Arrays.asList("A", "B", "C", "D"), "A");
        when(gameService.getQuestions()).thenReturn(Collections.singletonList(mockQuestion));

        controller.onJokerButtonClick();

        verify(a).setText("A");
        verify(b).setText("A");
        verify(c).setText("A");
        verify(d).setText("A");
        verify(joker).setDisable(true);
    }

    @Test
    void highlightLevel_appliesCorrectStyle() throws Exception {
        controller.highlightLevel(5);

        verify(label_5).getStyleClass().add("highlighted");
        verify(label_1, never()).getStyleClass().add("highlighted");
    }
}
