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
	
    /**
     * Serialization ID.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Spacing between buttons.
     */
    private static final int BUTTON_SPACING = 10;
    
    /**
     * String constant for "True" label and value.
     */
    private static final String TRUE_VALUE = "True";
    
    /**
     * String constant for "False" label and value.
     */
    private static final String FALSE_VALUE = "False";
    
    /**
     * True Button.
     */
    private JButton myTrueButton;
    
    /**
     * False Button.
     */
    private JButton myFalseButton;
    
    /**
     * The selected answer.
     */
    private String mySelectedAnswer;


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
        answerPanel.setLayout(new GridLayout(1, 2, BUTTON_SPACING, 0));

        myTrueButton = new JButton(TRUE_VALUE);
        myFalseButton = new JButton(FALSE_VALUE);

        // Auto-submit when True is clicked
        myTrueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theE) {
                // Set the answer and submit automatically
                mySelectedAnswer = TRUE_VALUE;
                mySubmitButton.doClick();  // Trigger the submit button
            }
        });

        // Auto-submit when False is clicked
        myFalseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theE) {
                // Set the answer and submit automatically
                mySelectedAnswer = FALSE_VALUE;
                mySubmitButton.doClick();  // Trigger the submit button
            }
        });

        answerPanel.add(myTrueButton);
        answerPanel.add(myFalseButton);
        add(answerPanel);
        
        // Hide the submit button so it's not visible but can still be triggered
        mySubmitButton.setVisible(false);
    }

    @Override
    public boolean checkAnswer() {
        // Check if the selected answer is correct
        final boolean isCorrect = myQuestion.isCorrect(mySelectedAnswer);

        // Provide feedback to the user
        if (isCorrect) {
            myFeedbackLabel.setText("Correct! You can proceed.");
            myFeedbackLabel.setForeground(Color.GREEN);
        } else {
            myFeedbackLabel.setText("Incorrect! Door is now permanently blocked.");
            myFeedbackLabel.setForeground(Color.RED);
        }

        return isCorrect;
    }
}