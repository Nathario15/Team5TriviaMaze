package view;

import model.ShortAnswerQuestion;
import model.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShortAnswerQuestionPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private ShortAnswerQuestion question;
    private GameState gameState;
    private JLabel questionLabel;
    private JTextField answerField;
    private JButton submitButton;
    private JLabel feedbackLabel;

    /**
     * Constructor for ShortAnswerQuestionPanel.
     *
     * @param question  The short answer question.
     * @param gameState The current game state.
     */
    public ShortAnswerQuestionPanel(ShortAnswerQuestion question, GameState gameState) {
        this.question = question;
        this.gameState = gameState;

        setLayout(new GridLayout(3, 1, 10, 10));

        // Question Label
        questionLabel = new JLabel("<html><b>Question:</b> " + question.myQuestion + "</html>");
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(questionLabel);

        // Answer Field
        answerField = new JTextField();
        add(answerField);

        // Submit Button
        submitButton = new JButton("Submit Answer");
        submitButton.addActionListener(new SubmitAnswerListener());
        add(submitButton);

        // Feedback Label
        feedbackLabel = new JLabel("");
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        feedbackLabel.setForeground(Color.RED);
        add(feedbackLabel);
    }

    /**
     * Inner class to handle answer submission.
     */
    private class SubmitAnswerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String answer = answerField.getText().trim();
            if (question.isCorrect(answer)) { // Check correctness using isCorrect
                feedbackLabel.setText("Correct! You can proceed.");
                feedbackLabel.setForeground(Color.GREEN);
                gameState.useQuestion(question.hashCode()); // Mark question as answered
            } else {
                feedbackLabel.setText("Incorrect! Try again.");
                feedbackLabel.setForeground(Color.RED);
            }
        }
    }
}
