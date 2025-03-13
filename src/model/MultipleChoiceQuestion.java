package model;

import java.util.Arrays;
import java.util.List;

/**
 * A multiple Choice Question, Has a multiple options for you to  pick, and you have to chose the right one.
 * @author Ibrahim Elnikety
 * @version 1 
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
	public MultipleChoiceQuestion(final String theQuestion, final String theAnswer, final String[] theChoices) {
		super(theQuestion, theAnswer);
		this.myChoices = theChoices;
	}

    /**
     * Gets the number of choices.
     * 
     * @return the number of choices
     */
    public int getChoiceCount() {
        return myChoices.length;
    }
    
    /**
     * Gets the choices.
     * 
     * @return array of choices
     */
    public String[] getChoices() {
        return Arrays.copyOf(myChoices, myChoices.length);
    }
    
    /**
     * Gets the choices as a list.
     * 
     * @return list of choices
     */
    public List<String> getChoicesList() {
        return Arrays.asList(myChoices);
    }
}
