package model;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Implements the factory design pattern and the Singleton Design Pattern.
 * @author Ibrahim Elnikety
 * @version 0.5
 */
public class QuestionFactory implements Serializable {
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
	public QuestionFactory() {

		@SuppressWarnings("unused")
		Connection conn = null;
		myQuestions=null;
		// TODO make a question database.
		try { // get connection will create a file if not found.
			conn = DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("user.dir"));
		} catch (final SQLException e) {

		}
		// TODO write rest, when database is set up
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
