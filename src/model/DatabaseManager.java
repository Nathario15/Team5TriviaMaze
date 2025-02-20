package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * DatabaseManager handles all database operations for the Minecraft Trivia Maze game.
 * Uses Microsoft Access database to store and retrieve questions.
 */
public class DatabaseManager {
    private Connection connection;
    private static final String DB_PATH = "minecraft_trivia.accdb";
    private String currentDifficulty;
    private static DatabaseManager instance;

    /**
     * Private constructor for singleton pattern
     */
    private DatabaseManager() {
        initializeDatabase();
    }

    /**
     * Get the singleton instance of DatabaseManager
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Initialize the database connection
     */
    private void initializeDatabase() {
        try {
            String url = "jdbc:ucanaccess://" + DB_PATH;
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
        }
    }

    /**
     * Set the current difficulty level for questions
     * @param difficulty The difficulty level (EASY, MEDIUM, or HARD)
     */
    public void setDifficulty(String difficulty) {
        this.currentDifficulty = difficulty.toUpperCase();
    }

    /**
     * Get a random question of the current difficulty
     * @return Question object or null if no questions available
     */
    public AbstractQuestion getRandomQuestion() {
        String sql = "SELECT TOP 1 * FROM trivia_questions WHERE difficulty = ? ORDER BY Rnd()";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, currentDifficulty);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createQuestionFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting random question: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Create appropriate Question object from database result
     */
    private AbstractQuestion createQuestionFromResultSet(ResultSet rs) throws SQLException {
        String questionType = rs.getString("question_type");
        String questionText = rs.getString("question");
        String correctAnswer = rs.getString("correct_answer");
        
        switch (questionType.toUpperCase()) {
            case "MULTIPLE_CHOICE":
                List<String> answers = new ArrayList<>();
                answers.add(correctAnswer);
                answers.add(rs.getString("wrong_answer1"));
                answers.add(rs.getString("wrong_answer2"));
                answers.add(rs.getString("wrong_answer3"));
                // Shuffle answers for randomized order
                shuffleAnswers(answers);
                return new MultipleChoiceQuestion(questionText, correctAnswer, (String[]) answers.toArray());
                
            case "TRUE_FALSE":
                return new TrueFalseQuestion(questionText, Boolean.valueOf(correctAnswer));
                
            case "SHORT_ANSWER":
                return new ShortAnswerQuestion(questionText, correctAnswer);
                
            default:
                throw new SQLException("Unknown question type: " + questionType);
        }
    }

    /**
     * Shuffle the answers for multiple choice questions
     */
    private void shuffleAnswers(List<String> answers) {
        Random rand = new Random();
        for (int i = answers.size() - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            String temp = answers.get(i);
            answers.set(i, answers.get(j));
            answers.set(j, temp);
        }
    }

    /**
     * Check if the database has questions for the current difficulty
     * @return true if questions exist, false otherwise
     */
    public boolean hasQuestionsForDifficulty() {
        String sql = "SELECT COUNT(*) FROM trivia_questions WHERE difficulty = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, currentDifficulty);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking questions: " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Get the current difficulty level
     * @return The current difficulty
     */
    public String getCurrentDifficulty() {
        return currentDifficulty;
    }

    /**
     * Close the database connection
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    /**
     * Test the database connection
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
     * Get total question count for current difficulty
     * @return number of questions available
     */
    public int getQuestionCount() {
        String sql = "SELECT COUNT(*) FROM trivia_questions WHERE difficulty = ?";
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, currentDifficulty);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting question count: " + e.getMessage());
        }
        
        return 0;
    }

    /**
     * Finalize method to ensure database connection is closed
     */
    @Override
    protected void finalize() throws Throwable {
        closeConnection();
        super.finalize();
    }
}