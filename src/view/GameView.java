package view;

import model.GameState;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * The main view for the Trivia Maze Game.
 */
public final class GameView extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private static final String SAVE_FILE = "game_save.dat";
    
    private GameState gameState;
    private CardLayout myCardLayout;
    private JPanel myMainPanel;

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
        JLabel gameLabel = new JLabel("Game currently in progress.", SwingConstants.CENTER);
        gamePanel.add(gameLabel, BorderLayout.CENTER);
        myMainPanel.add(gamePanel, "Game");
    }

    private void addInstructionsPanel() {
        final JPanel instructionsPanel = new JPanel(new BorderLayout());
        JLabel instructionsLabel = new JLabel(
                "<html><b>Instructions:</b> Navigate the maze by answering trivia questions correctly.</html>",
                SwingConstants.CENTER);
        instructionsPanel.add(instructionsLabel, BorderLayout.CENTER);

        final JButton backButton = new JButton("Back");
        backButton.addActionListener(_ -> myCardLayout.show(myMainPanel, "MainMenu"));
        instructionsPanel.add(backButton, BorderLayout.SOUTH);

        myMainPanel.add(instructionsPanel, "Instructions");
    }

    private void addAboutPanel() {
        final JPanel aboutPanel = new JPanel(new BorderLayout());
        JLabel aboutLabel = new JLabel("<html><b>Trivia Maze Game</b><br>Created by Nathaniel, Ibrahim, and Jayden.</html>",
                SwingConstants.CENTER);
        aboutPanel.add(aboutLabel, BorderLayout.CENTER);

        final JButton backBtn = new JButton("Back");
        backBtn.addActionListener(_ -> myCardLayout.show(myMainPanel, "MainMenu"));
        aboutPanel.add(backBtn, BorderLayout.SOUTH);

        myMainPanel.add(aboutPanel, "About");
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
            gameState = new GameState();
            JOptionPane.showMessageDialog(this, "Game started on " + selectedDifficulty + " difficulty.");
            myCardLayout.show(myMainPanel, "Game");
        }
    }

    /**
     * Saves the current game state.
     */
    public void saveGame() {
        if (gameState != null) {
            gameState.saveToFile(SAVE_FILE);
            JOptionPane.showMessageDialog(this, "Game saved successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "No game in progress to save.");
        }
    }

    /**
     * Loads a saved game.
     */
    public void loadGame() {
        File saveFile = new File(SAVE_FILE);
        if (saveFile.exists()) {
            gameState = GameState.loadFromFile(SAVE_FILE);
            JOptionPane.showMessageDialog(this, "Game loaded successfully!");
            myCardLayout.show(myMainPanel, "Game");
        } else {
            JOptionPane.showMessageDialog(this, "No saved game found.");
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
}
