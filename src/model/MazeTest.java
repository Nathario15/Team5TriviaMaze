package model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MazeTest {

	@Test
	void testDoors() {
		assertEquals(Maze.getRoom().myDoors.get(Direction.North), DoorState.Locked);
		assertEquals(Maze.getRoom(Direction.North).myDoors.get(Direction.North.getOpposite()), DoorState.Locked);
		assertEquals(Maze.getRoom().myDoors.get(Direction.South), DoorState.Locked);
		assertEquals(Maze.getRoom(Direction.South).myDoors.get(Direction.South.getOpposite()), DoorState.Locked);
		assertEquals(Maze.getRoom().myDoors.get(Direction.East), DoorState.Locked);
		assertEquals(Maze.getRoom(Direction.East).myDoors.get(Direction.East.getOpposite()), DoorState.Locked);
		assertEquals(Maze.getRoom().myDoors.get(Direction.West), DoorState.Locked);
		assertEquals(Maze.getRoom(Direction.West).myDoors.get(Direction.West.getOpposite()), DoorState.Locked);
		
		
	}

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
