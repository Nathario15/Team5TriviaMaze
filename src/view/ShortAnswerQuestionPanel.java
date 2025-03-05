package view;

import model.ShortAnswerQuestion;
import model.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class ShortAnswerQuestionPanel extends QuestionPanel {
    private static final long serialVersionUID = 1L;
    private JTextField myAnswerField;
    private JButton myClearButton;

    public ShortAnswerQuestionPanel(ShortAnswerQuestion question, GameState gameState) {
        super(question, gameState);
        createAnswerInput();
    }

    @Override
    protected void createAnswerInput() {
        // Set up the layout for the answer section
        JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new BorderLayout());

        // Text field for user input
        myAnswerField = new JTextField();
        myAnswerField.setToolTipText("Type your answer here...");
        answerPanel.add(myAnswerField, BorderLayout.CENTER);

        // Clear button for resetting input
        myClearButton = new JButton("Clear Answer");
        myClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                myAnswerField.setText(""); // Clears the text field
            }
        });
        answerPanel.add(myClearButton, BorderLayout.EAST);

        add(answerPanel);
    }

    @Override
    protected boolean checkAnswer() {
        String answer = myAnswerField.getText().trim();

        // Check if the answer is correct
        boolean isCorrect = myQuestion.isCorrect(answer);
        
        // Provide feedback to the user
        if (isCorrect) {
            myFeedbackLabel.setText("Correct! You can proceed.");
            myFeedbackLabel.setForeground(Color.GREEN);
            myAnswerField.setBackground(Color.GREEN);  // Green background for correct answer
        } else {
            myFeedbackLabel.setText("Incorrect! Try again.");
            myFeedbackLabel.setForeground(Color.RED);
            myAnswerField.setBackground(Color.RED);  // Red background for incorrect answer
        }

        return isCorrect;
    }
}