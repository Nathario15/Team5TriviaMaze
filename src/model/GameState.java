package model;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class GameState {
    private int currentPosition;
    private Set<Integer> lockedDoors;
    private Set<Integer> questionsUsed;

    public GameState() {
        this.currentPosition = 0;
        this.lockedDoors = new HashSet<>();
        this.questionsUsed = new HashSet<>();
    }

    private int getCurrentPosition() {
        return currentPosition;
    }

    private void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    private Set<Integer> getLockedDoors() {
        return lockedDoors;
    }

    private void lockDoor(int door) {
        lockedDoors.add(door);
    }

    private Set<Integer> getQuestionsUsed() {
        return questionsUsed;
    }

    private void useQuestion(int questionId) {
        questionsUsed.add(questionId);
    }

    private void saveToFile(String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static GameState loadFromFile(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (GameState) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new GameState(); // Return a new state if loading fails
        }
    }
}

