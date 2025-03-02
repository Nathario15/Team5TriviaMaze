package model;

import java.io.Serializable;

/**
 * @author Ibrahim Elnikety
 * @version 0.7 an abstract question class. Has some default methods, that all
 *          question classes should use.
 */
public abstract class AbstractQuestion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The Question that will be asked.
	 */
	public final String myQuestion;
	/**
	 * The correct answer.
	 */
	public final String myAnswer;

	/**
	 * Creates abstract question.
	 * 
	 * @param theQuestion the question that shows up
	 * @param theAnswer   correct answer
	 */
	public AbstractQuestion(final String theQuestion, final String theAnswer) {
		this.myQuestion = theQuestion;
		this.myAnswer = theAnswer;
	}

	/**
	 * Checks if answer is correct.
	 * 
	 * @param theAnswer
	 * @return
	 */
	public Boolean isCorrect(final String theAnswer) {
		return myAnswer == theAnswer;
	}
}
