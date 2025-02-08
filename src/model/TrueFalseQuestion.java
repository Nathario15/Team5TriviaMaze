package model;

public class TrueFalseQuestion extends AbstractQuestion {
	/**
	 * creates a true false question.
	 * @param theQuestion
	 * @param theAnswer
	 */
	public TrueFalseQuestion(final String theQuestion, final Boolean theAnswer) {
		super(theQuestion, theAnswer.toString());
	}
	/**
	 * An overload of isCorrect that accepts Booleans.
	 * @param theAnswer
	 * @return
	 */
	public Boolean isCorrect(final Boolean theAnswer) {
		return super.isCorrect(theAnswer.toString());
	}
}
