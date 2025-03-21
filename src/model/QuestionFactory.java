package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Implements the factory design pattern and the Singleton Design Pattern.
 * 
 * @author Team 5
 * @version 0.5
 */
public final class QuestionFactory implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * A list of questions.
	 */
	private static ArrayList<AbstractQuestion> myQuestions;

	private QuestionFactory() {

	};

	/**
	 * creates a question manager.
	 * 
	 * @param theQuestions the questions that will be asked.
	 */
	public static void intializeQuestionFactory() {
		myQuestions = new ArrayList<AbstractQuestion>();
		myQuestions = DatabaseManager.getInstance().getArrayList();
	}

	/**
	 * The map will stay the same size, but it's contents will change.
	 */
	protected static void loadQuestions(final ArrayList<AbstractQuestion> theArr) {
		myQuestions = theArr;
	}

	/**
	 * The map will stay the same size, but it's contents will change.
	 */
	protected static ArrayList<AbstractQuestion> returnQuestions() {
		return myQuestions;
	}

	/**
	 * Chooses a random question, makes sure it won't be asked again.
	 * 
	 * @return
	 */
	public static AbstractQuestion getQuestion() {
		final int index = (int) Math.round(Math.random() * (myQuestions.size() - 1));
		return myQuestions.remove(index);
	}
}
