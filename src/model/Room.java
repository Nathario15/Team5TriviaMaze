package model;

import controller.SystemControl;
import java.io.Serializable;
import java.util.EnumMap;

/**
 * A room, contains a question you have to answer, and a door that will open.
 * 
 * @author Ibrahim Elnikety
 * @version 1.3
 */
public class Room implements Serializable {
	/**
	 * Serialization.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Shows which doors are open.
	 */
	protected final EnumMap<Direction, DoorState> myDoors = new EnumMap<>(Direction.class);

	/**
	 * Creates a room object.
	 */
	public Room() {
//        this(0); // Call constructor with default ID
		myDoors.put(Direction.NORTH, DoorState.LOCKED);
		myDoors.put(Direction.SOUTH, DoorState.LOCKED);
		myDoors.put(Direction.EAST, DoorState.LOCKED);
		myDoors.put(Direction.WEST, DoorState.LOCKED);
	}

	/**
	 * When player enters a room.
	 * 
	 * @param theDir the direction you are going when you enter the room.
	 */
	private void unlocked(final Direction theDir) {
		// Set the opposite side door to OPEN
		myDoors.put(theDir.getOpposite(), DoorState.OPEN);
	}

	/**
	 * When player leaves a room.
	 * 
	 * @param theDir the direction you are going when you leave the room.
	 */
	public void unlock(final Direction theDir) {
		// Set the current room's door to OPEN
		myDoors.put(theDir, DoorState.OPEN);

		// Get the adjacent room
		final Room adjacentRoom = Maze.getRoom(theDir);

		// Check if player has reached the edge of the maze (win condition)
		if (adjacentRoom == null) {
			SystemControl.getInstance().endGame();
			return;
		}

		// Set the corresponding door in the adjacent room to OPEN
		adjacentRoom.unlocked(theDir);
	}

	/**
	 * When player fails to enter a room.
	 * 
	 * @param theDir the direction you are going when you fail to enter the room.
	 */
	private void blocked(final Direction theDir) {
		myDoors.put(theDir.getOpposite(), DoorState.BLOCKED);
	}

	/**
	 * When player fails to leave a room.
	 * 
	 * @param theDir the direction you are going when you fail to leave the room.
	 */
	public void block(final Direction theDir) {
		// Mark this side of the door as blocked
		myDoors.put(theDir, DoorState.BLOCKED);

		// Get the adjacent room - check if it exists before trying to block it
		final Room adjacentRoom = Maze.getRoom(theDir);
		if (adjacentRoom != null) {
			adjacentRoom.blocked(theDir);
		}
	}

	/**
	 * Gets the door state for a specific direction.
	 * 
	 * @param theDirection The direction to check
	 * @return The door state (Open, Locked, or Blocked)
	 */
	public DoorState getDoorState(final Direction theDirection) {
		return myDoors.get(theDirection);
	}
}