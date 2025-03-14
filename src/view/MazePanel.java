package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import javax.swing.JPanel;

import controller.SystemControl;
import model.Maze;
import model.Direction;
import model.DoorState;
import model.Room;

/**
 * MazePanel is responsible for rendering the maze visually.
 * @author Team5
 * @version 1.1
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
     * Width of door lines.
     */
    private static final int DOOR_WIDTH = 3;

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
        Graphics2D g2d = (Graphics2D) theG;
        
        // First draw all cells with their standard grid
        for (int row = 0; row < MAP_SIZE; row++) {
            for (int col = 0; col < MAP_SIZE; col++) {
                // Fill each room with light gray
                theG.setColor(Color.LIGHT_GRAY);
                theG.fillRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                
                // Draw standard black grid lines
                theG.setColor(Color.BLACK);
                theG.drawRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
        
        // Get the current room and its coordinates
        Room currentRoom = Maze.getRoom();
        final int playerX = Maze.getDisplayX();
        final int playerY = Maze.getDisplayY();
        
        // Draw doors with proper colors (thicker lines)
        if (currentRoom != null) {
            g2d.setStroke(new BasicStroke(DOOR_WIDTH));           
            
            // North door (top)
            drawDoor(g2d, playerX, playerY, Direction.NORTH, 
                    currentRoom.getDoorState(Direction.NORTH));
            
            // South door (bottom)
            drawDoor(g2d, playerX, playerY, Direction.SOUTH, 
                    currentRoom.getDoorState(Direction.SOUTH));
            
            // East door (right)
            drawDoor(g2d, playerX, playerY, Direction.WEST, 
                    currentRoom.getDoorState(Direction.WEST));
            
            // West door (left)
            drawDoor(g2d, playerX, playerY, Direction.EAST, 
                    currentRoom.getDoorState(Direction.EAST));
            
            // Reset stroke for other drawing
            g2d.setStroke(new BasicStroke(1.0f));
            
            // Highlight current room
            theG.setColor(new Color(200, 200, 255)); // Light blue highlight
            theG.fillRect(playerX * CELL_SIZE + 3, playerY * CELL_SIZE + 3, 
                    CELL_SIZE - 6, CELL_SIZE - 6);
        }

        // Draw Player Position
        theG.setColor(Color.RED);
        theG.fillOval(playerX * CELL_SIZE + 15, playerY * CELL_SIZE + 15, 
                CELL_SIZE - 30, CELL_SIZE - 30);
    }
    
    /**
     * Draws a colored door based on its state.
     * @param theG The Graphics2D context
     * @param theX The X coordinate of the room
     * @param theY The Y coordinate of the room
     * @param theDirection The direction of the door
     * @param theState The state of the door
     */
    private void drawDoor(final Graphics2D theG, final int theX, final int theY, 
            final Direction theDirection, final DoorState theState) {
        
        // Set color based on door state
        if (theState == DoorState.OPEN) {
            theG.setColor(Color.GREEN);
        } else if (theState == DoorState.LOCKED) {
            theG.setColor(Color.RED);
        } else { // BLOCKED
            theG.setColor(Color.BLACK);
        }
        
        int x = theX * CELL_SIZE;
        int y = theY * CELL_SIZE;
        
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
}