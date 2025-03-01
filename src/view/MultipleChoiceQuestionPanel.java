package view;

import javax.swing.*;
import java.awt.*;
import model.MultipleChoiceQuestion;
import model.GameState;

public class MultipleChoiceQuestionPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private GameView gameView;
    private MultipleChoiceQuestion question;
    private GameState gameState;
    
    private JLabel questionLabel;
    private ButtonGroup answerGroup;
    private JPanel answerPanel;
    private JButton submitButton;

    public MultipleChoiceQuestionPanel(GameView gameView, MultipleChoiceQuestion question) {
        this.gameView = gameView;
        this.question = question;
        //this.gameState = gameView.getGameState();
        
        setLayout(new BorderLayout());
        
        // Question label
        //questionLabel = new JLabel("<html><b>" + question.getQuestion() + "</b></html>");
        add(questionLabel, BorderLayout.NORTH);
        
        // Answer choices panel
        answerPanel = new JPanel();
        answerPanel.setLayout(new GridLayout(question.getChoices(), 1));
        answerGroup = new ButtonGroup();
        
//        for (String choice : question.getChoices()) {
//            JRadioButton choiceButton = new JRadioButton(choice);
//            answerGroup.add(choiceButton);
//            answerPanel.add(choiceButton);
//        }
        
        add(answerPanel, BorderLayout.CENTER);
        
        // Submit button
        submitButton = new JButton("Submit");
        //submitButton.addActionListener(_ -> checkAnswer());
        add(submitButton, BorderLayout.SOUTH);
    }
}
