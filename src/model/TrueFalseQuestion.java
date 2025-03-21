package model;

/**
 * A true false question, can take booleans as input.
 * @author Ibrahim Elnikety
 * @version 1
 */
public class TrueFalseQuestion extends AbstractQuestion {
	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * creates a true false question.
	 * 
	 * @param theQuestion
	 * @param theAnswer
	 */
	public TrueFalseQuestion(final String theQuestion, final Boolean theAnswer) {
		super(theQuestion, theAnswer.toString());
	}

	/**
	 * An overload of isCorrect that accepts Booleans.
	 * 
	 * @param theAnswer
	 * @return
	 */
	public Boolean isCorrect(final Boolean theAnswer) {
		return super.isCorrect(theAnswer.toString());
	}
}
