package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * This is a test class for Maze and Room.
 * @author Team 5
 * @version 1.0
 */
class MazeRoomTest {

	@Test
	void testDoors() {

		assertEquals(Maze.getX(), Maze.MAP_SIZE / 2);
		assertEquals(Maze.getY(), Maze.MAP_SIZE / 2);
    
		assertNotNull(Maze.getRoom());
		assertNotNull(Maze.getRoom().myDoors.get(Direction.NORTH));
		assertEquals(Maze.getRoom().myDoors.get(Direction.NORTH), DoorState.LOCKED);
		assertEquals(Maze.getRoom(Direction.NORTH).myDoors.get(Direction.NORTH.getOpposite()), DoorState.LOCKED);
		assertEquals(Maze.getRoom().myDoors.get(Direction.SOUTH), DoorState.LOCKED);
		assertEquals(Maze.getRoom(Direction.SOUTH).myDoors.get(Direction.SOUTH.getOpposite()), DoorState.LOCKED);
		assertEquals(Maze.getRoom().myDoors.get(Direction.EAST), DoorState.LOCKED);
		assertEquals(Maze.getRoom(Direction.EAST).myDoors.get(Direction.EAST.getOpposite()), DoorState.LOCKED);
		assertEquals(Maze.getRoom(Direction.WEST).myDoors.get(Direction.WEST.getOpposite()), DoorState.LOCKED);

		Maze.getRoom().block(Direction.NORTH);
		assertEquals(Maze.getRoom().myDoors.get(Direction.NORTH), DoorState.BLOCKED);
		assertEquals(Maze.getRoom(Direction.NORTH).myDoors.get(Direction.NORTH.getOpposite()), DoorState.BLOCKED);
		Maze.getRoom().block(Direction.SOUTH);
		assertEquals(Maze.getRoom().myDoors.get(Direction.SOUTH), DoorState.BLOCKED);
		assertEquals(Maze.getRoom(Direction.SOUTH).myDoors.get(Direction.SOUTH.getOpposite()), DoorState.BLOCKED);
		Maze.getRoom().block(Direction.EAST);
		assertEquals(Maze.getRoom().myDoors.get(Direction.EAST), DoorState.BLOCKED);
		assertEquals(Maze.getRoom(Direction.EAST).myDoors.get(Direction.EAST.getOpposite()), DoorState.BLOCKED);
		Maze.getRoom().block(Direction.WEST);
		assertEquals(Maze.getRoom().myDoors.get(Direction.WEST), DoorState.BLOCKED);
		assertEquals(Maze.getRoom(Direction.WEST).myDoors.get(Direction.WEST.getOpposite()), DoorState.BLOCKED);

		Maze.getRoom().unlock(Direction.NORTH);
		assertEquals(Maze.getRoom().myDoors.get(Direction.NORTH), DoorState.OPEN);
		assertEquals(Maze.getRoom(Direction.NORTH).myDoors.get(Direction.NORTH.getOpposite()), DoorState.OPEN);
		Maze.getRoom().unlock(Direction.SOUTH);
		assertEquals(Maze.getRoom().myDoors.get(Direction.SOUTH), DoorState.OPEN);
		assertEquals(Maze.getRoom(Direction.SOUTH).myDoors.get(Direction.SOUTH.getOpposite()), DoorState.OPEN);
		Maze.getRoom().unlock(Direction.EAST);
		assertEquals(Maze.getRoom().myDoors.get(Direction.EAST), DoorState.OPEN);
		assertEquals(Maze.getRoom(Direction.EAST).myDoors.get(Direction.EAST.getOpposite()), DoorState.OPEN);
		Maze.getRoom().unlock(Direction.WEST);
		assertEquals(Maze.getRoom().myDoors.get(Direction.WEST), DoorState.OPEN);
		assertEquals(Maze.getRoom(Direction.WEST).myDoors.get(Direction.WEST.getOpposite()), DoorState.OPEN);
	}

	@Test
	void testMovement() {
		final int start = Maze.MAP_SIZE / 2;
		assertEquals(Maze.getX(), start);
		assertEquals(Maze.getY(), start);
		Maze.setRoom(Direction.NORTH);
		assertEquals(Maze.getY(), start + 1);
		Maze.setRoom(Direction.SOUTH);
		assertEquals(Maze.getY(), start);
		Maze.setRoom(Direction.EAST);
		assertEquals(Maze.getX(), start + 1);
		Maze.setRoom(Direction.WEST);
		assertEquals(Maze.getX(), start);
	}
}
