package model;

/**
 * Test class for DatabaseManager functionality.
 * Run this class to verify database operations.
 */
public class DatabaseTest {
    
    /**
     * Main method to test database operations.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Testing Database Manager...");
        
        // Get database instance
        DatabaseManager dbManager = DatabaseManager.getInstance();
        
        // Test connection
        System.out.println("Connected to database: " + dbManager.isConnected());
        
        // Test each difficulty level
        testDifficulty(dbManager, Difficulty.EASY);
        testDifficulty(dbManager, Difficulty.MEDIUM);
        testDifficulty(dbManager, Difficulty.HARD);
        
        System.out.println("Database testing complete.");
    }
    
    /**
     * Test a specific difficulty level.
     * 
     * @param dbManager the database manager
     * @param difficulty the difficulty to test
     */
    private static void testDifficulty(DatabaseManager dbManager, Difficulty difficulty) {
        dbManager.setDifficulty(difficulty);
        System.out.println("\nTesting " + difficulty + " difficulty:");
        System.out.println("Questions available: " + dbManager.getQuestionCount());
        
        // Test getting a random question
        AbstractQuestion q = dbManager.getRandomQuestion();
        if (q != null) {
            System.out.println("Sample question: " + q.myQuestion);
            System.out.println("Correct answer: " + q.myAnswer);
            
            // Test specific question types
            if (q instanceof MultipleChoiceQuestion) {
                MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;
                System.out.println("Question type: Multiple Choice");
                System.out.println("Choices: " + String.join(", ", mcq.getChoices()));
            } else if (q instanceof TrueFalseQuestion) {
                System.out.println("Question type: True/False");
            } else if (q instanceof ShortAnswerQuestion) {
                System.out.println("Question type: Short Answer");
            }
            
            // Test answer verification
            boolean correctResult = q.isCorrect(q.myAnswer);
            boolean incorrectResult = q.isCorrect("Definitely wrong answer");
            System.out.println("Correct answer test: " + correctResult);
            System.out.println("Incorrect answer test: " + incorrectResult);
        } else {
            System.out.println("No questions found for " + difficulty + " difficulty");
        }
    }
}