package controller;

import model.AbstractQuestion;
import model.DatabaseManager;
import model.Direction;
import model.Difficulty;
import model.DoorState;
import model.Maze;
import model.MultipleChoiceQuestion;
import model.QuestionFactory;
import model.Room;
import model.ShortAnswerQuestion;
import model.TrueFalseQuestion;
import view.MultipleChoiceQuestionPanel;
import view.QuestionPanel;
import view.ShortAnswerQuestionPanel;
import view.TrueFalseQuestionPanel;

import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.LinkedList;
//import java.util.Queue;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * SystemControl serves as the main controller in the MVC pattern,
 * managing game flow and communication between model and view.
 * 
 * @author Team 5
 * @version 1.0
 */
public final class SystemControl {
    /** Logger for this class. */
    private static final Logger LOGGER = Logger.getLogger(SystemControl.class.getName());
    
    /** Singleton instance. */
    private static SystemControl myInstance;
    
    /** Database manager for question retrieval. */
    private final DatabaseManager myDatabaseManager;
    
    /** Whether a game is currently active. */
    private boolean myGameActive;
    
    /** The last direction the player attempted to move in. */
    private Direction myLastAttemptedDirection;
    
    /** Factory for creating questions. */
    private final QuestionFactory myQuestionFactory;
    
    /**
     * Private constructor for singleton pattern.
     */
    private SystemControl() {
        myDatabaseManager = DatabaseManager.getInstance();
        myQuestionFactory = new QuestionFactory();
        myGameActive = false;
    }
    
    /**
     * Get the singleton instance of SystemControl.
     * @return SystemControl instance
     */
    public static synchronized SystemControl getInstance() {
        if (myInstance == null) {
            myInstance = new SystemControl();
        }
        return myInstance;
    }
    
    /**
     * Initialize a new game with specified difficulty.
     * 
     * @param theDifficulty The game difficulty level
     * @return true if initialization successful
     */
    public boolean initializeGame(final Difficulty theDifficulty) {
        try {
            myDatabaseManager.setDifficulty(theDifficulty);
            if (!myDatabaseManager.hasQuestionsForDifficulty()) {
                LOGGER.warning("No questions available for difficulty: " + theDifficulty);
                return false;
            }
            
            // Mark current room (entrance) as visited
            Maze.getRoom().setEntrance(true);
            Maze.getRoom().setVisited(true);
            
            // Initialize questions for adjacent rooms
            setupQuestionsForCurrentRoom();
            
            myGameActive = true;
            return true;
        } catch (final Exception theE) {
            LOGGER.log(Level.SEVERE, "Failed to initialize game", theE);
            return false;
        }
    }
    
    /**
     * Sets up questions for doors in the current room.
     */
    private void setupQuestionsForCurrentRoom() {
        for (final Direction theDir : Direction.values()) {
            final Room nextRoom = Maze.getRoom(theDir);
            if (nextRoom != null && !Maze.getRoom().hasQuestion(theDir)) {
                final AbstractQuestion question = 
                    myQuestionFactory.getQuestion(myDatabaseManager.getCurrentDifficulty());
                if (question != null) {
                    Maze.getRoom().setQuestion(theDir, question);
                }
            }
        }
    }
    
    /**
     * Handles player movement attempt in a direction.
     * 
     * @param theDirection The direction to move
     * @return true if movement successful without needing a question
     */
    public boolean attemptMove(final Direction theDirection) {
        if (!myGameActive) {
            return false;
        }
        
        myLastAttemptedDirection = theDirection;
        
        final Room currentRoom = Maze.getRoom();
        final Room nextRoom = Maze.getRoom(theDirection);
        
        if (nextRoom == null) {
            return false;  // Can't move out of bounds
        }
        
        // Check door state
        final DoorState doorState = currentRoom.getDoorState(theDirection);
        if (doorState == DoorState.OPEN) {
            // Door is already open, move using Maze.move
            return Maze.move(theDirection);
        } else if (doorState == DoorState.BLOCKED) {
            // Door is permanently blocked
            return false;
        } else {
            // Door is locked, need question to unlock
            return false;  // Return false to trigger question in view
        }
    }
    
    /**
     * Gets the last direction the player attempted to move.
     * 
     * @return The last attempted direction
     */
    public Direction getLastAttemptedDirection() {
        return myLastAttemptedDirection;
    }
    
    /**
     * Processes the answer to a question.
     * 
     * @param theQuestion The question being answered
     * @param theAnswer The player's answer
     * @return true if answer is correct
     */
    public boolean processAnswer(final AbstractQuestion theQuestion, final String theAnswer) {
        if (!myGameActive || theQuestion == null || myLastAttemptedDirection == null) {
            return false;
        }
        
        final boolean correct = theQuestion.isCorrect(theAnswer);
        if (correct) {
            Maze.getRoom().unlock(myLastAttemptedDirection);
            Maze.move(myLastAttemptedDirection);
            
            // Check if player has reached the exit
            if (Maze.getRoom().isExit()) {
                myGameActive = false;
                LOGGER.info("Game won! Player reached the exit room.");
            }
        } else {
            Maze.getRoom().block(myLastAttemptedDirection);
            
            // Check if player can still reach the exit
            if (checkLoseCondition()) {
                myGameActive = false;
                LOGGER.info("Game lost! No valid path to exit remains.");
            }
        }
        
        return correct;
    }
    
    /**
     * Checks if the game has been won.
     * 
     * @return true if player has reached the end room
     */
    public boolean checkWinCondition() {
        return myGameActive && Maze.getRoom() != null && Maze.getRoom().isExit();
    }
    
    /**
     * Checks if the game has been lost.
     * 
     * @return true if player can no longer reach the end
     */
    public boolean checkLoseCondition() {
        if (!myGameActive) {
            return false;
        }
        
        // Check if all doors from current room are blocked
        boolean allBlocked = true;
        for (final Direction theDir : Direction.values()) {
            final Room nextRoom = Maze.getRoom(theDir);
            if (nextRoom != null && Maze.getRoom().getDoorState(theDir) != DoorState.BLOCKED) {
                allBlocked = false;
                break;
            }
        }
        
        // Simplified loss detection - if all doors blocked, game is lost
        return allBlocked;
    }
    
    /**
     * Gets the current game difficulty.
     * 
     * @return current Difficulty
     */
    public Difficulty getCurrentDifficulty() {
        return myDatabaseManager.getCurrentDifficulty();
    }
    
    /**
     * Checks if a game is currently active.
     * 
     * @return true if game is active
     */
    public boolean isGameActive() {
        return myGameActive;
    }
    
    /**
     * Ends the current game session.
     */
    public void endGame() {
        myGameActive = false;
        // Additional cleanup as needed
    }
    
    /**
     * Triggers a question for movement. This method is called from the Maze class.
     * The actual question handling is delegated to the view through the controller.
     * 
     * @return true if question answered correctly, false otherwise
     */
    public static boolean triggerQuestion() {
        final AbstractQuestion question = SystemControl.getInstance().getQuestionForDoor(
                SystemControl.getInstance().getLastAttemptedDirection());
        
        if (question == null) {
            return false;
        }
        
        // Create and display appropriate question dialog based on question type
        final JDialog questionDialog = new JDialog();
        questionDialog.setTitle("Answer Question to Proceed");
        questionDialog.setModal(true);
        questionDialog.setSize(400, 300);
        questionDialog.setLocationRelativeTo(null);
        
        JPanel questionPanel;
        if (question instanceof MultipleChoiceQuestion) {
            questionPanel = new MultipleChoiceQuestionPanel(
                    (MultipleChoiceQuestion) question, null);
        } else if (question instanceof TrueFalseQuestion) {
            questionPanel = new TrueFalseQuestionPanel(
                    (TrueFalseQuestion) question, null);
        } else if (question instanceof ShortAnswerQuestion) {
            questionPanel = new ShortAnswerQuestionPanel(
                    (ShortAnswerQuestion) question, null);
        } else {
            return false;
        }
        
        final boolean[] result = new boolean[1];
        
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            result[0] = ((QuestionPanel) questionPanel).checkAnswer();
            if (result[0]) {
                JOptionPane.showMessageDialog(questionDialog, 
                        "Correct! The door is now open.", "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                questionDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(questionDialog, 
                        "Incorrect! The door is now permanently blocked.", "Failed", 
                        JOptionPane.ERROR_MESSAGE);
                questionDialog.dispose();
            }
        });
        
        questionPanel.add(submitButton);
        questionDialog.add(questionPanel);
        questionDialog.setVisible(true);
        
        return result[0];
    }
    
    /**
     * Gets a random question for a specific door.
     * 
     * @param theDirection The direction of the door
     * @return A question object or null if none available
     */
    public AbstractQuestion getQuestionForDoor(final Direction theDirection) {
        // Try to get an existing question from the room
        AbstractQuestion question = Maze.getRoom().getQuestion(theDirection);
        
        // If no question exists, get a random one from the database
        if (question == null) {
            question = myDatabaseManager.getRandomQuestion();
            if (question != null) {
                Maze.getRoom().setQuestion(theDirection, question);
            }
        }
        
        return question;
    }
}