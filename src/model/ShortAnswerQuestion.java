package model;

/**
 * A Short Answer Question, has a textbox for you to type a answer.
 * @author Ibrahim Elnikety
 * @version 1
 */
public class ShortAnswerQuestion extends AbstractQuestion {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a short answer question.
	 * 
	 * @param theQuestion
	 * @param theAnswer
	 */
	public ShortAnswerQuestion(final String theQuestion, final String theAnswer) {
		super(theQuestion, theAnswer);
	}

}
