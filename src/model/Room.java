package model;

import controller.SystemControl;
import java.io.Serializable;
import java.util.EnumMap;

/**
 *A room, contains a question you have to answer, and a door that will open.
 * @author Ibrahim Elnikety
 * @version 1.3 
 */
public class Room implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Shows which doors are open.
	 */
	private EnumMap<Direction, Boolean> myDoors;
//	/**
//	 * Door to the north.
//	 */
//	private Boolean myNorthDoor = false;
//	/**
//	 * Door to the south.
//	 */
//	private Boolean mySouthDoor = false;
//	/**
//	 * Door to the east.
//	 */
//	private Boolean myEastDoor = false;
//	/**
//	 * Door to the west.
//	 */
//	private Boolean myWestDoor = false;

	/**
	 * Creates a room object.
	 */
	public Room() {
		myDoors.put(Direction.North, false);
		myDoors.put(Direction.South, false);
		myDoors.put(Direction.East, false);
		myDoors.put(Direction.East, false);
	}

	/**
	 * When player enters a room.
	 * 
	 * @param theDir the direction you are going when you enter the room.
	 */
	public void enter(final Direction theDir) {
		myDoors.put(theDir.getOpposite(), true);
	}
}
