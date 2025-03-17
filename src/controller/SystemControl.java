
package controller;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.AbstractQuestion;
import model.DatabaseManager;
import model.Difficulty;
import model.Direction;
import model.DoorState;
import model.GameState;
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
    
    /** Standard dialog width. */
    private static final int DIALOG_WIDTH = 500;
    
    /** Standard dialog height. */
    private static final int DIALOG_HEIGHT = 500;
    
    /** Singleton instance. */
    private static SystemControl myInstance;
    
    /** Database manager for question retrieval. */
    private final DatabaseManager myDatabaseManager;
    
    /** Whether a game is currently active. */
    private boolean myGameActive;
    
    /** The last direction the player attempted to move in. */
    private Direction myLastAttemptedDirection;
    
    /** Reference to the game view. */
    private view.GameView myGameView;
    
    /**
     * Private constructor for singleton pattern.
     */
    private SystemControl() {
        myDatabaseManager = DatabaseManager.getInstance();
        myGameActive = false;
    }
    
    /**
     * Sets the reference to the game view.
     * 
     * @param theGameView the game view to set
     */
    public void setGameView(final view.GameView theGameView) {
        myGameView = theGameView;
        System.out.println("GameView reference set in SystemControl");
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
        boolean success = false;
        
        try {
            myDatabaseManager.setDifficulty(theDifficulty);
            final boolean hasQuestions = myDatabaseManager.hasQuestionsForDifficulty();
            
            if (hasQuestions) {
                // Mark current room (entrance) as visited
//                Maze.getRoom().setEntrance(true);
//                Maze.getRoom().setVisited(true);
                
                // Initialize questions for adjacent rooms
//                setupQuestionsForCurrentRoom();
                
                myGameActive = true;
                success = true;
            } else {
                LOGGER.warning("No questions available for difficulty: " + theDifficulty);
            }
        } catch (final IllegalStateException ex1) {
            LOGGER.log(Level.SEVERE, "Failed to initialize game due to illegal state", ex1);
        } catch (final NullPointerException ex2) {
            LOGGER.log(Level.SEVERE, "Failed to initialize game due to null pointer", ex2);
        }

        return success;
    }
    
//    /**
//     * Sets up questions for doors in the current room.
//     */
//    private void setupQuestionsForCurrentRoom() {
//        for (final Direction dir : Direction.values()) {
//            final Room nextRoom = Maze.getRoom(dir);
//            if (nextRoom != null && !Maze.getRoom().hasQuestion(dir)) {
//                final AbstractQuestion question = 
//                    myQuestionFactory.getQuestion(myDatabaseManager.getCurrentDifficulty());
//                if (question != null) {
//                    Maze.getRoom().setQuestion(dir, question);
//                }
//            }
//        }
//    }
    
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
        
        // Check door state first
        final DoorState doorState = currentRoom.getDoorState(theDirection);
        return handleDoorState(doorState, currentRoom, theDirection);
    }
    
    /**
     * Handles the movement attempt based on the door state.
     * 
     * @param theDoorState the state of the door
     * @param theCurrentRoom the current room
     * @param theDirection the direction to move
     * @return true if movement was successful
     */
    private boolean handleDoorState(final DoorState theDoorState, final Room theCurrentRoom, 
                                   final Direction theDirection) {
        if (theDoorState == DoorState.OPEN) {
            // Door is already open - move freely
            Maze.move(theDirection);
            return true;
        } 
        
        if (theDoorState == DoorState.LOCKED) {
            return handleLockedDoor(theCurrentRoom, theDirection);
        }
        
        // Door is already blocked - can't move
        JOptionPane.showMessageDialog(null, 
                "This door is permanently blocked!", 
                "Blocked Path", 
                JOptionPane.WARNING_MESSAGE);
        return false;
    }
    
    /**
     * Handles the case of a locked door.
     * 
     * @param theCurrentRoom the current room
     * @param theDirection the direction to move
     * @return true if door was unlocked and movement successful
     */
    private boolean handleLockedDoor(final Room theCurrentRoom, final Direction theDirection) {
        // Door is locked - show question
        final boolean answeredCorrectly = triggerQuestion();
        
        if (answeredCorrectly) {
            // Correct answer - unlock door and move
            theCurrentRoom.unlock(theDirection);
            Maze.move(theDirection);
            return true;
        }
        
        // Wrong answer - permanently block door
        theCurrentRoom.block(theDirection);
        return false;
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
     * Sets the last attempted direction.
     * 
     * @param theDirection The direction to set
     */
    public void setLastAttemptedDirection(final Direction theDirection) {
        myLastAttemptedDirection = theDirection;
    }
    
//    /**
//     * Processes the answer to a question.
//     * 
//     * @param theQuestion The question being answered
//     * @param theAnswer The player's answer
//     * @return true if answer is correct
//     */
//    public boolean processAnswer(final AbstractQuestion theQuestion, final String theAnswer) {
//        // Invalid state checks
//        if (!myGameActive || theQuestion == null || myLastAttemptedDirection == null) {
//            return false;
//        }
//        
//        final boolean correct = theQuestion.isCorrect(theAnswer);
//        
//        if (correct) {
//            handleCorrectAnswer();
//        } else {
//            handleIncorrectAnswer();
//        }
//        
//        return correct;
//    }
    
//    /**
//     * Handles correct answer processing.
//     */
//    private void handleCorrectAnswer() {
//        Maze.getRoom().unlock(myLastAttemptedDirection);
//        Maze.move(myLastAttemptedDirection);
//        
//        // Check if player has reached the exit
//        if (Maze.getRoom().isExit()) {
//            myGameActive = false;
//            LOGGER.info("Game won! Player reached the exit room.");
//        }
//    }
    
    /**
     * Checks if the game has been won.
     * 
     * @return true if player has reached the end room
     */
    public boolean checkWinCondition() {
        return myGameActive && Maze.getRoom() != null /*&& Maze.getRoom().isExit()*/;
    }
    
    /**
     * Checks if the game has been lost.
     * 
     * @return true if player can no longer reach the end
     */
    public boolean checkLoseCondition() {
    	return !Maze.canSolve();
//        if (!myGameActive) {
//            return false;
//        }
//        
//        // Check if all doors from current room are blocked
//        boolean allBlocked = true;
//        for (final Direction dir : Direction.values()) {
//            final Room nextRoom = Maze.getRoom(dir);
//            if (nextRoom != null && Maze.getRoom().getDoorState(dir) != DoorState.BLOCKED) {
//                allBlocked = false;
//                break;
//            }
//        }
//        
//        // Simplified loss detection - if all doors blocked, game is lost
//        return allBlocked;
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
        
        System.out.println("Game won - player escaped the maze");
        
        // Show victory message directly
        JOptionPane.showMessageDialog(null, 
            "Congratulations! You've successfully escaped the Minecraft Trivia Maze!", 
            "Victory", JOptionPane.INFORMATION_MESSAGE);
        
        // Use the direct reference to return to main menu
        if (myGameView != null) {
            System.out.println("Calling returnToMainMenu() on GameView");
            myGameView.returnToMainMenu();
        } else {
            System.err.println("ERROR: No GameView reference in SystemControl!");
        }
    }
    
    /**
     * Creates the appropriate question panel based on question type.
     * 
     * @param theQuestion The question to display
     * @return The question panel created
     */
    private static JPanel createQuestionPanel(final AbstractQuestion theQuestion) {
        JPanel panel1 = null;
        
        if (theQuestion instanceof MultipleChoiceQuestion) {
            panel1 = new MultipleChoiceQuestionPanel(
                    (MultipleChoiceQuestion) theQuestion, null);
        } else if (theQuestion instanceof TrueFalseQuestion) {
            panel1 = new TrueFalseQuestionPanel(
                    (TrueFalseQuestion) theQuestion, null);
        } else if (theQuestion instanceof ShortAnswerQuestion) {
            panel1 = new ShortAnswerQuestionPanel(
                    (ShortAnswerQuestion) theQuestion, null);
        }
        
        return panel1;
    }
    
    /**
     * Sets up and displays the question dialog.
     * 
     * @param thePanel The panel containing the question
     * @param theDialog The dialog to display the question in
     * @return true if answered correctly, false otherwise
     */
    private static boolean displayQuestionDialog(final JPanel thePanel, final JDialog theDialog) {
        final boolean[] result = new boolean[1];
        
        JButton existingSubmit = null;
        for (Component c : thePanel.getComponents()) {
            if (c instanceof JButton && ((JButton) c).getText().contains("Submit")) {
                existingSubmit = (JButton) c;
                break;
            }
        }
        
        if (existingSubmit != null) {
            // Remove existing listeners
            for (ActionListener al : existingSubmit.getActionListeners()) {
                existingSubmit.removeActionListener(al);
            }
            
            // Add our listener
            existingSubmit.addActionListener(e -> {
                result[0] = ((QuestionPanel) thePanel).checkAnswer();
                handleQuestionResult(result[0], theDialog);
                theDialog.dispose();
            });
        }
        
        theDialog.add(thePanel);
        theDialog.setVisible(true);
        
        return result[0];
    }
    
    /**
     * Handles the result of answering a question.
     * 
     * @param theCorrect Whether the answer was correct
     * @param theDialog The dialog showing the question
     */
    private static void handleQuestionResult(final boolean theCorrect, final JDialog theDialog) {
        if (theCorrect) {
            JOptionPane.showMessageDialog(theDialog, 
                    "Correct! The door is now open.", "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            GameState.getInstance().addCorrect();
            GameState.getInstance().removeQuestion();
        } else {
            JOptionPane.showMessageDialog(theDialog, 
                    "Incorrect! The door is now permanently blocked.", "Failed", 
                    JOptionPane.ERROR_MESSAGE);
            GameState.getInstance().addIncorrect();
            GameState.getInstance().removeQuestion();
        }	
        theDialog.dispose();
    }
    
    /**
     * Triggers a question for movement. This method is called from the Maze class.
     * The actual question handling is delegated to the view through the controller.
     * 
     * @return true if question answered correctly, false otherwise
     */
    public static boolean triggerQuestion() {
        // Get direction and question
//        final Direction attemptedDir = SystemControl.getInstance().getLastAttemptedDirection();
//        if (attemptedDir == null) {
//            return false;
//        }
        
        // Get question for the door
        final AbstractQuestion question = QuestionFactory.getQuestion();
        if (question == null) {
            return false;
        }
        
        // Create dialog and panel
        return triggerQuestionDialog(question);
    }
    
    /**
     * Creates and shows a question dialog.
     * 
     * @param theQuestion The question to display
     * @return true if answered correctly, false otherwise
     */
    private static boolean triggerQuestionDialog(final AbstractQuestion theQuestion) {
        final JDialog questionDialog = new JDialog();
        questionDialog.setTitle("Answer Question to Proceed");
        questionDialog.setModal(true);
        questionDialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        questionDialog.setLocationRelativeTo(null);
        
        final JPanel questionPanel = createQuestionPanel(theQuestion);
        if (questionPanel == null) {
            return false;
        }
        
        return displayQuestionDialog(questionPanel, questionDialog);
    }
    
    /**
     * Gets a random question for a specific door.
     * 
     * @return A question object or null if none available
     */
    public AbstractQuestion getQuestionForDoor() {
        // Try to get an existing question from the room
        final AbstractQuestion question1 = DatabaseManager.getInstance().getRandomQuestion();
        
//        // If no question exists, get a random one
//        if (question1 == null) {
//            question1 = getNewQuestionForDoor(theDirection);
//        }
        
        return question1;
    }
    
//    /**
//     * Gets a new question from the database and assigns it to the door.
//     * 
//     * @param theDirection The direction of the door
//     * @return A new question or null if none available
//     */
//    private AbstractQuestion getNewQuestionForDoor(final Direction theDirection) {
//        final AbstractQuestion question1 = myDatabaseManager.getRandomQuestion();
//        if (question1 != null) {
//            Maze.getRoom().setQuestion(theDirection, question1);
//        }
//        return question1;
//    }
}
