package view;

import model.TrueFalseQuestion;
import model.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TrueFalseQuestionPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private TrueFalseQuestion myQuestion;
    private GameState myGameState;
    private JLabel myQuestionLabel;
    private JRadioButton myTrueButton;
    private JRadioButton myFalseButton;
    private ButtonGroup myButtonGroup;
    private JButton mySubmitButton;
    private JLabel myFeedbackLabel;

    /**
     * Constructor for TrueFalseQuestionPanel.
     *
     * @param question  The true/false question.
     * @param gameState The current game state.
     */
    public TrueFalseQuestionPanel(TrueFalseQuestion question, GameState gameState) {
        this.myQuestion = question;
        this.myGameState = gameState;

        setLayout(new GridLayout(5, 1, 10, 10));

        // Question Label
        myQuestionLabel = new JLabel("<html><b>Question:</b> " + question.myQuestion + "</html>");
        myQuestionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(myQuestionLabel);

        // True/False Buttons
        myTrueButton = new JRadioButton("True");
        myFalseButton = new JRadioButton("False");
        myButtonGroup = new ButtonGroup();
        myButtonGroup.add(myTrueButton);
        myButtonGroup.add(myFalseButton);

        add(myTrueButton);
        add(myFalseButton);

        // Submit Button
        mySubmitButton = new JButton("Submit Answer");
        mySubmitButton.addActionListener(new SubmitAnswerListener());
        add(mySubmitButton);

        // Feedback Label
        myFeedbackLabel = new JLabel("");
        myFeedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        myFeedbackLabel.setForeground(Color.RED);
        add(myFeedbackLabel);
    }

    /**
     * Inner class to handle answer submission.
     */
    private class SubmitAnswerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (myTrueButton.isSelected() || myFalseButton.isSelected()) {
                boolean selectedAnswer = myTrueButton.isSelected(); // True if selected, otherwise false
                if (myQuestion.isCorrect(selectedAnswer)) {
                    myFeedbackLabel.setText("Correct! You can proceed.");
                    myFeedbackLabel.setForeground(Color.GREEN);
                    myGameState.useQuestion(myQuestion.hashCode()); // Mark question as answered
                } else {
                    myFeedbackLabel.setText("Incorrect! Try again.");
                    myFeedbackLabel.setForeground(Color.RED);
                }
            } else {
                myFeedbackLabel.setText("Please select an answer.");
            }
        }
    }
}
