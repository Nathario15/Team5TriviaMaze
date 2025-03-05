package model;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public final class GameState implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int currentPosition;
    private Set<Integer> lockedDoors;
    private Set<Integer> questionsUsed;
    private Maze myMaze;
    private Difficulty myDifficulty;

    public GameState() {
        this.currentPosition = 0;
        this.lockedDoors = new HashSet<>();
        this.questionsUsed = new HashSet<>();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static GameState loadFromFile(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (GameState) in.readObject();
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

