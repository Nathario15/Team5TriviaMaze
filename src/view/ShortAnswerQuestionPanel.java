package view;

import model.ShortAnswerQuestion;
import model.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class ShortAnswerQuestionPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private ShortAnswerQuestion myQuestion;
    private GameState myGameState;
    private JLabel myQuestionLabel;
    private JTextField myAnswerField;
    private JButton mySubmitButton;
    private JLabel myFeedbackLabel;

    /**
     * Constructor for ShortAnswerQuestionPanel.
     *
     * @param question  The short answer question.
     * @param gameState The current game state.
     */
    public ShortAnswerQuestionPanel(ShortAnswerQuestion question, GameState gameState) {
        this.myQuestion = question;
        this.myGameState = gameState;

        setLayout(new GridLayout(3, 1, 10, 10));

        // Question Label
        myQuestionLabel = new JLabel("<html><b>Question:</b> " + question.myQuestion + "</html>");
        myQuestionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(myQuestionLabel);

        // Answer Field
        myAnswerField = new JTextField();
        add(myAnswerField);

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
            String answer = myAnswerField.getText().trim();
            if (myQuestion.isCorrect(answer)) { // Check correctness using isCorrect
                myFeedbackLabel.setText("Correct! You can proceed.");
                myFeedbackLabel.setForeground(Color.GREEN);
                myGameState.useQuestion(myQuestion.hashCode()); // Mark question as answered
            } else {
                myFeedbackLabel.setText("Incorrect! Try again.");
                myFeedbackLabel.setForeground(Color.RED);
            }
        }
    }
}
