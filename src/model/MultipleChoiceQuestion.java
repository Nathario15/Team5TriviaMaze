package model;
/**
 * @author Ibrahim ELnikety
 * @version 1
 * A multiple Choice Question, Has a multiple options for you to pick, and you have to chose the right one.
 */
public class MultipleChoiceQuestion extends AbstractQuestion {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the choices that will show up.
	 */
	protected final String[] myChoices;

	/**
	 * Creates a multiple choice question.
	 * 
	 * @param theQuestion the question that will be asked
	 * @param theAnswer   the correct answer
	 * @param theChoices  a list of possible answers (should include correct one)
	 */
	public MultipleChoiceQuestion(final String theQuestion, 
			final String theAnswer, final String[] theChoices) {
		super(theQuestion, theAnswer);
		myChoices = theChoices;
	}
	/**
	 * The whole point of this is to get the number of questions, 
	 * so GUI elements can be sized correctly.
	 * @return
	 */
	public int getChoices() {
		return myChoices.length;
	}
}
