package view;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import model.AbstractQuestion;
import model.GameState;

public abstract class AbstractQuestionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	/**
	 * rows.
	 */
	private static final int ROWS = 3;
	/**
	 * Gap in space.
	 */
	private static final int GAP = 10;

	/**
	 * the Question.
	 */
	protected AbstractQuestion myQuestion;
	/**
	 * GameState.
	 */
	protected GameState myGameState;
	/**
	 * Question.
	 */
	protected JLabel myQuestionLabel;
	/**
	 * SUbmit.
	 */
	protected JButton mySubmitButton;
	/**
	 * Feedback.
	 */
	protected JLabel myFeedbackLabel;

	/**
	 * Constructor for the question panel.
	 *
	 * @param theQuestion  The question for this panel.
	 * @param theGameState The current game state.
	 */
	public AbstractQuestionPanel(final AbstractQuestion theQuestion, final GameState theGameState) {
		this.myQuestion = theQuestion;
		this.myGameState = theGameState;

		setLayout(new GridLayout(ROWS, 1, GAP, GAP)); // Adjust this depending on the number of components

		// Question Label
		myQuestionLabel = new JLabel("<html><b>Question:</b> " + theQuestion.getQuestion() + "</html>");
		myQuestionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(myQuestionLabel);
		setUp();

	}

	private void setUp() {

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
	 * Abstract method to be implemented by subclasses to define how the user
	 * provides answers.
	 */
	protected abstract void createAnswerInput();

	/**
	 * Abstract method that must be implemented in subclasses to check the user's
	 * answer.
	 *
	 * @return true if the answer is correct, false otherwise.
	 */
	public abstract boolean checkAnswer();

	/**
	 * Inner class to handle answer submission.
	 */
	private final class SubmitAnswerListener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent theE) {
			final boolean correct = checkAnswer();

			if (correct) {
				myFeedbackLabel.setText("Correct! You can proceed.");
				myFeedbackLabel.setForeground(Color.GREEN);
			} else {
				myFeedbackLabel.setText("Incorrect! Door is now permanently blocked.");
				myFeedbackLabel.setForeground(Color.RED);
			}
		}
	}

}