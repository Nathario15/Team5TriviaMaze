package model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
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
	
	private ArrayList<AbstractQuestion> arr;

	/**
	 * the map.
	 */
	private Room[][] myMap;

	/**
	 * constructor.
	 */
	public GameState() {
		this.myCurrentX = 0;
		this.myLockedDoors = new HashSet<>();
		this.myQuestionsUsed = new HashSet<>();
	}

	/**
	 * returns the player's position.
	 * @return
	 */
	public String getPlayerPosition() {
	    return myCurrentX + ", " + myCurrentY;
	}

	/**
	 * 
	 * @param theCurrentPosition
	 */
	public void setCurrentPosition(int x, int y) {
	    this.myCurrentX = x;
	    this.myCurrentY = y;
	}

	/**
	 * return locked doors.
	 * @return
	 */
	public Set<Integer> getLockedDoors() {
		return new HashSet<>(myLockedDoors); // Return a copy to maintain encapsulation
	}
	
	/**
	 * Returns the number of questions answered.
	 */
	public int getQuestionsAnswered() {
		return myQuestionsUsed.size();
	}

	/**
	 * locks a door.
	 * @param theDoor
	 */
	public void lockDoor(final int theDoor) {
		myLockedDoors.add(theDoor);
	}

	/**
	 * Returns questions used.
	 * @return
	 */
	public Set<Integer> getQuestionsUsed() {
		return new HashSet<>(myQuestionsUsed);
	}

	/**
	 * use a question.
	 * @param theQuestionId
	 */
	public void useQuestion(final int theQuestionId) {
		myQuestionsUsed.add(theQuestionId);
	}
	
	/**
	 * Get's the player's X coordinate.
	 * @return
	 */
	public int getCurrentX() {
        return myCurrentX;
    }

	/**
	 * Get's the player's Y coordinate.
	 * @return
	 */
    public int getCurrentY() {
        return myCurrentY;
    }
	
	/**
	 * Returns current difficulty.
	 * @return
	 */
	public Difficulty getDifficulty() {
        return myDifficulty;
    }

	/**
	 * 
	 * @param difficulty
	 */
    public void setDifficulty(Difficulty difficulty) {
        this.myDifficulty = difficulty;
    }

	/**
	 * saves.
	 * @param theFilename
	 */
	public void saveToFile(final String theFilename) {
		myMap = Maze.returnMap();
		this.myCurrentX = Maze.getX();
		this.myCurrentY = Maze.getY();
		this.arr = QuestionFactory.returnQuestions();

		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(theFilename))) {
			out.writeObject(this);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * loads a file into a game sate.
	 * @param theFilename
	 * @return
	 */
	public static GameState loadFromFile(final String theFilename) {
		GameState other;
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(theFilename))) {
			other = (GameState) in.readObject();
			System.out.println("Game loaded successfully from: " + theFilename);
		} catch (final IOException | ClassNotFoundException e) {
			System.err.println("Error loading game: " + e.getMessage());
			e.printStackTrace();
			other = new GameState(); // Return a new instance if loading fails
		}
		other.loadToMaze();
		
		return other;
	}

	/**
	 * loads to a maze.
	 */
	public void loadToMaze() {
		Maze.loadMap(myMap, myCurrentX, myCurrentY);
		QuestionFactory.loadQuestions(arr);
	}

	/**
	 * checks if locked.
	 * @param theDoor
	 * @return
	 */
	public boolean isDoorLocked(final int theDoor) {
		return myLockedDoors.contains(theDoor);
	}

	/**
	 * checks if question used.
	 * @param theQuestionId
	 * @return
	 */
	public boolean isQuestionUsed(final int theQuestionId) {
		return myQuestionsUsed.contains(theQuestionId);
	}

	/**
	 * Reinitialize any components after loading. This is necessary because some
	 * objects have transient fields.
	 */
	private void readObject(final java.io.ObjectInputStream theIn) throws java.io.IOException, ClassNotFoundException {
		theIn.defaultReadObject();
		// Reconnect to the database
		DatabaseManager.getInstance().setDifficulty(myDifficulty);

//        // Reconnect the maze to any transient services
//        if (myMaze != null) {
//            myMaze.reconnectServices();
//        }
	}
}
