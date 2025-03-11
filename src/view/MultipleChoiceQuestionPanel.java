package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import model.GameState;
import model.MultipleChoiceQuestion;

public final class MultipleChoiceQuestionPanel extends QuestionPanel {
    private static final long serialVersionUID = 1L;
    /**
     * Buttons.
     */
    private ButtonGroup myChoiceGroup;
    /**
     * Choices.
     */
    private JPanel myChoicePanel;
    /**
     * Clear.
     */
    private JButton myClearButton;

    /**
     * Creates panel.
     * @param theQuestion
     * @param theGameState
     */
    public MultipleChoiceQuestionPanel(final MultipleChoiceQuestion theQuestion, final GameState theGameState) {
        super(theQuestion, theGameState);
        createAnswerInput();
    }

    @Override
    protected void createAnswerInput() {
        // Set up the layout for multiple choice options
        final JPanel answerPanel = new JPanel();
        answerPanel.setLayout(new BorderLayout());
        
        myChoicePanel = new JPanel();
        myChoicePanel.setLayout(new BoxLayout(myChoicePanel, BoxLayout.Y_AXIS));

        myChoiceGroup = new ButtonGroup();
        for (String choice : ((MultipleChoiceQuestion) myQuestion).getChoices()) {
            final JRadioButton radioButton = new JRadioButton(choice);
            myChoiceGroup.add(radioButton);
            myChoicePanel.add(radioButton);
        }

        // Clear button for resetting input
        myClearButton = new JButton("Clear Selection");
        myClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent theE) {
                myChoiceGroup.clearSelection(); // Clears the radio button selection
            }
        });
        
        answerPanel.add(myChoicePanel, BorderLayout.CENTER);
        answerPanel.add(myClearButton, BorderLayout.SOUTH);
        add(answerPanel);
    }

    @Override
    public boolean checkAnswer() {
        String selectedAnswer = null;
        for (AbstractButton button : Collections.list(myChoiceGroup.getElements())) {
            if (button.isSelected()) {
                selectedAnswer = button.getText();
                break;
            }
        }

        // Check if the selected answer is correct
        final boolean isCorrect = myQuestion.isCorrect(selectedAnswer);
        
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
