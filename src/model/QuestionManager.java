package model;

import java.util.ArrayList;

public class QuestionManager {
	/**
	 * A list of questions.
	 */
	private final ArrayList<AbstractQuestion> myQuestions;
	/**
	 * creates a question manager.
	 * @param theQuestions the questions that will be asked.
	 */
	public QuestionManager(final ArrayList<AbstractQuestion> theQuestions) {
		myQuestions = theQuestions;
	}
	/**
	 * Chooses a random question, makes sure it won't be asked again.
	 * @return
	 */
	public AbstractQuestion getQuestion() {
		final int index = (int) Math.round(Math.random() * (myQuestions.size() - 1));
		return myQuestions.remove(index);
	}
}
