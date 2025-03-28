package model;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * DatabaseManager handles all database operations for the Minecraft Trivia Maze
 * game. Uses SQLite database to store and retrieve questions.
 * 
 * @author Nathaniel Roy
 * @version 0.7
 */
public final class DatabaseManager {
	/**
	 * path of database.
	 */
	private static final String DB_PATH = "minecraft_trivia.db";
	/**
	 * A code used to access questions.
	 */
	private static final String QUESTION_ACCESS = "SELECT COUNT(*) FROM trivia_questions WHERE difficulty = ?";
	
	/**
	 * Maximum number of questions to retrieve per difficulty.
	 */
	private static final int MAX_QUESTIONS_PER_DIFFICULTY = 48;
	
	/**
	 * Error message for random question retrieval failure.
	 */
	private static final String ERROR_GETTING_QUESTION = "Error getting random question: ";
	
	/**
	 * Base SQL query for retrieving questions by difficulty.
	 */
	private static final String BASE_QUESTION_QUERY = "SELECT * FROM trivia_questions WHERE difficulty = ?";
	
	/**
	 * an instance of the singleton.
	 */
	private static DatabaseManager instance;
	/**
	 * connection to the database.
	 */
	private Connection myConnection;
	/**
	 * The current Difficulty.
	 */
	private Difficulty myDifficulty;

	/**
	 * Private constructor for singleton pattern.
	 */
	private DatabaseManager() {
		initializeDatabase();
	}

	/**
	 * Get the singleton instance of DatabaseManager.
	 */
	public static DatabaseManager getInstance() {
		if (instance == null) {
			instance = new DatabaseManager();
		}
		return instance;
	}

	/**
	 * Initialize the database connection.
	 */
	private void initializeDatabase() {
		try {
			final boolean newDatabase = !new File(DB_PATH).exists();
			myConnection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);

			if (newDatabase) {
				createTables();
			}
		} catch (final SQLException e) {
			System.err.println("Error connecting to database: " + e.getMessage());
		}
	}

	private void createTables() {
		final String sql = "CREATE TABLE IF NOT EXISTS trivia_questions (" + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "question_type TEXT NOT NULL, " + "question TEXT NOT NULL, " + "correct_answer TEXT NOT NULL, "
				+ "wrong_answer1 TEXT, " + "wrong_answer2 TEXT, " + "wrong_answer3 TEXT, "
				+ "difficulty TEXT NOT NULL)";

		try (Statement stmt = myConnection.createStatement()) {
			stmt.execute(sql);
		} catch (final SQLException e) {
			System.err.println("Error creating tables: " + e.getMessage());
		}
	}

	/**
	 * Set the current difficulty level for questions.
	 * 
	 * @param difficulty The difficulty level (EASY, MEDIUM, or HARD).
	 */
	public void setDifficulty(final Difficulty theDifficulty) {
		this.myDifficulty = theDifficulty;
	}

	/**
	 * Get a random question of the current difficulty.
	 * 
	 * @return Question object or null if no questions available
	 */
	public AbstractQuestion getRandomQuestion() {
		final String sql = BASE_QUESTION_QUERY + " ORDER BY RANDOM() LIMIT 1";

		try (PreparedStatement pstmt = myConnection.prepareStatement(sql)) {
			pstmt.setString(1, myDifficulty.toString());
			final ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return createQuestionFromResultSet(rs);
			}
		} catch (final SQLException e) {
		    System.err.println(ERROR_GETTING_QUESTION + e.getMessage());
		}

		return null;
	}

	/**
	 * Get a random question of the current difficulty.
	 * 
	 * @return Question object or null if no questions available
	 */
	public ArrayList<AbstractQuestion> getArrayList() {
		final String sql = BASE_QUESTION_QUERY + " ORDER BY question LIMIT " + MAX_QUESTIONS_PER_DIFFICULTY;

		try (PreparedStatement pstmt = myConnection.prepareStatement(sql)) {
			pstmt.setString(1, myDifficulty.toString());
			final ResultSet rs = pstmt.executeQuery();
			final ArrayList<AbstractQuestion> arr = new ArrayList<AbstractQuestion>();
			for (int i = 0; i < MAX_QUESTIONS_PER_DIFFICULTY; i++) {
			    if (rs.next()) {
			        arr.add(createQuestionFromResultSet(rs));
			    }
			}
			return arr;
		} catch (final SQLException e) {
		    System.err.println(ERROR_GETTING_QUESTION + e.getMessage());
		}

		return null;
	}

	/**
	 * Create appropriate Question object from database result.
	 */
	private AbstractQuestion createQuestionFromResultSet(final ResultSet theRS) throws SQLException {
		final String questionType = theRS.getString("question_type");
		final String questionText = theRS.getString("question");
		final String correctAnswer = theRS.getString("correct_answer");

		switch (questionType.toUpperCase()) {
		case "MULTIPLE_CHOICE":
			final List<String> answers = new ArrayList<>();
			answers.add(correctAnswer);
			answers.add(theRS.getString("wrong_answer1"));
			answers.add(theRS.getString("wrong_answer2"));
			answers.add(theRS.getString("wrong_answer3"));
			// Shuffle answers for randomized order
			shuffleAnswers(answers);
			return new MultipleChoiceQuestion(questionText, correctAnswer, answers.toArray(new String[answers.size()]));

		case "TRUE_FALSE":
			return new TrueFalseQuestion(questionText, Boolean.valueOf(correctAnswer));

		case "SHORT_ANSWER":
			return new ShortAnswerQuestion(questionText, correctAnswer);

		default:
			throw new SQLException("Unknown question type: " + questionType);
		}
	}

	/**
	 * Shuffle the answers for multiple choice questions.
	 */
	private void shuffleAnswers(final List<String> theAnswers) {
		final Random rand = new Random();
		for (int i = theAnswers.size() - 1; i > 0; i--) {
			final int j = rand.nextInt(i + 1);
			final String temp = theAnswers.get(i);
			theAnswers.set(i, theAnswers.get(j));
			theAnswers.set(j, temp);
		}
	}

	/**
	 * Check if the database has questions for the current difficulty.
	 * 
	 * @return true if questions exist, false otherwise
	 */
	public boolean hasQuestionsForDifficulty() {
		try (PreparedStatement pstmt = myConnection.prepareStatement(QUESTION_ACCESS)) {
			pstmt.setString(1, myDifficulty.toString());
			final ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (final SQLException e) {
		    System.err.println(ERROR_GETTING_QUESTION + e.getMessage());
		}

		return false;
	}

	/**
	 * Get the current difficulty level.
	 * 
	 * @return The current difficulty
	 */
	public Difficulty getCurrentDifficulty() {
		return myDifficulty;
	}

	/**
	 * Close the database connection.
	 */
	public void closeConnection() {
		try {
			if (myConnection != null && !myConnection.isClosed()) {
				myConnection.close();
			}
		} catch (final SQLException e) {
			System.err.println("Error closing database connection: " + e.getMessage());
		}
	}

	/**
	 * Test the database connection.
	 * 
	 * @return true if connected, false otherwise
	 */
	public boolean isConnected() {
		try {
			return myConnection != null && !myConnection.isClosed();
		} catch (final SQLException e) {
			return false;
		}
	}

	/**
	 * Get total question count for current difficulty.
	 * 
	 * @return number of questions available.
	 */
	public int getQuestionCount() {
		try (PreparedStatement pstmt = myConnection.prepareStatement(QUESTION_ACCESS)) {
			pstmt.setString(1, myDifficulty.toString());
			final ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (final SQLException e) {
			System.err.println("Error getting question count: " + e.getMessage());
		}

		return 0;
	}

	/**
	 * Properly close database resources when application shuts down. Should be
	 * called during application shutdown.
	 */
	public void shutdown() {
		closeConnection();
	}
}