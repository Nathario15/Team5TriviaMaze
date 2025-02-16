package controller;
import model.QuestionFactory;
/**
 * @author Ibrahim ELnikety
 * @version 0.1
 * Meant to interface between Model and View
 */
public final class SystemControl {
	/**
	 * All the questions used.
	 */
	protected static QuestionFactory questionManager;
	private SystemControl() {
		
	}
	/**
	 * pops up a question through gui.
	 * @return
	 */
	public static boolean triggerQuestion() {
		questionManager.getQuestion();
		//TODO add code to display
		return false;
	}

}
