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
}
