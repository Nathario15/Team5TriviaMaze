package model;

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
	protected final EnumMap<Direction, DoorState> myDoors = new EnumMap<>(Direction.class);
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
		myDoors.put(Direction.NORTH, DoorState.Locked);
		myDoors.put(Direction.SOUTH, DoorState.Locked);
		myDoors.put(Direction.EAST, DoorState.Locked);
		myDoors.put(Direction.WEST, DoorState.Locked);
	}

	/**
	 * When player enters a room.
	 * 
	 * @param theDir the direction you are going when you enter the room.
	 */
	private void unlocked(final Direction theDir) {
		myDoors.put(theDir.getOpposite(), DoorState.Open);
	}
	/**
	 * When player leaves a room.
	 * 
	 * @param theDir the direction you are going when you leave the room.
	 */
	public void unlock(final Direction theDir) {
		myDoors.put(theDir, DoorState.Open);
		Maze.getRoom(theDir).unlocked(theDir);
	}
	
	/**
	 * When player fails to enter a room.
	 * 
	 * @param theDir the direction you are going when you fail to enter the room.
	 */
	private void blocked(final Direction theDir) {
		myDoors.put(theDir.getOpposite(), DoorState.Blocked);
	}
	/**
	 * When player fails to leave a room.
	 * 
	 * @param theDir the direction you are going when you fail to leave the room.
	 */
	public void block(final Direction theDir) {
		myDoors.put(theDir, DoorState.Blocked);
		Maze.getRoom(theDir).blocked(theDir);
	}
}