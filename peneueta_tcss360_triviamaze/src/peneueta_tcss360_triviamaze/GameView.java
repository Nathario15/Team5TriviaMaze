package peneueta_tcss360_triviamaze;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameView extends JFrame {
    //private Maze maze; Will be used once Maze is implemented
    //private Player player; Will be used once Player is implemented
    //private GameState gameState; will be used once GameState is implemented
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    public GameView() {
        setTitle("Trivia Maze Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        //AddMainMenu();
        AddGamePanel();
        AddInstructionsPanel();
        //AddAboutPanel();
        
        add(mainPanel);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameView game = new GameView();
            game.setVisible(true);
        });
    }
    
    private void AddGamePanel() {
    	JPanel gamePanel = new JPanel();
    	gamePanel.add(new JLabel("Game currently in progress."));
    	mainPanel.add(gamePanel, "Game");
    }
    
    private void AddInstructionsPanel() {
    	JPanel instructionsPanel = new JPanel();
        instructionsPanel.add(new JLabel("Instructions: You must navigate the maze by answering trivia questions about Minecraft."));
        JButton backButton = new JButton("Back");
        backButton.addActionListener(_ -> cardLayout.show(mainPanel, "MainMenu"));
        instructionsPanel.add(backButton);
        mainPanel.add(instructionsPanel, "Instructions");
    }
    
    public void NewGame() {
    	//maze = new Maze();
    	//player = new Player();
    	//gameState = new GameState();
    	cardLayout.show(mainPanel, "Game");
    }
    
    public void LoadGame() {
        //gameState.loadState();
        JOptionPane.showMessageDialog(this, "Game loaded successfully!");
        cardLayout.show(mainPanel, "Game");
    }
}

