package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.GameState;
import model.ShortAnswerQuestion;

public final class ShortAnswerQuestionPanel extends QuestionPanel {
    private static final long serialVersionUID = 1L;
    /**
     * answer field.
     */
    private JTextField myAnswerField;
    /**
     * clear button.
     */
    private JButton myClearButton;
    
    /**
     * constructor.
     * @param theQuestion
     * @param theGameState
     */
    public ShortAnswerQuestionPanel(final ShortAnswerQuestion theQuestion, final GameState theGameState) {
        super(theQuestion, theGameState);
        createAnswerInput();
    }

    @Override
    protected void createAnswerInput() {
        // Set up the layout for the answer section
        final JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new BorderLayout());

        // Text field for user input
        myAnswerField = new JTextField();
        myAnswerField.setToolTipText("Type your answer here...");
        answerPanel.add(myAnswerField, BorderLayout.CENTER);

        // Clear button for resetting input
        myClearButton = new JButton("Clear Answer");
        myClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theE) {
                myAnswerField.setText(""); // Clears the text field
            }
        });
        answerPanel.add(myClearButton, BorderLayout.EAST);

        add(answerPanel);
    }

    @Override
    protected boolean checkAnswer() {
        final String answer = myAnswerField.getText().trim();

        // Check if the answer is correct
        final boolean isCorrect = myQuestion.isCorrect(answer);
        
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