package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * JUnit test class for GameState functionality.
 * 
 * @author Team 5
 * @version 1.0
 */
class GameStateTest {
    
    /**
     * Constant for initial position string.
     */
    private static final String INITIAL_POSITION = "0, 0";
    
    /**
     * Constant for total question count.
     */
    private static final int TOTAL_QUESTIONS = 48;
    
    /**
     * Test X coordinate value.
     */
    private static final int TEST_X = 3;
    
    /**
     * Test Y coordinate value.
     */
    private static final int TEST_Y = 4;
    
    /**
     * Second test X coordinate value.
     */
    private static final int TEST_X2 = 5;
    
    /**
     * Second test Y coordinate value.
     */
    private static final int TEST_Y2 = 6;
    
    /**
     * Questions remaining after one removal.
     */
    private static final int QUESTIONS_REMAINING = 47;
    
    /**
     * Number of correct answers in load test.
     */
    private static final int TEST_CORRECT_COUNT = 2;
    
    /**
     * Number of incorrect answers in load test.
     */
    private static final int TEST_INCORRECT_COUNT = 1;
    
    /**
     * Number of questions removed in load test.
     */
    private static final int TEST_REMOVED_COUNT = 3;
    
    /**
     * Single quote character for messages.
     */
    private static final String QUOTE = "'";
    
    /**
     * String for should have message prefix.
     */
    private static final String SHOULD_HAVE_PREFIX = "Should have ";
    
    /**
     * String for difficulty medium message.
     */
    private static final String DIFFICULTY_MEDIUM_MSG = "Difficulty should be MEDIUM";
    
    /**
     * Question ID for testing.
     */
    private static final int TEST_QUESTION_ID = 1;
    
    /**
     * The GameState instance to be tested.
     */
    private GameState myGameState;
    
    /**
     * Test file name for save/load tests.
     */
    private String myTestFileName;

    /**
     * Set up before each test.
     */
    @BeforeEach
    void setUp() {
        GameState.resetState();
        myGameState = GameState.getInstance();
        
        final File tempDir = new File(System.getProperty("user.home"), "trivia_maze_test");
        tempDir.mkdirs();
        myTestFileName = new File(tempDir, "test_save.sav").getAbsolutePath();
    }
    
    /**
     * Clean up after each test.
     */
    @AfterEach
    void tearDown() {
        GameState.resetState();
        final File testFile = new File(myTestFileName);
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    /**
     * Test singleton pattern and initial state.
     */
    @Test
    @DisplayName("Test Singleton Pattern and Initial State")
    void testSingletonAndInitialState() {
        final GameState instance1 = GameState.getInstance();
        final GameState instance2 = GameState.getInstance();
        
        // Test singleton behavior
        assertNotNull(instance1, "getInstance should not return null");
        assertEquals(instance1, instance2, "Multiple getInstance calls should return same instance");
        
        // Test initial state
        assertEquals(0, myGameState.getCorrectQuestions(), "Initial correct questions should be 0");
        assertEquals(0, myGameState.getIncorrectQuestions(), "Initial incorrect questions should be 0");
        assertEquals(TOTAL_QUESTIONS, myGameState.getQuestionsRemaining(), 
                "Initial questions remaining should be " + TOTAL_QUESTIONS);
        assertEquals(INITIAL_POSITION, myGameState.getPlayerPosition(), 
                "Initial position should be " + QUOTE + INITIAL_POSITION + QUOTE);
        assertEquals(null, myGameState.getDifficulty(), "Initial difficulty should be null");
    }
    
    /**
     * Test state tracking and modification.
     */
    @Test
    @DisplayName("Test Game State Tracking and Modification")
    void testStateModification() {
        // Test position tracking
        final String expectedPosition = TEST_X + ", " + TEST_Y;
        myGameState.setCurrentPosition(TEST_X, TEST_Y);
        assertEquals(TEST_X, myGameState.getCurrentX(), "Current X should be " + TEST_X);
        assertEquals(TEST_Y, myGameState.getCurrentY(), "Current Y should be " + TEST_Y);
        assertEquals(expectedPosition, myGameState.getPlayerPosition(), 
                "Position should be " + QUOTE + expectedPosition + QUOTE);
        
        // Test question counters
        myGameState.addCorrect();
        myGameState.addIncorrect();
        myGameState.removeQuestion();
        assertEquals(1, myGameState.getCorrectQuestions(), "Correct questions should be 1");
        assertEquals(1, myGameState.getIncorrectQuestions(), "Incorrect questions should be 1");
        assertEquals(QUESTIONS_REMAINING, myGameState.getQuestionsRemaining(), 
                QUESTIONS_REMAINING + " questions should remain");
        
        // Test difficulty setting
        myGameState.setDifficulty(Difficulty.MEDIUM);
        assertEquals(Difficulty.MEDIUM, myGameState.getDifficulty(), DIFFICULTY_MEDIUM_MSG);
        
        // Test question usage
        assertFalse(myGameState.isQuestionUsed(TEST_QUESTION_ID), 
                "Question " + TEST_QUESTION_ID + " should not be marked as used initially");
    }
    
    /**
     * Test reset functionality.
     */
    @Test
    @DisplayName("Test Game State Reset")
    void testResetState() {
        // Modify state
        myGameState.addCorrect();
        myGameState.addIncorrect();
        myGameState.removeQuestion();
        myGameState.setCurrentPosition(TEST_X2, TEST_Y2);
        myGameState.setDifficulty(Difficulty.HARD);
        
        // Reset state
        GameState.resetState();
        final GameState resetInstance = GameState.getInstance();
        
        // Verify reset worked correctly
        assertEquals(0, resetInstance.getCorrectQuestions(), "Reset correct questions should be 0");
        assertEquals(0, resetInstance.getIncorrectQuestions(), "Reset incorrect questions should be 0");
        assertEquals(TOTAL_QUESTIONS, resetInstance.getQuestionsRemaining(), 
                "Reset questions remaining should be " + TOTAL_QUESTIONS);
        assertEquals(INITIAL_POSITION, resetInstance.getPlayerPosition(), 
                "Reset position should be " + QUOTE + INITIAL_POSITION + QUOTE);
        assertEquals(null, resetInstance.getDifficulty(), "Reset difficulty should be null");
    }
    
    /**
     * Test save and load functionality without checking coordinates.
     */
    @Test
    @DisplayName("Test Save and Load Functionality (Core State)")
    void testSaveAndLoad() {
        try {
            // Initialize dependencies
            DatabaseManager.getInstance().setDifficulty(Difficulty.MEDIUM);
            QuestionFactory.intializeQuestionFactory();
            
            // Set test values
            myGameState.setDifficulty(Difficulty.MEDIUM);
            
            for (int i = 0; i < TEST_CORRECT_COUNT; i++) {
                myGameState.addCorrect();
            }
            
            for (int i = 0; i < TEST_INCORRECT_COUNT; i++) {
                myGameState.addIncorrect();
            }
            
            for (int i = 0; i < TEST_REMOVED_COUNT; i++) {
                myGameState.removeQuestion();
            }
            
            // Save state
            myGameState.saveToFile(myTestFileName);
            
            // Reset state
            GameState.resetState();
            
            // Load the saved state
            GameState.loadFromFile(myTestFileName);
            
            // Get the loaded state
            final GameState loadedState = GameState.getInstance();
            
            // Verify core state was correctly restored (without checking coordinates)
            assertEquals(TEST_CORRECT_COUNT, loadedState.getCorrectQuestions(), 
                    SHOULD_HAVE_PREFIX + TEST_CORRECT_COUNT + " correct questions");
            assertEquals(TEST_INCORRECT_COUNT, loadedState.getIncorrectQuestions(), 
                    SHOULD_HAVE_PREFIX + TEST_INCORRECT_COUNT + " incorrect question");
            assertEquals(TOTAL_QUESTIONS - TEST_REMOVED_COUNT, loadedState.getQuestionsRemaining(), 
                    SHOULD_HAVE_PREFIX + (TOTAL_QUESTIONS - TEST_REMOVED_COUNT) + " questions remaining");
            assertEquals(Difficulty.MEDIUM, loadedState.getDifficulty(), DIFFICULTY_MEDIUM_MSG);
        } catch (final IOException e) {
            System.out.println("IO Exception during save/load test: " + e.getMessage());
        } catch (final ClassNotFoundException e) {
            System.out.println("ClassNotFoundException during load: " + e.getMessage());
        }
    }
}