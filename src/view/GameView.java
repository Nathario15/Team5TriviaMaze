package view;

import model.GameState;
import model.DatabaseManager;
import model.Difficulty;
import model.Direction;
import model.Maze;

import javax.swing.*;
import java.awt.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.Random;

/**
 * The main view for the Trivia Maze Game.
 */
public final class GameView extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private Player player;
	/**
	 * games save data.
	 */
	private GameState myGameState;
	/**
	 * The card layout.
	 */
	private CardLayout myCardLayout;
	/**
	 * The main panel.
	 */
	private JPanel myMainPanel;
	/**
	 * Whether or not the player is currently in game.
	 */
    private boolean myInGame;
    /**
	 * The file name for the save file.
	 */
	private String myFilename = ""; //TODO fix filename

	/**
	 * Constructor.
	 */
    public GameView() {
        setTitle("Trivia Maze Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        myCardLayout = new CardLayout();
        myMainPanel = new JPanel(myCardLayout);
        myInGame = false;

        addMainMenu();
        addGamePanel();
        addInstructionsPanel();
        addAboutPanel();

        add(myMainPanel);
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(() -> {
            final GameView game = new GameView();
            game.setVisible(true);
        });
    }

    private void addMainMenu() {
        final JPanel menuPanel = new JPanel(new GridLayout(5, 1));

        final JButton newGameButton = new JButton("New Game");
        final JButton loadGameButton = new JButton("Load Game");
        final JButton instructionsButton = new JButton("Instructions");
        final JButton aboutBtn = new JButton("About");
        final JButton exitBtn = new JButton("Exit");

        newGameButton.addActionListener(_ -> newGame());
        loadGameButton.addActionListener(_ -> loadGame());
        instructionsButton.addActionListener(_ -> displayInstructions());
        aboutBtn.addActionListener(_ -> displayAbout());
        exitBtn.addActionListener(_ -> System.exit(0));

        menuPanel.add(newGameButton);
        menuPanel.add(loadGameButton);
        menuPanel.add(instructionsButton);
        menuPanel.add(aboutBtn);
        menuPanel.add(exitBtn);

        myMainPanel.add(menuPanel, "MainMenu");
    }

    private void addGamePanel() {
        final JPanel gamePanel = new JPanel(new BorderLayout());

        MazePanel mazePanel = new MazePanel();
        gamePanel.add(mazePanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new GridLayout(1, 4));
        JButton northButton = new JButton("North");
        JButton southButton = new JButton("South");
        JButton eastButton = new JButton("East");
        JButton westButton = new JButton("West");

        northButton.addActionListener(_ -> movePlayer(Direction.NORTH, mazePanel));
        southButton.addActionListener(_ -> movePlayer(Direction.SOUTH, mazePanel));
        eastButton.addActionListener(_ -> movePlayer(Direction.EAST, mazePanel));
        westButton.addActionListener(_ -> movePlayer(Direction.WEST, mazePanel));

        controlPanel.add(northButton);
        controlPanel.add(southButton);
        controlPanel.add(eastButton);
        controlPanel.add(westButton);

        gamePanel.add(controlPanel, BorderLayout.SOUTH);

        myMainPanel.add(gamePanel, "Game");
    }

    private void addInstructionsPanel() {
        final JPanel instructionsPanel = new JPanel(new BorderLayout());
        JLabel instructionsLabel = new JLabel(
                "<html><b>Instructions:</b> Navigate the maze by answering trivia questions correctly.</html>",
                SwingConstants.CENTER);
        instructionsPanel.add(instructionsLabel, BorderLayout.CENTER);

        final JButton backButton = new JButton("Back");
        backButton.addActionListener(_ -> {
            if (myInGame) {
                myCardLayout.show(myMainPanel, "Game");
            } else {
                myCardLayout.show(myMainPanel, "MainMenu");
            }
        });
        instructionsPanel.add(backButton, BorderLayout.SOUTH);

        myMainPanel.add(instructionsPanel, "Instructions");
    }

    private void addAboutPanel() {
        final JPanel aboutPanel = new JPanel(new BorderLayout());
        JLabel aboutLabel = new JLabel("<html><b>Trivia Maze Game</b><br>Created by Nathaniel, Ibrahim, and Jayden.</html>",
                SwingConstants.CENTER);
        aboutPanel.add(aboutLabel, BorderLayout.CENTER);

        final JButton backBtn = new JButton("Back");
        backBtn.addActionListener(_ -> {
            if (myInGame) {
                myCardLayout.show(myMainPanel, "Game");
            } else {
                myCardLayout.show(myMainPanel, "MainMenu");
            }
        });
        aboutPanel.add(backBtn, BorderLayout.SOUTH);

        myMainPanel.add(aboutPanel, "About");
    }

    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem saveGameItem = new JMenuItem("Save Game");
        JMenuItem loadGameItem = new JMenuItem("Load Game");
        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem exitGameItem = new JMenuItem("Exit");

        saveGameItem.addActionListener(_ -> saveGame());
        loadGameItem.addActionListener(_ -> loadGame());
        newGameItem.addActionListener(_ -> newGame());
        exitGameItem.addActionListener(_ -> System.exit(0));

        fileMenu.add(saveGameItem);
        fileMenu.add(loadGameItem);
        fileMenu.add(newGameItem);
        fileMenu.add(exitGameItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem instructionsGameItem = new JMenuItem("Instructions");
        JMenuItem aboutGameItem = new JMenuItem("About");

        instructionsGameItem.addActionListener(_ -> displayInstructions());
        aboutGameItem.addActionListener(_ -> displayAbout());

        helpMenu.add(instructionsGameItem);
        helpMenu.add(aboutGameItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Starts a new game, prompting the user to select a difficulty level.
     */
    public void newGame() {
        String[] difficulties = {"Easy", "Medium", "Hard"};
        String selectedDifficulty = (String) JOptionPane.showInputDialog(this, 
                "Select Difficulty:", "New Game",
                JOptionPane.QUESTION_MESSAGE, null, difficulties, difficulties[0]);

        if (selectedDifficulty != null) {
            myGameState = new GameState();
            JOptionPane.showMessageDialog(this, "Game started on " + selectedDifficulty + " difficulty.");
            myInGame = true;
            myCardLayout.show(myMainPanel, "Game");
            addMenuBar();
            DatabaseManager.getInstance().setDifficulty(Difficulty.EASY);
        }
    }

    /**
	 * begins serialization.
	 */
    public void saveGame() {
        myGameState.saveToFile(myFilename);
        JOptionPane.showMessageDialog(this, "Game saved successfully!");
    }

    /**
	 * begins deserialization.
	 */
    public void loadGame() {
        myGameState = GameState.loadFromFile(myFilename);
        JOptionPane.showMessageDialog(this, "Game loaded successfully!");
        myCardLayout.show(myMainPanel, "Game");
    }

    /**
     * Displays the instructions screen.
     */
    public void displayInstructions() {
        myCardLayout.show(myMainPanel, "Instructions");
    }

    /**
     * Displays the about screen.
     */
    public void displayAbout() {
        myCardLayout.show(myMainPanel, "About");
    }

    private void movePlayer(Direction direction, MazePanel mazePanel) {
        boolean success = Maze.move(direction.getOpposite());

        if (success) {
            mazePanel.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Cannot move in that direction!");
        }
    }
}
