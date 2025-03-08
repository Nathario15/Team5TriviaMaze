package view;

import model.Maze;
import javax.swing.*;
import java.awt.*;

/**
 * MazePanel is responsible for rendering the maze visually.
 * @author Team5
 * @version 1.0
 */
public final class MazePanel extends JPanel {
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
        final int playerX = Maze.getX();
        final int playerY = Maze.getY();
        theG.setColor(Color.RED);
        theG.fillOval(playerX * CELL_SIZE + 10, playerY * CELL_SIZE + 10, CELL_SIZE - 20, CELL_SIZE - 20);
    }
}