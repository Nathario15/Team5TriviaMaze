package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MazeTest {

	@Test
	void testDoors() {
		assertEquals(Maze.getRoom().myDoors.get(Direction.NORTH), DoorState.Locked);
		assertEquals(Maze.getRoom(Direction.NORTH).myDoors.get(Direction.NORTH.getOpposite()), DoorState.Locked);
		assertEquals(Maze.getRoom().myDoors.get(Direction.SOUTH), DoorState.Locked);
		assertEquals(Maze.getRoom(Direction.SOUTH).myDoors.get(Direction.SOUTH.getOpposite()), DoorState.Locked);
		assertEquals(Maze.getRoom().myDoors.get(Direction.EAST), DoorState.Locked);
		assertEquals(Maze.getRoom(Direction.EAST).myDoors.get(Direction.EAST.getOpposite()), DoorState.Locked);
		assertEquals(Maze.getRoom().myDoors.get(Direction.WEST), DoorState.Locked);
		assertEquals(Maze.getRoom(Direction.WEST).myDoors.get(Direction.WEST.getOpposite()), DoorState.Locked);
		
		
	}

	@Test
	void testMovement() {
		assertEquals(Maze.getX(), 0);
		assertEquals(Maze.getY(), 0);
		Maze.setRoom(Direction.NORTH);
		assertEquals(Maze.getY(), 1);
		Maze.setRoom(Direction.SOUTH);
		assertEquals(Maze.getY(), 0);
		Maze.setRoom(Direction.EAST);
		assertEquals(Maze.getX(), 1);
		Maze.setRoom(Direction.WEST);
		assertEquals(Maze.getX(), 0);
	}
}
