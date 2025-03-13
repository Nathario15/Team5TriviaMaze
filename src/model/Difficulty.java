package model;

/**
 * This indicates the difficulty of a question, used to make sure the player
 * gets a good distribution.
 * 
 * @author Ibrahim Elnikety
 * @version 1.0
 */
public enum Difficulty {
	/**
	 * This question is very hard.
	 */
	HARD,
	/**
	 * This question is of medium difficulty.
	 */
	MEDIUM,
	/**
	 * This question is easy.
	 */
	EASY;
	/**
	 * Returns a diffuclty.
	 * @param theString
	 * @return
	 */
	public Difficulty fromString(final String theString) {
		final Difficulty d;
		if ("HARD".equalsIgnoreCase(theString.trim())) {
			d = Difficulty.HARD;
		} else if ("MEDIUM".equalsIgnoreCase(theString.trim())) {
			d = Difficulty.MEDIUM;
		} else {
			d = Difficulty.EASY;
		}
		return d;
	}
}
