package model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * JUnit test class for DatabaseManager functionality.
 * Includes console output to show test progress and results.
 * @author Team 5
 * @version 1.0
 */
public class DatabaseTest {
    /**
     * database.
     */
    private static DatabaseManager myDatabaseManager;
    
    /**
     * sets up.
     */
    
    /**
     * String constant for "Testing ".
     */
    private static final String TESTING_PREFIX = "Testing ";
    
    /**
     * String constant for " difficulty".
     */
    private static final String DIFFICULTY_SUFFIX = " difficulty";
    
    /**
     * String constant for "No questions found for ".
     */
    private static final String NO_QUESTIONS_FOUND = "No questions found for ";
    
    /**
     * sets up.
     */
    @BeforeAll
    public static void setUp() {
        System.out.println("Testing Database Manager...");
        myDatabaseManager = DatabaseManager.getInstance();
    }
    
    @BeforeEach
    void displayTestInfo(final TestInfo theInfo) {
        System.out.println("\n-----------------------------------------");
        System.out.println("Executing: " + theInfo.getDisplayName());
        System.out.println("-----------------------------------------");
    }
    /**
     * tests connection.
     */
    @Test
    @DisplayName("Test database connection")
    public void testConnection() {
        final boolean isConnected = myDatabaseManager.isConnected();
        System.out.println("Connected to database: " + isConnected);
        assertTrue(isConnected, "Database should be connected");
    }
     
    /**
     * sees available questions.
     * @param theDifficulty
     */
    @ParameterizedTest
    @EnumSource(Difficulty.class)
    @DisplayName("Test questions available for each difficulty")
    public void testQuestionsAvailable(final Difficulty theDifficulty) {
        myDatabaseManager.setDifficulty(theDifficulty);
        final int count = myDatabaseManager.getQuestionCount();
        System.out.println(TESTING_PREFIX + theDifficulty + DIFFICULTY_SUFFIX + ":");
        System.out.println("Questions available: " + count);
        assertTrue(count > 0, "There should be questions available for " + theDifficulty + DIFFICULTY_SUFFIX);
    }
    
    /**
     * 
     * @param theDifficulty
     */
    @ParameterizedTest
    @EnumSource(Difficulty.class)
    @DisplayName("Test random question retrieval for each difficulty")
    public void testRandomQuestion(final Difficulty theDifficulty) {
        myDatabaseManager.setDifficulty(theDifficulty);
        final AbstractQuestion question = myDatabaseManager.getRandomQuestion();
        
        System.out.println(TESTING_PREFIX + theDifficulty + DIFFICULTY_SUFFIX + " question retrieval:");
        
        if (question != null) {
            System.out.println("Sample question: " + question.getQuestion());
            System.out.println("Correct answer: " + question.getAnswer());
            
            if (question instanceof MultipleChoiceQuestion) {
                final MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
                System.out.println("Question type: Multiple Choice");
                System.out.println("Choices: " + String.join(", ", mcq.getChoices()));
            } else if (question instanceof TrueFalseQuestion) {
                System.out.println("Question type: True/False");
            } else if (question instanceof ShortAnswerQuestion) {
                System.out.println("Question type: Short Answer");
            }
            
            assertNotNull(question, "Should retrieve a non-null question");
            assertNotNull(question.getQuestion(), "Question text should not be null");
            assertNotNull(question.getAnswer(), "Answer should not be null");
        } else {
            final String str = NO_QUESTIONS_FOUND + theDifficulty + DIFFICULTY_SUFFIX;
            System.out.println(str);
            fail(str);
        }
    }
    
    /**
     * 
     * @param theDifficulty
     */
    @ParameterizedTest
    @EnumSource(Difficulty.class)
    @DisplayName("Test answer verification for questions")
    public void testAnswerVerification(final Difficulty theDifficulty) {
        myDatabaseManager.setDifficulty(theDifficulty);
        final AbstractQuestion question = myDatabaseManager.getRandomQuestion();
        
        System.out.println(TESTING_PREFIX + theDifficulty + DIFFICULTY_SUFFIX + " answer verification:");
        
        if (question != null) {
            final boolean correctResult = question.isCorrect(question.getAnswer());
            final boolean incorrectResult = question.isCorrect("Definitely wrong answer");
            
            System.out.println("Question: " + question.getQuestion());
            System.out.println("Correct answer test: " + correctResult);
            System.out.println("Incorrect answer test: " + incorrectResult);
            
            assertTrue(correctResult, "Correct answer should be verified as correct");
            assertFalse(incorrectResult, "Incorrect answer should be verified as incorrect");
        } else {
            System.out.println(NO_QUESTIONS_FOUND + theDifficulty + DIFFICULTY_SUFFIX);
            fail("No question retrieved for " + theDifficulty + DIFFICULTY_SUFFIX);
        }
    }
    
    /**
     * Tests the database summary.
     */
    @Test
    @DisplayName("Test overall database functionality summary")
    public void testDatabaseSummary() {
        System.out.println("\nDatabase testing summary:");
        System.out.println("=======================");
        
        for (Difficulty difficulty : Difficulty.values()) {
            myDatabaseManager.setDifficulty(difficulty);
            final int count = myDatabaseManager.getQuestionCount();
            System.out.println(difficulty + " questions: " + count);
        }
        
        final String connectionStatus;
        if (myDatabaseManager.isConnected()) {
            connectionStatus = "ACTIVE";
        } else {
            connectionStatus = "INACTIVE";
        }
        System.out.println("Database connection: " + connectionStatus);
        System.out.println("Database testing complete.");
    }
}