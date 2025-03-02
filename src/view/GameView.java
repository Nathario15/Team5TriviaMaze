package view;

import java.awt.CardLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import model.GameState;

public final class GameView extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private Player player;
	private GameState gameState;
	/**
	 * The card layout.
	 */
	private CardLayout myCardLayout;
	/**
	 * The main panel.
	 */
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

	private void addMainMenu() {
		final JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new GridLayout(5, 1));

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
		final JPanel gamePanel = new JPanel();
		gamePanel.add(new JLabel("Game currently in progress."));
		myMainPanel.add(gamePanel, "Game");
	}

	private void addInstructionsPanel() {
		final JPanel instructionsPanel = new JPanel();
		instructionsPanel.add(
				new JLabel("Instructions: You must navigate the maze by answering trivia questions about Minecraft."));
		final JButton backButton = new JButton("Back");
		backButton.addActionListener(_ -> myCardLayout.show(myMainPanel, "MainMenu"));
		instructionsPanel.add(backButton);
		myMainPanel.add(instructionsPanel, "Instructions");
	}

	private void addAboutPanel() {
		JPanel aboutPanel = new JPanel();
		aboutPanel.add(new JLabel("Trivia Maze Game Initialization"));
		JButton backBtn = new JButton("Back");
		backBtn.addActionListener(e -> myCardLayout.show(myMainPanel, "MainMenu"));
		aboutPanel.add(backBtn);
		myMainPanel.add(aboutPanel, "About");
	}

	/**
	 * starts a new game.
	 */
	public void newGame() {
		//player = new Player();
		gameState = new GameState();
		myCardLayout.show(myMainPanel, "Game");
	}

	/**
	 * begins serialization.
	 */
	public void saveGame() {
		//gameState.saveState(maze, player);
		JOptionPane.showMessageDialog(this, "Game saved successfully!");
	}

	/**
	 * begins deserialization.
	 */
	public void loadGame() {
		//gameState.loadState();
		JOptionPane.showMessageDialog(this, "Game loaded successfully!");
		myCardLayout.show(myMainPanel, "Game");
	}

	/**
	 * display instructions.
	 */
	public void displayInstructions() {
		myCardLayout.show(myMainPanel, "Instructions");
	}

	/**
	 * display about.
	 */
	public void displayAbout() {
		myCardLayout.show(myMainPanel, "About");
	}
}
