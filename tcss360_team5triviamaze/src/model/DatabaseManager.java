package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    // Database connection
    private Connection connection;
    private static final String DB_PATH = "minecraft_trivia.db";

    /**
     * Constructor - initializes the database connection
     */
    public DatabaseManager() {
        initializeDatabase();
    }

    /**
     * Initialize the database connection and create tables if they don't exist
     */
    private void initializeDatabase() {
        try {
            // TODO: Create database connection using DriverManager.getConnection()
            
            // Create tables if they do not exist
            createTables();
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    /**
     * Create the necessary tables in the database
     */
    private void createTables() {
        // TODO: Write SQL to create table(s) for storing questions
        // I need columns for:
        // - question text
        // - correct answer
        // - question type (multiple choice, true/false, short answer)
        // - wrong answers (for multiple choice)
    }

    /**
     * Check if the database is empty
     */
    private boolean isDatabaseEmpty() {
        // TODO: Write SQL to count rows in questions table
        return true; // placeholder return
    }

    /**
     * Add some sample questions to the database
     */
    private void addSampleQuestions() {
        // TODO: Write SQL INSERT statements to add some initial questions
        // Remember to add different types (multiple choice, true/false, short answer)
    }

    /**
     * Get a random question from the database
     */
    public Question getRandomQuestion() {
        // TODO: Write SQL to select a random question
        return null; // placeholder return
    }

    /**
     * Add a new multiple choice question
     */
    public void addMultipleChoiceQuestion(String question, String correctAnswer, 
                                        String wrongAnswer1, String wrongAnswer2, String wrongAnswer3) {
        // TODO: Write SQL to insert a multiple choice question
    }

    /**
     * Add a new true/false question
     */
    public void addTrueFalseQuestion(String question, boolean correctAnswer) {
        // TODO: Write SQL to insert a true/false question
    }

    /**
     * Add a new short answer question
     */
    public void addShortAnswerQuestion(String question, String correctAnswer) {
        // TODO: Write SQL to insert a short answer question
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
}
