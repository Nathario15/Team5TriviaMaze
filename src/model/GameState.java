package model;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

public final class GameState {
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
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(currentPosition + "\n");
            writer.write(lockedDoors.toString() + "\n");
            writer.write(questionsUsed.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static GameState loadFromFile(String filename) {
        GameState gameState = new GameState();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            gameState.currentPosition = Integer.parseInt(reader.readLine());
            gameState.lockedDoors = new HashSet<>();
            gameState.questionsUsed = new HashSet<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gameState;
    }
}

