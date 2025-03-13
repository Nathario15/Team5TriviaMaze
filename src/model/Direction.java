package model;

/**
 * This uses the cardinal directions, for reference to things on the map relative to the location of the player.
 * @author Ibrahim Elnikety
 * @version 1.5 
 */
public enum Direction {
	/**
	 * North.
	 */
	NORTH,
	/**
	 * South.
	 */
	SOUTH,
	/**
	 * East.
	 */
	EAST,
	/**
	 * West.
	 */
	WEST;
	/**
	 * Returns the opposite direction.
	 * @return
	 */
	public Direction getOpposite() {
		Direction dir = null;
		if (this == NORTH) {
			dir = SOUTH;
		}
		if (this == SOUTH) {
			dir = NORTH;
		}
		if (this == EAST) {
			dir = WEST;
		}
		if (this == WEST) {
			dir = EAST;
		}
		return dir;
	}
}
