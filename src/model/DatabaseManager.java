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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DatabaseManager handles all database operations for the Minecraft Trivia Maze
 * game. Uses SQLite database to store and retrieve questions.
 * @author Nathaniel
 * @version 0.8
 */
public final class DatabaseManager implements AutoCloseable {
    /**
     * Logger for database operations.
     */
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    
    /**
     * path of database.
     */
    private static final String DB_PATH = "minecraft_trivia.db";
    /**
     * A code used to access questions.
     */
    private static final String QUESTION_ACCESS = "SELECT COUNT(*) FROM trivia_questions WHERE difficulty = ?";
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
        registerShutdownHook();
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
            File dbFile = new File(DB_PATH);
            if (!dbFile.exists()) {
                LOGGER.warning("Database file not found: " + DB_PATH);
                myConnection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
                createTables();
            } else {
                LOGGER.info("Connecting to existing database: " + DB_PATH);
                myConnection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            }
            
            // Set default difficulty
            myDifficulty = Difficulty.MEDIUM;
            
            // Verify database connection and content
            try (Statement stmt = myConnection.createStatement()) {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM trivia_questions");
                if (rs.next()) {
                    LOGGER.info("Database contains " + rs.getInt(1) + " total questions");
                }
            } catch (SQLException e) {
                LOGGER.warning("Error checking question count: " + e.getMessage());
            }
            
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Error connecting to database: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create the database tables if they don't exist.
     */
    private void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS trivia_questions (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "question_type TEXT NOT NULL, " +
                     "question TEXT NOT NULL, " +
                     "correct_answer TEXT NOT NULL, " +
                     "wrong_answer1 TEXT, " +
                     "wrong_answer2 TEXT, " +
                     "wrong_answer3 TEXT, " +
                     "difficulty TEXT NOT NULL)";
        
        try (Statement stmt = myConnection.createStatement()) {
            stmt.execute(sql);
            LOGGER.info("Database tables created successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating tables: " + e.getMessage(), e);
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
        final String sql = "SELECT * FROM trivia_questions WHERE difficulty = ? ORDER BY RANDOM() LIMIT 1";

        try (PreparedStatement pstmt = myConnection.prepareStatement(sql)) {
            pstmt.setString(1, myDifficulty.toString());
            final ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return createQuestionFromResultSet(rs);
            }
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting random question: " + e.getMessage(), e);
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
            
            // Add null check for optional wrong answers
            String wrong2 = theRS.getString("wrong_answer2");
            String wrong3 = theRS.getString("wrong_answer3");
            
            if (wrong2 != null && !wrong2.isEmpty()) {
                answers.add(wrong2);
            }
            
            if (wrong3 != null && !wrong3.isEmpty()) {
                answers.add(wrong3);
            }
            
            // Shuffle answers for randomized order
            shuffleAnswers(answers);
            return new MultipleChoiceQuestion(questionText, correctAnswer, 
                    answers.toArray(new String[answers.size()]));

        case "TRUE_FALSE":
            return new TrueFalseQuestion(questionText, Boolean.valueOf(correctAnswer));

        case "SHORT_ANSWER":
            return new ShortAnswerQuestion(questionText, correctAnswer);

        default:
            LOGGER.warning("Unknown question type: " + questionType);
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
            LOGGER.log(Level.SEVERE, "Error checking questions: " + e.getMessage(), e);
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
            LOGGER.log(Level.SEVERE, "Error getting question count: " + e.getMessage(), e);
        }

        return 0;
    }
    
    /**
     * Register a shutdown hook to ensure database connection is closed when the 
     * application exits.
     */
    private void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::closeConnection));
    }
    
    /**
     * Close the database connection.
     */
    private void closeConnection() {
        try {
            if (myConnection != null && !myConnection.isClosed()) {
                myConnection.close();
            }
        } catch (final SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing database connection: " + e.getMessage(), e);
        }
    }
    
    /**
     * Implements AutoCloseable interface to support try-with-resources.
     */
    @Override
    public void close() {
        closeConnection();
    }
}