package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * JUnit test class for DatabaseManager functionality.
 * Includes console output to show test progress and results.
 */
public class DatabaseTest {
    
    private static DatabaseManager myDatabaseManager;
    
    @BeforeAll
    public static void setUp() {
        System.out.println("Testing Database Manager...");
        myDatabaseManager = DatabaseManager.getInstance();
    }
    
    @BeforeEach
    void displayTestInfo(TestInfo testInfo) {
        System.out.println("\n-----------------------------------------");
        System.out.println("Executing: " + testInfo.getDisplayName());
        System.out.println("-----------------------------------------");
    }
    
    @Test
    @DisplayName("Test database connection")
    public void testConnection() {
        boolean isConnected = myDatabaseManager.isConnected();
        System.out.println("Connected to database: " + isConnected);
        assertTrue(isConnected, "Database should be connected");
    }
    
    @ParameterizedTest
    @EnumSource(Difficulty.class)
    @DisplayName("Test questions available for each difficulty")
    public void testQuestionsAvailable(Difficulty difficulty) {
        myDatabaseManager.setDifficulty(difficulty);
        int count = myDatabaseManager.getQuestionCount();
        System.out.println("Testing " + difficulty + " difficulty:");
        System.out.println("Questions available: " + count);
        assertTrue(count > 0, "There should be questions available for " + difficulty + " difficulty");
    }
    
    @ParameterizedTest
    @EnumSource(Difficulty.class)
    @DisplayName("Test random question retrieval for each difficulty")
    public void testRandomQuestion(Difficulty difficulty) {
        myDatabaseManager.setDifficulty(difficulty);
        AbstractQuestion question = myDatabaseManager.getRandomQuestion();
        
        System.out.println("Testing " + difficulty + " difficulty question retrieval:");
        
        if (question != null) {
            System.out.println("Sample question: " + question.getQuestion());
            System.out.println("Correct answer: " + question.getAnswer());
            
            if (question instanceof MultipleChoiceQuestion) {
                MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
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
            System.out.println("No questions found for " + difficulty + " difficulty");
            fail("No question retrieved for " + difficulty + " difficulty");
        }
    }
    
    @ParameterizedTest
    @EnumSource(Difficulty.class)
    @DisplayName("Test answer verification for questions")
    public void testAnswerVerification(Difficulty difficulty) {
        myDatabaseManager.setDifficulty(difficulty);
        AbstractQuestion question = myDatabaseManager.getRandomQuestion();
        
        System.out.println("Testing " + difficulty + " difficulty answer verification:");
        
        if (question != null) {
            boolean correctResult = question.isCorrect(question.getAnswer());
            boolean incorrectResult = question.isCorrect("Definitely wrong answer");
            
            System.out.println("Question: " + question.getQuestion());
            System.out.println("Correct answer test: " + correctResult);
            System.out.println("Incorrect answer test: " + incorrectResult);
            
            assertTrue(correctResult, "Correct answer should be verified as correct");
            assertFalse(incorrectResult, "Incorrect answer should be verified as incorrect");
        } else {
            System.out.println("No questions found for " + difficulty + " difficulty");
            fail("No question retrieved for " + difficulty + " difficulty");
        }
    }
    
    @Test
    @DisplayName("Test overall database functionality summary")
    public void testDatabaseSummary() {
        System.out.println("\nDatabase testing summary:");
        System.out.println("=======================");
        
        for (Difficulty difficulty : Difficulty.values()) {
            myDatabaseManager.setDifficulty(difficulty);
            int count = myDatabaseManager.getQuestionCount();
            System.out.println(difficulty + " questions: " + count);
        }
        
        System.out.println("Database connection: " + (myDatabaseManager.isConnected() ? "ACTIVE" : "INACTIVE"));
        System.out.println("Database testing complete.");
    }
}