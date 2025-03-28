package view;

import controller.SystemControl;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import model.AbstractQuestion;
import model.Direction;
import model.DoorState;
import model.Maze;
import model.ResourceManager;
import model.Room;

/**
 * MazePanel is responsible for rendering the maze visually.
 * 
 * @author Team5
 * @version 1.1
 */
public final class MazePanel extends JPanel implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Size of each cell in pixels.
	 */
	private static final int CELL_SIZE = 70; // was 50
	/**
	 * The size of the map that is displayed.
	 */
	private static final int MAP_SIZE = 7;

	/**
	 * Width of door lines.
	 */
	private static final int DOOR_WIDTH = 3; // was 3

	/**
	 * Player circle inset from cell edge.
	 */
	private static final int PLAYER_INSET = 15;

	/**
	 * Room highlight inset from cell edge.
	 */
	private static final int ROOM_HIGHLIGHT_INSET = 3; // was 3

	/**
	 * Player size reduction from cell size.
	 */
	private static final int PLAYER_SIZE_REDUCTION = 30;

	/**
	 * Room size reduction from cell size.
	 */
	private static final int ROOM_SIZE_REDUCTION = 6;

	/**
	 * Blue component value for highlighted rooms.
	 */
	private static final int HIGHLIGHT_BLUE = 255;

	/**
	 * Red/Green component value for highlighted rooms.
	 */
	private static final int HIGHLIGHT_RED_GREEN = 200;

	/**
	 * Adjusted player position.
	 */
	private static final int PLAYER_Y_ADJUSTMENT = 6;

	/**
	 * Width of cell borders.
	 */
	private static final int CELL_BORDER_WIDTH = 2; // was 1

	/**
	 * Width of outer borders.
	 */
	private static final int OUTER_BORDER_WIDTH = 3; // was 0

	/**
	 * MineCraft Dirt.
	 */
	@SuppressWarnings("unused")
	private static final Color DIRT = new Color(146, 108, 77);

	/**
	 * MineCraft Grass (used for cell borders).
	 */
	@SuppressWarnings("unused")
	private static final Color GRASS = new Color(172, 224, 119);

	/**
	 * Constructor.
	 */
	public MazePanel() {
		setPreferredSize(new Dimension(MAP_SIZE * CELL_SIZE, MAP_SIZE * CELL_SIZE));
	}

	@Override
	protected void paintComponent(final Graphics theG) {
		super.paintComponent(theG);
		drawMaze(theG);

	}

	private void drawMaze(final Graphics theG) {
		final Graphics2D g2d = (Graphics2D) theG;
		final Image m;
		
	    // Choose background image based on cheat status
	    if (AbstractQuestion.cheatsEnabled()) {
	    	m = ResourceManager.getInstance().loadImage("/images/Jack-Black.png");
	    } else {
	        m = ResourceManager.getInstance().loadImage("/images/GrassBlock.jpg");
	    }
		super.paintComponent(g2d);
		g2d.drawImage(m, 0, 0, MAP_SIZE * CELL_SIZE + OUTER_BORDER_WIDTH, MAP_SIZE * CELL_SIZE + OUTER_BORDER_WIDTH,
				this);
		// First draw all cells with their standard grid
		for (int row = 0; row < MAP_SIZE; row++) {
			for (int col = 0; col < MAP_SIZE; col++) {

				// Draw standard green grid lines
				theG.setColor(Color.BLACK);
				for (int i = 0; i < CELL_BORDER_WIDTH; i++) {
					theG.drawRect(col * CELL_SIZE + OUTER_BORDER_WIDTH + i, row * CELL_SIZE + OUTER_BORDER_WIDTH + i,
							CELL_SIZE - 2 * i, CELL_SIZE - 2 * i);
				}
			}
		}
		theG.setColor(Color.BLACK);
		for (int i = 0; i < OUTER_BORDER_WIDTH; i++) {
			theG.drawRect(i, i, MAP_SIZE * CELL_SIZE - 2 * i + OUTER_BORDER_WIDTH,
					MAP_SIZE * CELL_SIZE - 2 * i + OUTER_BORDER_WIDTH);
		}
		// Get the current room and its coordinates
		final Room currentRoom = Maze.getRoom();
		final int playerX = Maze.getDisplayX();
		final int playerY = PLAYER_Y_ADJUSTMENT - Maze.getDisplayY();

		// Draw doors with proper colors (thicker lines)
		if (currentRoom != null) {
			g2d.setStroke(new BasicStroke(DOOR_WIDTH));

			// North door (top)
			drawDoor(g2d, playerX, playerY, Direction.NORTH, currentRoom.getDoorState(Direction.NORTH));

			// South door (bottom)
			drawDoor(g2d, playerX, playerY, Direction.SOUTH, currentRoom.getDoorState(Direction.SOUTH));

			// East door (right)
			drawDoor(g2d, playerX, playerY, Direction.WEST, currentRoom.getDoorState(Direction.WEST));

			// West door (left)
			drawDoor(g2d, playerX, playerY, Direction.EAST, currentRoom.getDoorState(Direction.EAST));

			// Reset stroke for other drawing
			g2d.setStroke(new BasicStroke(1.0f));

			// Highlight current room
			theG.setColor(new Color(HIGHLIGHT_RED_GREEN, HIGHLIGHT_RED_GREEN, HIGHLIGHT_BLUE)); // Light blue highlight
			theG.fillRect(playerX * CELL_SIZE + ROOM_HIGHLIGHT_INSET + OUTER_BORDER_WIDTH,
					playerY * CELL_SIZE + ROOM_HIGHLIGHT_INSET + OUTER_BORDER_WIDTH, CELL_SIZE - ROOM_SIZE_REDUCTION,
					CELL_SIZE - ROOM_SIZE_REDUCTION);
		}

		final Image player;
		if (AbstractQuestion.cheatsEnabled()) {
			player = ResourceManager.getInstance().loadImage("/images/zombie.jpg");
		} else {
			player = ResourceManager.getInstance().loadImage("/images/steve-head.jpg");
		}
		g2d.drawImage(player, playerX * CELL_SIZE + PLAYER_INSET + OUTER_BORDER_WIDTH,
				playerY * CELL_SIZE + PLAYER_INSET + OUTER_BORDER_WIDTH, CELL_SIZE - PLAYER_SIZE_REDUCTION,
				CELL_SIZE - PLAYER_SIZE_REDUCTION, this);
	}

	/**
	 * Draws a colored door based on its state.
	 * 
	 * @param theG         The Graphics2D context
	 * @param theX         The X coordinate of the room
	 * @param theY         The Y coordinate of the room
	 * @param theDirection The direction of the door
	 * @param theState     The state of the door
	 */
	private void drawDoor(final Graphics2D theG, final int theX, final int theY, final Direction theDirection,
			final DoorState theState) {

		// Set color based on door state
		if (theState == DoorState.OPEN) {
			theG.setColor(Color.GREEN);
		} else if (theState == DoorState.LOCKED) {
			theG.setColor(Color.RED);
		} else { // BLOCKED
			theG.setColor(Color.BLUE);
		}

		final int x = theX * CELL_SIZE + OUTER_BORDER_WIDTH;
		final int y = theY * CELL_SIZE + OUTER_BORDER_WIDTH;

		// Draw the door on the appropriate side
		if (theDirection == Direction.NORTH) {
			theG.drawLine(x, y, x + CELL_SIZE, y);
		} else if (theDirection == Direction.SOUTH) {
			theG.drawLine(x, y + CELL_SIZE, x + CELL_SIZE, y + CELL_SIZE);
		} else if (theDirection == Direction.EAST) {
			theG.drawLine(x + CELL_SIZE, y, x + CELL_SIZE, y + CELL_SIZE);
		} else if (theDirection == Direction.WEST) {
			theG.drawLine(x, y, x, y + CELL_SIZE);
		}
	}

	@Override
	public void keyTyped(final KeyEvent theE) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(final KeyEvent theE) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(final KeyEvent theE) {
		final int keyCode = theE.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			SystemControl.getInstance().getGameView().movePlayer(Direction.NORTH, GameView.myMazePanel);
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			SystemControl.getInstance().getGameView().movePlayer(Direction.SOUTH, GameView.myMazePanel);
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			SystemControl.getInstance().getGameView().movePlayer(Direction.WEST, GameView.myMazePanel);
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			SystemControl.getInstance().getGameView().movePlayer(Direction.EAST, GameView.myMazePanel);
			break;
		default:
			// No action for other keys
			break;
		}
	}
}