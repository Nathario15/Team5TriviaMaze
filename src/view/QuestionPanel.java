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
    /**
     * GameState.
     */
    protected GameState myGameState;
    /**
     * Question.
     */
    protected JLabel myQuestionLabel;
    /**
     * SUbmit.
     */
    protected JButton mySubmitButton;
    /**
     * Feedback.
     */
    protected JLabel myFeedbackLabel;

    /**
     * Constructor for the question panel.
     *
     * @param theQuestion  The question for this panel.
     * @param theGameState The current game state.
     */
    public QuestionPanel(final AbstractQuestion theQuestion, final GameState theGameState) {
        this.myQuestion = theQuestion;
        this.myGameState = theGameState;

        setLayout(new GridLayout(3, 1, 10, 10)); // Adjust this depending on the number of components

        // Question Label
        myQuestionLabel = new JLabel("<html><b>Question:</b> " + theQuestion.getQuestion() + "</html>");
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
     * Abstract method that must be implemented in subclasses to check the user's answer.
     *
     * @return true if the answer is correct, false otherwise.
     */
    protected abstract boolean checkAnswer();
    
    /**
     * Inner class to handle answer submission.
     */
    private final class SubmitAnswerListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent theE) {
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

    
}