package controller;

import javax.swing.SwingUtilities;
import view.GameView;
import model.DatabaseManager;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize database
            DatabaseManager dbManager = DatabaseManager.getInstance();
            
            // Create SystemControl (controller)
            SystemControl controller = SystemControl.getInstance();
            
            // Create and display the view
            SwingUtilities.invokeLater(() -> {
                GameView view = new GameView();
                view.setVisible(true);
            });
            
        } catch (Exception e) {
            System.err.println("Error starting application: " + e.getMessage());
            e.printStackTrace();
        }
    }
}