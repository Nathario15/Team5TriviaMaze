package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class MazeTest {

	@Test
	void testDoors() {
		assertNotNull(Maze.getRoom());
		assertNotNull(Maze.getRoom().myDoors.get(Direction.North));
		assertEquals(Maze.getRoom().myDoors.get(Direction.North), DoorState.Locked);
		assertEquals(Maze.getRoom(Direction.North).myDoors.get(Direction.North.getOpposite()), DoorState.Locked);
		assertEquals(Maze.getRoom().myDoors.get(Direction.South), DoorState.Locked);
		assertEquals(Maze.getRoom(Direction.South).myDoors.get(Direction.South.getOpposite()), DoorState.Locked);
		assertEquals(Maze.getRoom().myDoors.get(Direction.East), DoorState.Locked);
		assertEquals(Maze.getRoom(Direction.East).myDoors.get(Direction.East.getOpposite()), DoorState.Locked);//fails here
//		assertEquals(Maze.getRoom().myDoors.get(Direction.West), DoorState.Locked);
//		assertEquals(Maze.getRoom(Direction.West).myDoors.get(Direction.West.getOpposite()), DoorState.Locked);
		
//		
//		Maze.getRoom().block(Direction.North);
//		assertEquals(Maze.getRoom().myDoors.get(Direction.North), DoorState.Blocked);
//		assertEquals(Maze.getRoom(Direction.North).myDoors.get(Direction.North.getOpposite()), DoorState.Blocked);
//		Maze.getRoom().block(Direction.South);
//		assertEquals(Maze.getRoom().myDoors.get(Direction.South), DoorState.Blocked);
//		assertEquals(Maze.getRoom(Direction.South).myDoors.get(Direction.South.getOpposite()), DoorState.Blocked);
//		Maze.getRoom().block(Direction.East);
//		assertEquals(Maze.getRoom().myDoors.get(Direction.East), DoorState.Blocked);
//		assertEquals(Maze.getRoom(Direction.East).myDoors.get(Direction.East.getOpposite()), DoorState.Blocked);
//		Maze.getRoom().block(Direction.West);
//		assertEquals(Maze.getRoom().myDoors.get(Direction.West), DoorState.Blocked);
//		assertEquals(Maze.getRoom(Direction.West).myDoors.get(Direction.West.getOpposite()), DoorState.Blocked);
//		
//		
//		Maze.getRoom().unlock(Direction.North);
//		assertEquals(Maze.getRoom().myDoors.get(Direction.North), DoorState.Open);
//		assertEquals(Maze.getRoom(Direction.North).myDoors.get(Direction.North.getOpposite()), DoorState.Open);
//		Maze.getRoom().unlock(Direction.South);
//		assertEquals(Maze.getRoom().myDoors.get(Direction.South), DoorState.Open);
//		assertEquals(Maze.getRoom(Direction.South).myDoors.get(Direction.South.getOpposite()), DoorState.Open);
//		Maze.getRoom().unlock(Direction.East);
//		assertEquals(Maze.getRoom().myDoors.get(Direction.East), DoorState.Open);
//		assertEquals(Maze.getRoom(Direction.East).myDoors.get(Direction.East.getOpposite()), DoorState.Open);
//		Maze.getRoom().unlock(Direction.West);
//		assertEquals(Maze.getRoom().myDoors.get(Direction.West), DoorState.Open);
//		assertEquals(Maze.getRoom(Direction.West).myDoors.get(Direction.West.getOpposite()), DoorState.Open);
	}

	@Test
	void testMovement() {
		assertEquals(Maze.getX(), 4);
		assertEquals(Maze.getY(), 4);
		Maze.setRoom(Direction.North);
		assertEquals(Maze.getY(), 5);
		Maze.setRoom(Direction.South);
		assertEquals(Maze.getY(), 4);
		Maze.setRoom(Direction.East);
		assertEquals(Maze.getX(), 5);
		Maze.setRoom(Direction.West);
		assertEquals(Maze.getX(), 4);
	}
}
