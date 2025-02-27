package view;

import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GameView extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private Maze maze; Will be used once Maze is implemented
	// private Player player; Will be used once Player is implemented
	// private GameState gameState; will be used once GameState is implemented
	/**
	 * Game.
	 */
	private static String Game = "Game";
	/**
	 * Instructions.
	 */
	private static String Instructions = "Instructions";
	/**
	 * About.
	 */
	private static String About = "About";
	/**
	 * MainMenu.
	 */
	private static String MainMenu = "MainMenu";
	/**
	 * The card layout.
	 */
	private CardLayout myCardLayout;
	/**
	 * The main panel.
	 */
	private JPanel myMainPanel;
	/**
	 * Determines the width of the window.
	 */
	private final int myWidth = 800;
	/**
	 * Determines the height of the window.
	 */
	private final int myHeight = 600;
	/**
	 * Determines the number of rows.
	 */
	private final int myRows = 5;
	/**
	 * Determines the number of columns.
	 */
	private final int myCols = 5;
	/**
	 * Constructor.
	 */
	public GameView() {
		setTitle("Trivia Maze Game");
		setSize(myWidth, myHeight);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		myCardLayout = new CardLayout();
		myMainPanel = new JPanel(myCardLayout);

		// AddMainMenu();
		addGamePanel();
		addInstructionsPanel();
		// AddAboutPanel();

		add(myMainPanel);
	}
	/**
	 * 
	 * @param args
	 */
	public static void main(final String[] theArgs) {
		SwingUtilities.invokeLater(() -> {
			final GameView game = new GameView();
			game.setVisible(true);
		});
	}

	@SuppressWarnings("unused")
	private void addMainMenu() {
		final JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(myRows, myCols));

		final JButton newGameButton = new JButton("New Game");
		final JButton loadGameButton = new JButton("Load Game");
		final JButton instructionsButton = new JButton(Instructions);
		final JButton aboutBtn = new JButton(About);
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

		myMainPanel.add(menuPanel, MainMenu);
	}

	private void addGamePanel() {
		final JPanel gamePanel = new JPanel();
		gamePanel.add(new JLabel("Game currently in progress."));
		myMainPanel.add(gamePanel, Game);
	}

	private void addInstructionsPanel() {
		final JPanel instructionsPanel = new JPanel();
		instructionsPanel.add(
				new JLabel("Instructions: You must navigate the maze by answering trivia questions about Minecraft."));
		final JButton backButton = new JButton("Back");
		backButton.addActionListener(_ -> myCardLayout.show(myMainPanel, MainMenu));
		instructionsPanel.add(backButton);
		myMainPanel.add(instructionsPanel, Instructions);
	}
	/**
	 * starts a new game.
	 */
	public void newGame() {
		// maze = new Maze();
		// player = new Player();
		// gameState = new GameState();
		myCardLayout.show(myMainPanel, Game);
	}
	/**
	 * begins serialization.
	 */
	public void saveGame() {
		// gameState.saveState(maze, player);
		JOptionPane.showMessageDialog(this, "Game saved successfully!");
	}
	/**
	 * begins deserialization.
	 */
	public void loadGame() {
		// gameState.loadState();
		JOptionPane.showMessageDialog(this, "Game loaded successfully!");
		myCardLayout.show(myMainPanel, Game);
	}
	/**
	 * display instructions.
	 */
	public void displayInstructions() {
		myCardLayout.show(myMainPanel, Instructions);
	}
	/**
	 * display about.
	 */
	public void displayAbout() {
		myCardLayout.show(myMainPanel, About);
	}
}
