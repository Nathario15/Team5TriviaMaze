package view;

import model.MultipleChoiceQuestion;
import model.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MultipleChoiceQuestionPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private MultipleChoiceQuestion question;
    private GameState gameState;
    private JLabel questionLabel;
    private ButtonGroup buttonGroup;
    private JRadioButton[] choiceButtons;
    private JButton submitButton;
    private JLabel feedbackLabel;

    /**
     * Constructor for MultipleChoiceQuestionPanel.
     *
     * @param question  The multiple-choice question.
     * @param gameState The current game state.
     */
    public MultipleChoiceQuestionPanel(MultipleChoiceQuestion question, GameState gameState) {
        this.question = question;
        this.gameState = gameState;

        setLayout(new GridLayout(question.getChoiceCount() + 3, 1, 10, 10)); 
        // +3: question label, submit button, feedback label

        // Question Label
        questionLabel = new JLabel("<html><b>Question:</b> " + question.myQuestion + "</html>");
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(questionLabel);

        // Choice Buttons
        buttonGroup = new ButtonGroup();
        String[] choices = question.getChoices();
        choiceButtons = new JRadioButton[choices.length];
        for (int i = 0; i < choices.length; i++) {
            choiceButtons[i] = new JRadioButton(choices[i]); // Use the actual choices from getChoices()
            buttonGroup.add(choiceButtons[i]);
            add(choiceButtons[i]);
        }

        // Submit Button
        submitButton = new JButton("Submit Answer");
        submitButton.addActionListener(new SubmitAnswerListener());
        add(submitButton);

        // Feedback Label
        feedbackLabel = new JLabel("");
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        feedbackLabel.setForeground(Color.RED);
        add(feedbackLabel);
    }

    /**
     * Inner class to handle answer submission.
     */
    private class SubmitAnswerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            for (JRadioButton choiceButton : choiceButtons) {
                if (choiceButton.isSelected()) {
                    String selectedAnswer = choiceButton.getText();
                    if (question.isCorrect(selectedAnswer)) { // Check correctness using isCorrect
                        feedbackLabel.setText("Correct! You can proceed.");
                        feedbackLabel.setForeground(Color.GREEN);
                        gameState.useQuestion(question.hashCode()); // Mark question as answered
                    } else {
                        feedbackLabel.setText("Incorrect! Try again.");
                        feedbackLabel.setForeground(Color.RED);
                    }
                    return;
                }
            }
            feedbackLabel.setText("Please select an answer.");
        }
    }
}
