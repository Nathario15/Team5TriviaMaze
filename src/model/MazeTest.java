package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MazeTest {

	@Test
	void test() {
		assertEquals(Maze.getX(), 0);
		assertEquals(Maze.getY(), 0);
		Maze.move(Direction.North);
		assertEquals(Maze.getY(), 1);
		Maze.move(Direction.South);
		assertEquals(Maze.getY(), 0);
		Maze.move(Direction.East);
		assertEquals(Maze.getX(), 1);
		Maze.move(Direction.West);
		assertEquals(Maze.getX(), 0);
	}

}
