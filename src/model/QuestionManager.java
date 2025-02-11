package model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class QuestionManager implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * A list of questions.
	 */
	private final ArrayList<AbstractQuestion> myQuestions;

	/**
	 * creates a question manager.
	 * 
	 * @param theQuestions the questions that will be asked.
	 */
	public QuestionManager(final ArrayList<AbstractQuestion> theQuestions) {
		myQuestions = theQuestions;

		@SuppressWarnings("unused")
		Connection conn = null;

		try {
			conn = DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("user.dir"));
		} catch (SQLException E) {

		}
	}

	/**
	 * Chooses a random question, makes sure it won't be asked again.
	 * 
	 * @return
	 */
	public AbstractQuestion getQuestion() {
		final int index = (int) Math.round(Math.random() * (myQuestions.size() - 1));
		return myQuestions.remove(index);
	}
}
