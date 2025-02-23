package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DatabaseManager handles all database operations for the Minecraft Trivia Maze game.
 * Implements both Singleton pattern and proper resource management.
 * @author Team 5
 * @version 1.0
 */
public final class DatabaseManager {
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    private static final String DB_PATH = "minecraft_trivia.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_PATH;
    
    private static DatabaseManager instance;
    private Connection connection;
    private Difficulty currentDifficulty;
    private final Random random;
    
    /**
     * Private constructor for singleton pattern.
     */
    private DatabaseManager() {
        random = new Random();
        initializeDatabase();
    }
    
    /**
     * Get the singleton instance of DatabaseManager.
     * @return DatabaseManager instance
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    /**
     * Initialize the database connection and create tables if they don't exist.
     */
    private void initializeDatabase() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            createTablesIfNotExist();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize database", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    
    /**
     * Create necessary database tables if they don't exist.
     */
    private void createTablesIfNotExist() throws SQLException {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS questions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                question_text TEXT NOT NULL,
                correct_answer TEXT NOT NULL,
                question_type TEXT NOT NULL,
                difficulty TEXT NOT NULL,
                wrong_answer1 TEXT,
                wrong_answer2 TEXT,
                wrong_answer3 TEXT
            )
        """;
        
        try (PreparedStatement stmt = connection.prepareStatement(createTableSQL)) {
            stmt.execute();
        }
    }
    
    /**
     * Set the current difficulty level for questions.
     * @param difficulty The difficulty level
     */
    public void setDifficulty(Difficulty difficulty) {
        this.currentDifficulty = difficulty;
    }
    
    /**
     * Get a random question of the current difficulty.
     * @return AbstractQuestion instance or null if no questions available
     */
    public AbstractQuestion getRandomQuestion() {
        if (currentDifficulty == null) {
            throw new IllegalStateException("Difficulty must be set before getting questions");
        }
        
        String sql = """
            SELECT * FROM questions 
            WHERE difficulty = ? 
            ORDER BY RANDOM() 
            LIMIT 1
        """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, currentDifficulty.name());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createQuestionFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error getting random question", e);
        }
        return null;
    }
    
    /**
     * Get a specific type of question.
     * @param type The type of question to get
     * @return AbstractQuestion of the specified type
     */
    public AbstractQuestion getQuestionByType(String type) {
        String sql = """
            SELECT * FROM questions 
            WHERE difficulty = ? AND question_type = ? 
            ORDER BY RANDOM() 
            LIMIT 1
        """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, currentDifficulty.name());
            stmt.setString(2, type);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createQuestionFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error getting question by type", e);
        }
        return null;
    }
    
    /**
     * Create the appropriate question object from database result.
     */
    private AbstractQuestion createQuestionFromResultSet(ResultSet rs) throws SQLException {
        String questionType = rs.getString("question_type");
        String questionText = rs.getString("question_text");
        String correctAnswer = rs.getString("correct_answer");
        
        return switch (questionType.toUpperCase()) {
            case "MULTIPLE_CHOICE" -> createMultipleChoiceQuestion(rs, questionText, correctAnswer);
            case "TRUE_FALSE" -> new TrueFalseQuestion(questionText, Boolean.parseBoolean(correctAnswer));
            case "SHORT_ANSWER" -> new ShortAnswerQuestion(questionText, correctAnswer);
            default -> throw new SQLException("Unknown question type: " + questionType);
        };
    }
    
    /**
     * Create a multiple choice question with shuffled answers.
     */
    private MultipleChoiceQuestion createMultipleChoiceQuestion(
            ResultSet rs, String questionText, String correctAnswer) throws SQLException {
        List<String> answers = new ArrayList<>();
        answers.add(correctAnswer);
        answers.add(rs.getString("wrong_answer1"));
        answers.add(rs.getString("wrong_answer2"));
        answers.add(rs.getString("wrong_answer3"));
        
        // Remove any null answers
        answers.removeIf(answer -> answer == null || answer.trim().isEmpty());
        
        // Shuffle the answers
        for (int i = answers.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            String temp = answers.get(i);
            answers.set(i, answers.get(j));
            answers.set(j, temp);
        }
        
        return new MultipleChoiceQuestion(questionText, correctAnswer, 
                answers.toArray(new String[0]));
    }
    
    /**
     * Add a new question to the database.
     * @return true if successful, false otherwise
     */
    public boolean addQuestion(String questionText, String correctAnswer, 
            String questionType, Difficulty difficulty, String... wrongAnswers) {
        String sql = """
            INSERT INTO questions (question_text, correct_answer, question_type, 
                difficulty, wrong_answer1, wrong_answer2, wrong_answer3)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, questionText);
            stmt.setString(2, correctAnswer);
            stmt.setString(3, questionType);
            stmt.setString(4, difficulty.name());
            
            // Set wrong answers if provided, otherwise set to null
            for (int i = 0; i < 3; i++) {
                stmt.setString(5 + i, i < wrongAnswers.length ? wrongAnswers[i] : null);
            }
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error adding question", e);
            return false;
        }
    }
    
    /**
     * Get the count of questions for the current difficulty.
     * @return number of questions available
     */
    public int getQuestionCount() {
        if (currentDifficulty == null) {
            return 0;
        }
        
        String sql = "SELECT COUNT(*) FROM questions WHERE difficulty = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, currentDifficulty.name());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error getting question count", e);
        }
        return 0;
    }
    
    /**
     * Check if database has questions for current difficulty.
     * @return true if questions exist, false otherwise
     */
    public boolean hasQuestionsForDifficulty() {
        return getQuestionCount() > 0;
    }
    
    /**
     * Close the database connection.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error closing database connection", e);
        }
    }
    
    /**
     * Check if database is connected.
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    /**
     * Get current difficulty level.
     * @return current Difficulty
     */
    public Difficulty getCurrentDifficulty() {
        return currentDifficulty;
    }
    
    @Override
    protected void finalize() throws Throwable {
        closeConnection();
        super.finalize();
    }
}