package view;

import model.MultipleChoiceQuestion;
import model.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class MultipleChoiceQuestionPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private MultipleChoiceQuestion myQuestion;
    private GameState myGameState;
    private JLabel myQuestionLabel;
    private ButtonGroup myButtonGroup;
    private JRadioButton[] myChoiceButtons;
    private JButton mySubmitButton;
    private JLabel myFeedbackLabel;

    /**
     * Constructor for MultipleChoiceQuestionPanel.
     *
     * @param question  The multiple-choice question.
     * @param gameState The current game state.
     */
    public MultipleChoiceQuestionPanel(MultipleChoiceQuestion theQuestion, GameState theGameState) {
        this.myQuestion = theQuestion;
        this.myGameState = theGameState;

        setLayout(new GridLayout(myQuestion.getChoiceCount() + 3, 1, 10, 10)); 
        // +3: question label, submit button, feedback label

        // Question Label
        myQuestionLabel = new JLabel("<html><b>Question:</b> " + myQuestion.myQuestion + "</html>");
        myQuestionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(myQuestionLabel);

        // Choice Buttons
        myButtonGroup = new ButtonGroup();
        String[] choices = myQuestion.getChoices();
        myChoiceButtons = new JRadioButton[choices.length];
        for (int i = 0; i < choices.length; i++) {
        	myChoiceButtons[i] = new JRadioButton(choices[i]); // Use the actual choices from getChoices()
            myButtonGroup.add(myChoiceButtons[i]);
            add(myChoiceButtons[i]);
        }

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
     * Inner class to handle answer submission.
     */
    private class SubmitAnswerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (JRadioButton choiceButton : myChoiceButtons) {
                if (choiceButton.isSelected()) {
                    String selectedAnswer = choiceButton.getText();
                    if (myQuestion.isCorrect(selectedAnswer)) { // Check correctness using isCorrect
                    	myFeedbackLabel.setText("Correct! You can proceed.");
                    	myFeedbackLabel.setForeground(Color.GREEN);
                    	myGameState.useQuestion(myQuestion.hashCode()); // Mark question as answered
                    } else {
                    	myFeedbackLabel.setText("Incorrect! Try again.");
                    	myFeedbackLabel.setForeground(Color.RED);
                    }
                    return;
                }
            }
            myFeedbackLabel.setText("Please select an answer.");
        }
    }
}
