package view;

import model.MultipleChoiceQuestion;
import model.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

public final class MultipleChoiceQuestionPanel extends QuestionPanel {
    private static final long serialVersionUID = 1L;
    private ButtonGroup myChoiceGroup;
    private JPanel myChoicePanel;
    private JButton myClearButton;

    public MultipleChoiceQuestionPanel(MultipleChoiceQuestion question, GameState gameState) {
        super(question, gameState);
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
    protected boolean checkAnswer() {
        String selectedAnswer = null;
        for (AbstractButton button : Collections.list(myChoiceGroup.getElements())) {
            if (button.isSelected()) {
                selectedAnswer = button.getText();
                break;
            }
        }

        // Check if the selected answer is correct
        boolean isCorrect = myQuestion.isCorrect(selectedAnswer);
        
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
