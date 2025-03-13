package model;

import controller.SystemControl;

//import java.io.Serializable;
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
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
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
		Room temp = null;
		if (theDirection == Direction.NORTH) {
			temp = MAP[x][y + 1];
		}
		if (theDirection == Direction.SOUTH) {
			temp = MAP[x][y - 1];
		}
		if (theDirection == Direction.EAST) {
			temp = MAP[x + 1][y];
		}
		if (theDirection == Direction.WEST) {
			temp = MAP[x - 1][y];
		}
		return temp;
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
	protected static void setRoom(final Direction theDirection) {
		if (theDirection == Direction.NORTH) {
			y++;
		} else if (theDirection == Direction.SOUTH) {
			y--;
		} else if (theDirection == Direction.EAST) {
			x++;
		} else {
			x--;
		}
	}

	/**
	 * moves and returns if successful.
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean move(final Direction theDirection) {
		// checks the room isn't null, checks that it is open
		// if it is not open, checks that it is locked, then attempts to unlock it
		if( getRoom(theDirection) != null && getRoom().myDoors.get(theDirection) == DoorState.OPEN
				|| getRoom().myDoors.get(theDirection) == DoorState.LOCKED && attempt(theDirection)) {
			setRoom(theDirection);
			return true;
		}
		return false;
			
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
