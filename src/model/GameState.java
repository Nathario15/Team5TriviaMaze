package model;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * GameState class captures and manages the state of the trivia maze game.
 * This class handles saving and loading game state through serialization.
 */
public final class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int currentPosition;
    private Set<Integer> lockedDoors;
    private Set<Integer> questionsUsed;
    private Maze myMaze;
    private Difficulty myDifficulty;
    
    private boolean myGameActive;

    /**
     * Creates a new game state with default values
     */
    public GameState() {
        this.currentPosition = 0;
        this.lockedDoors = new HashSet<>();
        this.questionsUsed = new HashSet<>();
        this.myDifficulty = Difficulty.MEDIUM;
        this.myGameActive = true;
        
//        this.myMaze = Maze.getInstance();
    }
    
    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public Set<Integer> getLockedDoors() {
        return new HashSet<>(lockedDoors);  // Return a copy to maintain encapsulation
    }

    public void lockDoor(int door) {
        lockedDoors.add(door);
    }

    public Set<Integer> getQuestionsUsed() {
        return new HashSet<>(questionsUsed);
    }

    public void useQuestion(int questionId) {
        questionsUsed.add(questionId);
    }

    public void saveToFile(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
            System.out.println("Game saved to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameState loadFromFile(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            GameState state = (GameState) in.readObject();
            System.out.println("Game loaded from " + filename);
        	return state;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new GameState(); // Return a new instance if loading fails
        }
    }

    public boolean isDoorLocked(int door) {
        return lockedDoors.contains(door);
    }

    public boolean isQuestionUsed(int questionId) {
        return questionsUsed.contains(questionId);
    }
    
    /**
     * Gets the maze.
     * @return The maze
     */
    public Maze getMaze() {
        return myMaze;
    }
    
    /**
     * Gets the current difficulty level.
     * @return The current difficulty
     */
    public Difficulty getDifficulty() {
        return myDifficulty;
    }
    
    /**
     * Sets the difficulty level.
     * @param theDifficulty The difficulty to set
     */
    public void setDifficulty(Difficulty theDifficulty) {
        this.myDifficulty = theDifficulty;
        DatabaseManager.getInstance().setDifficulty(theDifficulty);
    }
    
    /**
     * Check if the game is active.
     * @return true if the game is active
     */
    public boolean isGameActive() {
        return myGameActive;
    }
    
    /**
     * Set the game active state.
     * @param theActive The active state to set
     */
    public void setGameActive(boolean theActive) {
        this.myGameActive = theActive;
    }
    
    /**
     * Reinitialize any components after loading.
     * This is necessary because some objects have transient fields.
     */
    private void readObject(java.io.ObjectInputStream in) 
            throws java.io.IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Reconnect to the database
        DatabaseManager.getInstance().setDifficulty(myDifficulty);
        
        // Reconnect the maze to any transient services
        if (myMaze != null) {
            myMaze.reconnectServices();
        }
    }
}

