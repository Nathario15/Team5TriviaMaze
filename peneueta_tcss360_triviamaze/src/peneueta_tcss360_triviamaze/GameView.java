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
        addGamePanel();
        addInstructionsPanel();
        //addAboutPanel();
        
        add(mainPanel);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameView game = new GameView();
            game.setVisible(true);
        });
    }
    
    private void addGamePanel() {
    	JPanel gamePanel = new JPanel();
    	gamePanel.add(new JLabel("Game currently in progress."));
    	mainPanel.add(gamePanel, "Game");
    }
    
    private void addInstructionsPanel() {
    	JPanel instructionsPanel = new JPanel();
        instructionsPanel.add(new JLabel("Instructions: You must navigate the maze by answering trivia questions about Minecraft."));
        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(_ -> cardLayout.show(mainPanel, "MainMenu"));
        instructionsPanel.add(backBtn);
        mainPanel.add(instructionsPanel, "Instructions");
    }
}

