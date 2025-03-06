package model;

import java.io.Serializable;

/**
 * an abstract question class. Has some default methods, that all question classes should use.
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
		//TODO check data
	}

	/**
	 * Checks if answer is correct.
	 * 
	 * @param theAnswer
	 * @return
	 */
	public boolean isCorrect(final String theAnswer) {
		return myAnswer.equalsIgnoreCase(theAnswer.trim());
	}
	
	/**
	 * The Question that will be asked.
	 */
    public String getQuestion() {
        return myQuestion;
    }
    /**
	 * The correct answer.
	 */
    public String getAnswer() {
        return myAnswer;
    }
}
