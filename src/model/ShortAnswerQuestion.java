package model;

/**
 * @author Ibrahim ELnikety
 * @version 1 A short Answer Question, has a textbox for you to type a answer.
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
