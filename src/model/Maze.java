package model;

public class Maze {
	public static final Room[][]MAP=null;
	private static int y;
	private static int x;
	public static Room getNorth() {
		return MAP[x][y+1];
	}
	public static Room getSouth() {
		return MAP[x][y-1];
	}
	public static Room getEast() {
		return MAP[x+1][y];
	}
	public static Room getWest() {
		return MAP[x+1][y];
	}
	
}
