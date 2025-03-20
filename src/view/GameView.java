package view;

import controller.SystemControl;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.AbstractQuestion;
import model.DatabaseManager;
import model.Difficulty;
import model.Direction;
import model.GameState;
import model.Maze;
import model.QuestionFactory;
import model.SoundManager;

/**
 * The main view for the Trivia Maze Game.
 * 
 * @author Team 5
 * @version 1.0
 */
public final class GameView extends JFrame implements KeyListener {
	/**
	 * window width.
	 */
	public static final int WIDTH = 800;
	/**
	 * window height.
	 */
	public static final int HEIGHT = 600;
	/**
	 * New Game.
	 */
	public static final String NEW_GAME = "New Game";
	/**
	 * Load Game.
	 */
	public static final String LOAD_GAME = "Load Game";
	/**
	 * Game.
	 */
	public static final String GAME = "Game";
	/**
	 * Instructions.
	 */
	public static final String INSTRUCTIONS = "Instructions";
	/**
	 * About.
	 */
	public static final String ABOUT = "About";
	/**
	 * Exit.
	 */
	public static final String EXIT = "Exit";
	/**
	 * MainMenu.
	 */
	public static final String MAIN_MENU = "MainMenu";
	/**
	 * Back.
	 */
	public static final String BACK = "Back";
	/**
	 * Save Game.
	 */
	public static final String SAVE_GAME = "Save Game";
	/**
	 * Game Over.
	 */
	public static final String GAME_OVER = "Game Over";
	/**
	 * .sav .
	 */
	public static final String FILE_EXTENSION = "sav";
	/**
	 * Cheats.
	 */
	public static final String CHEATS = "Enable Cheats";
	/**
	 * 
	 */
	public static final String MUSIC = "Enable Music";

//	/**
//	 * Instance.
//	 */
//	protected static GameView instance;

	/**
	 * Instance.
	 */
	protected static MazePanel myMazePanel;
	
	/**
	 * Button.
	 */
	private static final int BUTTONS = 5;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Creates the sounds in the game.
	 */
	private static SoundManager mySoundManager;

//	/**
//	 * games save data.
//	 */
//	private static GameState myGameState;
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
//	/**
//	 * Whether or not cheats are enabled.
//	 */
//	private boolean myCheatsEnabled;
	/**
	 * The file name for the save file.
	 */
	private String myFilename = "";

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
	/**
	 * The label for tracking whether cheats are enabled.
	 */
	private JLabel myCheatsLabel;

	/**
	 * Constructor.
	 */
	public GameView() {
		setTitle("Trivia Maze Game");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		mySoundManager = new SoundManager();

		myCardLayout = new CardLayout();
		myMainPanel = new JPanel(myCardLayout);
		myInGame = false;

		// Register this view with SystemControl
		SystemControl.getInstance().setGameView(this);
//		System.out.println("GameView registered with SystemControl");
		setUp();

	}
	
	/**
	 * sets up.
	 */
	private void setUp() {
		addMainMenu();
		addGamePanel();
		addInstructionsPanel();
		addAboutPanel();

		add(myMainPanel);
//		this.instance = this;

		// Add key listener for keyboard navigation
		setFocusable(true);
		addKeyListener(this);
	}

//	public static void main(final String[] args) {
//		SwingUtilities.invokeLater(() -> {
//			final GameView game = new GameView();
//			game.setVisible(true);
//		});
//	}

	private void addMainMenu() {
		final JPanel menuPanel = new JPanel(new GridLayout(5, 1));

		final JButton newGameButton = new JButton(NEW_GAME);
		final JButton loadGameButton = new JButton(LOAD_GAME);
		final JButton instructionsButton = new JButton(INSTRUCTIONS);
		final JButton aboutButton = new JButton(ABOUT);
		final JButton exitButton = new JButton(EXIT);

		newGameButton.addActionListener(_ -> {
			mySoundManager.playClickSound();
			newGame();
		});    
		loadGameButton.addActionListener(_ -> {
			mySoundManager.playClickSound();
			loadGame();
		});
		instructionsButton.addActionListener(_ -> {
			mySoundManager.playClickSound();
			displayInstructions();
		});
		aboutButton.addActionListener(_ -> {
			mySoundManager.playClickSound();
			displayAbout();
		});
		exitButton.addActionListener(_ -> {
			mySoundManager.playClickSound();
			System.exit(0);
		});

		menuPanel.add(newGameButton);
		menuPanel.add(loadGameButton);
		menuPanel.add(instructionsButton);
		menuPanel.add(aboutButton);
		menuPanel.add(exitButton);

		myMainPanel.add(menuPanel, MAIN_MENU);
	}

	private void addGamePanel() {
		final JPanel gamePanel = new JPanel(new BorderLayout());

		myMazePanel = new MazePanel();
		gamePanel.add(myMazePanel, BorderLayout.CENTER);

		final JPanel controlPanel = new JPanel(new GridLayout(1, 4));
		final JButton northButton = new JButton("North");
		final JButton southButton = new JButton("South");
		final JButton westButton = new JButton("West");
		final JButton eastButton = new JButton("East");
		
		northButton.addActionListener(_ -> {
			mySoundManager.playClickSound();
			movePlayer(Direction.NORTH, myMazePanel);
		});
		northButton.addKeyListener(myMazePanel);
		southButton.addActionListener(_ -> {
			mySoundManager.playClickSound();
			movePlayer(Direction.SOUTH, myMazePanel);
		});
		southButton.addKeyListener(myMazePanel);
		westButton.addActionListener(_ -> {
			mySoundManager.playClickSound();
			movePlayer(Direction.WEST, myMazePanel);
		});
		westButton.addKeyListener(myMazePanel);
		eastButton.addActionListener(_ -> {
			mySoundManager.playClickSound();
			movePlayer(Direction.EAST, myMazePanel);
		});
		eastButton.addKeyListener(myMazePanel);

		controlPanel.add(northButton);
		controlPanel.add(southButton);
		controlPanel.add(westButton);
		controlPanel.add(eastButton);

		gamePanel.add(controlPanel, BorderLayout.SOUTH);
		addTrackerPanel(gamePanel);

		myMainPanel.add(gamePanel, GAME);
	}

	private void addTrackerPanel(final JPanel theGamePanel) {
		myTrackerPanel = new JPanel(new GridLayout(BUTTONS, 1));

		myPositionLabel = new JLabel("Position: (4,4)");
		myCorrectQuestionsLabel = new JLabel("Correct Questions: 0");
		myIncorrectQuestionsLabel = new JLabel("Incorrect Questions: 0");
		myLockedDoorsLabel = new JLabel("Questions Remaining: 48");
		myCheatsLabel = new JLabel("Cheats: Off");

		myPositionLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		myCorrectQuestionsLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		myIncorrectQuestionsLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		myLockedDoorsLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		myCheatsLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

		myTrackerPanel.add(myPositionLabel);
		myTrackerPanel.add(myCorrectQuestionsLabel);
		myTrackerPanel.add(myIncorrectQuestionsLabel);
		myTrackerPanel.add(myLockedDoorsLabel);
		myTrackerPanel.add(myCheatsLabel);

		theGamePanel.add(myTrackerPanel, BorderLayout.EAST);
	}

	private void updateTracker() {
		if (GameState.getInstance() != null) {
			myPositionLabel.setText("Position: (" + GameState.getInstance().getPlayerPosition() + ")");
			myCorrectQuestionsLabel.setText("Correct Questions: " + GameState.getInstance().getCorrectQuestions());
			myIncorrectQuestionsLabel
					.setText("Incorrect Questions: " + GameState.getInstance().getIncorrectQuestions());
			myLockedDoorsLabel.setText("Questions Remaining: " + GameState.getInstance().getQuestionsRemaining());
			final String cheatsStatus;
		    if (AbstractQuestion.cheatsEnabled()) {
		        cheatsStatus = "On";
		    } else {
		        cheatsStatus = "Off";
		    }
		    myCheatsLabel.setText("Cheats: " + cheatsStatus);
		}
	}

	private void addInstructionsPanel() {
		final JPanel instructionsPanel = new JPanel(new BorderLayout());
		final JLabel instructionsLabel = new JLabel(
				"<html><b>Instructions:</b> Navigate the maze by answering Minecraft trivia questions correctly."
				+ " Moving in a direction that is currently outlined with a red line will prompt the user"
				+ " to answer a trivia question. If they get it wrong, that door is locked permanently and"
				+ " outlined with a gray line. If they get it right, the line becomes green and they can now"
				+ " freely navigate in that direction as desired. The player can navigate the maze using either"
				+ " WASD keys or via the navigation buttons provided to them once they start the game."
				+ " Additionally, you can enable cheats via the menu bar.</html>",
				SwingConstants.CENTER);
		instructionsPanel.add(instructionsLabel, BorderLayout.CENTER);

		final JButton backButton = new JButton(BACK);
		backButton.addActionListener(_ -> {
			mySoundManager.playClickSound();
			if (myInGame) {
				myCardLayout.show(myMainPanel, GAME);
			} else {
				myCardLayout.show(myMainPanel, MAIN_MENU);
			}
		});
		instructionsPanel.add(backButton, BorderLayout.SOUTH);

		myMainPanel.add(instructionsPanel, INSTRUCTIONS);
	}

	private void addAboutPanel() {
		final JPanel aboutPanel = new JPanel(new BorderLayout());
		final JLabel aboutLabel = new JLabel(
				"<html><b>Trivia Maze Game</b><br>Created by Team 5 for the class of TCSS 360 Winter 2025,"
				+ " consisting of Nathaniel Roy, Ibrahim Elnikety, and Jayden Peneueta.</html>",
				SwingConstants.CENTER);
		aboutPanel.add(aboutLabel, BorderLayout.CENTER);

		final JButton backBtn = new JButton(BACK);
		backBtn.addActionListener(_ -> {
			mySoundManager.playClickSound();
			if (myInGame) {
				myCardLayout.show(myMainPanel, GAME);
			} else {
				myCardLayout.show(myMainPanel, MAIN_MENU);
			}
		});
		aboutPanel.add(backBtn, BorderLayout.SOUTH);

		myMainPanel.add(aboutPanel, ABOUT);
	}

	@SuppressWarnings("unused")
	private void addMenuBar() {
		// Create the menu bar
		final JMenuBar menuBar = new JMenuBar();

		// Add the file button
		final JMenu fileMenu = new JMenu("File");
		final JMenuItem saveGameItem = new JMenuItem(SAVE_GAME);
		final JMenuItem loadGameItem = new JMenuItem(LOAD_GAME);
		final JMenuItem newGameItem = new JMenuItem(NEW_GAME);
		final JMenuItem exitGameItem = new JMenuItem(EXIT);

		saveGameItem.addActionListener(e -> {
			mySoundManager.playClickSound();
			saveGame();
		});
		loadGameItem.addActionListener(e -> {
			mySoundManager.playClickSound();
			loadGame();
		});
		newGameItem.addActionListener(e -> {
			mySoundManager.playClickSound();
			newGame();
		});
		exitGameItem.addActionListener(e -> {
			mySoundManager.playClickSound();
			returnToMainMenu();
			mySoundManager.stopBackgroundMusic();
		});

		fileMenu.add(saveGameItem);
		fileMenu.add(loadGameItem);
		fileMenu.add(newGameItem);
		fileMenu.add(exitGameItem);

		// Add the help button
		final JMenu helpMenu = new JMenu("Help");
		final JMenuItem instructionsGameItem = new JMenuItem(INSTRUCTIONS);
		final JMenuItem aboutGameItem = new JMenuItem(ABOUT);

		instructionsGameItem.addActionListener(e -> {
			mySoundManager.playClickSound();
			displayInstructions();
		});
		aboutGameItem.addActionListener(e -> {
			mySoundManager.playClickSound();
			displayAbout();
		});
		
		helpMenu.add(instructionsGameItem);
		helpMenu.add(aboutGameItem);
		
		// Add the musics button
		final JMenu musicMenu = new JMenu("Music");
		final JMenuItem musicToggle = new JCheckBoxMenuItem(MUSIC, mySoundManager.getMusicEnabled());
		
		musicToggle.addActionListener(e -> {
			mySoundManager.playClickSound();
			mySoundManager.toggleMusic();
			musicToggle.setSelected(mySoundManager.getMusicEnabled());
		});
		
		musicMenu.add(musicToggle);
		
		// Add the cheats button
		final JMenu cheatsMenu = new JMenu("Cheats");
		final JMenuItem cheatsCheckBox = new JCheckBoxMenuItem(CHEATS);
		
		cheatsCheckBox.addActionListener(e -> {
			mySoundManager.playClickSound();
//			myCheatsEnabled = !myCheatsEnabled;
			AbstractQuestion.toggleCheats();
			updateTracker();
		});

		cheatsMenu.add(cheatsCheckBox);
		
		// Add the menu options to the menu bar
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		menuBar.add(musicMenu);
		menuBar.add(cheatsMenu);

		setJMenuBar(menuBar);
	}
	
	/**
	 * Starts a new game, prompting the user to select a difficulty level.
	 */
	public void newGame() {
//		System.out.println("\n\n==================================================");
//		System.out.println("================= STARTING NEW GAME ===============");
//		System.out.println("==================================================\n\n");

		// First clean up existing game state if we're in a game
		if (myInGame) {
			// Clear existing game data
//			myGameState = null;
			GameState.resetState();
		}

		final String[] difficulties = { "EASY", "MEDIUM", "HARD" };
		final String selectedDifficulty = (String) JOptionPane.showInputDialog(this, "Select Difficulty:", NEW_GAME,
				JOptionPane.QUESTION_MESSAGE, null, difficulties, difficulties[0]);
		if (selectedDifficulty != null) {
			// Click sound for pressing OK on difficulty message dialog
			mySoundManager.playClickSound();
			
			// Reset player position to center of maze
			Maze.reset();

			// Reset game state
			GameState.resetState();

			// Initialize a new game state
//			myGameState = new GameState();
			GameState.getInstance().setDifficulty(Difficulty.valueOf(selectedDifficulty));

			// Set up the database for questions
			DatabaseManager.getInstance().setDifficulty(Difficulty.valueOf(selectedDifficulty.trim().toUpperCase()));

			// Initialize question factory
			QuestionFactory.intializeQuestionFactory();
			
			// Initialize GameState position to match Maze's initial position
			final int newX = Maze.getDisplayX() + 1; // Convert from 0-based to 1-based coordinates
			final int newY = Maze.getDisplayY() + 1; // Convert from 0-based to 1-based coordinates
			GameState.getInstance().setCurrentPosition(newX, newY);

			// Update UI state
			myInGame = true;
			myCardLayout.show(myMainPanel, GAME);
			addMenuBar();

			// Make sure maze display is refreshed
			myMazePanel.repaint();

			// Update tracker panel
			updateTracker();
			
			// Start the background music
			mySoundManager.playBackgroundMusic();

			JOptionPane.showMessageDialog(this, "Game started on " + selectedDifficulty + " difficulty.");
			
			// Click sound for pressing OK on 'game started on' the message dialog
			mySoundManager.playClickSound();
		}
		mySoundManager.playClickSound();
	}

	/**
	 * end game.
	 */
	public void endGame() {
		this.newGame();
	}

	/**
	 * Checks for win/loss conditions after a move.
	 */
	private void checkGameState() {
		System.out.println("GameView.checkGameState: " + SystemControl.getInstance().checkLoseCondition());
		// Check if path to exit is blocked
		if (SystemControl.getInstance().checkLoseCondition()) {
			mySoundManager.playLoseSound();
			System.out.println("GameView.checkGameState: lose cond");
			JOptionPane.showMessageDialog(this, "All paths to the exit are blocked! Game over.", GAME_OVER,
					JOptionPane.ERROR_MESSAGE);
			
			mySoundManager.playClickSound();

			// Ask if player wants to start a new game
			final int response = JOptionPane.showConfirmDialog(this, "Would you like to start a new game?", GAME_OVER,
					JOptionPane.YES_NO_OPTION);

			if (response == JOptionPane.YES_OPTION) {
				mySoundManager.playClickSound();
				newGame();
			} else {
				mySoundManager.playClickSound();
				returnToMainMenu();
			}
		}
	}

	/**
	 * begins serialization.
	 */
	public void saveGame() {
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(SAVE_GAME);
		fileChooser.setFileFilter(new FileNameExtensionFilter("Game Save Files (*." + "sav)", FILE_EXTENSION));

		final int userSelection = fileChooser.showSaveDialog(this);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			final File fileToSave = fileChooser.getSelectedFile();
			// Ensure file has .sav extension
			String filePath = fileToSave.getAbsolutePath();
			if (!filePath.toLowerCase().endsWith("." + FILE_EXTENSION)) {
				filePath += ".sav";
			}

			myFilename = filePath;
			GameState.getInstance().saveToFile(myFilename);
			JOptionPane.showMessageDialog(this, "Game saved successfully to " + fileToSave.getName() + "!");
		}
	}

	/**
	 * begins deserialization.
	 */
	public static void loadGame() {
		if (AbstractQuestion.cheatsEnabled()) {
			AbstractQuestion.toggleCheats();
			SystemControl.getInstance().getGameView().updateTracker();
		}
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle(LOAD_GAME);
		fileChooser.setFileFilter(new FileNameExtensionFilter("Game Save Files (*.sav)", FILE_EXTENSION));

		final int userSelection = fileChooser.showOpenDialog(SystemControl.getInstance().getGameView());
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			final File fileToLoad = fileChooser.getSelectedFile();
			SystemControl.getInstance().getGameView().myFilename = fileToLoad.getAbsolutePath();

			try {
				mySoundManager.playClickSound();
				GameState.loadFromFile(SystemControl.getInstance().getGameView().myFilename);
				SystemControl.getInstance().getGameView().myInGame = true;
				SystemControl.getInstance().getGameView().addMenuBar();
				SystemControl.getInstance().getGameView().myCardLayout.show(SystemControl.getInstance().getGameView().myMainPanel, GAME);
				myMazePanel.repaint();
				mySoundManager.playBackgroundMusic();
				SystemControl.getInstance().getGameView().updateTracker();
				JOptionPane.showMessageDialog(SystemControl.getInstance().getGameView(), "Game loaded successfully!");
				mySoundManager.playClickSound();
			} catch (final IOException | ClassNotFoundException e) {
				JOptionPane.showMessageDialog(SystemControl.getInstance().getGameView(), "Error loading game: " + e.getMessage(), "Load Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		mySoundManager.playClickSound();
		SystemControl.getInstance().getGameView().updateTracker();
	}

	/**
	 * Displays the instructions screen.
	 */
	public void displayInstructions() {
		myCardLayout.show(myMainPanel, INSTRUCTIONS);
	}

	/**
	 * Displays the about screen.
	 */
	public void displayAbout() {
		myCardLayout.show(myMainPanel, ABOUT);
	}

	void movePlayer(final Direction theDirection, final MazePanel theMazePanel) {
		// Check if we're in a valid game state
		if (GameState.getInstance() == null) {
			return; // Don't try to move if game state is null
		}

		final boolean success;
		if (theDirection == Direction.NORTH || theDirection == Direction.SOUTH) {
			success = Maze.move(theDirection);
		} else {
			success = Maze.move(theDirection);
		}

		if (success) {
			// Only update position if we're still in a valid game state
			if (GameState.getInstance() != null) {
				final int newX = Maze.getDisplayX() + 1;
				final int newY = Maze.getDisplayY() + 1;
				GameState.getInstance().setCurrentPosition(newX, newY);
				theMazePanel.repaint();
				updateTracker();
			}
		} else {
			theMazePanel.repaint();
			updateTracker();
		}

		// Check for win/loss condition
		checkGameState();
		System.out.println("Can solve?: " + Maze.canSolve());
	}

	/**
	 * returns to main menu.
	 */
	public void returnToMainMenu() {
//		System.out.println("\n\n==================================================");
//		System.out.println("================ RETURNING TO MENU ================");
//		System.out.println("==================================================\n\n");

		// Switch to main menu first for immediate visual feedback
		myCardLayout.show(myMainPanel, MAIN_MENU);

		// Reset game state
		myInGame = false;
//		myGameState = null;
		GameState.resetState();

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
	public void keyTyped(final KeyEvent theE) {
		// Not used
	}

	@Override
	public void keyPressed(final KeyEvent theE) {
		// Not used
	}

	@Override
	public void keyReleased(final KeyEvent theE) {
		final int keyCode = theE.getKeyCode();
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
		default:
			break;
		}
	}
}