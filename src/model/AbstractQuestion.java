package model;

import java.io.Serializable;
import java.util.Objects;

/**
 * an abstract question class. Has some default methods, that all question
 * classes should use.
 * 
 * @author Ibrahim Elnikety
 * @version 0.7
 */
public abstract class AbstractQuestion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The Question that will be asked.
	 */
	protected final String myQuestion;
	/**
	 * The correct answer.
	 */
	protected final String myAnswer;

	/**
	 * Creates abstract question.
	 * 
	 * @param theQuestion the question that shows up
	 * @param theAnswer   correct answer
	 */
	public AbstractQuestion(final String theQuestion, final String theAnswer) {
		this.myQuestion = theQuestion;
		this.myAnswer = theAnswer;
		// TODO check data
	}

	/**
	 * Checks if answer is correct.
	 * 
	 * @param theAnswer
	 * @return
	 */
	public boolean isCorrect(final String theAnswer) {
		if (theAnswer == null) {
			return false;
		}
		return myAnswer.equalsIgnoreCase(theAnswer.trim());
	}

	/**
	 * The Question that will be asked.
	 */
	public String getQuestion() {
		return myQuestion;
	}

	@Override
	public final boolean equals(final Object theOther) {
		if (theOther.getClass() != this.getClass()) {
			return false;
		}
		final AbstractQuestion otherQ = this.getClass().cast(theOther);
		return Objects.equals(this.myQuestion, otherQ.myQuestion) && Objects.equals(this.myAnswer, otherQ.myAnswer);
	}

	@Override
	public final int hashCode() {
		return Objects.hash(myQuestion, myAnswer);
	}

	@Override
	public final String toString() {
		return myQuestion + ": " + myAnswer + ".";
	}

	/**
	 * The correct answer.
	 */
	public String getAnswer() {
		return myAnswer;
	}
}
