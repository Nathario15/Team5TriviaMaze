package view;

import javax.swing.*;
import java.awt.*;
import model.MultipleChoiceQuestion;
import model.Player;
import model.GameState;

public class MultipleChoiceQuestionPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private GameView gameView;
    private MultipleChoiceQuestion question;
    private Player player;
    private GameState gameState;
    
    private JLabel questionLabel;
    private ButtonGroup answerGroup;
    private JPanel answerPanel;
    private JButton submitButton;
    
}
