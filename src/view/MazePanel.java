package view;

import model.Maze;
import javax.swing.*;
import java.awt.*;

/**
 * MazePanel is responsible for rendering the maze visually.
 */
public final class MazePanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int CELL_SIZE = 50; // Size of each cell in pixels
    private static final int MAP_SIZE = Maze.MAP_SIZE;

    public MazePanel() {
        setPreferredSize(new Dimension(MAP_SIZE * CELL_SIZE, MAP_SIZE * CELL_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawMaze(g);
    }

    private void drawMaze(Graphics g) {
        for (int row = 0; row < MAP_SIZE; row++) {
            for (int col = 0; col < MAP_SIZE; col++) {
                if (Maze.getRoom() != null) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        // Draw Player Position
        int playerX = Maze.getX();
        int playerY = Maze.getY();
        g.setColor(Color.RED);
        g.fillOval(playerX * CELL_SIZE + 10, playerY * CELL_SIZE + 10, CELL_SIZE - 20, CELL_SIZE - 20);
    }
}