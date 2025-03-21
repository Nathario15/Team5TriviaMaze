package view;

import controller.SystemControl;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
	 * Music.
	 */
	public static final String MUSIC = "Toggle Music";
	/**
	 * Start string.
	 */
	public static final String BEGIN = "<html><div style='text-align: center;'>";
	/**
	 * End string.
	 */
	public static final String END = "</div></html>";
	/**
	 * Background.
	 */
	public static final String BACKGROUND = "resources/images/main_background.png";
	/**
	 * Instance.
	 */
	protected static MazePanel myMazePanel;
	/**
	 * Button.
	 */
	private static final int BUTTONS = 5;
	/**
	 * Button width.
	 */
	private static final int BUTTON_WIDTH = 200;
	/**
	 * Button height.
	 */
	private static final int BUTTON_HEIGHT = 50;
	/**
	 * Scales.
	 */
	private static final int INSET_SCALE = 10;
	/**
	 * Arial backup value.
	 */
	private static final int ARIAL = 16;
	/**
	 * Minecraftia font scale.
	 */
	private static final float MINECRAFTIA = 16f;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Creates the sounds in the game.
	 */
	private static SoundManager mySoundManager = SoundManager.getInstance();
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
		setTitle("Minecraft Trivia Maze");
		setSize(WIDTH, HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		myCardLayout = new CardLayout();
		myMainPanel = new JPanel(myCardLayout);
		myInGame = false;

		SystemControl.getInstance().setGameView(this);
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

	private void addMainMenu() {
		final JPanel menuPanel = new JPanel(new GridBagLayout()) {
			private static final long serialVersionUID = 1L;
	        private final Image myBackground = new ImageIcon(BACKGROUND).getImage();

	        @Override
	        protected void paintComponent(final Graphics theG) {
	            super.paintComponent(theG);
	            theG.drawImage(myBackground, 0, 0, getWidth(), getHeight(), this);
	        }
	    };

	    menuPanel.setOpaque(false);
	    final GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(INSET_SCALE, 0, INSET_SCALE, 0);
	    gbc.gridx = 0;
	    
	    // Title label
	    final ImageIcon titleIcon = new ImageIcon("resources/images/trivia_maze_logo.png");

	    // Scale the image to fit the menu panel
	    final Image scaledImage = titleIcon.getImage().getScaledInstance(400, 100, Image.SCALE_SMOOTH);

	    // Create a JLabel with the scaled image
	    final JLabel title = new JLabel(new ImageIcon(scaledImage));

	    // Ensure proper alignment
	    title.setHorizontalAlignment(JLabel.CENTER);

	    // Add to the menu panel
	    gbc.gridy = 0;
	    gbc.fill = GridBagConstraints.HORIZONTAL; // Allows stretching if needed
	    menuPanel.add(title, gbc);

	    // Button Panel
	    final JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 10));
	    buttonPanel.setOpaque(false);

	    final JButton newGameButton = createStyledButton(NEW_GAME);
	    final JButton loadGameButton = createStyledButton(LOAD_GAME);
	    final JButton instructionsButton = createStyledButton(INSTRUCTIONS);
	    final JButton aboutButton = createStyledButton(ABOUT);
	    final JButton exitButton = createStyledButton(EXIT);

	    // Add action listeners
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

	    // Add buttons to panel
	    buttonPanel.add(newGameButton);
	    buttonPanel.add(loadGameButton);
	    buttonPanel.add(instructionsButton);
	    buttonPanel.add(aboutButton);
	    buttonPanel.add(exitButton);

	    gbc.gridy = 1;
	    menuPanel.add(buttonPanel, gbc);

	    myMainPanel.add(menuPanel, MAIN_MENU);
	}

	private void addGamePanel() {
	    final JPanel gamePanel = new JPanel(new BorderLayout());

	    myMazePanel = new MazePanel();
	    gamePanel.add(myMazePanel, BorderLayout.CENTER);

	    final JPanel controlPanel = new JPanel(new GridLayout(1, 4));
	    final JButton northButton = new StoneButton("North");
	    final JButton southButton = new StoneButton("South");
	    final JButton westButton = new StoneButton("West");
	    final JButton eastButton = new StoneButton("East");

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

	    myPositionLabel = new StoneLabel("Position: (4,4)");
	    myCorrectQuestionsLabel = new StoneLabel("Correct Questions: 0");
	    myIncorrectQuestionsLabel = new StoneLabel("Incorrect Questions: 0");
	    myLockedDoorsLabel = new StoneLabel("Questions Remaining: 48");
	    myCheatsLabel = new StoneLabel("Cheats: Off");

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
	    final JPanel instructionsPanel = new JPanel(new BorderLayout()) {
	        private static final long serialVersionUID = 1L;

	        // Override paintComponent to draw the background image
	        @Override
	        protected void paintComponent(final Graphics theG) {
	            super.paintComponent(theG);
	            // Load the background image
	            final Image background = new ImageIcon(BACKGROUND).getImage();
	            // Draw the image to fill the entire panel
	            theG.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	        }
	    };

	    // Create a JLabel for the instructions with HTML formatting for wrapping
	    final JLabel instructionsLabel = new JLabel(
	        BEGIN
	        + "<b>Instructions:</b><br>"
	        + "Navigate the maze by answering Minecraft trivia questions correctly.<br>"
	        + "Moving in a direction that is currently outlined with a red line will prompt the user "
	        + "to answer a trivia question. If they get it wrong, that door is locked permanently and "
	        + "outlined with a gray line. If they get it right, the line becomes green and they can now "
	        + "freely navigate in that direction as desired. The player can navigate the maze using either "
	        + "WASD keys or via the navigation buttons provided to them once they start the game.<br>"
	        + "Additionally, you can enable cheats via the menu bar."
	        + END,
	        SwingConstants.CENTER
	    );
	    instructionsLabel.setForeground(Color.WHITE); // Set text color to white

	    // Set custom font (using the method to load the custom font)
	    instructionsLabel.setFont(loadCustomFont());

	    // Add the instructions label to the instructions panel, centered
	    instructionsPanel.add(instructionsLabel, BorderLayout.CENTER);

	    // Create a panel for the back button with GridBagLayout to control positioning
	    final JPanel buttonPanel = new JPanel(new GridBagLayout());
	    buttonPanel.setOpaque(false); // Make sure the button panel is transparent

	    // Create the back button with the stone texture functionality
	    final JButton backButton = new StoneButton(BACK);
	    backButton.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
	    backButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

	    // Set up GridBagConstraints to position the back button
	    final GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.insets = new Insets(INSET_SCALE, 0, INSET_SCALE, 0); // Vertical spacing between label and button
	    gbc.anchor = GridBagConstraints.CENTER; // Center the button horizontally

	    // Add ActionListener to the back button
	    backButton.addActionListener(_ -> {
	        mySoundManager.playClickSound();
	        if (myInGame) {
	            myCardLayout.show(myMainPanel, GAME); // Show game panel if in game
	        } else {
	            myCardLayout.show(myMainPanel, MAIN_MENU); // Show main menu if not in game
	        }
	    });

	    // Add the back button to the button panel
	    buttonPanel.add(backButton, gbc);

	    // Add the button panel to the bottom of the instructions panel
	    instructionsPanel.add(buttonPanel, BorderLayout.SOUTH);

	    // Add the instructions panel to the main panel
	    myMainPanel.add(instructionsPanel, INSTRUCTIONS);
	}

	private void addAboutPanel() {
	    final JPanel aboutPanel = new JPanel(new BorderLayout()) {
	        private static final long serialVersionUID = 1L;

	        // Override paintComponent to draw the background image
	        @Override
	        protected void paintComponent(final Graphics theG) {
	            super.paintComponent(theG);
	            // Load the background image
	            final Image background = new ImageIcon(BACKGROUND).getImage();
	            // Draw the image to fill the entire panel
	            theG.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	        }
	    };

	    // Create a JLabel to display the about information with HTML formatting for wrapping
	    final JLabel aboutLabel = new JLabel(
	        BEGIN
	        + "<b>Trivia Maze Game</b><br>"
	        + "Created by Team 5 for the class of TCSS 360 Winter 2025,<br>"
	        + "consisting of Nathaniel Roy, Ibrahim Elnikety, and Jayden Peneueta."
	        + END,
	        SwingConstants.CENTER
	    );
	    aboutLabel.setForeground(Color.WHITE); // Set text color to white

	    // Set custom font (using the method to load the custom font)
	    aboutLabel.setFont(loadCustomFont());

	    // Add the about label to the about panel, centered
	    aboutPanel.add(aboutLabel, BorderLayout.CENTER);

	    // Create a panel for the back button with GridBagLayout to control positioning
	    final JPanel buttonPanel = new JPanel(new GridBagLayout());
	    buttonPanel.setOpaque(false); // Make sure the button panel is transparent

	    // Create the back button with the stone texture functionality
	    final JButton backButton = new StoneButton(BACK);
	    backButton.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
	    backButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

	    // Set up GridBagConstraints to position the back button
	    final GridBagConstraints gbc = new GridBagConstraints();
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.insets = new Insets(INSET_SCALE, 0, INSET_SCALE, 0); // Vertical spacing between label and button
	    gbc.anchor = GridBagConstraints.CENTER; // Center the button horizontally

	    // Add ActionListener to the back button
	    backButton.addActionListener(_ -> {
	        mySoundManager.playClickSound();
	        if (myInGame) {
	            myCardLayout.show(myMainPanel, GAME); // Show game panel if in game
	        } else {
	            myCardLayout.show(myMainPanel, MAIN_MENU); // Show main menu if not in game
	        }
	    });

	    // Add the back button to the button panel
	    buttonPanel.add(backButton, gbc);

	    // Add the button panel to the bottom of the about panel
	    aboutPanel.add(buttonPanel, BorderLayout.SOUTH);

	    // Add the about panel to the main panel
	    myMainPanel.add(aboutPanel, ABOUT);
	}

	private void addMenuBar() {
	    // Create the menu bar
	    final JMenuBar menuBar = new StoneMenuBar();

	    // Add the file button
	    final JMenu fileMenu = new StoneMenu("File");
	    final JMenuItem saveGameItem = new StoneMenuItem(SAVE_GAME);
	    final JMenuItem loadGameItem = new StoneMenuItem(LOAD_GAME);
	    final JMenuItem newGameItem = new StoneMenuItem(NEW_GAME);
	    final JMenuItem exitGameItem = new StoneMenuItem(EXIT);

	    saveGameItem.addActionListener(_ -> {
	        mySoundManager.playClickSound();
	        saveGame();
	    });
	    loadGameItem.addActionListener(_ -> {
	        mySoundManager.playClickSound();
	        loadGame();
	    });
	    newGameItem.addActionListener(_ -> {
	        mySoundManager.playClickSound();
	        newGame();
	    });
	    exitGameItem.addActionListener(_ -> {
	        mySoundManager.playClickSound();
	        returnToMainMenu();
	        mySoundManager.stopBackgroundMusic();
	    });

	    fileMenu.add(saveGameItem);
	    fileMenu.add(loadGameItem);
	    fileMenu.add(newGameItem);
	    fileMenu.add(exitGameItem);

	    // Add the help button
	    final JMenu helpMenu = new StoneMenu("Help");
	    final JMenuItem instructionsGameItem = new StoneMenuItem(INSTRUCTIONS);
	    final JMenuItem aboutGameItem = new StoneMenuItem(ABOUT);

	    instructionsGameItem.addActionListener(_ -> {
	        mySoundManager.playClickSound();
	        displayInstructions();
	    });
	    aboutGameItem.addActionListener(_ -> {
	        mySoundManager.playClickSound();
	        displayAbout();
	    });

	    helpMenu.add(instructionsGameItem);
	    helpMenu.add(aboutGameItem);

	    // Add the music button
	    final JMenu musicMenu = new StoneMenu("Music");
	    final StoneCheckBoxMenuItem musicToggle = new StoneCheckBoxMenuItem(MUSIC);
	    musicToggle.setSelected(mySoundManager.getMusicEnabled());

	    musicToggle.addActionListener(_ -> {
	        mySoundManager.playClickSound();
	        mySoundManager.toggleMusic();
	        musicToggle.setSelected(mySoundManager.getMusicEnabled());
	    });

	    musicMenu.add(musicToggle);

	    // Add the cheats button
	    final JMenu cheatsMenu = new StoneMenu("Cheats");
	    final JMenuItem cheatsCheckBox = new StoneCheckBoxMenuItem(CHEATS);

	    cheatsCheckBox.addActionListener(_ -> {
	        mySoundManager.playClickSound();
	        AbstractQuestion.toggleCheats(cheatsCheckBox.isSelected());
	        updateTracker();
	        myMazePanel.repaint();
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
		AbstractQuestion.toggleCheats(false);
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
        mySoundManager.stopBackgroundMusic();
		this.newGame();
	}

	/**
	 * Checks for win/loss conditions after a move.
	 */
	private void checkGameState() {
//		System.out.println("GameView.checkGameState: " + SystemControl.getInstance().checkLoseCondition());
		// Check if path to exit is blocked
		if (SystemControl.getInstance().checkLoseCondition()) {
			mySoundManager.playLoseSound();
			mySoundManager.stopBackgroundMusic();
//			System.out.println("GameView.checkGameState: lose cond");
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
				mySoundManager.stopBackgroundMusic();
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
		AbstractQuestion.toggleCheats(false);
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
//		System.out.println("Can solve?: " + Maze.canSolve());
	}

	/**
	 * returns to main menu.
	 */
	public void returnToMainMenu() {
        mySoundManager.stopBackgroundMusic();

		myCardLayout.show(myMainPanel, MAIN_MENU);

		myInGame = false;
		GameState.resetState();

		setJMenuBar(null);

		repaint();
		validate();

		addMainMenu();

//		System.out.println("Now in main menu");

		SwingUtilities.invokeLater(() -> {
			repaint();
			validate();
		});
	}
	
	private JButton createStyledButton(final String theText) {
		return new StoneButton(theText);
	}

	private static Font loadCustomFont() {
	    try {
	        // Load the font from the resources directory
	        final File fontFile = new File("resources/fonts/Minecraftia.ttf");
	        
	        // Create the font from the file
	        final Font minecraftiaFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
	        
	        // Register the font with the GraphicsEnvironment
	        final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        ge.registerFont(minecraftiaFont);
	        
	        // Return the font with the desired size
	        return minecraftiaFont.deriveFont(MINECRAFTIA);
	        
	    } catch (final FontFormatException | IOException e) {
	        e.printStackTrace();
	        return new Font("Arial", Font.PLAIN, ARIAL);
	    }
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