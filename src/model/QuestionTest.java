package model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * This is a test class for Abstract Question and it's child classes and
 * Question Factory.
 * 
 * @author Team 5
 * @version 1.0
 */
class QuestionTest {

	@Test
	void testQuestions() {
		final String a = "A";
		final String b = "B";
		final String[] str = { a, b, "C" };
		final MultipleChoiceQuestion multi = new MultipleChoiceQuestion("The answer is A", a, str);
		assertFalse(multi.isCorrect(b));
		assertTrue(multi.isCorrect(a));
		final ShortAnswerQuestion saq = new ShortAnswerQuestion("The answer is B", b);
		assertFalse(saq.isCorrect(a));
		assertTrue(saq.isCorrect(b));
		final TrueFalseQuestion tf = new TrueFalseQuestion("This is True", true);
		assertFalse(tf.isCorrect(false));
		assertFalse(tf.isCorrect("false"));
		assertTrue(tf.isCorrect(true));
		assertTrue(tf.isCorrect("true"));
	}

	@Test
	void testQuestionManager() {
		DatabaseManager.getInstance().setDifficulty(Difficulty.EASY);
		QuestionFactory.intializeQuestionFactory();
		assertTrue(Maze.canSolve());
		final int max = QuestionFactory.returnQuestions().size();
		final AbstractQuestion[] arr = new AbstractQuestion[max];
		for (int i = 0; i < max; i++) {
			arr[i] = QuestionFactory.getQuestion();
		}
		for (int i = 0; i < max; i++) {
			for (int j = i + 1; j < max; j++) {
				assertFalse(arr[i].equals(arr[j]));
			}
		}
		assertFalse(Maze.canSolve());
	}

}
