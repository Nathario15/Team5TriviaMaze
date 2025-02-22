package model;

/**
 * @author Ibrahim Elnikety
 * @version 1.0 This uses the cardinal directions, for reference to things on
 *          the map relative to the location of the player.
 */
public enum Direction {
	/**
	 * North.
	 */
	North,
	/**
	 * South.
	 */
	South,
	/**
	 * East.
	 */
	East,
	/**
	 * West.
	 */
	West;
	/**
	 * Returns the opposite direction.
	 * @return
	 */
	public Direction getOpposite() {
		Direction dir = null;
		if (this == North) {
			dir = South;
		}
		if (this == South) {
			dir = North;
		}
		if (this == East) {
			dir = West;
		}
		if (this == West) {
			dir = East;
		}
		return dir;
	}
}
