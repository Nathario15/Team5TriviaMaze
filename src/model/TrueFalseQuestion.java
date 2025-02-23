package model;

/**
 * @author Ibrahim ELnikety
 * @version 1 A true false question, can take booleans as input.
 */
public class TrueFalseQuestion extends AbstractQuestion {
	/**
	 * 
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
