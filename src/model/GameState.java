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
	 * Total number of locked doors in the maze initially.
	 */
	private static final int TOTAL_DOORS = 49;
	
	/**
	 * The Locked Doors.
	 */
	private static int myLockedDoors = TOTAL_DOORS;
 	
 	/**
 	 * The correct question count.
 	 */
 	private static int myCorrectQuestions;
 	
 	/**
 	 * The incorrect question count.
 	 */
 	private static int myIncorrectQuestions;
 	
 	/**
 	 * Singleton instance of GameState.
 	 */
 	private static GameState myInstance;
 	
	/**
	 * The current X.
	 */
	private int myCurrentX;

	/**
	 * The current Y.
	 */
	private int myCurrentY;
	
	/**
	 * The questions used.
	 */
	private Set<Integer> myQuestionsUsed;
//    private Maze myMaze;
	/**
	 * The difficulty.
	 */
	private Difficulty myDifficulty;
	
	/**
	 * The array of questions.
	 */
	private ArrayList<AbstractQuestion> myQuestionArray;

	/**
	 * the map.
	 */
	private Room[][] myMap;

	/**
	 * constructor.
	 */
	public GameState() {
		this.myCurrentX = 0;
		this.myCurrentY = 0;
		this.myQuestionsUsed = new HashSet<>();
	}
	
	/**
     * Get the singleton instance of SystemControl.
     * @return SystemControl instance
     */
    public static synchronized GameState getInstance() {
        if (myInstance == null) {
            myInstance = new GameState();
        }
        return myInstance;
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
	public void setCurrentPosition(final int theX, final int theY) {
	    this.myCurrentX = theX;
	    this.myCurrentY = theY;
	}
	
	/**
	 * Returns the number of questions answered.
	 */
	public int getCorrectQuestions() {
 		return myCorrectQuestions;
	}

	/**
	 * Returns the number of questions answered incorrectly.
	 * 
	 * @return the count of incorrectly answered questions
	 */
 	public int getIncorrectQuestions() {
 	 	return myIncorrectQuestions;
 	}

 	/**
 	 * Returns the number of locked doors remaining in the maze.
 	 * 
 	 * @return the count of locked doors
 	 */
	public int getLockedDoors() {
 		return myLockedDoors;
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
	 * @param theDifficulty the difficulty to set
	 */
    public void setDifficulty(final Difficulty theDifficulty) {
        this.myDifficulty = theDifficulty;
    }
    
    /**
     * Increments the correct questions counter.
     */
    public void addCorrect() {
 		myCorrectQuestions++;
 	}
 	
    /**
     * Increments the incorrect questions counter.
     */
 	public void addIncorrect() {
 		myIncorrectQuestions++;
 	}
 	
 	/**
 	 * Decrements the locked doors counter.
 	 */
 	public void removeLockedDoor() {
 		myLockedDoors--;
 	}
 	
 	/**
 	 * Resets the game state counters to initial values.
 	 */
	public void resetState() {
 		myCorrectQuestions = 0;
 		myIncorrectQuestions = 0;
 		myLockedDoors = TOTAL_DOORS;
 	}

	/**
	 * saves.
	 * @param theFilename
	 */
	public void saveToFile(final String theFilename) {
		myMap = Maze.returnMap();
		this.myCurrentX = Maze.getX();
		this.myCurrentY = Maze.getY();
		this.myQuestionArray = QuestionFactory.returnQuestions();

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
		QuestionFactory.loadQuestions(myQuestionArray);
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
