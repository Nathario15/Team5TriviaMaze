package model;

import controller.SystemControl;
import javax.swing.JOptionPane;

/**
 * Implements the singleton design pattern. TODO make the fields not static, so
 * it can be serialized.
 * 
 * @author Ibrahim Elnikety
 * @version 0.6
 */
public final class Maze /* implements Serializable */ {
	/**
	 * How big the map is, .
	 */
	public static final int MAP_SIZE = 8;

    /**
     * String constant for position logging.
     */
    private static final String POSITION_STRING = " from position (";
    
    /**
     * String constant for parenthesis.
     */
    private static final String PARENTHESIS_CLOSE = ")";
    
    /**
     * String constant for comma.
     */
    private static final String COMMA = ",";
    
    /**
     * Array for checking maze pathfinding.
     */
	private static boolean[][] mapCheck;
	
    /**
     * Flag indicating if exit has been reached.
     */
    private static boolean myExitAchieved;

	/**
	 * A map of rooms.
	 */
	private static Room[][] MAP = {
			// 0 1 2 3 4 5 6 7 8
			{ null, null, null, null, null, null, null, null, null }, // 0

			{ null, new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), null }, // 1

			{ null, new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), null }, // 2

			{ null, new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), null }, // 3

			{ null, new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), null }, // 4

			{ null, new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), null }, // 5

			{ null, new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), null }, // 6

			{ null, new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), null }, // 7

			{ null, null, null, null, null, null, null, null, null } }; // 8

	/**
	 * y coordinate.
	 */
	private static int y = MAP_SIZE / 2;
	/**
	 * x coordinate.
	 */
	private static int x = MAP_SIZE / 2;

	/**
	 * The Database manager.
	 */
	@SuppressWarnings("unused")
	private transient DatabaseManager myDatabaseManager;

	private Maze() {

	}

	/**
	 * used for testing.
	 * 
	 * @return
	 */
	static int getY() {
		return y;
	}

	/**
	 * used for display.
	 * 
	 * @return
	 */
	public static int getDisplayY() {
		return y - 1;
	}

	/**
	 * used for testing.
	 * 
	 * @return
	 */
	static int getX() {
		return x;
	}

	/**
	 * used for display.
	 * 
	 * @return
	 */
	public static int getDisplayX() {
		return x - 1;
	}

	/**
	 * The map will stay the same size, but it's contents will change.
	 */
	protected static void loadMap(final Room[][] theMap, final int theX, final int theY) {
		MAP = theMap;
		x = theX;
		y = theY;
	}

	/**
	 * The map will stay the same size, but it's contents will change.
	 */
	protected static Room[][] returnMap() {
		return MAP;
	}

//	/**
//	 * Returns room North of player.
//	 * @return
//	 */
//	public static Room getNorth() {
//		return MAP[x][y + 1];
//	}
//	/**
//	 * Returns room South of player.
//	 * @return
//	 */
//	public static Room getSouth() {
//		return MAP[x][y - 1];
//	}
//	/**
//	 * Returns room East of player.
//	 * @return
//	 */
//	public static Room getEast() {
//		return MAP[x + 1][y];
//	}
//	/**
//	 * Returns room West of player.
//	 * @return
//	 */
//	public static Room getWest() {
//		return MAP[x - 1][y];
//	}
	/**
	 * Returns room based on direction.
	 * 
	 * @param theDirection
	 * @return
	 */
	public static Room getRoom(final Direction theDirection) {
		// Calculate potential new position
		int newX = x;
		int newY = y;

		if (theDirection == Direction.NORTH) {
			newY++; // NORTH increases y
		} else if (theDirection == Direction.SOUTH) {
			newY--; // SOUTH decreases y
		} else if (theDirection == Direction.EAST) {
			newX++; // EAST increases x
		} else if (theDirection == Direction.WEST) {
			newX--; // WEST decreases x
		}

		// Check if new position is out of the playable area
		if (newX <= 0 || newX >= MAP.length - 1 || newY <= 0 || newY >= MAP[0].length - 1) {
			// Out of bounds - this is an exit
            System.out.println("EXIT DETECTED at direction: " + theDirection + POSITION_STRING + x + COMMA + y
                    + PARENTHESIS_CLOSE + " to (" + newX + COMMA + newY + PARENTHESIS_CLOSE);
			return null;
		}

		return MAP[newX][newY];
	}

	/**
	 * Returns room.
	 * 
	 * @param theDirection
	 * @return
	 */
	public static Room getRoom() {
		return MAP[x][y];
	}

	/**
	 * moves and returns new room.
	 * 
	 * @param dir
	 * @return
	 */
	protected static Room setRoom(final Direction theDirection) {
		if (theDirection == Direction.NORTH) {
			y++; // NORTH increases y (moves up)
		} else if (theDirection == Direction.SOUTH) {
			y--; // SOUTH decreases y (moves down)
		} else if (theDirection == Direction.EAST) {
			x++; // EAST decreases x (moves right)
		} else if (theDirection == Direction.WEST) {
			x--; // WEST increases x (moves left)
		}
		return getRoom();
	}

	/**
	 * moves and returns if successful.
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean move(final Direction theDirection) {
		boolean result = false;
		
        // If game already won, prevent further movement
        if (!myExitAchieved) {
            System.out.println("Move attempt: " + theDirection + POSITION_STRING + x + COMMA + y + PARENTHESIS_CLOSE);
        
			// Get the door state for this direction
			final DoorState doorState = getRoom().myDoors.get(theDirection);

			// Check if this would be an exit
			if (getRoom(theDirection) == null) {
                result = handleExitMovement(theDirection, doorState);
            } else {
                result = handleNonExitMovement(theDirection, doorState);
            }
        }
	
		return result;
	}

	/**
	 * Handles movement to an exit.
	 * 
	 * @param theDirection direction to exit
	 * @param theDoorState current door state
	 * @return true if exit successful
	 */
	private static boolean handleExitMovement(final Direction theDirection, final DoorState theDoorState) {
        System.out.println("Exit detected in direction: " + theDirection);
        boolean result = false;
        
			// Check if this exit is already blocked
			if (theDoorState == DoorState.BLOCKED) {
				JOptionPane.showMessageDialog(null, "This exit is blocked! You must find another way out.",
						"Blocked Exit", JOptionPane.WARNING_MESSAGE);
			} else {
				// Exit is available - trigger a question
				final boolean answeredCorrectly = SystemControl.triggerQuestion();
				if (answeredCorrectly) {
					// Correct answer - win game!
					System.out.println("Question answered correctly - VICTORY!");
					myExitAchieved = true; // Set flag to prevent more movement
					SystemControl.getInstance().endGame();
					result = true;
			} else {
				// Wrong answer - block this exit
				System.out.println("Question answered incorrectly - exit blocked");
				getRoom().myDoors.put(theDirection, DoorState.BLOCKED);
			}
		}
		
        return result;
	}
	
    /**
     * Handles movement within the maze (not to an exit).
     * 
     * @param theDirection direction to move
     * @param theDoorState current door state
     * @return true if movement successful
     */
	private static boolean handleNonExitMovement(final Direction theDirection, final DoorState theDoorState) {
		if (theDoorState == DoorState.OPEN) {
			setRoom(theDirection);
			return true;
		} else if (theDoorState == DoorState.LOCKED && attempt(theDirection)) {
			setRoom(theDirection);
			return true;
		} else if (theDoorState == DoorState.BLOCKED) {
			JOptionPane.showMessageDialog(null, "This door is permanently blocked!", "Blocked Path",
					JOptionPane.WARNING_MESSAGE);
		}
		return false;
	}

	/**
	 * Resets the maze to its initial state. This includes player position and all
	 * door states.
	 */
	public static void reset() {
		// Reset player position
		x = MAP_SIZE / 2;
		y = MAP_SIZE / 2;

		// Reset exit flag
		myExitAchieved = false;

        System.out.println("Maze fully reset - player at position (" + x + COMMA + y + PARENTHESIS_CLOSE);

		// Reset all rooms (recreate the map)
		MAP = new Room[][] {
				// Your original map initialization
				// 0 1 2 3 4 5 6 7 8
				{ null, null, null, null, null, null, null, null, null }, // 0
				{ null, new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), null }, // 1
				{ null, new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), null }, // 2
				{ null, new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), null }, // 3
				{ null, new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), null }, // 4
				{ null, new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), null }, // 5
				{ null, new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), null }, // 6
				{ null, new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), new Room(), null }, // 7
				{ null, null, null, null, null, null, null, null, null } // 8
		};
	}

	private static boolean attempt(final Direction theDirection) {
		if (SystemControl.triggerQuestion()) {
			getRoom().unlock(theDirection);
			return true;
		} else {
			getRoom().block(theDirection);
			return false;
		}
	}

	/**
	 * recursive method that checks rooms.
	 * 
	 * @return
	 */
	public static boolean canSolve() {
		if (QuestionFactory.returnQuestions().size() == 0) {
			return false;
		}
		final int currX = x;
		final int currY = y;
		x = MAP_SIZE / 2;
		y = MAP_SIZE / 2;
		mapCheck = new boolean[MAP_SIZE + 1][MAP_SIZE + 1];
		for (int i = 0; i <= MAP_SIZE; i++) {
			for (int j = 0; j <= MAP_SIZE; j++) {
				mapCheck[i][j] = false;
			}
		}
		mapCheck[x][y] = true;
		final Room cur = getRoom();
		for (Direction dir : Direction.values()) {
			if (cur.myDoors.get(dir) != DoorState.BLOCKED) {
				if (canSolve(dir)) {
					x = currX;
					y = currY;
					return true;
				}
			}
		}
		x = currX;
		y = currY;
		return false;
	}

    /**
     * Helper method for canSolve that checks if a path exists.
     * 
     * @param theDir direction to check
     * @return true if path exists
     */
	private static boolean canSolve(final Direction theDir) {
		final Room cur = setRoom(theDir);
		boolean result = false;
		
        // If we've reached a null room (exit), we've found a solution
		if (cur == null) {
			return true;
		} else if (!mapCheck[x][y]) {
			// Mark this room as visited
			mapCheck[x][y] = true;
			
            // Try all directions
            for (Direction dir : Direction.values()) {
                if (!result && cur.myDoors.get(dir) != DoorState.BLOCKED) {
                    result = canSolve(dir);
                }
            }
		}
		
		return result;
	}

	/**
	 * Reconnect to any services after deserialization. This is called from
	 * GameState's readObject method.
	 */
	public void reconnectServices() {
		myDatabaseManager = DatabaseManager.getInstance();
	}
//
//	/**
//	 * Special method called during deserialization to 
//	 * reestablish the database manager.
//	 */
//	private void readObject(java.io.ObjectInputStream in) 
//	        throws java.io.IOException, ClassNotFoundException {
//	    in.defaultReadObject();
//	    // Reconnect to the database
//	    myDatabaseManager = DatabaseManager.getInstance();
//	}

}
