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
//    /**
//     * Questions associated with each door.
//     */
//    protected final EnumMap<Direction, AbstractQuestion> myQuestions = new EnumMap<>(Direction.class);
    /**
     * Room identifier.
     */
    private final int myRoomId;
    /**
     * Whether this room has been visited.
     */
    private boolean myIsVisited;
    /**
     * Whether this room is the exit.
     */
    private boolean myIsExit;
    /**
     * Whether this room is the entrance.
     */
    private boolean myIsEntrance;
	/**
	 * Creates a room object.
	 */
	public Room() {
        this(0); // Call constructor with default ID
	}
	
    /**
     * Creates a room with a specific ID.
     * 
     * @param theRoomId The room's identifier
     */
    public Room(final int theRoomId) {
        myRoomId = 0; // Default ID
        myIsVisited = false;
        myIsExit = false;
        myIsEntrance = false;
        
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
        
        // Get the adjacent room and set its corresponding door to OPEN
        Room adjacentRoom = Maze.getRoom(theDir);
        if (adjacentRoom != null) {
            adjacentRoom.myDoors.put(theDir.getOpposite(), DoorState.OPEN);
        }
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
		myDoors.put(theDir, DoorState.BLOCKED);
		Maze.getRoom(theDir).blocked(theDir);
	}
	
//    /**
//     * Sets a question for a specific door direction.
//     * 
//     * @param theDirection The direction of the door
//     * @param theQuestion The question to associate with the door
//     */
//    public void setQuestion(final Direction theDirection, final AbstractQuestion theQuestion) {
//        myQuestions.put(theDirection, theQuestion);
//    }
    
//    /**
//     * Gets the question for a specific door direction.
//     * 
//     * @param theDirection The direction of the door
//     * @return The question associated with the door, or null if none
//     */
//    public AbstractQuestion getQuestion(final Direction theDirection) {
//        return myQuestions.get(theDirection);
//    }
    
//    /**
//     * Checks if a door has an associated question.
//     * 
//     * @param theDirection The direction of the door
//     * @return true if the door has a question, false otherwise
//     */
//    public boolean hasQuestion(final Direction theDirection) {
//        return myQuestions.containsKey(theDirection);
//    }
    
    /**
     * Sets the visited status of the room.
     * 
     * @param theVisited Whether the room has been visited
     */
    public void setVisited(final boolean theVisited) {
        myIsVisited = theVisited;
    }
    
    /**
     * Checks if the room has been visited.
     * 
     * @return true if the room has been visited, false otherwise
     */
    public boolean isVisited() {
        return myIsVisited;
    }
    
//    /**
//     * Sets whether this room is the exit.
//     * 
//     * @param theIsExit Whether this room is the exit
//     */
//    public void setExit(final boolean theIsExit) {
//        myIsExit = theIsExit;
//    }
//    
//    /**
//     * Checks if this room is the exit.
//     * 
//     * @return true if this room is the exit, false otherwise
//     */
//    public boolean isExit() {
//        return myIsExit;
//    }
//    
//    /**
//     * Sets whether this room is the entrance.
//     * 
//     * @param theIsEntrance Whether this room is the entrance
//     */
//    public void setEntrance(final boolean theIsEntrance) {
//        myIsEntrance = theIsEntrance;
//    }
//    
//    /**
//     * Checks if this room is the entrance.
//     * 
//     * @return true if this room is the entrance, false otherwise
//     */
//    public boolean isEntrance() {
//        return myIsEntrance;
//    }
    
    /**
     * Gets the room identifier.
     * 
     * @return The room identifier
     */
    public int getRoomId() {
        return myRoomId;
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