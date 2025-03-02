package controller;

import model.DatabaseManager;
import model.AbstractQuestion;
import model.Difficulty;
import model.Direction;
import model.Maze;
import model.Room;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * SystemControl serves as the main controller in the MVC pattern,
 * managing game flow and communication between model and view.
 * 
 * @author Team 5
 * @version 1.0
 */
public final class SystemControl {
    private static final Logger LOGGER = Logger.getLogger(SystemControl.class.getName());
    
    // Singleton instance
    private static SystemControl instance;
    
    // Model components
    private final DatabaseManager databaseManager;
    private boolean gameActive;
    
    /**
     * Private constructor for singleton pattern.
     */
    private SystemControl() {
        databaseManager = DatabaseManager.getInstance();
        gameActive = false;
    }
    
    /**
     * Get the singleton instance of SystemControl.
     * @return SystemControl instance
     */
    public static synchronized SystemControl getInstance() {
        if (instance == null) {
            instance = new SystemControl();
        }
        return instance;
    }
    
    /**
     * Initialize a new game with specified difficulty.
     * @param difficulty The game difficulty level
     * @return true if initialization successful
     */
    public boolean initializeGame(Difficulty difficulty) {
        try {
            databaseManager.setDifficulty(difficulty);
            if (!databaseManager.hasQuestionsForDifficulty()) {
                LOGGER.warning("No questions available for difficulty: " + difficulty);
                return false;
            }
            

            gameActive = true;
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize game", e);
            return false;
        }
    }
    
//    /**
//     * Handles player movement attempt in a direction.
//     * @param direction The direction to move
//     * @return true if movement successful
//     */
//    public boolean attemptMove(Direction direction) {
//        if (!gameActive) {
//            return false;
//        }
//        
//        Room nextRoom = maze.getRoom(direction);
//        if (nextRoom == null || !canEnterRoom(nextRoom)) {
//            return false;
//        }
//        
//        AbstractQuestion question = databaseManager.getRandomQuestion();
//        if (question == null) {
//            LOGGER.warning("Failed to get question for room entry");
//            return false;
//        }
//        
//        // The view will need to handle displaying the question and getting the answer
//        return true;
//    }
    
//    /**
//     * Processes the answer to a question.
//     * @param question The question being answered
//     * @param answer The player's answer
//     * @return true if answer is correct
//     */
//    public boolean processAnswer(AbstractQuestion question, String answer) {
//        if (!gameActive || question == null) {
//            return false;
//        }
//        
//        boolean correct = question.isCorrect(answer);
//        if (correct) {
//            moveToNextRoom();
//        } else {
//            handleIncorrectAnswer();
//        }
//        
//        return correct;
//    }
    
    /**
     * Checks if the game has been won.
     * @return true if player has reached the end room
     */
    public boolean checkWinCondition() {
        //return gameActive && currentRoom != null && currentRoom.isEndRoom();
    	return false;
    	//TODO
    }
    
    /**
     * Checks if the game has been lost.
     * @return true if player can no longer reach the end
     */
    public boolean checkLoseCondition() {
        //return gameActive && !maze.hasValidPathToEnd();
    	return false;
    	//TODO
    }
    
//    /**
//     * Updates game state after correct answer and movement.
//     */
//    private void moveToNextRoom() {
//        if (currentRoom != null) {
//            currentRoom = maze.getCurrentRoom();
//            if (checkWinCondition()) {
//                gameActive = false;
//                // View should be notified of win
//            }
//        }
//    }
    
//    /**
//     * Handles consequences of incorrect answer.
//     */
//    private void handleIncorrectAnswer() {
//        if (currentRoom != null) {
//            currentRoom.lockDoor(maze.getLastAttemptedDirection());
//            if (checkLoseCondition()) {
//                gameActive = false;
//                // View should be notified of loss
//            }
//        }
//    }
    
//    /**
//     * Checks if a room can be entered.
//     * @param room The room to check
//     * @return true if room can be entered
//     */
//    private boolean canEnterRoom(Room room) {
//        return room != null && !room.isLocked();
//    }
//    
    /**
     * Gets the current game difficulty.
     * @return current Difficulty
     */
    public Difficulty getCurrentDifficulty() {
        return databaseManager.getCurrentDifficulty();
    }
    
    /**
     * Checks if a game is currently active.
     * @return true if game is active
     */
    public boolean isGameActive() {
        return gameActive;
    }
    
    /**
     * Ends the current game session.
     */
    public void endGame() {
        gameActive = false;
        // Additional cleanup as needed
    }

	public static boolean triggerQuestion() {
		// TODO Auto-generated method stub
		return false;
	}
}