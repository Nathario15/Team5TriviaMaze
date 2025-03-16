package view;

import model.GameState;
import model.DatabaseManager;
import model.Difficulty;
import model.Direction;
import model.Maze;
import model.QuestionFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import controller.SystemControl;

import java.util.Random;

/**
 * The main view for the Trivia Maze Game.
 */
public final class GameView extends JFrame implements KeyListener {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
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
     * The panel for tracking the state of the game.
     */
    private JPanel myTrackerPanel;
    /**
     * The label for tracking the player's position.
     */
    private JLabel myPositionLabel;
    /**
     * The label for tracking the correct question count.
     */
    private JLabel myCorrectQuestionsLabel;
    /**
     * The label for tracking the incorrect question count.
     */
    private JLabel myIncorrectQuestionsLabel;
    /**
     * The label for tracking the locked door count.
     */
    private JLabel myLockedDoorsLabel;
    
    public static GameView instance;
    static MazePanel myMazePanel;

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

        // Register this view with SystemControl
        SystemControl.getInstance().setGameView(this);
        System.out.println("GameView registered with SystemControl");

        addMainMenu();
        addGamePanel();
        addInstructionsPanel();
        addAboutPanel();

        add(myMainPanel);
        this.instance = this;
        
        // Add key listener for keyboard navigation
        setFocusable(true);
        addKeyListener(this);
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
        final JButton aboutButton = new JButton("About");
        final JButton exitButton = new JButton("Exit");

        newGameButton.addActionListener(_ -> newGame());
        loadGameButton.addActionListener(_ -> loadGame());
        instructionsButton.addActionListener(_ -> displayInstructions());
        aboutButton.addActionListener(_ -> displayAbout());
        exitButton.addActionListener(_ -> System.exit(0));

        menuPanel.add(newGameButton);
        menuPanel.add(loadGameButton);
        menuPanel.add(instructionsButton);
        menuPanel.add(aboutButton);
        menuPanel.add(exitButton);

        myMainPanel.add(menuPanel, "MainMenu");
    }

    private void addGamePanel() {
        final JPanel gamePanel = new JPanel(new BorderLayout());

        myMazePanel = new MazePanel();
        gamePanel.add(myMazePanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new GridLayout(1, 4));
        JButton northButton = new JButton("North");
        JButton southButton = new JButton("South");
        JButton eastButton = new JButton("East");
        JButton westButton = new JButton("West");

        northButton.addActionListener(_ -> movePlayer(Direction.NORTH, myMazePanel));
        southButton.addActionListener(_ -> movePlayer(Direction.SOUTH, myMazePanel));
        eastButton.addActionListener(_ -> movePlayer(Direction.EAST, myMazePanel));
        westButton.addActionListener(_ -> movePlayer(Direction.WEST, myMazePanel));

        controlPanel.add(northButton);
        controlPanel.add(southButton);
        controlPanel.add(eastButton);
        controlPanel.add(westButton);

        gamePanel.add(controlPanel, BorderLayout.SOUTH);
        addTrackerPanel(gamePanel);

        myMainPanel.add(gamePanel, "Game");
    }
    
    private void addTrackerPanel(JPanel gamePanel) {
        myTrackerPanel = new JPanel(new GridLayout(4, 1));

        myPositionLabel = new JLabel("Position: (4,4)");
        myCorrectQuestionsLabel = new JLabel("Correct Questions: 0");
        myIncorrectQuestionsLabel = new JLabel("Incorrect Questions: 0");
        myLockedDoorsLabel = new JLabel("Locked Doors: 49");

        myPositionLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        myCorrectQuestionsLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        myIncorrectQuestionsLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        myLockedDoorsLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        myTrackerPanel.add(myPositionLabel);
        myTrackerPanel.add(myCorrectQuestionsLabel);
        myTrackerPanel.add(myIncorrectQuestionsLabel);
        myTrackerPanel.add(myLockedDoorsLabel);

        gamePanel.add(myTrackerPanel, BorderLayout.EAST);
    }

    private void updateTracker() {
        if (myGameState != null) {
            myPositionLabel.setText("Position: (" + myGameState.getPlayerPosition() + ")");
            myCorrectQuestionsLabel.setText("Correct Questions: " + GameState.getInstance().getCorrectQuestions());
            myIncorrectQuestionsLabel.setText("Incorrect Questions: " + GameState.getInstance().getIncorrectQuestions());
            myLockedDoorsLabel.setText("Locked Doors: " + GameState.getInstance().getLockedDoors());
        }
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

        saveGameItem.addActionListener(e -> saveGame());
        loadGameItem.addActionListener(e -> loadGame());
        newGameItem.addActionListener(e -> newGame());
        exitGameItem.addActionListener(e -> returnToMainMenu());

        fileMenu.add(saveGameItem);
        fileMenu.add(loadGameItem);
        fileMenu.add(newGameItem);
        fileMenu.add(exitGameItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem instructionsGameItem = new JMenuItem("Instructions");
        JMenuItem aboutGameItem = new JMenuItem("About");

        instructionsGameItem.addActionListener(e -> displayInstructions());
        aboutGameItem.addActionListener(e -> displayAbout());

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
        System.out.println("\n\n==================================================");
        System.out.println("================= STARTING NEW GAME ===============");
        System.out.println("==================================================\n\n");
        
        // First clean up existing game state if we're in a game
        if (myInGame) {
            // Clear existing game data
            myGameState = null;
        }
        
        String[] difficulties = {"EASY", "MEDIUM", "HARD"};
        String selectedDifficulty = (String) JOptionPane.showInputDialog(this, 
                "Select Difficulty:", "New Game",
                JOptionPane.QUESTION_MESSAGE, null, difficulties, difficulties[0]);

        if (selectedDifficulty != null) {
            // Reset player position to center of maze
            Maze.reset();
            
            // Reset game state
            GameState.getInstance().resetState();
            
            // Initialize a new game state
            myGameState = new GameState();
            myGameState.setDifficulty(Difficulty.valueOf(selectedDifficulty));
            
            // Set up the database for questions
            DatabaseManager.getInstance().setDifficulty(Difficulty.valueOf(selectedDifficulty.trim().toUpperCase()));
            
            // Initialize question factory
            QuestionFactory.IntializeQuestionFactory();
            
            // Update UI state
            myInGame = true;
            myCardLayout.show(myMainPanel, "Game");
            addMenuBar();
            
            // Make sure maze display is refreshed
            myMazePanel.repaint();
            
            // Update tracker panel
            updateTracker();
            
            JOptionPane.showMessageDialog(this, "Game started on " + selectedDifficulty + " difficulty.");
        }
    }
    
    public void endGame() {
        this.newGame();
    }
    
    /**
     * Checks for win/loss conditions after a move.
     */
    private void checkGameState() {
        // Check if path to exit is blocked
        if (SystemControl.getInstance().checkLoseCondition()) {
            JOptionPane.showMessageDialog(this, 
                "All paths to the exit are blocked! Game over.", 
                "Game Over", 
                JOptionPane.ERROR_MESSAGE);
            
            // Ask if player wants to start a new game
            int response = JOptionPane.showConfirmDialog(this,
                "Would you like to start a new game?",
                "Game Over",
                JOptionPane.YES_NO_OPTION);
                
            if (response == JOptionPane.YES_OPTION) {
                newGame();
            } else {
                returnToMainMenu();
            }
        }
    }

    /**
     * begins serialization.
     */
    public void saveGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Game Save Files (*.sav)", "sav"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Ensure file has .sav extension
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".sav")) {
                filePath += ".sav";
            }
            
            myFilename = filePath;
            myGameState.saveToFile(myFilename);
            JOptionPane.showMessageDialog(this, "Game saved successfully to " + fileToSave.getName() + "!");
        }
    }

    /**
     * begins deserialization.
     */
    public void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Game");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Game Save Files (*.sav)", "sav"));
        
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            myFilename = fileToLoad.getAbsolutePath();
            
            try {
                myGameState = GameState.loadFromFile(myFilename);
                myInGame = true;
                addMenuBar();
                myCardLayout.show(myMainPanel, "Game");
                myMazePanel.repaint();
                updateTracker();
                JOptionPane.showMessageDialog(this, "Game loaded successfully!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error loading game: " + e.getMessage(), 
                    "Load Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
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

    void movePlayer(Direction direction, MazePanel mazePanel) {
        // Check if we're in a valid game state
        if (myGameState == null) {
            return; // Don't try to move if game state is null
        }
        
        boolean success = Maze.move(direction);

        if (success) {
            // Only update position if we're still in a valid game state
            if (myGameState != null) {
                int newX = Maze.getDisplayX() + 1;
                int newY = Maze.getDisplayY() + 1;
                myGameState.setCurrentPosition(newX, newY);
                mazePanel.repaint();
                updateTracker();
            }
        } else {
            mazePanel.repaint();
            updateTracker();
        }
        
        // Check for win/loss condition
        checkGameState();
    }
    
    public void returnToMainMenu() {
        System.out.println("\n\n==================================================");
        System.out.println("================ RETURNING TO MENU ================");
        System.out.println("==================================================\n\n");
        
        // Switch to main menu first for immediate visual feedback
        myCardLayout.show(myMainPanel, "MainMenu");
        
        // Reset game state
        myInGame = false;
        myGameState = null;
        
        // Remove existing menu bar
        setJMenuBar(null);
        
        // Force repaint immediately
        repaint();
        validate();
        
        // Add any needed main menu components
        addMainMenu();
        
        System.out.println("Now in main menu");
        
        // Final UI refresh to ensure changes are visible
        SwingUtilities.invokeLater(() -> {
            repaint();
            validate();
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
        case KeyEvent.VK_UP:
        case KeyEvent.VK_W:
            movePlayer(Direction.NORTH, myMazePanel);
            break;
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_S:
            movePlayer(Direction.SOUTH, myMazePanel);
            break;
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_A:
            movePlayer(Direction.WEST, myMazePanel);
            break;
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_D:
            movePlayer(Direction.EAST, myMazePanel);
            break;
        }
    }
}