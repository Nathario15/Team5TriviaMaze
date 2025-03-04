package view;

import model.MultipleChoiceQuestion;
import model.GameState;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

public final class MultipleChoiceQuestionPanel extends QuestionPanel {
    private static final long serialVersionUID = 1L;
    private ButtonGroup myButtonGroup;
    private JRadioButton[] myChoiceButtons;

    public MultipleChoiceQuestionPanel(MultipleChoiceQuestion question, GameState gameState) {
        super(question, gameState);
        createAnswerInput();
    }

    @Override
    protected void createAnswerInput() {
        myButtonGroup = new ButtonGroup();
        String[] choices = ((MultipleChoiceQuestion) myQuestion).getChoices();
        myChoiceButtons = new JRadioButton[choices.length];

        // Choice Buttons
        for (int i = 0; i < choices.length; i++) {
            myChoiceButtons[i] = new JRadioButton(choices[i]);
            myButtonGroup.add(myChoiceButtons[i]);
            add(myChoiceButtons[i]);
        }
    }

    @Override
    protected boolean checkAnswer() {
        for (JRadioButton choiceButton : myChoiceButtons) {
            if (choiceButton.isSelected()) {
                String selectedAnswer = choiceButton.getText();
                return myQuestion.isCorrect(selectedAnswer);
            }
        }
        myFeedbackLabel.setText("Please select an answer.");
        return false;
    }
}