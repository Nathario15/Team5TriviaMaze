package model;

/**
 * Test class for DatabaseManager functionality.
 * Run this class to verify database operations.
 * @author Team 5
 * @version 1.0
 */
public final class DatabaseTest {
	
	private DatabaseTest() {
		
	}
    
    /**
     * Main method to test database operations.
     * 
     * @param args command line arguments
     */
    public static void main(final String[] theArgs) {
        System.out.println("Testing Database Manager...");
        
        // Get database instance
        final DatabaseManager dbManager = DatabaseManager.getInstance();
        
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
     * @param theDBManager the database manager
     * @param theDifficulty the difficulty to test
     */
    private static void testDifficulty(final DatabaseManager theDBManager, final Difficulty theDifficulty) {
        theDBManager.setDifficulty(theDifficulty);
        System.out.println("\nTesting " + theDifficulty + " difficulty:");
        System.out.println("Questions available: " + theDBManager.getQuestionCount());
        
        // Test getting a random question
        final AbstractQuestion q = theDBManager.getRandomQuestion();
        if (q != null) {
            System.out.println("Sample question: " + q.myQuestion);
            System.out.println("Correct answer: " + q.myAnswer);
            
            // Test specific question types
            if (q instanceof MultipleChoiceQuestion) {
                final MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) q;
                System.out.println("Question type: Multiple Choice");
                System.out.println("Choices: " + String.join(", ", mcq.getChoices()));
            } else if (q instanceof TrueFalseQuestion) {
                System.out.println("Question type: True/False");
            } else if (q instanceof ShortAnswerQuestion) {
                System.out.println("Question type: Short Answer");
            }
            
            // Test answer verification
            final boolean correctResult = q.isCorrect(q.myAnswer);
            final boolean incorrectResult = q.isCorrect("Definitely wrong answer");
            System.out.println("Correct answer test: " + correctResult);
            System.out.println("Incorrect answer test: " + incorrectResult);
        } else {
            System.out.println("No questions found for " + theDifficulty + " difficulty");
        }
    }
}