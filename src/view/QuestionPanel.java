package view;

import model.AbstractQuestion;
import model.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class QuestionPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    /**
     * the Question.
     */
    protected AbstractQuestion myQuestion;
    protected GameState myGameState;
    protected JLabel myQuestionLabel;
    protected JButton mySubmitButton;
    protected JLabel myFeedbackLabel;

    /**
     * Constructor for the question panel.
     *
     * @param question  The question for this panel.
     * @param gameState The current game state.
     */
    public QuestionPanel(AbstractQuestion question, GameState gameState) {
        this.myQuestion = question;
        this.myGameState = gameState;

        setLayout(new GridLayout(3, 1, 10, 10)); // Adjust this depending on the number of components

        // Question Label
        myQuestionLabel = new JLabel("<html><b>Question:</b> " + question.getQuestion() + "</html>");
        myQuestionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(myQuestionLabel);

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
     * Abstract method to be implemented by subclasses to define how the user provides answers.
     */
    protected abstract void createAnswerInput();

    /**
     * Inner class to handle answer submission.
     */
    private class SubmitAnswerListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            if (checkAnswer()) {
                myFeedbackLabel.setText("Correct! You can proceed.");
                myFeedbackLabel.setForeground(Color.GREEN);
                myGameState.useQuestion(myQuestion.hashCode()); // Mark question as answered
            } else {
                myFeedbackLabel.setText("Incorrect! Try again.");
                myFeedbackLabel.setForeground(Color.RED);
            }
        }
    }

    /**
     * Abstract method that must be implemented in subclasses to check the user's answer.
     *
     * @return true if the answer is correct, false otherwise.
     */
    protected abstract boolean checkAnswer();
}