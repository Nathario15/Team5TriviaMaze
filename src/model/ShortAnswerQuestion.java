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
	
	/**
	 * Checks if provided answer is correct, with more flexible matching.
	 * 
	 * @param theAnswer the answer to check
	 * @return true if the answer is correct
	 */
	@Override
	public boolean isCorrect(final String theAnswer) {
	    // Check if cheats are enabled first
	    if (AbstractQuestion.cheatsEnabled()) {
	        return true;
	    }
	    
	    if (theAnswer == null) {
	        return false;
	    }
	    
	    // Convert both answers to lowercase for case-insensitive comparison
	    final String normalizedAnswer = theAnswer.trim().toLowerCase();
	    final String normalizedCorrect = myAnswer.trim().toLowerCase();
	    
	    return normalizedAnswer.equals(normalizedCorrect);
	}
}
