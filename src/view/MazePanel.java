package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import controller.SystemControl;
import model.Direction;
import model.Maze;

/**
 * MazePanel is responsible for rendering the maze visually.
 * 
 * @author Team5
 * @version 1.0
 */
public final class MazePanel extends JPanel implements KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Size of each cell in pixels.
	 */
	private static final int CELL_SIZE = 50;
	/**
	 * The size of the map that is displayed.
	 */
	private static final int MAP_SIZE = 7;

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
		for (int row = 0; row < MAP_SIZE; row++) {
			for (int col = 0; col < MAP_SIZE; col++) {
				if (Maze.getRoom() != null) {
					theG.setColor(Color.LIGHT_GRAY);
					theG.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
					theG.setColor(Color.BLACK);
					theG.drawRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
				}
			}
		}

		// Draw Player Position
		final int playerX = Maze.getDisplayX();
		final int playerY = Maze.getDisplayY();
		theG.setColor(Color.RED);
		theG.fillOval(playerX * CELL_SIZE + 10, playerY * CELL_SIZE + 10, CELL_SIZE - 20, CELL_SIZE - 20);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			GameView.instance.movePlayer(Direction.SOUTH, GameView.myMazePanel);
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			GameView.instance.movePlayer(Direction.SOUTH, GameView.myMazePanel);
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			GameView.instance.movePlayer(Direction.SOUTH, GameView.myMazePanel);
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			GameView.instance.movePlayer(Direction.SOUTH, GameView.myMazePanel);
			break;

		}
	}
}