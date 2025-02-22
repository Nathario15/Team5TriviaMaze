package model;

import controller.SystemControl;
import java.io.Serializable;
import java.util.EnumMap;

/**
 * @author Ibrahim ELnikety
 * @version 1 A room, contains a question you have to answer, and a door that
 *          will open.
 */
public class Room implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Shows which doors are open.
	 */
	private EnumMap<Direction, Boolean> Doors;
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
		Doors.put(Direction.North, false);
		Doors.put(Direction.South, false);
		Doors.put(Direction.East, false);
		Doors.put(Direction.East, false);
	}

	/**
	 * When player enters a room.
	 * 
	 * @param theDir the direction you are going when you enter the room.
	 */
	public void enter(final Direction theDir) {
		Doors.put(theDir.getOpposite(), true);
	}
}
