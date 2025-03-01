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
	 * How big the map is.
	 */
	public static final int MAP_SIZE = 8;
	/**
	 * A map of rooms.
	 */
	static final Room[][] MAP = {
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

	private Maze() {

	} // might make the class not static later

	/**
	 * used for testing.
	 * 
	 * @return
	 */
	public static int getY() {
		return y;
	}

	/**
	 * used for testing.
	 * 
	 * @return
	 */
	public static int getX() {
		return x;
	}

	/**
	 * The map will stay the same size, but it's contents will change.
	 */
	protected static void loadMap() {

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
		if (theDirection == Direction.North) {
			temp = MAP[x][y + 1];
		}
		if (theDirection == Direction.South) {
			temp = MAP[x][y - 1];
		}
		if (theDirection == Direction.East) {
			temp = MAP[x + 1][y];
		}
		if (theDirection == Direction.West) {
			temp = MAP[x + 1][y];
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
		if (theDirection == Direction.North) {
			y++;
		} else if (theDirection == Direction.South) {
			y--;
		} else if (theDirection == Direction.East) {
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
		return getRoom(theDirection) != null && getRoom().myDoors.get(theDirection) == DoorState.Open
				|| getRoom().myDoors.get(theDirection) == DoorState.Locked && attempt(theDirection);
	}

	private static boolean attempt(final Direction theDirection) {
		if (SystemControl.triggerQuestion()) {
			getRoom().unlock(theDirection);
			setRoom(theDirection);
			return true;
		} else {
			getRoom().block(theDirection);
			return false;
		}
	}

}
