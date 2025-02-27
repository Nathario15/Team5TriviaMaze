package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MazeTest {

	@Test
	void testMovement() {
		assertEquals(Maze.getX(), 0);
		assertEquals(Maze.getY(), 0);
		Maze.setRoom(Direction.North);
		assertEquals(Maze.getY(), 1);
		Maze.setRoom(Direction.South);
		assertEquals(Maze.getY(), 0);
		Maze.setRoom(Direction.East);
		assertEquals(Maze.getX(), 1);
		Maze.setRoom(Direction.West);
		assertEquals(Maze.getX(), 0);
	}

}
