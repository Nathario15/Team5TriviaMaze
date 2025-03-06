package view;

import model.TrueFalseQuestion;
import model.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class TrueFalseQuestionPanel extends QuestionPanel {
    private static final long serialVersionUID = 1L;
    private JButton myTrueButton;
    private JButton myFalseButton;

    public TrueFalseQuestionPanel(TrueFalseQuestion question, GameState gameState) {
        super(question, gameState);
        createAnswerInput();
    }

    @Override
    protected void createAnswerInput() {
        // Set up the layout for True/False buttons
        final JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new GridLayout(1, 2));

        myTrueButton = new JButton("True");
        myFalseButton = new JButton("False");

        myTrueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                checkAnswer();
            }
        });

        myFalseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                checkAnswer();
            }
        });

        answerPanel.add(myTrueButton);
        answerPanel.add(myFalseButton);
        add(answerPanel);
    }

    @Override
    protected boolean checkAnswer() {
        // Check if the selected answer is correct
        final String answer = myTrueButton.getModel().isPressed() ? "True" : "False";
        final boolean isCorrect = myQuestion.isCorrect(answer);

        // Provide feedback to the user
        if (isCorrect) {
            myFeedbackLabel.setText("Correct! You can proceed.");
            myFeedbackLabel.setForeground(Color.GREEN);
        } else {
            myFeedbackLabel.setText("Incorrect! Try again.");
            myFeedbackLabel.setForeground(Color.RED);
        }

        return isCorrect;
    }
}