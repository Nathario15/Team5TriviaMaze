package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import model.GameState;
import model.TrueFalseQuestion;

public final class TrueFalseQuestionPanel extends QuestionPanel {
    private static final long serialVersionUID = 1L;
    /**
     * True Button.
     */
    private JButton myTrueButton;
    /**
     * False Button.
     */
    private JButton myFalseButton;

    /**
     * Constructor.
     * @param theQuestion
     * @param theGameState
     */
    public TrueFalseQuestionPanel(final TrueFalseQuestion theQuestion, final GameState theGameState) {
        super(theQuestion, theGameState);
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
            public void actionPerformed(final ActionEvent theE) {
                checkAnswer();
            }
        });

        myFalseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theE) {
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