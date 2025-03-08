package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public final class GameState implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * The current X.
	 */
	private int myCurrentX;

	/**
	 * The current Y.
	 */
	private int myCurrentY;

	/**
	 * The Locked Doors.
	 */
	private Set<Integer> myLockedDoors;
	/**
	 * The questions used.
	 */
	private Set<Integer> myQuestionsUsed;
//    private Maze myMaze;
	/**
	 * The difficulty.
	 */
	private Difficulty myDifficulty;

	private Room[][] map;

	/**
	 * constructor.
	 */
	public GameState() {
		this.myCurrentX = 0;
		this.myLockedDoors = new HashSet<>();
		this.myQuestionsUsed = new HashSet<>();
	}

	/**
	 * returns current postion.
	 * 
	 * @return
	 */
	public int getCurrentPosition() {
		return myCurrentX;
	}

	/**
	 * 
	 * @param theCurrentPosition
	 */
	public void setCurrentPosition(final int theCurrentPosition) {
		this.myCurrentX = theCurrentPosition;
	}

	public Set<Integer> getLockedDoors() {
		return new HashSet<>(myLockedDoors); // Return a copy to maintain encapsulation
	}

	public void lockDoor(int door) {
		myLockedDoors.add(door);
	}

	public Set<Integer> getQuestionsUsed() {
		return new HashSet<>(myQuestionsUsed);
	}

	public void useQuestion(int questionId) {
		myQuestionsUsed.add(questionId);
	}

	public void saveToFile(String filename) {
		map = Maze.returnMap();
		this.myCurrentX = Maze.getX();
		this.myCurrentY = Maze.getY();

		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
			out.writeObject(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static GameState loadFromFile(String filename) {
		GameState other;
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
			other = (GameState) in.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			other = new GameState(); // Return a new instance if loading fails
		}
		other.loadToMaze();
		return other;
	}

	public void loadToMaze() {
		Maze.loadMap(map, myCurrentX, myCurrentY);
	}

	public boolean isDoorLocked(int door) {
		return myLockedDoors.contains(door);
	}

	public boolean isQuestionUsed(int questionId) {
		return myQuestionsUsed.contains(questionId);
	}

	/**
	 * Reinitialize any components after loading. This is necessary because some
	 * objects have transient fields.
	 */
	private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
		in.defaultReadObject();
		// Reconnect to the database
		DatabaseManager.getInstance().setDifficulty(myDifficulty);

//        // Reconnect the maze to any transient services
//        if (myMaze != null) {
//            myMaze.reconnectServices();
//        }
	}
}
