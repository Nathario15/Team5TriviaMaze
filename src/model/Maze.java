package model;

public final class Maze {
	/**
	 * A map of rooms.
	 */
	public static final Room[][]MAP = null;
	/**
	 * y coordinate.
	 */
	private static int y;
	/**
	 * x coordinate.
	 */
	private static int x;
	private Maze() {
		
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
	 * @param theDirection
	 * @return
	 */
	public static Room getRoom() {
		return MAP[x][y];
	}
	/**
	 * moves and returns new room.
	 * @param dir
	 * @return
	 */
	public static Room move(final Direction theDirection) {
		if (theDirection == Direction.North) {
			y++;
		}
		if (theDirection == Direction.South) {
			y--;
		}
		if (theDirection == Direction.East) {
			x++;
		}
		if (theDirection == Direction.West) {
			x--;
		}
		return MAP[x][y];
		
	}
}
